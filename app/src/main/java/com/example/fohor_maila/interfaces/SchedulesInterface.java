package com.example.fohor_maila.interfaces;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SchedulesInterface {

    @Headers("Content-Type: application/json")
    @GET("api/backend/v1/schedule")
    Call<ResponseBody> fetch(@Query("user_id") Integer Id);
}
