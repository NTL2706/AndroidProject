package com.example.photolibrary;

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
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private Toolbar toolbar_favorite;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container,false);
        recyclerView = view.findViewById(R.id.favor_category);
        toolbar_favorite = view.findViewById(R.id.toolbar_favorite);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar_favorite);
        activity.getSupportActionBar().setTitle("Favorite");
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuFavorite_Search:
                Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuFavorite_Add:
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuFavorite_Delete:
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }


}