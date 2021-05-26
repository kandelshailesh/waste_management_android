package com.example.fohor_maila.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.example.fohor_maila.MainActivity;
import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.PackageAdapter;
import com.example.fohor_maila.adapters.PickupAdapter;
import com.example.fohor_maila.interfaces.PackageInterface;
import com.example.fohor_maila.interfaces.PickupInterface;
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

public class PickupFragment extends Fragment {

    private RecyclerView recyclerView;
    TextView result;
    private static final int REQUEST_CALL = 1;
    Bundle bundle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pickup, container, false);
        result = root.findViewById(R.id.result);
        recyclerView= root.findViewById(R.id.rvPickup);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (bundle != null) {
            String number = bundle.getString("phone");
            Log.d("Phone",number);
            if (number != null) {
                makePhoneCall(number);
            }
        }
        Retrofit retrofit = new Network().getRetrofit1();
        PickupInterface jsonPlaceholder = retrofit.create(PickupInterface.class);
        Call<ResponseBody> call = jsonPlaceholder.fetch();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        result.setVisibility(View.GONE);
                        String re= response.body().string();
                        JSONObject obj = new JSONObject(re);
                        Log.d("Data",re);
                        if(obj.getJSONArray("DATA").length()>0) {
                            PickupAdapter myAdapter = new PickupAdapter(getContext(), obj.getJSONArray("DATA"),PickupFragment.this);
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


    public void makePhoneCall(String number) {
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(bundle.getString("phone"));
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}