package com.example.fohor_maila.network;

import com.example.fohor_maila.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Network {
    public Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().

                baseUrl(BuildConfig.API_URL).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public Retrofit getRetrofit1() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BuildConfig.API_URL).
                addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit;
    }
}
