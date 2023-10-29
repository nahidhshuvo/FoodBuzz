package com.example.nahid.foodbuzzv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private  TextView uid;
    private EditText username,address;
    private Button save;
    private String varuid;
   public static String useru;
    private String add;

    public FirebaseAuth mAuth;
    private DatabaseReference SetRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SetRef = FirebaseDatabase.getInstance().getReference().child("User");
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser userid = FirebaseAuth.getInstance().getCurrentUser();


        uid = (TextView) findViewById(R.id.tvuid);
        username = (EditText) findViewById(R.id.etuname);
        address = (EditText) findViewById(R.id.etaddress);
        save = (Button) findViewById(R.id.btn_update);


        uid.setText(userid.getUid().toString());
        varuid = userid.getUid().toString();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                useru = username.getText().toString();


                add = address.getText().toString();
                Updatedata();

                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(intent);




            }
        });



    }




    private void Updatedata() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("uid",varuid );
        productMap.put("uname", useru);
        productMap.put("uadd", add);


        SetRef.child(varuid).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            Toast.makeText(SettingActivity.this, "Information Updated", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            Toast.makeText(SettingActivity.this, "Error" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }



}







