package com.example.art.familinkthesislastdraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

public class firebaseLogin extends AppCompatActivity {

    /*
    Button btnLogin;

    private final static int LOGIN_PERMISSION=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_login);

        btnLogin =findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                    .setAllowNewEmailAccounts(true).build(),LOGIN_PERMISSION
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
          Intent intent = new Intent(firebaseLogin.this,ListOnline.class);
          startActivity(intent);
          finish();
        }
        else{
            Toast.makeText(this,"Login failed.", Toast.LENGTH_SHORT).show();
        }
    }
        */
}
