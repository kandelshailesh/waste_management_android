package com.example.fohor_maila.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogHolder> {
    Context context;
    JSONArray blogs;
    Intent intent;

    public BlogAdapter(Context ct, JSONArray p) {
        context = ct;
        blogs = p;
    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_blog_rows, parent, false);
        return new BlogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogHolder holder, int position) {
        try {
            JSONObject a = (JSONObject) blogs.get(position);
            Log.d("From", a.toString());
            holder.ID.setText("1");
            String imageUrl = BuildConfig.API_URL +'/'+ a.getString("image");
            String description = Html.fromHtml(a.getString("description")).toString();
            String title = a.getString("title");
            Picasso.with(context).load(Uri.parse(imageUrl)).into(holder.image);
            holder.title.setText(a.getString("title"));
            holder.description.setText(Html.fromHtml(a.getString("description")));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    intent = new Intent(view.getContext(), BlogDetailFragment.class);
//                    intent.putExtra("description",description);
//                    intent.putExtra("image_url",imageUrl);
//                        intent.putExtra("title",title);
//                    context.startActivity(intent);
                    BlogDetailFragment blogDetailFragment = new BlogDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("description", description);
                    bundle.putString("image_url", imageUrl);
                    bundle.putString("title", title);
                    blogDetailFragment.setArguments(bundle);
                    Navigation.findNavController(view).navigate(R.id.nav_blog_details,bundle);
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

    public class BlogHolder extends RecyclerView.ViewHolder {
        TextView title, ID, description;
        ImageView image;
        public ItemClickListener listener;

        public BlogHolder(@NonNull View itemView) {
            super(itemView);
            ID = (TextView) itemView.findViewById(R.id.blog_id);
            image = (ImageView) itemView.findViewById(R.id.blog_image);
            title = (TextView) itemView.findViewById(R.id.blog_title);
            description = (TextView) itemView.findViewById(R.id.blog_description);
        }

        public void setItemClickListner(ItemClickListener listener) {
            this.listener = listener;
        }

        public void onClick(View view) {

            listener.onClick(view, getAdapterPosition(), false);
        }
    }
}