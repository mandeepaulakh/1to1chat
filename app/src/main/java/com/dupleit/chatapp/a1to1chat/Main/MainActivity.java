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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

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

        }
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
