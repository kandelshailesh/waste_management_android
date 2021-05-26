package com.example.fohor_maila.adapters;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.example.fohor_maila.MainActivity;
import com.example.fohor_maila.R;
import com.example.fohor_maila.interfaces.ItemClickListener;
import com.example.fohor_maila.ui.PickupFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.stream.Stream;

public class PickupAdapter extends RecyclerView.Adapter<PickupAdapter.PickupHolder> {
    Context context;
    JSONArray blogs;
    Fragment mContext = new Fragment();

    public PickupAdapter(Context ct, JSONArray p, PickupFragment a) {
        context = ct;
        blogs = p;
        mContext=a;
    }




    @NonNull
    @Override
    public PickupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_pickup_rows, parent, false);
        return new PickupHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PickupHolder holder, int position) {
        try {
            JSONObject a = (JSONObject) blogs.get(position);
            String name = a.getString("fullName");
            String phone = a.getString("phone");
            holder.name.setText(name);
            holder.phone.setText(phone);
            holder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PickupFragment)mContext).makePhoneCall(phone);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return blogs.length();
    }

    public class PickupHolder extends RecyclerView.ViewHolder {
        TextView name,phone;
        Button callBtn;
        public ItemClickListener listener;

        public PickupHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
            callBtn=itemView.findViewById(R.id.callBtn);
        }

        public void setItemClickListner(ItemClickListener listener) {
            this.listener = listener;
        }

        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), false);
        }
    }
}