package com.example.fohor_maila.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fohor_maila.R;


public class BlogDetailFragment extends Fragment {
    ImageView image;
    TextView title,description;
    Bundle bundle;

    public BlogDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Opening Details","Shailesh");

        bundle = this.getArguments();
        return inflater.inflate(R.layout.fragment_blog_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image = view.findViewById(R.id.blogd_image);
        title= view.findViewById(R.id.blogd_title);
        description=view.findViewById(R.id.blogd_description);
        if(bundle!=null) {
            String title1 = bundle.get("title").toString();
            String description1 = bundle.get("description").toString();
            String imageUrl1 = bundle.get("image_url").toString();
            Log.d("Title", title1);
            Log.d("Description", description1);
            Log.d("Image url", imageUrl1);
            title.setText(title1);
            description.setText(description1);
            image.setImageURI(Uri.parse(imageUrl1));
        }
    }
}