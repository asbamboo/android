Asbamboo Android SDK
============================


索引
----------

#. 简介_

#. 环境需求_

#. 快速体验_

#. 引用SDK_

#. 通过SDK调起支付工具_

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

通过SDK调起支付工具
-------------------------------

SDK将替你调用trade.pay接口（目前SDK仅支持两种渠道（channel）ALIPAY_APP与WXPAY_APP，然后根据响应值中的app_pay_json调起第三方支付工具, 在接收到支付结果后，通过回调方法 onPaycallback 告诉你支付的结果。

#. 调用SDK的activity需要implements TradePay.AcvivityInterface。并且实现其中的onPaycallback方法（用来处理支付结果）

    ::
    
        ...
        
        import android.support.v7.app.AppCompatActivity;
        import asbamboo.android.sdk.tool.TradePay;

        ...
        
        public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

            ***
            
        }

#. 

#. onPaycallback接收的参数是一个json字符串



两行代码调起支付工具，其中实例化TradePay传递的初始化参数是一个HashMap值，具体的key请查阅trade.pay接口文档。

::

   TradePay tool   = new TradePay(tradeData(channel));
   tool.execute(this);

public class MainActivity extends AppCompatActivity implements TradePay.AcvivityInterface {

onpaycallback中json结果的说明


通过修改asbamboo.android.sdk.Configure.API_URL，实现开发环境的请求。


AndroidManifest.xml需要获取如下权限
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。