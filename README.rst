Asbamboo Android SDK
============================


索引
----------

#. 简介_

#. 版本要求_

#. 快速体验_

简介
---------------

asbamboo/android项目是，使用android studio开发的，为 http://www.asbamboo.com 开发的android客户端SDK与demo程序。你可以将sdk(library)作为依赖库，导入你的项目。app目录是简单的接入示例，仅供参考。

版本要求
---------------

Android SDK 要求 Android 4.0.3 及以上版本 请使用 Java 8 或以上版本


快速体验
---------------

在android studio中导入整个asbamboo/android项目即可运行demo。

* 配置你的app key 与 app secret
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

    