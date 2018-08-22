package com.dupleit.chatapp.a1to1chat.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dupleit.chatapp.a1to1chat.Main.MainActivity;
import com.dupleit.chatapp.a1to1chat.R;
import com.dupleit.chatapp.a1to1chat.helper.checkInternetState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class registerActivity extends AppCompatActivity {

    public EditText etDisplayName,etEmail,etPassword,etContact,etStatus;
    public LinearLayout buttonCreateAccount;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(registerActivity.this);
        mAuth = FirebaseAuth.getInstance();
        etDisplayName = findViewById(R.id.etDisplayName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etContact = findViewById(R.id.etContact);
        etStatus = findViewById(R.id.etStatus);
        buttonCreateAccount = findViewById(R.id.createAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = etDisplayName.getText().toString().trim();
                String edittextEmail = etEmail.getText().toString().trim();
                String edittextPassword = etPassword.getText().toString().trim();
                String edittextContact = etContact.getText().toString().trim();
                String edittextStatus= etStatus.getText().toString().trim();

                if (!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(edittextEmail)|| !TextUtils.isEmpty(edittextContact) || !TextUtils.isEmpty(edittextStatus)  || !TextUtils.isEmpty(edittextPassword)){
                    progressDialog.setTitle("Creating Account");
                    progressDialog.setMessage("please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if (!checkInternetState.getInstance(registerActivity.this).isOnline()) {


                        Toasty.warning(registerActivity.this, "No internet connection.", Toast.LENGTH_SHORT, true).show();

                    }else {

                        registerUser(displayName,edittextEmail,edittextPassword,edittextContact,edittextStatus);
                    }
                }else {
                    Toasty.warning(registerActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT, true).show();
                }

            }
        });


    }

    private void registerUser(final String displayName, String edittextEmail, String edittextPassword, final String edittextContact, final String edittextStatus) {
        mAuth.createUserWithEmailAndPassword(edittextEmail, edittextPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("registerActivity", "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String uId = currentUser.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(uId);

                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("name",displayName);
                            userMap.put("contact",edittextContact);
                            userMap.put("status",edittextStatus);
                            userMap.put("image","default");
                            userMap.put("uid",uId);

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(registerActivity.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Log.d("registerActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
