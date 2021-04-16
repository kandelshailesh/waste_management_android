package com.example.fohor_maila.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fohor_maila.BuildConfig;
import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.EventAdapter;
import com.example.fohor_maila.interfaces.ComplaintInterface;
import com.example.fohor_maila.interfaces.EventsInterface;
import com.example.fohor_maila.interfaces.Users;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.network.Network;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {
    ImageView imageView,changeProfileView;
    int PICK_IMAGE = 1;
    File file = null;
    Integer user_id;
    TextView fullName,username,email,address,phone;
    Button editProfile,changePasswordBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = root.findViewById(R.id.profile_image);
        changeProfileView= root.findViewById(R.id.change_profile_icon);
        fullName= root.findViewById(R.id.profile_fullname);
        username=root.findViewById(R.id.profile_username);
        email= root.findViewById(R.id.profile_email);
        address=root.findViewById(R.id.profile_address);
        phone=root.findViewById(R.id.profile_phone);
        editProfile= root.findViewById(R.id.profile_editBtn);
        changePasswordBtn= root.findViewById(R.id.profile_changePasswordBtn);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_change_password);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_profile_edit);
            }
        });

        changeProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_info = sharedPreferences.getString("user_info", "");
        String image_url=null;
        if (user_info.isEmpty()) {
            Log.d("User", "Not found");
        } else {
            try {
                JSONObject s = new JSONObject(user_info);
                user_id= s.getInt("id");
                fullName.setText("Fullname: "+s.getString("fullName"));
                username.setText("Username: "+s.getString("username"));
                address.setText("Address: "+s.getString("address"));
                email.setText("Email: "+s.getString("email"));
                phone.setText("Mobile No. "+s.getString("phone"));
                image_url = BuildConfig.API_URL+'/'+s.getString("image");
                Picasso.with(getContext()).load(Uri.parse(image_url)).into(imageView);
//                firstName.setText(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
                Picasso.with(getContext()).load(path).into(imageView);
                Retrofit retrofit = new Network().getRetrofit1();
                Users jsonPlaceholder = retrofit.create(Users.class);
                Map<String, RequestBody> map = new HashMap<>();
                if (file != null) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                    map.put("image\"; filename=\"pp.png\"", fileBody);
                    Call<ResponseBody> call = jsonPlaceholder.edit(user_id,map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                try {
                                    String re = response.body().string();
                                    JSONObject obj = null;
                                    obj = new JSONObject(re);
                                    String user= null;
                                    user= obj.getJSONObject("data").toString();
                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("login",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user_info",user);
                                    editor.apply();
                                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_LONG).show();
                                    file=null;
                                    Navigation.findNavController(getView()).navigate(R.id.nav_profile);
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }

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
                }
            }
        } catch (RuntimeException err) {

            Log.d("Err", err.toString());
        }
    }
}