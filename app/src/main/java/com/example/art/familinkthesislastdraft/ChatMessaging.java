package com.example.art.familinkthesislastdraft;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.art.familinkthesislastdraft.Remote.APIService;
import com.example.art.familinkthesislastdraft.model.MyResponse;
import com.example.art.familinkthesislastdraft.model.Notification;
import com.example.art.familinkthesislastdraft.model.Sender;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessaging extends AppCompatActivity {


    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_chat_messaging;
    FloatingActionButton fab;

    APIService mService;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chat_messaging, "You have been signed out.", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Snackbar.make(activity_chat_messaging,"Successully signed in. Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                Snackbar.make(activity_chat_messaging,"Could not sign you in. Please try again later.", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging);

        Common.CurrentToken =FirebaseInstanceId.getInstance().getToken();

        //for multi
        FirebaseMessaging.getInstance().subscribeToTopic("MyTopic");

        mService = Common.getFCMClient();
        /**Log.d("MY TOKEN",Common.CurrentToken);*/

        activity_chat_messaging=(RelativeLayout) findViewById(R.id.activity_chat_messaging);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input=(EditText) findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("chats").push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                //input.setText("");
                BubbleTextView bubbleTextView = (BubbleTextView)findViewById(R.id.message_text);
                bubbleTextView.setText("");

                //SEND NOTIF
                String NotifName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Notification notification = new Notification(input.getText().toString(),NotifName + " on FamiLink");
                Sender sender = new Sender("/topics/MyTopic",notification); //send to topic
                //Sender sender = new Sender(Common.CurrentToken,notification); //send to topic
                mService.sendNotification(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                /*if(response.body().success == 1)
                                    Toast.makeText(ChatMessaging.this, "Success", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(ChatMessaging.this, "Failed", Toast.LENGTH_SHORT).show();
                                */
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable throwable) {
                                //Log.e("ERROR",t.getMessage());
                                Toast.makeText(ChatMessaging.this, "Failed onFailure", Toast.LENGTH_SHORT).show();
                            }
                        });
                input.setText("");
            }

        });

        //check if not sign-in then navigate signin page
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {

            FirebaseUser username=FirebaseAuth.getInstance().getCurrentUser();
            if (username != null) {
                String name=username.getDisplayName();
                //String email=username.getEmail();


                Snackbar.make(activity_chat_messaging, "Welcome " + name, Snackbar.LENGTH_SHORT).show();

                //Snackbar.make(activity_chat_messaging,"Welcome "+ FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();

                //load content
                displayChatMessage();
            }
        }
    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child("chats"))
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                //Get references to the views of list_item.xml
                TextView messageText,messageUser,messageTime;

                messageText = (BubbleTextView)v.findViewById(R.id.message_text);
                messageUser = (TextView)v.findViewById(R.id.message_user);
                messageTime = (TextView)v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("MMM-dd-yyyy (hh:mm aa)",model.getMessageTime()));



            }
        };
        listOfMessage.setAdapter(adapter);

    }

}
