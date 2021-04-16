package com.example.fohor_maila.interfaces;

import com.example.fohor_maila.models.users.Login;
import com.example.fohor_maila.models.users.Register;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Users {

    @Headers("Content-Type: application/json")
    @POST("api/backend/v1/user/login")
    Call<ResponseBody> login(@Body String user);

    @FormUrlEncoded
    @POST("api/backend/v1/user/signup")
    Call<ResponseBody> signup(@Field("fullName") String fullName, @Field("username") String username, @Field("email") String email, @Field("password") String password, @Field("phone") String phone, @Field("address") String address, @Field("gender") String gender);

    @POST("api/backend/v1/user/{id}")
    Call<ResponseBody> fetch(@Path("id") Integer i);

    @Multipart
    @PATCH("api/backend/v1/user/{id}")
    Call<ResponseBody> edit(@Path("id") Integer id, @PartMap Map<String, RequestBody> Params);

    @Headers("Content-Type: application/json")
    @PATCH("api/backend/v1/user/change_password/{id}")
    Call<ResponseBody> change_password(@Path("id") Integer id, @Body String user);
}
