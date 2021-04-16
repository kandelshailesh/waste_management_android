package com.example.fohor_maila.ui;

import android.content.Intent;
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

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.EventAdapter;
import com.example.fohor_maila.adapters.PackageAdapter;
import com.example.fohor_maila.interfaces.EventsInterface;
import com.example.fohor_maila.interfaces.PackageInterface;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PackageFragment extends Fragment {

    private RecyclerView recyclerView;
    private static final String CONFIG_ENVIRONMENT = ESewaConfiguration.ENVIRONMENT_TEST;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private ESewaConfiguration eSewaConfiguration;
    RecyclerView.LayoutManager layoutManager;
    TextView result;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_packages, container, false);
        result = root.findViewById(R.id.package_result);
        recyclerView= root.findViewById(R.id.rvPackages);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Retrofit retrofit = new Network().getRetrofit1();
        PackageInterface jsonPlaceholder = retrofit.create(PackageInterface.class);
        Call<ResponseBody> call = jsonPlaceholder.fetch();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    try {
                        result.setVisibility(View.GONE);
                        String re= response.body().string();
                        JSONObject obj = new JSONObject(re);
                        Log.d("Jjj", obj.getJSONArray("DATA").get(0).toString());
                        PackageAdapter myAdapter = new PackageAdapter(getContext(), obj.getJSONArray("DATA"));
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ESEWAM","Payment");
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
                Toast.makeText(getContext(), "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Canceled By User", Toast.LENGTH_SHORT).show();
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Toast.makeText(getContext(), "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
                Log.i("Proof of Payment", s);
            }
        }
    }


}