package com.example.fohor_maila.interfaces;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface PackageInterface {

    @Headers("Content-Type:application/json")
    @GET("api/backend/v1/package")
    Call<ResponseBody> fetch();
}