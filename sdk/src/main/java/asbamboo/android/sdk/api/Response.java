package asbamboo.android.sdk.api;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asbamboo.android.sdk.Json;

public class Response {
    protected boolean is_success = false;
    protected String message;
    protected boolean is_valid_sign = false;
    protected HashMap<String, Object> decoded_data;
    protected Integer http_code;
    protected String http_body;
    protected Map<String, List<String>> http_headers;

    public static Response create(Integer http_code, String http_body, Map<String, List<String>> http_headers) throws Exception {
        return new Response(http_code, http_body, http_headers);
    }

    public Response(Integer http_code, String http_body, Map<String, List<String>> http_headers) throws Exception {
        this.http_code = http_code;
        this.http_body = http_body;
        this.http_headers = http_headers;
        this.parse();
    }

    public boolean getIsSuccess() {
        return this.is_success;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDataCode() {
        return this.getDecodedData().get("code").toString();
    }

    public HashMap<String, Object> getDecodedData() {
        return this.decoded_data;
    }

    public Integer getHttpCode() {
        return this.http_code;
    }

    public String getHttpBody() {
        return this.http_body;
    }

    public Map<String, List<String>> getHttpHeaders() {
        return this.http_headers;
    }

    private void parse() throws Exception {
        if (this.checkHttpCode()) {
            if (this.parseHttpBody()) {
                if (this.checkSign()) {
                    if (this.checkIsSuccess()) {
                        ;
                    }
                }
            }
        }
    }

    private boolean checkHttpCode() {
        if (!this.http_code.equals(200)) {
            this.is_success = false;
            this.message = "HTTP Response Code:" + this.http_code;
            return false;
        } else if (this.http_code.equals(200)) {
            return true;
        } else {
            this.is_success = false;
            return false;
        }
    }

    private boolean parseHttpBody() {
        this.decoded_data = Json.decode(this.getHttpBody());
        if (!this.decoded_data.get("code").equals("0")) {
            this.is_success = false;
            this.message = this.decoded_data.get("message").toString();
        }

        if (this.decoded_data.get("code").equals("0")) {
            this.message = this.decoded_data.get("message").toString();
            return true;
        } else {
            this.is_success = false;
            return true;
        }
    }

    /**
     * sign 没有做校验
     *  - 因为这里不能知道secret
     *
     * @return
     * @throws Exception
     */
    private boolean checkSign() {
        this.is_valid_sign = true;
        return true;
    }

    private boolean checkIsSuccess() {
        if (this.http_code.equals(200) && this.decoded_data.get("code").equals("0") && this.is_valid_sign) {
            this.is_success = true;
            return true;
        } else {
            return false;
        }
    }
}
