package com.example.fohor_maila.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ComplaintInterface {
    
    @Headers("Content-Type:application/json")
    @POST("api/backend/v1/complaint")
    Call<ResponseBody> create(@Body String p);
}