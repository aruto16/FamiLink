package com.example.art.familinkthesislastdraft.Remote;

import com.example.art.familinkthesislastdraft.model.MyResponse;
import com.example.art.familinkthesislastdraft.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Johannes Agustin on 03/03/2018.
 */

public interface APIService {

    @Headers({
            "Content-type:application/json",
            "Authorization:key=AAAAmI0eZSg:APA91bGzbA45oFFLMuWGXvm1Rt_5m-Us3cPAiaJqtONL5rj0VaN15w8AQVK2l-Zg2COWeBwML8GP0ccfI2GgXxvgeLyQU5j9QtrDITncJmwn07obICSsq7EDaOwah4LeYcUNPXw2SjSo"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
