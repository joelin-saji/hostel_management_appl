package com.example.demo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//to fetch image from url
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    ImageView uimage;
    EditText uname, uemail, ugender, uphone_num, ucountry, ucollege;
    Button update_button;

    //to store in realtime database
    DatabaseReference dbreference;

    //to store in firebase storage
    StorageReference storageReference;

    //to upload file
    Uri filepath;
    //to display image
    Bitmap bitmap;

    //to get user id
    String UserID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        uimage = (ImageView)findViewById(R.id.uimage);
        uname = (EditText)findViewById(R.id.uname);
        uemail = (EditText)findViewById(R.id.uemail);
        ugender = (EditText)findViewById(R.id.ugender);
        uphone_num = (EditText)findViewById(R.id.uphone_num);
        ucountry = (EditText)findViewById(R.id.ucountry);
        ucollege = (EditText)findViewById(R.id.ucollege);
        update_button = (Button)findViewById(R.id.update_button);

        //to get the current user, and get their id
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();

        //initialize firebase
        dbreference = FirebaseDatabase.getInstance().getReference().child("userprofile");
        storageReference = FirebaseStorage.getInstance().getReference();


        //to browse the image and display
        uimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dexter - runtime permissions
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Please select file"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        }).check();


            }
        });

        //update button
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatetofirebase();
            }
        });
    }

    //To click on the image view and browse the image and display in image view
//if everything math(result code)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK)
        {
            //to upload to firebase
            filepath=data.getData();
            try{
                //to get the path of the image
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                //display image in the display view
                uimage.setImageBitmap(bitmap);
            }catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    //called when update button is clicked
    public void updatetofirebase() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File Uploader");
        pd.show();

        //to take the link of the image in this format from storage and put file in file path
        final StorageReference uploader = storageReference.child("profileimages/" + "img" + System.currentTimeMillis());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //map is an object, which has all the information to put under the UID - in "userprofile"
                                final Map<String, Object> map = new HashMap<>();
                                map.put("uimage", uri.toString());
                                map.put("uname", uname.getText().toString());
                                map.put("uphone_num", uphone_num.getText().toString());
                                map.put("uemail", uemail.getText().toString());
                                map.put("ugender", ugender.getText().toString());
                                map.put("ucountry", ucountry.getText().toString());
                                map.put("ucollege", ucollege.getText().toString());

                                //to check if the object is already there, if it is there, then we update the profile, if its not, then we upload and create one
                                dbreference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            //if exists - then update under the UID
                                            dbreference.child(UserID).updateChildren(map);
                                        } else {
                                            //else insert map under a new UID
                                            dbreference.child(UserID).setValue(map);
                                        }

                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                    }
                })

                //while uploading
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        //how much uploaded / how much byte
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :" + (int) percent + "%");
                    }
                });
    }


// to check if the user already created a profile earlier, using UID
    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserID = user.getUid();
        dbreference.child(UserID).addValueEventListener(new ValueEventListener(){
            @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    uname.setText(snapshot.child("uname").getValue().toString());
                    uphone_num.setText(snapshot.child("uphone_num").getValue().toString());
                    uemail.setText(snapshot.child("uemail").getValue().toString());
                    ugender.setText(snapshot.child("ugender").getValue().toString());
                    ucountry.setText(snapshot.child("ucountry").getValue().toString());
                    ucollege.setText(snapshot.child("ucollege").getValue().toString());
                    //to show the image from the link
                    Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                }
            }

            @Override
                    public void onCancelled(@NonNull DatabaseError error){
            }
        });
    }
}