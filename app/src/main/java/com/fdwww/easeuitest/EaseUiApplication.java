package com.fdwww.easeuitest;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by LiFei on 2016/9/8.
 */
public class EaseUiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        EaseUI.getInstance().init(this, null);  //初始化EaseUI
        EMClient.getInstance().setDebugMode(true);  //设置debug模式

    }
}
