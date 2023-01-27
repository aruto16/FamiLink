package com.example.art.familinkthesislastdraft;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

//import java.text.DateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class EventPlanner extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{


    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    private TextView dateValue ;
    private AutoCompleteTextView autoCompleteTextView, autoCompleteTextView3;
    private Button setDate;

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter, mPlaceAutocompleteAdapter2;
    private GoogleApiClient mGoogleApiClient;

    Button button;
    EditText recipient, notes;
    String phone, where, date, sNotes;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_planner);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoCompleteTextView3= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView3);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    String suggestion = suggestionSnapshot.child("phone").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView3);
        ACTV.setAdapter(autoComplete);

        dateValue = (TextView)findViewById(R.id.dateValue);
        setDate = (Button)findViewById(R.id.setDate);



        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventPlanner.this, EventPlanner.this,
                        year, month,day);
                datePickerDialog.show();

                /*
                Intent intent = new Intent(EventPlanner.this, CalendarActivity.class);
                startActivity(intent);
                */
            }
        });

        button      =(Button)findViewById(R.id.button6);
        notes       =(EditText)findViewById(R.id.sendersNotes);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                phone = autoCompleteTextView3.getText().toString();
                sNotes = notes.getText().toString();

                where = autoCompleteTextView.getText().toString();
                date = dateValue.getText().toString();

                Intent send = new Intent(Intent.ACTION_VIEW);

                //Intent send = new Intent(Intent.ACTION_SENDTO, uri);
                //send.putExtra("address", phone);
                //send.putExtra("sms_body", "This is message Body");


                send.setData(Uri.parse("sms:"));
                //send.putExtra("address", uri);
                send.putExtra("address", phone  );
                send.putExtra("sms_body", "When: " + date  + "\n" + "Where: " + where + "\n" + "\n" + "Other Notes: " + sNotes);

                //send.setType("vnd.android-dir/mms-sms");
                startActivity(send);
            }
        });

        init();
    }

    private void init(){
        //Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        autoCompleteTextView.setAdapter(mPlaceAutocompleteAdapter);

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                }
                return false;
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EventPlanner.this, EventPlanner.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String AM_PM ;
        hourFinal = i;
        minuteFinal = i1;

        if(hourFinal < 12) {
            AM_PM = "AM";

        } else {
            AM_PM = "PM";
        }

/*
        dateValue.setText("year: " + yearFinal +    "\n" +
                "month: " + monthFinal +    "\n" +
                "day: " + dayFinal +    "\n" +
                "hour: " + hourFinal +    "\n" +
                "minute: " + minuteFinal +    "\n" );
                */
        dateValue.setText("on "+ monthFinal + "/" + dayFinal + "/" + yearFinal + " at " + hourFinal + ":" + minuteFinal + AM_PM);




    }
}
