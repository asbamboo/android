package asbamboo.android.sdk.tool;

import android.arch.core.util.Function;
import android.content.Context;

import asbamboo.android.alipay.*;
import asbamboo.java.sdk.model.TradePayRequest;
//import asbamboo.java.sdk.model.TradePayResponse;

public class TradePay {

    private TradePayRequest api;

    private FunctionalInterface success;

    public TradePay(){
        this.api = new TradePayRequest();
    }

    public void setChannel(String channel){
        this.api.setChannel(channel);
    }

    public void setClientIp(String client_Ip){
        this.api.setClientIp(client_Ip);
    }

    public void setNotifyUrl(String notify_url){
        this.api.setNotifyUrl(notify_url);
    }

    public void setOutTradeNo(String out_trade_no){
        this.api.setOutTradeNo(out_trade_no);
    }

    public void setReturnUrl(String return_url){
        this.api.setReturnUrl(return_url);
    }

    public void setThirdPart(String third_part){
        this.api.setThirdPart(third_part);
    }

    public void setTitle(String title){
        this.api.setTitle(title);
    }

    public void setTotalFee(Integer total_fee){
        this.api.setTotalFee(total_fee);
    }

    public void setSign(String sign){
        this.api.request_data.put("sign", sign);
    }

    public TradePayResponse apiRequest(){
        return this.api.post(false)();
    }

    public void onSuccess(FunctionalInterface callback){
        this.success    = callback;
    }

    public void execute(final Context context){
//        TradePayResponse api_response = this.apiRequest();
        if(api_response.getChannel().equals("ALIPAY_APP")){
            asbamboo.android.alipay.Client client = new asbamboo.android.alipay.Client();
            client.pay(context, this.api, new asbamboo.android.alipay.Client.CallbackHandler(){

                public asbamboo.android.alipay.Client.CallbackHandler(TradePay trade_pay){
                    this.trade_pay = trade_pay;
                }

                @Override
                public void onSuccess() {
                    this.trade_pay.success();
                }

                @Override
                public void onError() {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onDealing() {

                }
            });
        }if(api_response.getChannel().equals("WXPAY_APP")){

        }
    }
}
