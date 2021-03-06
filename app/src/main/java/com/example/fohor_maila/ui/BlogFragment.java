package com.example.fohor_maila.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fohor_maila.LoginActivity;
import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.BlogAdapter;
import com.example.fohor_maila.interfaces.BlogsInterface;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.models.blogs.Blogs;
import com.example.fohor_maila.network.Network;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BlogFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView result;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blogs, container, false);
        result = root.findViewById(R.id.blog_result);
        recyclerView= root.findViewById(R.id.rvBlogs);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Retrofit retrofit = new Network().getRetrofit1();
        BlogsInterface jsonPlaceholder = retrofit.create(BlogsInterface.class);
        Call<ResponseBody> call = jsonPlaceholder.fetch();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        result.setVisibility(View.GONE);
                        String re= response.body().string();
                        JSONObject obj = new JSONObject(re);
                         if(obj.getJSONArray("DATA").length()>0) {
                             BlogAdapter myAdapter = new BlogAdapter(getContext(), obj.getJSONArray("DATA"));
                             recyclerView.setAdapter(myAdapter);
                             recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                         }
                         else
                         {
                             result.setVisibility(View.VISIBLE);
                         }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    result.setVisibility(View.VISIBLE);
                    ApiError error = ErrorUtils.parseError(retrofit, response);
                    Log.d("Error",error.getError());
                    Toast.makeText(getActivity(),error.getError(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }


}