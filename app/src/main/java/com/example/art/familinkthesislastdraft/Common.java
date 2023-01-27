package com.example.art.familinkthesislastdraft;

import com.example.art.familinkthesislastdraft.Remote.APIService;
import com.example.art.familinkthesislastdraft.Remote.RetrofitClient;

/**
 * Created by Johannes Agustin on 03/03/2018.
 */

public class Common {
    public static String CurrentToken = "";

    private static String baseUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient()
    {
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }
}
