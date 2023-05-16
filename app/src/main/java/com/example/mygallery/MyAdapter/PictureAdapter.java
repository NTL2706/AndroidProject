package com.example.mygallery.MyAdapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.FullScreenImageActivity;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private String albumName;
    private ArrayList<String> picturePaths;

    public PictureAdapter(String albumName, ArrayList<String> picturePaths) {
        this.albumName = albumName;
        this.picturePaths = picturePaths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(picturePaths.get(position))
                .into(holder.pictureImageView);
    }

    @Override
    public int getItemCount() {
        return picturePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView pictureImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            pictureImageView = itemView.findViewById(R.id.Photo);

            pictureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), FullScreenImageActivity.class);
                    intent.putExtra("imagePath", picturePaths.get(position));
                    intent.putExtra("albumName", albumName);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

}
