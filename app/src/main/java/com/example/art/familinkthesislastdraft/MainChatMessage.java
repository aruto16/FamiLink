package com.example.art.familinkthesislastdraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MainChatMessage extends Fragment {



    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main_chat_message;
    FloatingActionButton fab;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main_chat_message,"You have been signed out.", Snackbar.LENGTH_SHORT).show();

                }
            });
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.main_menu2,menu);
        //getMenuInflater().inflate(R.menu.main_menu,menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (requestCode == RESULT_OK) {
                //Toast.makeText(getApplicationContext(),"Successfully signed in. Welcome!", Toast.LENGTH_SHORT).show();
                Snackbar.make(activity_main_chat_message, "Successfully signed in. Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                //Toast.makeText(getApplicationContext(),"We could'nt sign you in. Please try again later", Toast.LENGTH_SHORT).show();
                Snackbar.make(activity_main_chat_message,"We could'nt sign you in. Please try again later",Snackbar.LENGTH_SHORT).show();

            }
        }
    }
    @Nullable

    public  View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main_chat_message);
        final View rootView = inflater.inflate(R.layout.activity_main_chat_message,container,false);
        //View v = inflater.inflate(R.layout.activity_main_chat_message, container, false);
        //input=(EditText)v.findViewById(R.id.input);
        activity_main_chat_message = (RelativeLayout)rootView.findViewById(R.id.activity_main_chat_message);


        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

               // LayoutInflater inflater = getLayoutInflater();
                //View v = inflater.inflate(R.layout.activity_main_chat_message, null);
                //View rootView = inflater.inflate(R.layout.activity_main_chat_message, container,false);

                EditText input = rootView.findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");

            }
        });

        //check if not sign in then navigate signinpage
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE );

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            //Snackbar.make(activity_main_chat_message,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            //Load content

            displayChatMessage();
        }

        return rootView;
    }



    private void displayChatMessage() {



        LayoutInflater inflater = getLayoutInflater();
       // View v = getLayoutInflater().inflate(R.layout.activity_main_chat_message ,null);

        View rootView = inflater.inflate(R.layout.activity_main_chat_message, null);
        ListView listOfMessage = rootView.findViewById(R.id.list_of_message);
        adapter =  new FirebaseListAdapter<ChatMessage>(getActivity(),ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference())
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                //get reference to the views of  list_item.xml
                TextView messageText,messageUser,messageTime;
                messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
            }
        };
        listOfMessage.setAdapter(adapter);

    }
}
