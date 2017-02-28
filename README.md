# EaseUiTest
EaseUI环信集成
   对于现在APP端的聊天功能一直是比较感兴趣的，借此闲暇时间来对EaseUI进行了一定的了解，从而写了一个测试的Demo
   1.首先需要在环信平台进行开发者注册，https://console.easemob.com/index.html
   注册成功后创建应用，如下图所示
   ![这里写图片描述](http://img.blog.csdn.net/20160908162531439)
   注册模式可以根据需要设置，我这里选择的是开发模式
   点击“确定”后进入应用名称后台如下图

![这里写图片描述](http://img.blog.csdn.net/20160908163454092)
2.在环信官网上下载最新的SDK，解压后为
![这里写图片描述](http://img.blog.csdn.net/20160908163724489)
其中doc为环信的开发文档，libs下是所需的jar包，tools主要是为了对一些使用环信低版本升级时所需的配置文件，我们主要使用的是examples中的easeui，examples中另外一个是环信官方的Demo
3.在我们新建的AS中导入EaseUI，过程为“File-new-import module”,导入后尽量将easeui中的sdk版本配置成和自已项目中的一致，以免发生不兼容问题，最后将easeui关联到我们的项目中
![这里写图片描述](http://img.blog.csdn.net/20160908164518681)
4.关联后运行一次，看是否报错，我这里就遇到了一个，如图
![这里写图片描述](http://img.blog.csdn.net/20160908164717779)
最后查到是自已的gradle版本太低了，我使用的是1.5，我改成了2.0后就好了


----------


**代码开始部分**
1.清单文件中添加所需的权限

```
<uses-permission android:name="android.permission.VIBRATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.GET_TASKS"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
```
设置环信APPkey

```
<!-- 设置环信应用的AppKey -->
        <meta-data
            android:name="EASEMOB_APPKEY"
            android:value="you key"/>
        <!-- 声明SDK所需的service SDK核心功能-->
        <service
            android:name="com.hyphenate.chat.EMChatService"
            android:exported="true"/>
        <!-- 声明SDK所需的receiver -->
        <receiver android:name="com.hyphenate.chat.EMMonitorReceiver">
            <intent-filter>
                <action android:name="android.intent.action.PACKAGE_REMOVED"/>
                <data android:scheme="package"/>
            </intent-filter>
            <!-- 可选filter -->
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
                <action android:name="android.intent.action.USER_PRESENT"/>
            </intent-filter>
        </receiver>
```
2.初始化EaseUI，我这里是新建了EaseUIApplication并继承了Application，并在里面进行了初始化，这个按需求来做

```
public class EaseUiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        EaseUI.getInstance().init(this, null);  //初始化EaseUI
        EMClient.getInstance().setDebugMode(true);  //设置debug模式

    }
}
```
3.EaseUI注册，这里需要异步的，是没有回调的

```
  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(mName.getText().toString().trim(),
                                    mPwd.getText().toString().trim());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Log.i("lf","注册失败  "+e.getErrorCode()+" , "+e.getMessage());
                        }
                    }
                }).start();
```
4.EaseUI登录，参数中有回调的

```
EMClient.getInstance().login(mName.getText().toString().trim(),
                        mPwd.getText().toString().trim(), new EMCallBack() {
                            @Override
                            public void onSuccess() {  //登录成功
                                startActivity(new Intent(MainActivity.this,MainActivity.class));
                            }

                            @Override
                            public void onError(int i, String s) {  //登录失败
                                Log.i("lf","登录失败"+i+" , "+s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
```
里面的三个方法可以按需求进行逻辑处理，效果图
![这里写图片描述](http://img.blog.csdn.net/20160908170054501)
注：
            在注册时如果遇到错误，会返回各种对应的ErrorCode，这个可以去环信上查对应的原因，我这里遇到的一个是"208，Registration failed."，这是因为我在创建应用时注册模式我选择的是“授权模式”，改成“开放模式”就好了；
5.退出登录，有回调，同样可以按需求进行相应的逻辑处理

```
 EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("lf","退出登录失败"+i+" , "+s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
```
![这里写图片描述](http://img.blog.csdn.net/20160908170930606)
6.“发起聊天”界面，创建一个Activity，里面一个空布局

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/layout_chat"
        android:orientation="vertical">

    </LinearLayout>
</LinearLayout>
```
在该activity中使用环信封装好的聊天界面

```
public class ChatActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);


        EaseChatFragment easeChatFragment = new EaseChatFragment();  //环信聊天界面
        easeChatFragment.setArguments(getIntent().getExtras()); //需要的参数
        getSupportFragmentManager().beginTransaction().add(R.id.layout_chat,easeChatFragment).commit();  //Fragment切换

    }
}
```
7.通过“发起聊天”按钮跳转到聊天界面

```
 if (!TextUtils.isEmpty(mNumber.getText().toString().trim())){
                    Intent chat = new Intent(this,ChatActivity.class);
                    chat.putExtra(EaseConstant.EXTRA_USER_ID,mNumber.getText().toString().trim());  //对方账号
                    chat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat); //单聊模式
                    startActivity(chat);
                }else{
                    Toast.makeText(LoginActivity.this, "请输入要聊天的对方的账号", Toast.LENGTH_SHORT).show();
                }
```
最后的效果为
![这里写图片描述](http://img.blog.csdn.net/20160908171901697)
另外，在发送图片后查看图片以及发送位置时会报错，这是因为easeui中对这两个相对应的activity没有在清单文件中注册，需要我们自已来写

```
<meta-data
        android:name="com.baidu.lbsapi.API_KEY"
        android:value="Baidu AppKey"/>

    <!-- 定位 -->
    <service
        android:name="com.baidu.location.f"
        android:enabled="true"
        android:process=":remote">
    </service>


    <!-- EaseUI中发送图片后查看以及发送地理位置activit注册   -->
    <activity android:name="com.hyphenate.easeui.ui.EaseBaiduMapActivity"/>
    <activity android:name="com.hyphenate.easeui.ui.EaseShowBigImageActivity"/>
```
以上就可以实现简单的消息发送了


----------
注：①在真正开始写项目前，一定要确定是否有百度地图和环信聊天的需要，如果都有的话，就不用集成百度地图了。如果先集成百度地图，在集成环信的话，这样会引起许多的问题，包括包的冲突，so文件的冲突，以及加入混淆后安装在手机上打不开APP等等各种问题，解决起来是很麻烦的
      ②对于环信聊天界面的优化以及功能的添加，只需要到集成的easeui中修改即可

就写到这里，哪里有些的不对的或者不好的，请各位指点一番，谢谢，下班走起

