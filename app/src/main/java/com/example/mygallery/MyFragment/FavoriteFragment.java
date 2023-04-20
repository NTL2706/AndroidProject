package com.example.mygallery.MyFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyAdapter.ItemAlbumAdapter;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.SlideShowActivity;
import com.example.mygallery.mainActivities.data_favor.DataLocalManager;
import com.example.mygallery.models.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;

    private List<String> imageListPath;

    private List<Image> imageList;
    private androidx.appcompat.widget.Toolbar toolbar_favor;
    private Context context;
    private Set<String> imgListFavor;


    @Override
    public void onResume() {
        super.onResume();
        imageListPath = DataLocalManager.getListImg();
        for(int i=0;i<imageListPath.size();i++) {
            File file = new File(imageListPath.get(i));
            if(!file.canRead()) {
                imageListPath.remove(i);
            }
        }

        DataLocalManager.setListImgByList(imageListPath);
        recyclerView.setAdapter(new ItemAlbumAdapter(new ArrayList<>(imageListPath)));
        //FavoriteFragment.MyAsyncTask myAsyncTask = new FavoriteFragment.MyAsyncTask();
        //myAsyncTask.execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container,false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.favor_category);
        toolbar_favor = view.findViewById(R.id.toolbar_favor);

        // Toolbar events
        toolbar_favor.inflateMenu(R.menu.menu_top_favor);
        toolbar_favor.setTitle(context.getResources().getString(R.string.favorite));

        toolbar_favor.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.album_item_search:
                        eventSearch(menuItem);
                        break;
                    case R.id.album_item_slideshow:
                        slideShowEvents();
                        break;
                }

                return true;
            }
        });

        imageListPath = DataLocalManager.getListImg();
        imgListFavor=DataLocalManager.getListSet();
        setRyc();

        return view;
    }


    private void setRyc() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(new ItemAlbumAdapter(new ArrayList<>(imageListPath)));

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite_item_search:
                Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite_item_add:
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite_item_delete:
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

     private void eventSearch(@NonNull MenuItem item) {
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setQueryHint("Type to search");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<String> listImageSearch = new ArrayList<>();
                for (String image : imageListPath) {
                    if (image.toLowerCase().contains(s.toLowerCase())) {
                        listImageSearch.add(image);
                    }

                }

                if (listImageSearch.size() != 0) {
                    recyclerView.setAdapter(new ItemAlbumAdapter(listImageSearch));
                    synchronized (FavoriteFragment.this) {
                        FavoriteFragment.this.notifyAll();
                    }
                } else {
                    Toast.makeText(getContext(), "Searched image not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recyclerView.setAdapter(new ItemAlbumAdapter(new ArrayList<>(imageListPath)));
                synchronized (FavoriteFragment.this) {
                    FavoriteFragment.this.notifyAll();
                }
                return true;
            }
        });
    }
    private void slideShowEvents() {
        Intent intent = new Intent(getView().getContext(), SlideShowActivity.class);

        ArrayList<String> list = new ArrayList<>(imageListPath.size());
        list.addAll(imageListPath);
        intent.putStringArrayListExtra("data_slide", list);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getView().getContext().startActivity(intent);
    }
}
