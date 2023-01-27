package com.example.art.familinkthesislastdraft.Service;

import com.example.art.familinkthesislastdraft.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Johannes Agustin on 03/03/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Common.CurrentToken = refreshedToken;
    }
}
