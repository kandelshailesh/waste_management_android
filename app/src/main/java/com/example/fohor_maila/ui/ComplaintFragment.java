package com.example.fohor_maila.ui;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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
import com.example.fohor_maila.interfaces.ComplaintInterface;
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

public class ComplaintFragment extends Fragment {
    Button submitBtn,uploadBtn;
    EditText location, remarks;
    int PICK_IMAGE= 1;
  Bitmap bitmap;
  ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_complaint, container, false);
        location = root.findViewById(R.id.complaint_location);
        remarks = root.findViewById(R.id.complaint_remarks);
        imageView =root.<ImageView>findViewById(R.id.complaint_image);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadBtn= getActivity().findViewById(R.id.complaint_upload);

        submitBtn= getActivity().findViewById(R.id.complaint_submit);

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
                    ComplaintInterface jsonPlaceholder = retrofit.create(ComplaintInterface.class);
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
                                Toast.makeText(getActivity(),"Complaint added successfully",Toast.LENGTH_LONG).show();
                            } else {
                                ApiError error = ErrorUtils.parseError(retrofit, response);
                                Log.d("Error", error.getError());
                                Toast.makeText(getActivity(), error.getError(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Request code",Integer.toString(requestCode));
        if(requestCode== PICK_IMAGE && resultCode== -1 && data!=null)
        {
            Uri path= data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
           imageView.setImageBitmap(bitmap);
           imageView.setMaxHeight(200);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
