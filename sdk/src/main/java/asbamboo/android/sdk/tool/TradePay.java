package asbamboo.android.sdk.tool;


import java.util.Map;

import asbamboo.android.sdk.api.Request;

public class TradePay {

    private Request request;

    public TradePay(Map<String, Object> trade_data){
        this.request    = new Request(trade_data);
    }

    public interface AcvivityInterface{
        void onPaycallback(String json);
    }

    public void execute(AcvivityInterface context){
        if (this.request.request_data.get("channel").equals("ALIPAY_APP")) {
            asbamboo.android.sdk.alipay.Client client = new asbamboo.android.sdk.alipay.Client();

            client.pay(context, this.request);

        } else if (this.request.request_data.get("channel").equals("WXPAY_APP")) {

        }
    }
}
