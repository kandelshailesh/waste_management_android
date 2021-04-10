package com.example.fohor_maila.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fohor_maila.BuildConfig;
import com.example.fohor_maila.R;
import com.example.fohor_maila.interfaces.ItemClickListener;
import com.example.fohor_maila.ui.BlogDetailFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {
    Context context;
    JSONArray blogs;
    Intent intent;

    public ScheduleAdapter(Context ct, JSONArray p) {
        context = ct;
        blogs = p;
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_home_rows, parent, false);
        return new ScheduleHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, int position) {
        try {
            JSONObject a = (JSONObject) blogs.get(position);
            Log.d("From", a.toString());
            DateTimeFormatter formatter =
                    ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            LocalDateTime date = LocalDateTime.parse(a.getString("collection_date"), formatter);
            holder.day.setText(DateTimeFormatter.ofPattern("E").format(date));
            holder.date.setText(DateTimeFormatter.ofPattern("MMM dd").format(date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ScheduleHolder extends RecyclerView.ViewHolder {
        TextView day, date;
        public ItemClickListener listener;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
          day= (TextView) itemView.findViewById(R.id.schedule_day);
            date = (TextView) itemView.findViewById(R.id.schedule_date);
        }

        public void setItemClickListner(ItemClickListener listener) {
            this.listener = listener;
        }

        public void onClick(View view) {

            listener.onClick(view, getAdapterPosition(), false);
        }
    }
}