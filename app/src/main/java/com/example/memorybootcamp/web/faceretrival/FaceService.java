package com.example.memorybootcamp.web.faceretrival;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FaceService {
    /*url: 'https://randomuser.me/api/?inc=gender,name,picture&results=5&format=json',
    dataType: 'json',*/
    @GET("api/")
    Call<Faces> getFaces(@Query("inc") String include,
                         @Query("results") String count,
                         @Query("format") String format);
}
