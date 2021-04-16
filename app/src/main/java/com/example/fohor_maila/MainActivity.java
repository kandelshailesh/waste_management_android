package com.example.fohor_maila;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("token")) {
            String token = sharedPreferences.getString("token", "");
            if (!token.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
            }
        }
    }

    public void openRegister(View v)
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void openLogin(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}