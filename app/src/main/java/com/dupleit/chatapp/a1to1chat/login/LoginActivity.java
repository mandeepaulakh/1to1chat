package com.dupleit.chatapp.a1to1chat.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dupleit.chatapp.a1to1chat.Main.MainActivity;
import com.dupleit.chatapp.a1to1chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public EditText  etEmail, etPassword;
    public LinearLayout buttonLoginAccount;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        buttonLoginAccount = findViewById(R.id.createAccount);

        buttonLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edittextEmail = etEmail.getText().toString();
                String edittextPassword = etPassword.getText().toString();

                if (!TextUtils.isEmpty(edittextEmail) || !TextUtils.isEmpty(edittextPassword)) {
                    progressDialog.setTitle("Logging In");
                    progressDialog.setMessage("please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    loginUser(edittextEmail, edittextPassword);
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void loginUser(String edittextEmail, String edittextPassword) {
        mAuth.signInWithEmailAndPassword(edittextEmail, edittextPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loginActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Log.d("loginActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Can't sign in. please check form",
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
