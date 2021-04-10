package com.example.fohor_maila.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.ScheduleAdapter;
import com.example.fohor_maila.interfaces.CollectionRequestInterface;
import com.example.fohor_maila.interfaces.SchedulesInterface;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.network.Network;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CollectionRequestFragment extends Fragment {
    Button submitBtn;
    EditText location, remarks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_collection_request, container, false);
        location = root.findViewById(R.id.collection_request_location);
        remarks = root.findViewById(R.id.collection_request_remarks);
        submitBtn = root.findViewById(R.id.collection_request_btn);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(location.getText().toString())) {
                    Toast.makeText(getContext(), "Please select location",
                            Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(remarks.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter description",
                            Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(location.getText().toString()) && !TextUtils.isEmpty(remarks.getText().toString())) {
                    Retrofit retrofit = new Network().getRetrofit1();
                    CollectionRequestInterface jsonPlaceholder = retrofit.create(CollectionRequestInterface.class);
                    JSONObject req = new JSONObject();
                    try {
                        req.put("location", location.getText().toString());
                        req.put("remarks", remarks.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ResponseBody> call = jsonPlaceholder.create(req.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                try {
                                    String re = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getActivity(),"Collection Request added successfully",Toast.LENGTH_LONG).show();

                            } else {
                                ApiError error = ErrorUtils.parseError(retrofit, response);
                                Log.d("Error", error.getError());
                                Toast.makeText(getActivity(), error.getError(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            result.setText(t.getMessage());
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

        });

    }

}
