package com.example.fohor_maila.interfaces;

import com.example.fohor_maila.models.posts.Post;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceholder {

    @GET("posts")
    Call<List<Post>> getPosts();
}
