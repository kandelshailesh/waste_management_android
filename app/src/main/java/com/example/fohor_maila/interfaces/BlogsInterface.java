package com.example.fohor_maila.interfaces;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BlogsInterface {
    @Headers("Content-Type: application/json")
    @GET("api/backend/v1/blog")
    Call<ResponseBody> fetch();
}
