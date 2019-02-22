package asbamboo.android.sdk.alipay;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import asbamboo.android.sdk.Configure;
import asbamboo.android.sdk.Json;
import asbamboo.android.sdk.api.Request;
import asbamboo.android.sdk.api.Response;
import asbamboo.android.sdk.tool.TradePay;

public class Client {
    private static final String ALIPAY_STATUS_SUCCESS           = "9000";
    private static final String ALIPAY_STATUS_PAYING            = "8000";
    private static final String ALIPAY_STATUS_FAILED            = "4000";
    private static final String ALIPAY_STATUS_REPEAT            = "5000";
    private static final String ALIPAY_STATUS_USER_CANCEL       = "6001";
    private static final String ALIPAY_STATUS_NETWORK_EXCEPTION = "6002";
    private static final String ALIPAY_STATUS_UNKNOWN           = "6004";

    public void pay(final TradePay.AcvivityInterface context, final Request request){

        //调起支付宝
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //序列化请求参数
                String pay_params = "";
                try{
                    Response response = request.post();
                    if(response.getIsSuccess()){
                        Map<String, Object> data    = (Map<String, Object>) response.getDecodedData().get("data");
                        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                        pay_params  = data.get("app_pay_json").toString();
                        Log.d("pay params", pay_params);
                    }
                }catch(Exception e){
                    Log.d("exception", e.toString());
                }
                if(pay_params == "") {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            context.onPaycallback("{\"status\":\"failed\", \"message\":\"未能调起支付渠道,请检查.\", \"pay_info\":null}");
                        }
                    });
                }else{
                    HashMap<String, Object>  decoded_pay_params = Json.decode(pay_params);
                    StringBuilder query = new StringBuilder();
                    Iterator pay_params_iterator = decoded_pay_params.keySet().iterator();

                    try{
                        while(pay_params_iterator.hasNext()) {
                            String key = (String)pay_params_iterator.next();
                            String value = decoded_pay_params.get(key).toString();
                            key = URLEncoder.encode(key, "UTF-8");
                            value = URLEncoder.encode(value, "UTF-8");
                            query.append(key + "=" + value + "&");
                        }
                    }catch(UnsupportedEncodingException e){

                    }

                    String alipay_params    = query.toString();
                    alipay_params           = alipay_params.substring(0, alipay_params.length() - 1);

                    PayTask pay_task = new PayTask((Activity) context);
                    Map<String, String> alipay_result = pay_task.payV2(alipay_params, true);

                    Log.d("alipay result", alipay_result.toString());

                    /**
                     * callback
                     */
                    String callresult = "";
                    try{
                        if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_SUCCESS)){
                            String org_api_url              = Configure.API_URL;
                            Configure.API_URL               = Configure.API_URL + "/ALIPAY_APP/return";

                            Map<String, Object> req_data    = new HashMap<>();
                            req_data.put("result", alipay_result.get("result"));
                            req_data.put("resultStatus", alipay_result.get("resultStatus"));
                            req_data.put("memo", alipay_result.get("memo"));

                            Request req     = new Request(req_data);
                            Response rep    = req.post();
                            Log.d("return rep", rep.toString());
                            if(rep.getIsSuccess()){
                                Map<String, Object> callresult_map  = new HashMap<>();
                                callresult_map.put("status", "success");
                                callresult_map.put("message", "支付成功");
                                callresult_map.put("pay_info", rep.getDecodedData().get("data"));
                                callresult  = Json.encode(callresult_map);
                            }else{
                                callresult  = "{\"status\":\"failed\", \"message\":\"支付结果存在异常.\", \"pay_info\":null}";
                            }

                            Configure.API_URL   = org_api_url;
                        }else if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_PAYING)){
                            callresult  = "{\"status\":\"paying\", \"message\":\"正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态.\", \"pay_info\":null}";
                        }else if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_FAILED)){
                            callresult  = "{\"status\":\"failed\", \"message\":\"订单支付失败.\", \"pay_info\":null}";
                        }else if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_REPEAT)){
                            callresult  = "{\"status\":\"repeated\", \"message\":\"重复请求.\", \"pay_info\":null}";
                        }else if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_USER_CANCEL)){
                            callresult  = "{\"status\":\"user_cancel\", \"message\":\"用户中途取消.\", \"pay_info\":null}";
                        }else if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_NETWORK_EXCEPTION)){
                            callresult  = "{\"status\":\"network_exception\", \"message\":\"网络连接出错.\", \"pay_info\":null}";
                        }else if(alipay_result.get("resultStatus").equals(ALIPAY_STATUS_UNKNOWN)){
                            callresult  = "{\"status\":\"unknown\", \"message\":\"支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态.\", \"pay_info\":null}";
                        }else{
                            callresult  = "{\"status\":\"failed\", \"message\":\"支付结果不明确.\", \"pay_info\":null}";
                        }
                    } catch (Exception e){
                        callresult  = "{\"status\":\"failed\", \"message\":\"支付结果不明确, 系统异常.\", \"pay_info\":null, \"e\":"+e.toString()+"}";
                    }finally {
                        final String return_callresult = callresult;
                        // 解析支付结果
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                context.onPaycallback(return_callresult);
                            }
                        });
                    }
                }
            }
        }).start();
    }
}
