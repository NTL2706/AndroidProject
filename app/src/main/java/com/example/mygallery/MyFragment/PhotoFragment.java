package com.example.mygallery.MyFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyAdapter.CategoryAdapter;
import com.example.mygallery.R;
import com.example.mygallery.models.Category;
import com.example.mygallery.models.Image;
import com.example.mygallery.utility.Get_All_Image_From_Gallery;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends Fragment {
    private Context context;
    private CategoryAdapter categoryAdapter;
    private Toolbar toolbar_photo;
    private List<Image> imageList;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout photo_layout = (RelativeLayout)inflater.inflate(R.layout.fragment_show_photo, container, false);

        context = photo_layout.getContext();
        recyclerView = photo_layout.findViewById(R.id.show_photo);
        toolbar_photo = photo_layout.findViewById(R.id.toolbar_photo);
        setting_event_toolbar();
        SetRecycleView();
        return photo_layout;

    }

    private void SetRecycleView(){
        categoryAdapter = new CategoryAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(getListCategory());
        recyclerView.setAdapter(categoryAdapter);
    }
    private void setting_event_toolbar (){
        toolbar_photo.inflateMenu(R.menu.menu_of_top);
        toolbar_photo.setTitle("Photo");

        toolbar_photo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.menuCamera:
                        break;
                    case R.id.search_image:
                        break;
                    case R.id.menuChoose:
                        break;
                    case R.id.duplicateImages:
                        break;
                    case R.id.menuSettings:
                        break;
                    case R.id.menuSearch_Advanced:
                        break;
                }
                return true;
            }
        });
    }

    private List<Category> getListCategory() {
        List<Category> categoryList = new ArrayList<>();
        int categoryCount = 0;
        imageList = Get_All_Image_From_Gallery.getAllImageFromGallery(getContext());

        try {
            categoryList.add(new Category(imageList.get(0).getDateTaken(), new ArrayList<>()));
            categoryList.get(categoryCount).addListGirl(imageList.get(0));
            for (int i = 1; i < imageList.size(); i++) {
                if (!imageList.get(i).getDateTaken().equals(imageList.get(i - 1).getDateTaken())) {
                    categoryList.add(new Category(imageList.get(i).getDateTaken(), new ArrayList<>()));
                    categoryCount++;
                }
                categoryList.get(categoryCount).addListGirl(imageList.get(i));
            }
            return categoryList;
        } catch (Exception e) {
            return null;
        }
    }
}
