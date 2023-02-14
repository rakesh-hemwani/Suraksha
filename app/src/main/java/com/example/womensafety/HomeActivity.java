package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;


import android.Manifest;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.biometrics.BiometricPrompt;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeActivity extends AppCompatActivity {



    DbHelper db;
    private LocationManager locationManager;
    private LocationListener locationListener;

    FloatingActionButton floatingActionButton;

    private static final int REQUEST_CHECK_SETTINGS=0x1;

    private  final long MIN_TIME =1000;
    private final long MIN_DIST = 5;
    String Permission[];

     CardView cardView1, cardView2;

    TextView tap1 , tap2;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db=new DbHelper(this);

        cardView1=findViewById(R.id.card1);
        cardView2=findViewById(R.id.card2);
        floatingActionButton=findViewById(R.id.float_button);



        Permission=new String[]{

                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

        };

            ActivityCompat.requestPermissions(this,Permission,PackageManager.PERMISSION_GRANTED);
            turnOnGps();


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String phoneNo = "1091";
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phoneNo));
                startActivity(i);
            }
        });


        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTrack();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this,EditActivity.class));
            }
        });




    }



    private void startTrack() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
        {
            turnOnGps();

        }

            locationListener = new LocationListener() {
                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    turnOnGps();
                }
                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
                @Override
                public void onLocationChanged(Location location) {
                    try{
                        Cursor cursor=new DbHelper(getApplicationContext()).readalldata();

                        String phoneNumber1 = null;
                        String phoneNumber2 = null;
                        String name = null;

                        while(cursor.moveToNext())
                        {
                            name=cursor.getString(1);
                            phoneNumber1 =cursor.getString(2);
                            phoneNumber2 =cursor.getString(3);

                        }

                        SmsManager smsManager = SmsManager.getDefault();
                        StringBuffer smsBody = new StringBuffer();

                        String message = "I am "+name+", I am not feeling safe please track my location from link below"+"\n\nhttp://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                        smsManager.sendTextMessage(phoneNumber1, null, message, null, null);
                        smsManager.sendTextMessage(phoneNumber2, null, message, null, null);
                        Toast.makeText(HomeActivity.this, "Live Location Shared Successfully", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,MIN_DIST,locationListener);

            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
        }

    private void turnOnGps()
    {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000/2);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());


        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                if (e instanceof ResolvableApiException){
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(HomeActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }


}