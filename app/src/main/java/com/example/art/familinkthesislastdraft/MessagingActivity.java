package com.example.art.familinkthesislastdraft;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.art.familinkthesislastdraft.Remote.APIService;
import com.example.art.familinkthesislastdraft.model.MyResponse;
import com.example.art.familinkthesislastdraft.model.Notification;
import com.example.art.familinkthesislastdraft.model.Sender;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MessagingActivity extends Fragment {


    Button btnSendData;
    EditText edtTitle,edtContent;

    APIService mService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_messaging);
        View v = inflater.inflate(R.layout.activity_messaging, null);

        Common.CurrentToken = FirebaseInstanceId.getInstance().getToken();

        //For multi
        FirebaseMessaging.getInstance().subscribeToTopic("MyTopic");
        mService = Common.getFCMClient();

        btnSendData = (Button) v.findViewById(R.id.btnSendData);
        edtContent = (EditText) v.findViewById(R.id.edtContent);
        edtTitle = (EditText) v.findViewById(R.id.edtTitle);

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Send notification to topic
                Notification notification =  new Notification(edtTitle.getText().toString(),edtContent.getText().toString());
                Sender sender = new Sender("/topics/MyTopic",notification); //send to topic

                mService.sendNotification(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.body().success ==1)
                                {
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());
                            }
                        });
                /*
                //Send request to token
                Notification notification =  new Notification(edtTitle.getText().toString(),edtContent.getText().toString());
                Sender sender = new Sender(Common.CurrentToken,notification); //send to itself

                mService.sendNotification(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.body().success ==1)
                                {
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());
                            }
                        });
                */
            }
        });

    return v;
    }
}
