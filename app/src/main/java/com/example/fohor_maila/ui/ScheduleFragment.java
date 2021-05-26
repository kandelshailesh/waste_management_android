package com.example.fohor_maila.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.ScheduleAdapter;
import com.example.fohor_maila.interfaces.SchedulesInterface;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScheduleFragment extends Fragment {
    Button homeBtn;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView result;
    EditText date;
    int year,month,day;
    Button search;
    JSONObject obj;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        result = root.findViewById(R.id.schedule_result);
        recyclerView = root.findViewById(R.id.rvSchedule);
        date= root.findViewById(R.id.date);
        search = root.findViewById(R.id.search);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date.setInputType(0);
        final Calendar calendar = Calendar.getInstance();
        search.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try {
                    if (obj.getJSONArray("DATA").length() > 0) {
                        JSONArray data = new JSONArray();
                        data = obj.getJSONArray("DATA");
                        JSONArray result1 = new JSONArray();
                        for (int i = 0; i < data.length(); i++) {
                            if(data.getJSONObject(i).getString("collection_date").split("T")[0].toString().equals(date.getText().toString())) {
                                        result1.put(result1.length(),data.getJSONObject(i));
                                Log.d("Date",data.getJSONObject(0).getString("collection_date").split("T")[0].toString());
                                Log.d("Edit",date.getText().toString());
                            }
                        }
                        result.setVisibility(View.GONE);
                        ScheduleAdapter myAdapter = new ScheduleAdapter(getContext(),result1);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        result.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year= calendar.get(Calendar.YEAR);
                month= calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar c= Calendar.getInstance();
                        c.set(Calendar.YEAR,i);
                        c.set(Calendar.MONTH,i1);
                        c.set(Calendar.DAY_OF_MONTH,i2);
                        date.setText(DateFormat.format("yyyy-MM-dd",c.getTimeInMillis()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        Integer Id=-1;
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_info = sharedPreferences.getString("user_info", "");
        if (user_info.isEmpty()) {
            Log.d("User", "Not found");
        } else {
            try {
                JSONObject s = new JSONObject(user_info);
                Id= s.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Retrofit retrofit = new Network().getRetrofit1();
        SchedulesInterface jsonPlaceholder = retrofit.create(SchedulesInterface.class);
        Call<ResponseBody> call = jsonPlaceholder.fetch(Id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String re = response.body().string();
                        obj = new JSONObject(re);
                        Log.d("Jjj", obj.getJSONArray("DATA").toString());
                        if(obj.getJSONArray("DATA").length()>0) {
                            result.setVisibility(View.GONE);
                            ScheduleAdapter myAdapter = new ScheduleAdapter(getContext(), obj.getJSONArray("DATA"));
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

                } else {
                    ApiError error = ErrorUtils.parseError(retrofit, response);
                    Log.d("Error", error.getError());
                    Toast.makeText(getActivity(), error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

}
