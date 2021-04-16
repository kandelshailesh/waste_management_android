package com.example.fohor_maila.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.ScheduleAdapter;
import com.example.fohor_maila.interfaces.CollectionRequestInterface;
import com.example.fohor_maila.interfaces.ComplaintInterface;
import com.example.fohor_maila.interfaces.SchedulesInterface;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.network.Network;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

public class ComplaintFragment extends Fragment {
    Button submitBtn;
    ImageButton uploadBtn;
    EditText location, remarks, title;
    int PICK_IMAGE = 1;
    Bitmap bitmap;
    ImageButton imageView;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageButton getLocation;
    File file = null;
    String imagePath;
    private int STORAGE_PERMISSION_CODE = 10;


    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_complaint, container, false);
        location = root.findViewById(R.id.complaint_location);
        remarks = root.findViewById(R.id.complaint_remarks);
        imageView = root.<ImageButton>findViewById(R.id.complaint_upload);
        title = root.findViewById(R.id.complaint_title);
        getLocation = root.findViewById(R.id.get_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getContext(),"Granted permission",Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestStoragePermission();
        }
            return root;
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
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
        uploadBtn = getActivity().findViewById(R.id.complaint_upload);

        submitBtn = getActivity().findViewById(R.id.complaint_submit);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(title.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter title",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(location.getText().toString())) {
                    Toast.makeText(getContext(), "Please select location",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(remarks.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter description",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                Retrofit retrofit = new Network().getRetrofit1();
                ComplaintInterface jsonPlaceholder = retrofit.create(ComplaintInterface.class);
                JSONObject req = new JSONObject();
                Map<String, RequestBody> map = new HashMap<>();

                try {
                    req.put("location", location.getText().toString());
                    req.put("description", remarks.getText().toString());
                    req.put("title", title.getText().toString());
                    map.put("title", toRequestBody(title.getText().toString()));
                    map.put("description", toRequestBody(remarks.getText().toString()));
                    map.put("location", toRequestBody(location.getText().toString()));
                    if(file!=null) {
                        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                        map.put("image\"; filename=\"pp.png\"", fileBody);
                    }
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String user_info = sharedPreferences.getString("user_info", "");
                    if (user_info.isEmpty()) {
                        Log.d("User", "Not found");
                    } else {
                        try {
                            JSONObject s = new JSONObject(user_info);
                            req.put("user_id", s.getInt("id"));
                            map.put("user_id", toRequestBody(Integer.toString(s.getInt("id"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Call<ResponseBody> call = jsonPlaceholder.create(map);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                try {
                                    String re = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getActivity(), "Thank you for your suggestion.", Toast.LENGTH_LONG).show();
                                location.setText("");
                                remarks.setText("");
                                title.setText("");
                                Navigation.findNavController(view).navigate(R.id.nav_home);
                            } else {
                                ApiError error = ErrorUtils.parseError(retrofit, response);
                                Log.d("Error", error.getError());
                                Toast.makeText(getActivity(), error.getError(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                    Call<ResponseBody> call = jsonPlaceholder.create(req.toString());

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("Request code", Integer.toString(requestCode));
            Log.d("Result code", Integer.toString(resultCode));

            if (requestCode == PICK_IMAGE && resultCode == -1 && data != null) {
                Log.d("Request code", Integer.toString(requestCode));
                Log.d("Result code", Integer.toString(resultCode));
                Uri path = data.getData();
                file = new File(new FileUtils().getPath(path,
                        getContext()));

                imageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(path).into(imageView);
//                Log.d("Image path", imagePath);

            }
        } catch (RuntimeException err) {

            Log.d("Err", err.toString());
        }
    }
}
