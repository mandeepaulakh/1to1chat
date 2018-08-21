package com.dupleit.chatapp.a1to1chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dupleit.chatapp.a1to1chat.login.LoginActivity;
import com.dupleit.chatapp.a1to1chat.register.registerActivity;

public class startActivity extends AppCompatActivity {
    Button buttonForRegister,buttonForLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        buttonForRegister = findViewById(R.id.buttonForRegister);
        buttonForLogin =findViewById(R.id.buttonForLogin);

        buttonForRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(startActivity.this,registerActivity.class));
            }
        });
        buttonForLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(startActivity.this,LoginActivity.class));
            }
        });
    }
}
