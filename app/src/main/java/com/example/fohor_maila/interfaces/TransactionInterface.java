package com.example.fohor_maila.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TransactionInterface {
    @Headers("Content-Type:application/json")
    @GET("api/backend/v1/transaction")
    Call<ResponseBody> fetch(@Query("user_id") Integer user_id);

    @Headers("Content-Type:application/json")
    @POST("api/backend/v1/transaction")
    Call<ResponseBody> fetch(@Body String user_id);
}