package com.fdwww.easeuitest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by LiFei on 2016/9/8.
 */
public class ChatActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);


        EaseChatFragment easeChatFragment = new EaseChatFragment();
        easeChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.layout_chat,easeChatFragment).commit();


    }
}
