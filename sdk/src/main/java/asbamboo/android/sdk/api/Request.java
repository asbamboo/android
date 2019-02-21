//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package asbamboo.android.sdk.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import asbamboo.android.sdk.Configure;

public class Request
{
    public Map<String, Object> request_data;

    public Request(Map<String, Object> request_data){
        this.request_data   = request_data;
    }

    public String submit() throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE HTML>");
        sb.append("<html>");
        sb.append("<body>");
        sb.append("<form method=\"post\" action=\"" + this.getApiUrl() + "\">\n");
        Iterator var2 = this.request_data.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            String value = this.request_data.get(key).toString().replace("\"", "&quot;");
            sb.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />\n");
        }

        sb.append("<input type=\"submit\" value=\"提交\">\n");
        sb.append("</form>\n");
        sb.append("<script>document.forms[0].submit();</script>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    public Response post() throws Exception {

        URL asbamboo_url = new URL(this.getApiUrl());
        Log.d("API接口请求URL:", this.getApiUrl());
        HttpURLConnection conn = (HttpURLConnection)asbamboo_url.openConnection();

        try {
            conn.setConnectTimeout(this.getHttpTimeout());
            conn.setReadTimeout(this.getHttpTimeout());
            conn.setUseCaches(false);
            conn.setRequestProperty("X-Asbamboo-Client-User-Agent", this.getHttpUserAgentValue());
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream out = conn.getOutputStream();

            try {
                String http_query = this.httpBuildQuery();
                out.write(http_query.getBytes("UTF-8"));
                Log.d("API接口请求参数:\n", http_query);
            } finally {
                if (out != null) {
                    out.close();
                }

            }

            int http_res_code = conn.getResponseCode();
            StringBuilder http_res_data = new StringBuilder();
            Map<String, List<String>> http_res_headers = conn.getHeaderFields();
            InputStream input;
            if (http_res_code >= 200 && http_res_code < 300) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            BufferedReader bf_reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

            String line;
            while((line = bf_reader.readLine()) != null) {
                http_res_data.append(line);
            }

            Log.d("API接口响应HTTPCODE:", String.valueOf(http_res_code));
            Log.d("API接口响应值:\n", http_res_data.toString());
            Response var11 = Response.create(http_res_code, http_res_data.toString(), http_res_headers);
            return var11;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
    }

    protected String getApiUrl() {
        return Configure.API_URL;
    }

    public String getHttpUserAgentValue() {
        return Configure.HTTP_HEADER_USER_AGENT_VALUE;
    }

    public String httpBuildQuery() throws IOException {
        StringBuilder query = new StringBuilder();
        Iterator var2 = this.request_data.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            String value = this.request_data.get(key).toString();
            key = URLEncoder.encode(key, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            query.append(key + "=" + value + "&");
        }

        String query_string = query.toString();
        query_string = query_string.substring(0, query_string.length() - 1);
        return query_string;
    }

    protected Integer getHttpTimeout() {
        return Configure.HTTP_REQUEST_TIMEOUT;
    }
}
