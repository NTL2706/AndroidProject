package com.example.mygallery.MyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyFragment.PhotoFragment;
import com.example.mygallery.R;
import com.example.mygallery.models.Category;
import com.example.mygallery.models.Image;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> listCategory;
    private PhotoFragment photoFragment;
    private List<Image>multiPick;
    public CategoryAdapter(Context context, PhotoFragment photoFragment,List<Image>multiPick) {
        this.context = context;
        this.photoFragment = photoFragment;
        this.multiPick = multiPick;
    }

    public void setData(List<Category> listCategory) {
        this.listCategory = listCategory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = listCategory.get(position);
        if (category == null) {
            return;
        }
        holder.tvNameCategory.setText(category.getNameCategory());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.RecycleViewPictures.setLayoutManager(gridLayoutManager);


        ImageAdapter imageAdapter = new ImageAdapter(context.getApplicationContext());
        imageAdapter.setData(category.getListGirl(), photoFragment,multiPick);
        imageAdapter.setListCategory(listCategory);
        holder.RecycleViewPictures.setAdapter(imageAdapter );

    }

    @Override
    public int getItemCount() {
        if (listCategory != null) {
            return listCategory.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameCategory;
        private RecyclerView RecycleViewPictures;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCategory = itemView.findViewById(R.id.NameCategory);
            RecycleViewPictures = itemView.findViewById(R.id.RecyPictures);
        }
    }
}
