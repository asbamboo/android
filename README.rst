Asbamboo Android SDK
============================


索引
----------

#. 简介_

#. 环境需求_

#. 快速体验_

#. 引用SDK_

#. 修改AndroidManifest.xml向系统申请权限_

#. 通过SDK调起支付工具_

#. 开发与生产环境_

#. SDK支持的支付渠道_

简介
---------------

asbamboo/android项目是，使用android studio开发的，为 http://www.asbamboo.com 开发的android客户端SDK与demo程序。你可以将sdk(library)作为依赖库，导入你的项目。app目录是简单的接入示例，仅供参考。

环境需求
---------------

Android SDK 要求 Android 4.0.3 及以上版本 请使用 Java 8 或以上版本


快速体验
---------------

在android studio中导入整个asbamboo/android项目即可运行demo。

**你需要修改配置 app key 与 app secret 设置为你自己的**

::

    // 文件 app/src/main/java/asbamboo/android/demo/MainActivity.java
    
    package asbamboo.android.demo;

    ...

    public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

        ... 

        /**
         * 聚合平台应用的app key
         *  - 在 http://www.asbamboo.com 中获取。
         */
        public static final String APP_KEY = "xxxxxxxxxx";
    
        /**
         * 聚合平台应用的 secret
         *  - 在 http://www.asbamboo.com 中获取。
         */
        public static final String APP_SECRET = "xxxxxxxxxxxxxxxxxxxxxxxxx";

        ...
    }

引用SDK
--------------------------

下载libs/asbamboo-android-sdk-1.0.aar文件，并且在你的项目中引用它。

gradle 导入方式

#. 将sdk文件下载到你的项目的app/libs目录下。
#. 修改build.gradle文件中的dependencies：

    ::

        ...

        dependencies {
            implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
        }

        ....


修改AndroidManifest.xml向系统申请权限
---------------------------------------------------

::

    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"

        ...
        
        <uses-permission android:name="android.permission.INTERNET"/>
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
        <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

        ...
        
    </manifest>

**在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。**    

通过SDK调起支付工具
-------------------------------

SDK将替你调用trade.pay接口（目前SDK仅支持两种渠道（channel）ALIPAY_APP与WXPAY_APP，然后根据响应值中的app_pay_json调起第三方支付工具, 在接收到支付结果后，通过回调方法 onPaycallback 告诉你支付的结果。

#. 调用SDK的activity需要implements TradePay.AcvivityInterface。

    ::
    
        ...
        
        import android.support.v7.app.AppCompatActivity;
        import asbamboo.android.sdk.tool.TradePay;

        public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

            ...
            
        }

#. 申请系统权限

    ::
    
        ...
        
        import android.content.pm.PackageManager;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v4.content.ContextCompat;
        import asbamboo.android.sdk.tool.TradePay;

        public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

            ...
            
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                //申请获取权限
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

            ...                    
        }

#. 使用TradePay调起支付工具，其中实例化TradePay传递的初始化参数是一个HashMap值，具体的key请查阅trade.pay接口文档。

    ::

        ...
        
        import android.support.v7.app.AppCompatActivity;
        import asbamboo.android.sdk.tool.TradePay;

        public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

            ...
            
            public void onClick(View v){
            
                HashMap<String, Object> trade_data  = new HashMap<>();
                trade_data.put("api_name", "trade.pay");
                trade_data.put("format", "json");
                trade_data.put("timestamp", this.getTimestamp());
                trade_data.put("version", "v1.0");
                trade_data.put("channel", "ALIPAY_APP");
                trade_data.put("out_trade_no", this.getOutTradeNo());
                trade_data.put("title", channel + "支付测试");
                trade_data.put("total_fee", 1);
                trade_data.put("client_ip", "127.0.0.1");
                trade_data.put("notify_url", "your notify url");
                trade_data.put("app_key", YOUR_APP_KEY);
                trade_data.put("sign", "sign string");
        
                TradePay tool   = new TradePay(trade_data);
                tool.execute(this);
            }

            ...
            
        }




#. 实现 TradePay.AcvivityInterface 的 onPaycallback 方法该方法接收的参数是一个json字符串

    **该json字符串说明：**
    
    :字段: 说明
    
    :status: 状态

    :message: 对状态的描述

    :pay_info: 交易信息（未支付成功时，可能是null）

    **status字段的取值范围:**

    :success: 支付成功
    
    :paying: 支付中

    :failed: 支付失败

    :repeated: 重复请求

    :user_cancel: 用户取消

    :network_exception: 网络异常

    :unknown: 支付结果未知

    **pay_info字段的包含如下字段信息**

    :字段: 说明

    :channel: 支付渠道，应该等钱请求参数中的channel

    :in_trade_no: 交易编号是唯一的(asbamboo.com系统生成)

    :title: 交易标题

    :out_trade_no: 交易编号(你的系统中的编号)
    
    :total_fee: 交易金额 单位为分 

    :client_ip: 客户端ip

    :trade_status: 交易状态 NOPAY[尚未支付] CANCLE[取消支付] PAYFAILED[支付失败] PAYING[正在支付] PAYOK[支付成功-可退款] PAYED[支付成功-不可退款]

    :payok_ymdhis: 交易支付成功[可退款]时间(YYYY-mm-dd HH:ii:ss)

    :payed_ymdhis: 交易支付成功[不可退款]时间(YYYY-mm-dd HH:ii:ss)

    :cancel_ymdhis: 交易取消时间(YYYY-mm-dd HH:ii:ss)

    响应值json示例:

    ::

        {
          "status": "success",
          "message": "支付成功",
          "data": {
            "title": "ALIPAY_APP测试",
            "payok_ymdhis": "2019-02-27 20:11:12",
            "in_trade_no": "1905754946510194",
            "payed_ymdhis": "",
            "cancel_ymdhis": "",
            "total_fee": 1,
            "out_trade_no": "0227201055-1474",
            "channel": "ALIPAY_APP",
            "trade_status": "PAYOK",
            "client_ip": "123.123.123.123"
          }
        }

    你的onPaycallback方法看起来应该像这个样子:

    ::

        ...
        
        import android.support.v7.app.AppCompatActivity;
        import asbamboo.android.sdk.tool.TradePay;

        public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

            ...
            
            public void onPaycallback(String json)
            {
                HashMap<String, Object> decode_json = json_decode(json);
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
            }

            ... 
        }

开发与生产环境
-----------------------------------------------
默认情况下SDK是作为生产环境请求支付的。但是你可以通过asbamboo.android.sdk.Configure.API_URL修改当前环境。

* asbamboo.android.sdk.Configure.API_URL = "http://developer.asbamboo.com/api"(开发测试)

* asbamboo.android.sdk.Configure.API_URL = "http://api.asbamboo.com"(生产环境)

SDK支持的支付渠道
------------------------------------------

:ALIPAY_APP: 支付宝APP支付

:WXPAY_APP: 微信APP支付
