package com.neusoft.liufeiyu.smartrefrigerator;
/***
 * 0121-刘飞宇
 * 设定启动页面，延时3秒关闭
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (!StartActivity.this.isFinishing()) {
                    Intent intent = new Intent(StartActivity.this,MainActivity.class);//3秒之后跳转到主界面执行
                    startActivity(intent);
                    StartActivity.this.finish();
                }
            }
        }, 3000);
    }
}
