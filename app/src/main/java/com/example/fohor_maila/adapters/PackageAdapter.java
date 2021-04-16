package com.example.fohor_maila.adapters;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.example.fohor_maila.R;
import com.example.fohor_maila.interfaces.ItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageHolder> {
    Context context;
    JSONArray blogs;
    private static final String CONFIG_ENVIRONMENT = ESewaConfiguration.ENVIRONMENT_TEST;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private ESewaConfiguration eSewaConfiguration;

    private static final String MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    private static final String MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";
    Activity mContext = null;


    public PackageAdapter(Context ct, JSONArray p) {
        context = ct;
        blogs = p;
    }

    @NonNull
    @Override
    public PackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_package_rows, parent, false);
        eSewaConfiguration = new ESewaConfiguration()
                .clientId(MERCHANT_ID)
                .secretKey(MERCHANT_SECRET_KEY)
                .environment(CONFIG_ENVIRONMENT);
        return new PackageHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PackageHolder holder, int position) {
        try {
            JSONObject a = (JSONObject) blogs.get(position);
            Log.d("From", a.toString());
            String description = a.getString("details");
            String name = a.getString("name");
            String unit = a.getString("unit");
            String duration = a.getString("duration");
            String cost = a.getString("cost");
            Integer id= a.getInt("id");
            holder.name.setText(name);
            holder.description.setText(description);
            holder.cost.setText(cost);
            holder.duration.setText(duration.toUpperCase() + ' ' + unit);
            holder.buySubscriptionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("cost", cost);
                    bundle.putString("plan_id",id.toString());
                    bundle.putString("from","package");
                    Navigation.findNavController(view).navigate(R.id.nav_subscriptions, bundle);
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

    public class PackageHolder extends RecyclerView.ViewHolder {
        TextView name, cost, duration, description;
        ImageView image;
        Button buySubscriptionBtn;
        public ItemClickListener listener;

        public PackageHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.package_name);
            cost = itemView.findViewById(R.id.package_cost);
            duration = itemView.findViewById(R.id.package_duration);
            description = (TextView) itemView.findViewById(R.id.package_description);
            buySubscriptionBtn = itemView.findViewById(R.id.buySubscriptionBtn);
        }

        public void setItemClickListner(ItemClickListener listener) {
            this.listener = listener;
        }

        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), false);
        }
    }
}