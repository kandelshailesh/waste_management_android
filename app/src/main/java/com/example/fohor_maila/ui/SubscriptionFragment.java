package com.example.fohor_maila.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.example.fohor_maila.R;
import com.example.fohor_maila.adapters.EventAdapter;
import com.example.fohor_maila.interfaces.EventsInterface;
import com.example.fohor_maila.interfaces.SubscriptionInterface;
import com.example.fohor_maila.models.ApiError;
import com.example.fohor_maila.models.ErrorUtils;
import com.example.fohor_maila.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SubscriptionFragment extends Fragment {

    private static final String CONFIG_ENVIRONMENT = ESewaConfiguration.ENVIRONMENT_TEST;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int CREATE_CODE_PAYMENT = 2;
    private ESewaConfiguration eSewaConfiguration;
    LinearLayout subscriptionCard;

    private static final String MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    private static final String MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";
    TextView result, name, cost, activation_date, renewal_date, status;
    Button addSubscription, makePaymentBtn;
    Bundle bundle;
    Integer plan_id;
    Double packageCost;
    JSONObject data1 = new JSONObject();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        eSewaConfiguration = new ESewaConfiguration()
                .clientId(MERCHANT_ID)
                .secretKey(MERCHANT_SECRET_KEY)
                .environment(CONFIG_ENVIRONMENT);
        name = root.findViewById(R.id.subscription_name);
        cost = root.findViewById(R.id.subscription_cost);
        activation_date = root.findViewById(R.id.subscription_activation_date);
        renewal_date = root.findViewById(R.id.subscription_renewal_date);
        addSubscription = root.findViewById(R.id.subscription_btn);
        makePaymentBtn = root.findViewById(R.id.makePaymentBtn);
        subscriptionCard = root.findViewById(R.id.subscription_layout);
        bundle = this.getArguments();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (bundle != null) {
            String check = bundle.getString("from");
            if (check != null && check == "package") {
                String cost = bundle.getString("cost");
                makePayment1(cost);
            }
        }
        addSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_packages);
            }
        });

        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment("200");
            }
        });
        Retrofit retrofit = new Network().getRetrofit1();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_info = sharedPreferences.getString("user_info", "");
        JSONObject s = null;
        Integer user_id = null;
        try {
            s = new JSONObject(user_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user_id = s.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SubscriptionInterface jsonPlaceholder = retrofit.create(SubscriptionInterface.class);
        Call<ResponseBody> call = jsonPlaceholder.fetch(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        subscriptionCard.setVisibility(View.VISIBLE);
                        addSubscription.setVisibility(View.GONE);
                        String re = response.body().string();
                        Log.d("Data", re);
                        JSONObject obj = new JSONObject(re);
                        if (obj.getJSONArray("DATA").length() > 0) {
                            data1 = (JSONObject) obj.getJSONArray("DATA").get(0);
                            name.setText(data1.getJSONObject("package").getString("name"));
                            cost.setText(data1.getJSONObject("package").getString("cost"));
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                            LocalDateTime date = LocalDateTime.parse(data1.getString("activation_date"), dateTimeFormatter);
                            activation_date.setText(DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm a").format(date));
                            date = LocalDateTime.parse(data1.getString("renewal_date"), dateTimeFormatter);
                            renewal_date.setText(DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm a").format(date));
                            plan_id = data1.getInt("package_id");
                            packageCost = data1.getJSONObject("package").getDouble("cost");
                        } else {
                            addSubscription.setVisibility(View.VISIBLE);
                            subscriptionCard.setVisibility(View.GONE);
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void makePayment(String amount) {
        ESewaPayment eSewaPayment = new ESewaPayment(amount, "someProductName", "someUniqueId_" + System.nanoTime(), "");
        Intent intent = new Intent(getActivity(), ESewaPaymentActivity.class);
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private void makePayment1(String amount) {
        ESewaPayment eSewaPayment = new ESewaPayment(amount, "someProductName", "someUniqueId_" + System.nanoTime(), "");
        Intent intent = new Intent(getActivity(), ESewaPaymentActivity.class);
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
        startActivityForResult(intent, CREATE_CODE_PAYMENT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                String s1 = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                String user_info = sharedPreferences.getString("user_info", "");
                JSONObject s = null;
                Integer user_id = null;
                try {
                    s = new JSONObject(user_info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user_id = s.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Retrofit retrofit = new Network().getRetrofit1();
                SubscriptionInterface jsonPlaceholder = retrofit.create(SubscriptionInterface.class);
                JSONObject res = new JSONObject();
                try {
                    res = new JSONObject(s1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject trans = new JSONObject();
                try {
//                    trans.put("id",res.getJSONObject("transactionDetails").getString("referenceId"));
                    trans.put("user_id", user_id);
                    trans.put("plan_id", plan_id);
                    trans.put("paid_amount", packageCost);
                    data1.put("transaction", trans);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<ResponseBody> call = jsonPlaceholder.update(user_id, data1.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                addSubscription.setVisibility(View.GONE);
                                String re = response.body().string();
                                Toast.makeText(getContext(), "Subscription updated successfully", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(getView()).navigate(R.id.nav_subscriptions);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            addSubscription.setVisibility(View.VISIBLE);
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
                Log.i("Proof of Payment", s1);
                Toast.makeText(getContext(), "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Canceled By User", Toast.LENGTH_SHORT).show();
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
            }
        }

        if (requestCode == CREATE_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                String s1 = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                String user_info = sharedPreferences.getString("user_info", "");
                JSONObject s = null;
                Integer user_id = null;
                try {
                    s = new JSONObject(user_info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    user_id = s.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Retrofit retrofit = new Network().getRetrofit1();
                SubscriptionInterface jsonPlaceholder = retrofit.create(SubscriptionInterface.class);
                JSONObject res = new JSONObject();
                try {
                    res = new JSONObject(s1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject trans = new JSONObject();

                try {
//                    trans.put("id",res.getJSONObject("transactionDetails").getString("referenceId"));
                    if (bundle != null) {
                        data1.put("user_id", user_id);
                        data1.put("package_id", bundle.getString("plan_id"));
                        data1.put("status", "active");
                        data1.put("activation_date", new Date());
                    }
                    trans.put("user_id", user_id);
                    trans.put("plan_id", bundle.getString("plan_id"));
                    trans.put("paid_amount", bundle.getString("cost"));
                    data1.put("transaction", trans);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<ResponseBody> call = jsonPlaceholder.create(data1.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                addSubscription.setVisibility(View.GONE);
                                String re = response.body().string();
                                Toast.makeText(getContext(), "Subscription created successfully", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(getView()).navigate(R.id.nav_subscriptions);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            addSubscription.setVisibility(View.VISIBLE);
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
                Log.i("Proof of Payment", s1);
                Toast.makeText(getContext(), "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Canceled By User", Toast.LENGTH_SHORT).show();
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Retrofit retrofit = new Network().getRetrofit1();
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
            String user_info = sharedPreferences.getString("user_info", "");
            JSONObject s = null;
            Integer user_id = null;
            try {
                s = new JSONObject(user_info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                user_id = s.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SubscriptionInterface jsonPlaceholder = retrofit.create(SubscriptionInterface.class);
            Call<ResponseBody> call = jsonPlaceholder.fetch(user_id);
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            subscriptionCard.setVisibility(View.VISIBLE);
                            addSubscription.setVisibility(View.GONE);
                            String re = response.body().string();
                            Log.d("Data", re);
                            JSONObject obj = new JSONObject(re);
                            if (obj.getJSONArray("DATA").length() > 0) {
                                data1 = (JSONObject) obj.getJSONArray("DATA").get(0);
                                name.setText(data1.getJSONObject("package").getString("name"));
                                cost.setText(data1.getJSONObject("package").getString("cost"));
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                LocalDateTime date = LocalDateTime.parse(data1.getString("activation_date"), dateTimeFormatter);
                                activation_date.setText(DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm a").format(date));
                                date = LocalDateTime.parse(data1.getString("renewal_date"), dateTimeFormatter);
                                renewal_date.setText(DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm a").format(date));
                                plan_id = data1.getInt("package_id");
                                packageCost = data1.getJSONObject("package").getDouble("cost");
                            } else {
                                addSubscription.setVisibility(View.VISIBLE);
                                subscriptionCard.setVisibility(View.GONE);
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
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}