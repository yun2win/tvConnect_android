[![LOGO](http://8225117.s21i-8.faiusr.com/4/ABUIABAEGAAg5o3ztwUoivKDrgQwuAE4Mg.png)](http://www.yun2win.com)
### tvConnect_android1.0

tvConnect_android1.0 SDK开发android版，提供用户接入我们SDK，能够把图片快速的投屏到电视，大屏看图片，用户不需要开发电视版应用及可实现电视看图片。

-
### 编译环境
编译环境：android studio 2.2.2

-
### 快速接入
#### 1.下载Module下的talklib并导入您的应用作为依赖工程
#### 2.配置文件添加如下代码
     ........
        <!-- 加入应用需要的权限 -->
        <!-- 网络相关 -->
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.WRITE_SETTINGS" />
         .......
        <!-- 添加二维码扫描activity（可以调用用户自己的扫描界面） -->
        <activity
                android:name="com.liyueyun.talklib.ui.activity.CaptureActivity"
                android:screenOrientation="portrait" />        
#### 3.项目Application类onCreate()中初始化（回调可不传，判断是否初始化成功）

`TalkManage.getInstance().initTalk(this, "用户的UserId（可传空字符串）","用户的UserId（可传空字符串，默认显示陌生人）","用户头像Url（可传空字符串）", "AppKey（必填）","AppSecret（必填）",null});                                                                         `

#### 4.投屏图片到电视
`//组建投频消息
String message = TalkManage.getInstance().galleryMsg("投屏类型","文件地址","文件名字");
//推送消息到tv
TalkManage.getInstance().pushMessageTv(message,"电视的UserId（扫描获得）",null);                                                   `
    
-
### SDK 中扫描会等到电视的ID和名字，用户可以自己管理电视实体（添加，删除，修改备注等）
-
### 开发文档
http://console.yun2win.com/docs/android/Y2W_tvConnect/index.html<br>



-
### 链接
官方网站 : http://www.yun2win.com<br>
安卓 : https://github.com/yun2win/yun2win-sdk-android<br>
iOS : https://github.com/yun2win/yun2win-sdk-iOS<br>
Web : https://github.com/yun2win/yun2win-sdk-web<br>
Server : https://github.com/yun2win/yun2win-sdk-server<br>

-
### License
tvConnect-android is available under the MIT license. See the [LICENSE](https://github.com/yun2win/tvConnect_android/blob/master/LICENSE) file for more info.
















