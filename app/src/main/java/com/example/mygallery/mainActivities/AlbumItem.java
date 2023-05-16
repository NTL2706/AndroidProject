package com.example.mygallery.mainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyAdapter.ImageAdapter;
import com.example.mygallery.MyAdapter.PictureAdapter;
import com.example.mygallery.R;
import com.example.mygallery.utility.AlbumUtility;

import java.util.ArrayList;

public class AlbumItem extends AppCompatActivity {

    private String albumName;
    private ArrayList<String> picturePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        Intent intent = getIntent();
        albumName = intent.getStringExtra("album_name");

        picturePaths = AlbumUtility.getInstance(getApplicationContext()).findDataByAlbumName(albumName).getPicturePaths();

        RecyclerView picturesRecyclerView = findViewById(R.id.ryc_list_album);
        PictureAdapter adapter = new PictureAdapter(albumName, picturePaths);
        picturesRecyclerView.setAdapter(adapter);
        picturesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}
