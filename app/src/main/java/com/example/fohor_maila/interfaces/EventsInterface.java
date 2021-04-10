package com.example.fohor_maila.interfaces;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface EventsInterface {
    @Headers("Content-Type: application/json")
    @GET("api/backend/v1/event")
    Call<ResponseBody> fetch();
}
