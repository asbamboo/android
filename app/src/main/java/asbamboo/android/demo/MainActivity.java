package asbamboo.android.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import asbamboo.android.sdk.Configure;
import asbamboo.android.sdk.Json;
import asbamboo.android.sdk.tool.TradePay;

public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

    /**
     * 聚合平台应用的app key
     */
    public static final String APP_KEY = "5c3074f378913";

    /**
     * 聚合平台应用的 secret
     *  - 作用于生成签名（sign）
     *  - 为了安全, 你应该在你的服务端处理签名（sign）的生成
     *  - 为了安全, 你不应该让secret泄露到客户端
     *  - 由于这个demo演示时为了说明, 如何使用asbamboo app pay, 为了简单, 所有demo没有服务端.
     */
    public static final String APP_SECRET = "3a91aa8a688e812ad8c9013eea122dae";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    /**
     * 检查支付宝 SDK 所需的权限，并在必要的时候动态获取。
     * 在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。
     * 如果接入支付宝 SDK 的应用 targetSdk 在 23 以下，可以省略这个步骤。
     */
    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1002);

        }
    }

    public void onClick(View v){
        String channel;
        switch (v.getId()){
            case R.id.alipay_button:
                channel = "ALIPAY_APP";
                break;
            case R.id.wxpay_button:
                channel = "WXPAY_APP";
                break;
            default:
                Toast.makeText(this, R.string.paymethod_not_supported, Toast.LENGTH_SHORT).show();
                return;
        }

        Configure.API_URL   = "http://developer.asbamboo.com/api";
        TradePay tool   = new TradePay(tradeData(channel));
        tool.execute(this);
    }

    public void onPaycallback(String json)
    {
        Log.d("return rep", json);
        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        HashMap<String, Object> decode_json = Json.decode(json);
        if(decode_json.get("status").equals("success")) {
            // 支付成功
        }else if(decode_json.get("status").equals("paying")) {
            // 支付中
        }else if(decode_json.get("status").equals("failed")){
            // 支付失败
        }else if(decode_json.get("status").equals("repeated")){
            // 重复请求
        }else if(decode_json.get("status").equals("user_cancel")){
            // 用户中途取消
        }else if(decode_json.get("status").equals("network_exception")){
            // 网络连接出错
        }else if(decode_json.get("status").equals("unknown")){
            // 支付结果未知
        }else{
            // 系统异常
        }
        AlertDialog.Builder alert   = new AlertDialog.Builder(this);
        alert.setTitle("支付结果");
        alert.setMessage(json);
        alert.show();
    }

    /**
     * 生成支付请求的数据信息
     *  - 这个demo为了演示方便, 所以简单的是否一个方法返回数据信息
     *  - 你的应用应该通过自己的服务端生成请求数据
     *
     * @param channel
     * @return
     */
    private HashMap<String, Object> tradeData(String channel) {
        HashMap<String, Object> trade_data  = new HashMap<>();
        trade_data.put("api_name", "trade.pay");
        trade_data.put("format", "json");
        trade_data.put("timestamp", this.getTimestamp());
        trade_data.put("version", "v1.0");
        trade_data.put("channel", channel);
        trade_data.put("out_trade_no", this.getOutTradeNo());
        trade_data.put("title", channel + "支付测试");
        trade_data.put("total_fee", 1);
        trade_data.put("client_ip", "127.0.0.1");
        trade_data.put("notify_url", "your notify url");
        trade_data.put("app_key", APP_KEY);
        trade_data.put("sign", this.makeSign(trade_data));
        return trade_data;
    }

    protected String getTimestamp() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return format.format(date);
    }

    /**
     * 要求外部订单号必须唯一。
     * @return
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * 生成签名
     *  - 这个demo只是做简单的演示, 没有服务端.
     *  - 为了安全, 你应该在服务端生成sign,
     *  - 为了安全, secret不要泄露到客户端
     *
     * @param data
     * @return
     */
    private String makeSign(HashMap<String, Object> data)
    {
        String[] keys = (String[])data.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder query = new StringBuilder();
        String[] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String key = var3[var5];
            String value = data.get(key).toString();
            query.append(key).append(value);
        }

        query.append(APP_SECRET);
        return this.md5(query.toString());
    }

    private String md5(String sign_string)
    {
        MessageDigest md5;
        byte[] bytes;
        try{
            md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(sign_string.getBytes("UTF-8"));
            StringBuilder sign_builder = new StringBuilder();

            for(int i = 0; i < bytes.length; ++i) {
                String hex = Integer.toHexString(bytes[i] & 255);
                if (hex.length() == 1) {
                    sign_builder.append("0");
                }

                sign_builder.append(hex.toUpperCase());
            }

            return sign_builder.toString();
        }catch(Exception e){

        }
        return "";
    }
}
