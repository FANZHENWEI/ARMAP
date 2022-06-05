package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录界面Activity类

 */
public class LoginActivity extends AppCompatActivity {

    EditText EtStuNumber,EtStuPwd;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvRegister = findViewById(R.id.tv_register);
        setTitle("登录");
        //跳转到注册界面
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        EtStuNumber = findViewById(R.id.et_username);
        EtStuPwd = findViewById(R.id.et_password);
        final CheckBox checkBox=findViewById(R.id.cb);

        String string = getSharedPreferences("data", MODE_PRIVATE).getString("n", "");
        if(!TextUtils.isEmpty(string)){

            EtStuNumber.setText(string);
        }

        String string1 = getSharedPreferences("data", MODE_PRIVATE).getString("p", "");
        if(!TextUtils.isEmpty(string)){

            EtStuPwd.setText(string1);
        }


        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if(CheckInput()) {

                    register(EtStuNumber.getText().toString(),EtStuPwd.getText().toString());


                    //否则提示登录失败,需要重新输入

                }
            }
        });
    }

    public void register(final String s1, final String s2){
        String url = RequestUtil.BASE_URL+"user/login?username=%s&password=%s";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(String.format(url,s1,s2))
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
                                Toast.makeText(LoginActivity.this,"登录成功!",Toast.LENGTH_SHORT).show();

                                getSharedPreferences("data",MODE_PRIVATE).edit().putString("n",s1).putString("p",s2).commit();

                                //销毁当前界面
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(LoginActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
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

    //检查输入是否符合要求
    public boolean CheckInput() {
        String StuNumber = EtStuNumber.getText().toString();
        String StuPwd = EtStuPwd.getText().toString();
        if(StuNumber.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"学号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuPwd.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
