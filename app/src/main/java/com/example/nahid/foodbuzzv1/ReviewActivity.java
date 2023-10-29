package com.example.nahid.foodbuzzv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.nahid.foodbuzzv1.SettingActivity.useru;

public class ReviewActivity extends AppCompatActivity
{
    private EditText Caption;
    private String Description;
    private String caption;
    private String Rname;
    private String saveCurrentDate;
    private String saveCurrentTime;
    private Button AddNewRevButton;
    private ImageView InputRevImage;
    private EditText InputCaption, InputRevDescription, InputRestName;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference RevImagesRef;
    private DatabaseReference RevRef;
    private ProgressDialog loadingBar;
    public FirebaseAuth mAuth;
    private String varuid;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        //Caption = getIntent().getExtras().get("Caption").toString();
        RevImagesRef = FirebaseStorage.getInstance().getReference().child("Review Images");
        RevRef = FirebaseDatabase.getInstance().getReference().child("Review");
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser userinfo = FirebaseAuth.getInstance().getCurrentUser();
        varuid = userinfo.getUid().toString();


        AddNewRevButton = (Button) findViewById(R.id.add_new_rev);
        InputRevImage = (ImageView) findViewById(R.id.select_review_image);
        Caption = (EditText) findViewById(R.id.rev_title);
        InputRevDescription = (EditText) findViewById(R.id.rev_description);
        InputRestName = (EditText) findViewById(R.id.rev_rest_name);
        loadingBar = new ProgressDialog(this);


        InputRevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewRevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }



    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputRevImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {
        Description = InputRevDescription.getText().toString();
        Rname = InputRestName.getText().toString();
        caption = Caption.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Insert a photo for your review", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write review description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Rname))
        {
            Toast.makeText(this, "Please write restaurant name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(caption))
        {
            Toast.makeText(this, "Please give your review a title", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("New Review");
        loadingBar.setMessage("Please wait while review are being posted");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = RevImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(ReviewActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(ReviewActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();


                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("revid", productRandomKey);
        productMap.put("revdate", saveCurrentDate);
        productMap.put("revtime", saveCurrentTime);
        productMap.put("revdescription", Description);
        productMap.put("revimage", downloadImageUrl);
        productMap.put("revuid", useru);
        productMap.put("restname", Rname);
        productMap.put("revcaption", caption);

        RevRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                           // Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                           // startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(ReviewActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReviewActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(ReviewActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
