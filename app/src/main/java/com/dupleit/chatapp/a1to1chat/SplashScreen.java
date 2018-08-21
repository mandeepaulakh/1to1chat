package com.dupleit.chatapp.a1to1chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dupleit.chatapp.a1to1chat.Main.MainActivity;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    TextView splashText;
    private static final int REQUEST= 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashText = findViewById(R.id.splashText);
        /*Typeface type = Typeface.createFromAsset(getAssets(),"fonts/LatoRegular.ttf");
        splashText.setTypeface(type);
*/
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                checkPermissionUser();
            }
        }, 3000);
    }
    private void checkPermissionUser() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
            if (!hasPermissions(SplashScreen.this, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions(SplashScreen.this, PERMISSIONS, REQUEST);
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");
                goToEvidenceActivity();
            }
        } else {
            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
            goToEvidenceActivity();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    goToEvidenceActivity();

                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(SplashScreen.this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }else
                {
                    goToEvidenceActivity();
                    return true;
                }
            }
        }
        return true;
    }

    private void goToEvidenceActivity() {
        Intent intent  = new Intent(SplashScreen.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }


}
