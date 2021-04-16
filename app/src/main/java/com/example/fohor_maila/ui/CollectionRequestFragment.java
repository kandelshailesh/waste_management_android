package com.example.fohor_maila.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.example.fohor_maila.models.collection_request.CollectionRequest;
import com.example.fohor_maila.network.Network;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CollectionRequestFragment extends Fragment {
    Button submitBtn;
    ImageButton getLocation;
    EditText location, remarks;
    FusedLocationProviderClient fusedLocationProviderClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_collection_request, container, false);
        location = root.findViewById(R.id.collection_request_location);
        remarks = root.findViewById(R.id.collection_request_remarks);
        submitBtn = root.findViewById(R.id.collection_request_btn);
        getLocation = root.findViewById(R.id.get_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        return root;
    }

    public void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location1 = task.getResult();
                if (location1 != null) {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> address = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                        location.setText(address.get(0).getAddressLine(0).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                else
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},44);
                }

            }
        });
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

                Retrofit retrofit = new Network().getRetrofit1();
                CollectionRequestInterface jsonPlaceholder = retrofit.create(CollectionRequestInterface.class);
                JSONObject req = new JSONObject();
                try {
                    req.put("location", location.getText().toString());
                    req.put("remarks", remarks.getText().toString());
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String user_info = sharedPreferences.getString("user_info", "");
                    if (user_info.isEmpty()) {
                        Log.d("User", "Not found");
                    } else {
                        try {
                            JSONObject s = new JSONObject(user_info);
                            req.put("user_id", s.getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
                            Toast.makeText(getActivity(), "We will get back to you soon.", Toast.LENGTH_LONG).show();
                            location.setText("");
                            remarks.setText("");
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


        });

    }

}
