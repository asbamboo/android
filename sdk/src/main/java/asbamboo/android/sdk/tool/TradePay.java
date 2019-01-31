package asbamboo.android.sdk.tool;

import asbamboo.java.sdk.model.TradePayRequest;
import asbamboo.java.sdk.model.TradePayResponse;

public class TradePay {

    private TradePayRequest api;

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

    public String execute(){
        TradePayResponse api_response = this.apiRequest();
        if(api_response.getChannel().equals("ALIPAY_APP")){

        }
        if(api_response.getChannel().equals("WXPAY_APP")){

        }
    }
}
