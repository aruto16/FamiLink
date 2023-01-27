package com.example.art.familinkthesislastdraft;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button btnLogin;
    Button btnSignUp;
    FirebaseAuth auth;
    FirebaseUser user;

    PermissionManager manager;
    private final static int LOGIN_PERMISSION=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null)
        {
            setContentView(R.layout.activity_main);
            manager = new PermissionManager() {};
            manager.checkAndRequestPermissions(this);
        }
        else
        {
            Intent myIntent = new Intent(MainActivity.this,UserLocationMainActivity.class);
            startActivity(myIntent);
            finish();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode,permissions,grantResults);

        ArrayList<String> denied = manager.getStatus().get(0).denied;

        if (denied.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Permissions enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLogin(View v) {

        btnLogin = findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setAllowNewEmailAccounts(true).build(), LOGIN_PERMISSION
                );
            }
        });
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_PERMISSION){
            startNewActivity(resultCode,data);
        }
    }
    private void startNewActivity(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Intent intent = new Intent(MainActivity.this,UserLocationMainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this,"Login failed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToRegister(View v)
    {
        btnSignUp = findViewById(R.id.btnSignUp);
        //setContentView(R.layout.firebase_register);
        Intent myIntent = new Intent(MainActivity.this,firebaseRegister.class);
        startActivity(myIntent);

    }
}
