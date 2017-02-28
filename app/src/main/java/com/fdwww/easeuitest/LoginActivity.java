package com.fdwww.easeuitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

/**
 * Created by LiFei on 2016/9/8.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mNumber = (EditText) findViewById(R.id.eu_number);

        findViewById(R.id.eu_chat).setOnClickListener(this);
        findViewById(R.id.eu_loginup).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eu_chat:  //发起聊天
                if (!TextUtils.isEmpty(mNumber.getText().toString().trim())){
                    Intent chat = new Intent(this,ChatActivity.class);
                    chat.putExtra(EaseConstant.EXTRA_USER_ID,mNumber.getText().toString().trim());
                    chat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat); //单聊模式
                    startActivity(chat);
                }else{
                    Toast.makeText(LoginActivity.this, "请输入要聊天的对方的账号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.eu_loginup: //退出登录
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("lifei","退出登录失败"+i+" , "+s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
        }
    }
}
