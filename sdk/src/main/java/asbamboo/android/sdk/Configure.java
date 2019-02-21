package asbamboo.android.sdk;

import java.io.File;

/**
 * 配置信息
 */
public class Configure
{
    public static String API_URL = "http://developer.asbamboo.com/api";
    public static Integer HTTP_REQUEST_TIMEOUT = 30000;
    public static String HTTP_HEADER_USER_AGENT_VALUE = "ANDROID_SDK_1.0";
    public static String PROJECT_LOG_PATH;

    public Configure() {
    }

    static {
        PROJECT_LOG_PATH = System.getProperty("user.dir").concat(File.separator).concat("var").concat(File.separator).concat("project.log");
    }
}
