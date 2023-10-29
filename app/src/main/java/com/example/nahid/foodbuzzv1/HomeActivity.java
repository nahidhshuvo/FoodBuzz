package com.example.nahid.foodbuzzv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nahid.foodbuzzv1.Model.Rev;
import com.example.nahid.foodbuzzv1.ViewHolder.RestViewHolder;
import com.example.nahid.foodbuzzv1.ViewHolder.RevViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.nahid.foodbuzzv1.SettingActivity.useru;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    private DatabaseReference RevRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public FirebaseAuth mAuth;
    private String showname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);




        userNameTextView.setText(useru);



//        if(user.getDisplayName() != null) {
//            userNameTextView.setText(user.getDisplayName());
//            profileImageView.setImageURI(user.getPhotoUrl());
//
//        }else
//        {
//            userNameTextView.setText(user.getUid().toString());
//        }


        RevRef = FirebaseDatabase.getInstance().getReference().child("Review");
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);






    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Rev> options =
                new FirebaseRecyclerOptions.Builder<Rev>()
                        .setQuery(RevRef, Rev.class)
                        .build();


        FirebaseRecyclerAdapter<Rev, RevViewHolder> adapter =
                new FirebaseRecyclerAdapter<Rev, RevViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RevViewHolder holder, int position, @NonNull Rev model) {

                        holder.tvcaption.setText(model.getRevcaption());
                        holder.tvDescription.setText(model.getRevdescription());
                        holder.tvPrice.setText("Restaurent: " + model.getRestname());
                        holder.tvuname.setText(model.getRevuid());
                        Picasso.get().load(model.getRevimage()).into(holder.ivrev);


                    }


                    @NonNull
                    @Override
                    public RevViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurent_layout, parent, false);
                        RevViewHolder holder = new RevViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.nav_setting) {
//
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id==R.id.nav_review){
            Intent intent = new Intent(HomeActivity.this, ReviewActivity.class);
            startActivity(intent);

        }if (id==R.id.nav_setting){

            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);

        }else if (
                id==R.id.nav_terms){

        }else if (id==R.id.nav_about){

        }else if (id==R.id.nav_help){

        }else if (id == R.id.nav_logout) {

            mAuth.signOut();
            Toast.makeText(HomeActivity.this,"You are successfully logged out",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
