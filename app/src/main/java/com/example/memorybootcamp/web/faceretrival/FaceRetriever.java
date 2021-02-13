package com.example.memorybootcamp.web.faceretrival;

import com.squareup.moshi.Moshi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Based on tutorials:
 * https://guides.codepath.com/android/consuming-apis-with-retrofit
 * https://github.com/elodieferrais/SampleApp/blob/master/app/src/main/java/com/elodieferrais/myapplication/DummyService.java
 * https://github.com/Marimonbert/Retrofit-Moshi-Json/tree/master/RecyclerFast
 * https://github.com/square/retrofit/blob/master/samples/src/main/java/com/example/retrofit/SimpleService.java
 * https://square.github.io/retrofit/
 *
 * Uses API:
 * https://randomuser.me/documentation
* */

public class FaceRetriever {

    private FaceService service;

    private static final String BASE_URL = "https://randomuser.me/";

    public FaceRetriever(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder().build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(FaceService.class);
                //.getFaces(include, count, format);
    }

    public void getFaces(int count, Callback<Faces> callback){
        Call<Faces> faceCall = service.getFaces("gender,name,picture", String.valueOf(count), "json");
        faceCall.enqueue(callback);
    }
}
