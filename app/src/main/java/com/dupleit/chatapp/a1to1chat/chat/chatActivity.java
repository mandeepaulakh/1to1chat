package com.dupleit.chatapp.a1to1chat.chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dupleit.chatapp.a1to1chat.R;
import com.dupleit.chatapp.a1to1chat.helper.getTimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class chatActivity extends AppCompatActivity {

    private String recUserId,recUserName;
    private Toolbar mToolbar;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recUserId = getIntent().getStringExtra("recUserId");
        recUserName = getIntent().getStringExtra("recUserName");
        mToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setTitle(recUserName);


        mDatabase.child("users").child(recUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").toString();
                String lastSeen = dataSnapshot.child("lastSeen").toString();
                if (online.equals("true")){
                    getSupportActionBar().setSubtitle(recUserName);
                }else {
                    Long lastseenTime = Long.parseLong(online);
                    getSupportActionBar().setSubtitle(new getTimeAgo().getTimeAgo(lastseenTime,getApplicationContext()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
