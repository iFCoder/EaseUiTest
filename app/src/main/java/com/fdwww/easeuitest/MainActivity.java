package com.fdwww.easeuitest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mName,mPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        mName = (EditText) findViewById(R.id.eu_uname);
        mPwd = (EditText) findViewById(R.id.eu_pwd);

        findViewById(R.id.eu_reg).setOnClickListener(this);
        findViewById(R.id.eu_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.eu_reg:  //注册     //异步方法，没有回调
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(mName.getText().toString().trim(),
                                    mPwd.getText().toString().trim());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Log.i("lifei","注册失败  "+e.getErrorCode()+" , "+e.getMessage());
                        }
                    }
                }).start();

                break;
            case R.id.eu_login:  //登录
                EMClient.getInstance().login(mName.getText().toString().trim(),
                        mPwd.getText().toString().trim(), new EMCallBack() {
                            @Override
                            public void onSuccess() {  //登录成功
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            }

                            @Override
                            public void onError(int i, String s) {  //登录错误
                                Log.i("lifei","登录失败"+i+" , "+s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                break;
        }
    }
}
