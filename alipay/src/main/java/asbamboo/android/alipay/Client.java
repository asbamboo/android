package asbamboo.android.alipay;

import android.app.Activity;
import android.os.Handler;
import com.alipay.sdk.app.PayTask;
import java.util.Map;
import asbamboo.java.sdk.model.TradePayRequest;

public class Client {

    public interface CallbackHandler{
        void onSuccess(String result);
        void onDealing();
        void onCancel();
        void onError();
    }

    public void pay(final Activity context, final TradePayRequest request, final CallbackHandler callback){
        //序列化请求参数
        final String pay_params   = "";
        this.request;

        //调起支付宝
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask pay_task = new PayTask(context);
                final Map<String, String> pay_result = pay_task.payV2(pay_params, true);
                // 解析支付结果
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(pay_result.toString());
                    }
                });
            }
        }).start();
    }
}
