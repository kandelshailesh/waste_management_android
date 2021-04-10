package com.example.fohor_maila.interfaces;

import com.example.fohor_maila.models.users.Login;
import com.example.fohor_maila.models.users.Register;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CollectionRequestInterface {

    @Headers("Content-Type:application/json")
    @POST("api/backend/v1/collection_request")
    Call<ResponseBody> create(@Body String p);
}