package com.dupleit.chatapp.a1to1chat.Main;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dupleit.chatapp.a1to1chat.Main.adapter.sectionPagerAdapter;
import com.dupleit.chatapp.a1to1chat.R;
import com.dupleit.chatapp.a1to1chat.profile.profileActivity;
import com.dupleit.chatapp.a1to1chat.startActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private ViewPager mViewPager;
    private sectionPagerAdapter mAdpater;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.mainpage_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Duple Chat");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        mUserDatabase.keepSynced(true);
        mViewPager = findViewById(R.id.tabPager);
        mTabLayout = findViewById(R.id.mTabLayout);
        mAdpater = new sectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdpater);

        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            sendToStart();

        }else {
            mUserDatabase.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        mUserDatabase.child("online").setValue("false");

    }

    private void sendToStart() {
        startActivity(new Intent(this,startActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main_drawer,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Logout) {
            mAuth.getInstance().signOut();
            sendToStart();
            return true;
        }
        if (id == R.id.action_Profile) {
            startActivity(new Intent(MainActivity.this, profileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
