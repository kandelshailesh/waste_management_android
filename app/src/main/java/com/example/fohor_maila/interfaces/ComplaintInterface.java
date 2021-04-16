package com.example.fohor_maila.interfaces;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ComplaintInterface {

//    @Headers("Content-Type:application/json")
//    @POST("api/backend/v1/complaint")
//    Call<ResponseBody> create(@Body String p);

    @Multipart
    @POST("api/backend/v1/complaint")
    Call<ResponseBody> create(@PartMap Map<String, RequestBody> params);
}