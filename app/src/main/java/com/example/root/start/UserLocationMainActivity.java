package com.example.root.start;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class UserLocationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    FirebaseAuth auth;
    GoogleApiClient client;
    LocationRequest request;
    GoogleMap mMap;
    LatLng latLng,friendLatlng;
    DatabaseReference databaseReference;


    FirebaseUser user;
    String current_user_name,current_user_email,current_user_imagerUrl,inviteCode;
    Marker marker;
    TextView c_name,c_email;
    ImageView c_image;
    FloatingActionButton fab;
    String userid,friendLat,friendLng,friendName,friendImage;
    Location myLocation,friendLocation;
    boolean partnerNotPresentMessage = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent myintent = getIntent();
        if(myintent!=null){
            userid = myintent.getStringExtra("userid");
            Toast.makeText(getApplicationContext(),userid,Toast.LENGTH_LONG).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //accesing data from firebase to display on side bar
        View header = navigationView.getHeaderView(0);
        c_name = header.findViewById(R.id.name);
        c_email = header.findViewById(R.id.email);
        c_image = header.findViewById(R.id.imageView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");





        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                    current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                    current_user_imagerUrl = dataSnapshot.child(user.getUid()).child("imageUrl").getValue(String.class);
                    inviteCode = dataSnapshot.child(user.getUid()).child("code").getValue(String.class);
                    c_name.setText(current_user_name);
                    c_email.setText(current_user_email);
                Picasso.get().load(current_user_imagerUrl).into(c_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude),12.0f));
            }
        });




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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);


        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //connection current location
        client.connect();

    }














    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sharingOn)
        {
            databaseReference.child(user.getUid()).child("issharing").setValue("true");
            Intent myintent = new Intent(UserLocationMainActivity.this,LocationShareService.class);
            //startForegroundService(myintent);
            startService(myintent);

        }
        else if (id == R.id.sharingOff)
        {
          //  databaseReference.child(user.getUid()).child("issharing").setValue("false");
            Intent myintent = new Intent(UserLocationMainActivity.this,LocationShareService.class);
            stopService(myintent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_joinCircle)
        {
            Intent myintent = new Intent(UserLocationMainActivity.this,JoinCircleActivity.class);
            startActivity(myintent);
        }
        else if (id == R.id.nav_myCircle) {
            Intent myintent = new Intent(UserLocationMainActivity.this,MyCircleActivity.class);
            startActivity(myintent);

        }
        else if (id == R.id.nav_joinedCircle) {

        }
        else if (id == R.id.nav_inviteMembers) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "Hello \n I am "+ current_user_name + "\nJoin My Circle to share location. \n My circle code is\n"+ inviteCode );
                startActivity(i.createChooser(i, "Invite using: "));



        }
        else if (id == R.id.nav_shareLocation) {
            if(latLng!=null) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "My Location is: " + "https://www.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
                startActivity(i.createChooser(i, "Share using: "));
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to get Your Location",Toast.LENGTH_SHORT).show();
            }

        }
        else if (id == R.id.nav_editprofile)
        {
            Intent myintent = new Intent(UserLocationMainActivity.this,UpdateDetailsActivity.class);
            startActivity(myintent);

        }

        else if (id == R.id.signOut) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                auth.signOut();
                finish();
                Intent myintent = new Intent(UserLocationMainActivity.this, step1.class);
                startActivity(myintent);
            }


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(3000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
        //after this we receive our location in onLocationChanged method

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location)
    {
        mMap.clear();


        if (location==null){
            Toast.makeText(getApplicationContext(),"Could not get your Location",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (marker!=null){
                marker.remove();
            }
            myLocation = location;
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
           // mMap.setMaxZoomPreference(5);

            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.title("Current Location");

            marker = mMap.addMarker(options);
           // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),12.0f));
        }

        if (userid!=null)
        {
            showFriendLocation();
        }

    }



    public void showFriendLocation()
    {
        friendLocation =new Location("");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).child("issharing").getValue(String.class).equals("true"))
                {
                    friendLat = dataSnapshot.child(userid).child("lat").getValue(String.class);
                    friendLng = dataSnapshot.child(userid).child("lng").getValue(String.class);
                    friendName = dataSnapshot.child(userid).child("name").getValue(String.class);
                    friendImage = dataSnapshot.child(userid).child("name").getValue(String.class);

                    //creating location to calculate distace
                    friendLocation.setLatitude(Double.parseDouble(friendLat));
                    friendLocation.setLongitude(Double.parseDouble(friendLng));


                    friendLatlng = new LatLng(Double.parseDouble(friendLat),Double.parseDouble(friendLng));

                    MarkerOptions options1 = new MarkerOptions();
                    options1.position(friendLatlng);
                    options1.title(friendName);
                    options1.snippet("Distance " + new DecimalFormat("#.#").format((myLocation.distanceTo(friendLocation))/1000) + " KM");
                    options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    marker = mMap.addMarker(options1);

                   // String URL = getURL(latLng,friendLatlng,"driving");

                    partnerNotPresentMessage = false;
                }
                else if (partnerNotPresentMessage)
                {
                    Toast.makeText(getApplicationContext(),"Your Partner is not sharing Location",Toast.LENGTH_LONG).show();
                    partnerNotPresentMessage = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });

    }


//
//    public String getURL(LatLng origin,LatLng dest, String directionMode)
//    {
//        //origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        //Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        //Mode
//        String mode = "mode=" + directionMode;
//        //building parameters
//        String parameters = str_origin + "&" + str_dest + "&" + mode;
//        //output format
//        String output ="json";
//        //building url
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
//        return url;
//    }


}
