package com.example.art.familinkthesislastdraft;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;


import com.example.art.familinkthesislastdraft.model.Tracking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class EmergencyMain extends AppCompatActivity implements LocationListener {

    public double latitude;
    public double longitude;
    private TextView completeadd, lati, longi, chkvalue;
    //private LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;


    DatabaseReference locations;
    Button btn1, btn2, btn3;
    private AutoCompleteTextView autoCompleteTextView2;
    String phone, where, date, situation;


    LocationManager locationmanager;
    boolean isGPSEnabled, isNetworkEnabled, canGetLocation;

    Double lat, lng;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_main);


        //locations = FirebaseDatabase.getInstance().getReference("Locations");

        autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                    String suggestion = suggestionSnapshot.child("phone").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        ACTV.setAdapter(autoComplete);


        final CheckBox simpleCheckBox = (CheckBox) findViewById(R.id.chkInjury);
        final CheckBox simpleCheckBox2 = (CheckBox) findViewById(R.id.chkFire);
        simpleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (simpleCheckBox2.isChecked()) {
                    simpleCheckBox2.setChecked(false);
                }

                chkvalue = (TextView) findViewById(R.id.chkValue);
                chkvalue.setText("injury");

            }
        });

        simpleCheckBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (simpleCheckBox.isChecked()) {
                    simpleCheckBox.setChecked(false);
                }
                TextView chkvalue = (TextView) findViewById(R.id.chkValue);
                chkvalue.setText("fire");
            }
        });
        Button btnnoti = (Button) findViewById(R.id.btnNoti);
        completeadd = (TextView) findViewById(R.id.completeAdd);

        btnnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!simpleCheckBox.isChecked() && !simpleCheckBox2.isChecked()){
                    Toast.makeText(EmergencyMain.this, "Please choose the type of emergency", Toast.LENGTH_SHORT).show();
                }
                else {
                    where=completeadd.getText().toString();
                    phone=autoCompleteTextView2.getText().toString();
                    situation=chkvalue.getText().toString();


                    Intent send=new Intent(Intent.ACTION_VIEW);

                    //Intent send = new Intent(Intent.ACTION_SENDTO, uri);
                    //send.putExtra("address", phone);
                    //send.putExtra("sms_body", "This is message Body");


                    send.setData(Uri.parse("sms:"));
                    send.putExtra("address", phone);
                    send.putExtra("sms_body", "THIS IS AN EMERGENCY!" + "\n" + "I am currently at: " + where + "\n" + "The type of emergency im dealing with is: " + situation + "\n" + "Please respond immediately.");

                    //send.setType("vnd.android-dir/mms-sms");
                    startActivity(send);
                }
            }
        });


        //completeadd = (TextView) findViewById(R.id.completeAdd);
        lati = (TextView) findViewById(R.id.lati);
        longi = (TextView) findViewById(R.id.longi);


        btn1 = (Button) findViewById(R.id.btnFire);
        btn2 = (Button) findViewById(R.id.btnNat);
        btn3 = (Button) findViewById(R.id.btnPnp);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location;
        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (!isGPSEnabled && !isNetworkEnabled) {
            // no GPS Provider and no network provider is enabled
        } else {   // Either GPS provider or network provider is enabled

            // First get location from Network Provider
            if (isNetworkEnabled) {
                locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationmanager != null) {
                    location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        this.canGetLocation = true;

                        longi.setText("Longitude: " + longitude);
                        lati.setText("Latitude: " + latitude);


                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            Address address = addresses.get(0);
                            String fullAdd = address.getAddressLine(0);
                            completeadd.setText(fullAdd);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }// End of IF network enabled

            if (isGPSEnabled)
            {
                locationmanager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationmanager != null)
                {
                    location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        this.canGetLocation = true;

                        longi.setText("Longitude: " + longitude);
                        lati.setText("Latitude: " + latitude);




                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            Address address = addresses.get(0);
                            String fullAdd = address.getAddressLine(0);
                            completeadd.setText(fullAdd);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }// End of if GPS Enabled
        }// End of Either GPS provider or network provider is enabled
/*
        Criteria criteria = new Criteria();
        String provider=locationmanager.getBestProvider(criteria,false);


        if(provider!=null & !provider.equals(""))
        {
            Location location=locationmanager.getLastKnownLocation(provider);
            locationmanager.requestLocationUpdates(provider,2000,1,this);
            if(location!=null)
            {
                onLocationChanged(location);
            }
            else{
                Toast.makeText(getApplicationContext(),"location not found",Toast.LENGTH_LONG ).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();
        }
*/
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "426-0219";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: " + number));

                    if (ActivityCompat.checkSelfPermission(EmergencyMain.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "911";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: " + number));

                    if (ActivityCompat.checkSelfPermission(EmergencyMain.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "117";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: " + number));

                    if (ActivityCompat.checkSelfPermission(EmergencyMain.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
            });

        }


        @Override
        public void onLocationChanged (Location location){

            //locationManager.removeUpdates(this);


            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Toast.makeText(EmergencyMain.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();


            longi.setText("Longitude: " + longitude);
            lati.setText("Latitude: " + latitude);


            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address address = addresses.get(0);
                String fullAdd = address.getAddressLine(0);
                completeadd.setText(fullAdd);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged (String s,int i, Bundle bundle){

        }

        @Override
        public void onProviderEnabled (String s){

        }

        @Override
        public void onProviderDisabled (String s){

        }


}

