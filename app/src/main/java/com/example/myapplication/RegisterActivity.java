package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;


/**
 * 注册界面Activity类
 */
public class RegisterActivity extends AppCompatActivity {

    EditText tvStuNumber,tvStuPwd,tvStuConfirmPwd,tvXueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnCancel = findViewById(R.id.btn_cancel);
        //返回到登录界面
        setTitle("注册");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStuNumber = findViewById(R.id.et_username);
        tvStuPwd = findViewById(R.id.et_password);
        tvXueId=findViewById(R.id.et_num);
        tvStuConfirmPwd = findViewById(R.id.et_confirm_password);
        //注册点击事件
        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先确保不为空
                if(CheckInput()) {
                    register(tvStuNumber.getText().toString(),tvStuPwd.getText().toString(),tvXueId.getText().toString());

                }
            }
        });
    }

    //判断输入是否符合规范
    public boolean CheckInput() {
        String username = tvStuNumber.getText().toString();
        String password = tvStuPwd.getText().toString();
        String confirm_password = tvStuConfirmPwd.getText().toString();
        String xue=tvXueId.getText().toString();
        if(username.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"用户名不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirm_password.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"确认密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.trim().equals(confirm_password.trim())) {
            Toast.makeText(RegisterActivity.this,"两次密码输入不一致!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(xue.trim().equals("")) {
            Toast.makeText(RegisterActivity.this,"学号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void register(final String s1, String s2, String s3){
        String url = RequestUtil.BASE_URL+"user/regist?username=%s&password=%s&studentno=%s";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(String.format(url,s1,s2,s3))
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String string = response.body().string();
                    final JSONObject jsonObject=new JSONObject(string);
                    if(jsonObject.getBoolean("success")){
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(RegisterActivity.this,"恭喜你注册成功!",Toast.LENGTH_SHORT).show();
                               //销毁当前界面
                               finish();
                           }
                       });
                    }else {
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  Toast.makeText(RegisterActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }

                          }
                      });
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
