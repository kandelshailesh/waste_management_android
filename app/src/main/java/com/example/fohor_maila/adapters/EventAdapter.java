package com.example.fohor_maila.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
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
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static java.text.DateFormat.*;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    Context context;
    JSONArray blogs;

    public EventAdapter(Context ct, JSONArray p) {
        context = ct;
        blogs = p;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_events_rows, parent, false);
        return new EventHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        try {
            JSONObject a = (JSONObject) blogs.get(position);
            Log.d("From", a.toString());
            String description = a.getString("description");
            String title = a.getString("title");
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            LocalDateTime date = LocalDateTime.parse(a.getString("start_time"), formatter);

            holder.title.setText(title);
            holder.description.setText(description);
            holder.time.setText(DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm a").format(date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        TextView title,time, description;
        ImageView image;
        public ItemClickListener listener;

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.event_title);
            time = (TextView) itemView.findViewById(R.id.event_start);
            description = (TextView) itemView.findViewById(R.id.event_description);
        }
        public void setItemClickListner(ItemClickListener listener) {
            this.listener = listener;
        }

        public void onClick(View view) {

            listener.onClick(view, getAdapterPosition(), false);
        }
    }
}