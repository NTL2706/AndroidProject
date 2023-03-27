package com.example.mygallery.MyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygallery.R;
import com.example.mygallery.models.Category;
import com.example.mygallery.models.Image;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context context;
    private List<Image> listImages;
    private List<Category> listCategory;
    public ImageAdapter(Context context){
        this.context = context;
    }

    public void setData(List<Image> listImages) {
        this.listImages = listImages;
        notifyDataSetChanged();
    }
    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = listImages.get(position);
        if (image == null)
            return;

        Glide.with(context).load(image.getThumb()).into(holder.imgPhoto);

        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listImages != null) {
            return listImages.size();
        }
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPhoto;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto= itemView.findViewById(R.id.Photo);
        }
    }
}
