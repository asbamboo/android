package asbamboo.android.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import asbamboo.android.sdk.tool.TradePay;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        String channel;
        switch (v.getId()){
            case R.id.alipay_button:
                channel = "ALIPAT_APP";
                break;
            case R.id.wxpay_button:
                channel = "WXPAY_APP";
                break;
            default:
                Toast.makeText(this, R.string.paymethod_not_supported, Toast.LENGTH_SHORT).show();
                return;
        }

        TradePay tool   = new TradePay();
        tool.setChannel(channel);
        tool.setOutTradeNo("12345465467879");
        tool.setTitle("支付测试");
        tool.setTotalFee("1");
        tool.setClientIp("127.0.0.1");
//        tool.setNotifyUrl("notify_url");
        tool.setSign("签名");
        String result = tool.execute();
        //你应在需要在你的服务端处理支付结果,并生成对应的客户端显示信息.
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}
