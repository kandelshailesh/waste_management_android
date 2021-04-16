package com.example.fohor_maila;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.fohor_maila.interfaces.Users;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.models.users.Login;
import com.example.fohor_maila.network.Network;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button loginBtn;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.username, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(this,R.id.password, RegexTemplate.NOT_EMPTY,R.string.invalid_password);
        SharedPreferences sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("token")) {
            String token = sharedPreferences.getString("token", "");
            if (!token.isEmpty()) {
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                startActivity(intent);
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setEnabled(false);
                if(awesomeValidation.validate())
                {
                    handleLogin();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter valid information",Toast.LENGTH_SHORT).show();
                    loginBtn.setEnabled(true);
                }
            }
        });
    }

    public  void handleLogin() {
//        result = findViewById(R.id.postList);
        Retrofit retrofit = new Network().getRetrofit1();
        Users users = retrofit.create(Users.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email",username.getText().toString());
            jsonObject.put("password",password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<ResponseBody> call = users.login(jsonObject.toString());
        Log.d("Username",username.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Response",new Gson().toJson(response.errorBody()));
                if(response.isSuccessful()) {
                    loginBtn.setEnabled(true);
                    String re= null;
                    try {
                        re = response.body().string();
                        JSONObject obj = null;
                        obj = new JSONObject(re);
                        String token = null;
                        token = obj.getString("token");
                        String user= null;
                        user= obj.getJSONObject("data").toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token",token);
                        editor.putString("user_info",user);
                        editor.apply();
                        Log.d("Token",token);
                        Log.d("User",user);
                        Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(intent);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

//                    String token = response.body().getToken();


                }
                else
                {
                    Log.d("Login_call", response.code() + "");
                    ApiError error = ErrorUtils.parseError(retrofit, response);
                    Log.d("Error",error.getError());
                    Toast.makeText(LoginActivity.this,error.getError(),Toast.LENGTH_SHORT).show();
                    Log.d("Login_call_error", new Gson().toJson(error));
                    loginBtn.setEnabled(true);
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                loginBtn.setEnabled(true);
                Log.d("Error",t.toString());
            }
        });
    }


    public void openRegister(View v)
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void openDashboard(View v)
    {
        Intent intent = new Intent(this,Dashboard.class);
        startActivity(intent);
    }
}