package com.dupleit.chatapp.a1to1chat.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.chatapp.a1to1chat.Main.MainActivity;
import com.dupleit.chatapp.a1to1chat.R;
import com.dupleit.chatapp.a1to1chat.helper.checkInternetState;
import com.dupleit.chatapp.a1to1chat.register.registerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class profileActivity extends AppCompatActivity {
    EditText etDisplayName,etContact,etStatus;
    TextView userEmail;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    RelativeLayout frame;
    LinearLayout updateProfile;
    ProgressDialog progressDialog;
    CircleImageView UserProfileImage;
    ImageView changeImage;
    StorageReference mImageStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = new ProgressDialog(profileActivity.this);

        etDisplayName =findViewById(R.id.etDisplayName);
        etContact = findViewById(R.id.etContact);
        etStatus = findViewById(R.id.etStatus);
        userEmail = findViewById(R.id.userEmail);
        frame = findViewById(R.id.frame);
        UserProfileImage =findViewById(R.id.UserProfileImage);
        changeImage = findViewById(R.id.changeImage);
        frame.setVisibility(View.GONE);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        String currentUserId = mCurrentUser.getUid();
        userEmail.setText(mCurrentUser.getEmail());
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        mUserDatabase.keepSynced(true);
        updateProfile =findViewById(R.id.updateProfile);

        if (!checkInternetState.getInstance(profileActivity.this).isOnline()) {

            frame.setVisibility(View.GONE);

            Toasty.warning(profileActivity.this, "No internet connection.", Toast.LENGTH_SHORT, true).show();

        }else {
            getDataFromServer();
            progressDialog.setTitle("Getting Profile");
            progressDialog.setMessage("please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = etDisplayName.getText().toString().trim();
                String edittextContact = etContact.getText().toString().trim();
                String edittextStatus= etStatus.getText().toString().trim();

                if (validateData()){
                    progressDialog.setTitle("Updating Profile");
                    progressDialog.setMessage("please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if (!checkInternetState.getInstance(profileActivity.this).isOnline()) {
                        Toasty.warning(profileActivity.this, "No internet connection.", Toast.LENGTH_SHORT, true).show();

                    }else {
                        update_profile(displayName,edittextContact,edittextStatus);
                    }
                }else {
                    Toasty.warning(profileActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT, true).show();
                }

            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(profileActivity.this);

            }
        });


    }

    private void update_profile(String displayName, String edittextContact, String edittextStatus) {

        mUserDatabase.child("name").setValue(displayName);
        mUserDatabase.child("contact").setValue(edittextContact);
        mUserDatabase.child("status").setValue(edittextStatus);

        progressDialog.dismiss();
        Toasty.success(profileActivity.this, "Profile updated", Toast.LENGTH_SHORT, true).show();


    }

    private boolean validateData() {
        if (etDisplayName.getText().toString().trim().equalsIgnoreCase("")) {
            etDisplayName.setError("Please fill name");
            etDisplayName.requestFocus();
            return false;
        }else if (etContact.getText().toString().trim().equalsIgnoreCase("")) {
            etContact.setError("Please fill contact");
            etContact.requestFocus();
            return false;
        }else if (etContact.getText().toString().length()!=10) {
            etContact.setError("10 digit required");
            etContact.requestFocus();
            return false;
        }else if (etStatus.getText().toString().trim().equalsIgnoreCase("")) {
            etStatus.setError("Please fill status");
            etStatus.requestFocus();
            return false;
        }
        return true;
    }
    private void getDataFromServer() {
        // Read from the database
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                frame.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                String name = dataSnapshot.child("name").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                etDisplayName.setText(name);
                etContact.setText(contact);
                etStatus.setText(status);

                if (!image.equals("default")||image.equals("")){
                    Glide.with(getApplicationContext()).load(image).into(UserProfileImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("databaseError",""+error);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri selectedImageUri = result.getUri();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading Image");
                progressDialog.show();

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");

                //adding the file to reference
                filepath.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {
                            //dismissing the progress dialog
                            final String download_url = task.getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if(thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                   // Toast.makeText(profileActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();
                                                    Toasty.success(profileActivity.this, "Uploading successfully.", Toast.LENGTH_SHORT, true).show();
                                                }

                                            }
                                        });

                                    } else {
                                        Toasty.error(profileActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_SHORT, true).show();
                                        progressDialog.dismiss();

                                    }


                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

                        }
                    });
                    /*.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()){


                                String download_url = task.getResult().getStorage().getDownloadUrl().toString();
                                Log.e("download_url",""+download_url);

                                mUserDatabase.child("image").setValue(download_url);
                                progressDialog.dismiss();
                                Toasty.success(profileActivity.this, "Image updated "+download_url, Toast.LENGTH_SHORT, true).show();
                            }else {
                                Toasty.error(profileActivity.this, "Image not updated", Toast.LENGTH_SHORT, true).show();

                            }

                        }
                    });*/
            }
        }
    }


}
