package com.example.mygallery.mainActivities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyAdapter.ItemAlbumAdapter;
import com.example.mygallery.MyAdapter.ItemAlbumAdapter2;
import com.example.mygallery.MyAdapter.ItemAlbumAdapter3;
import com.example.mygallery.R;

import java.io.File;
import java.util.ArrayList;

public class ItemAlbumActivity extends AppCompatActivity {
    private ArrayList<String> myAlbum;
    private String path_folder;
    private RecyclerView ryc_album;
    private RecyclerView ryc_list_album;
    private Intent intent;
    private String album_name;
    Toolbar toolbar_item_album;
    private ItemAlbumAdapter itemAlbumAdapter;
    private ItemAlbumAdapter2 itemAlbumAdapter2;
    private ItemAlbumAdapter3 itemAlbumAdapter3;
    private int spanCount;
    private int isSecret;
    private int duplicateImg;
    private int isAlbum;
    private static final int REQUEST_CODE_PIC = 10;
    private static final int REQUEST_CODE_CHOOSE = 55;
    private static final int REQUEST_CODE_ADD = 56;
    private static final int REQUEST_CODE_SECRET = 57;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_album);
        intent = getIntent();
        setUpSpanCount();
        mappingControls();
        setData();
        setRyc();
        events();
    }

    private void setUpSpanCount() {
        SharedPreferences sharedPref = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        spanCount = sharedPref.getInt("span_count", 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD) {
            ArrayList<String> resultList = data.getStringArrayListExtra("list_result");
            if (resultList != null) {
                myAlbum.addAll(resultList);
                spanAction();
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE) {
            if (data != null) {
                int isMoved = data.getIntExtra("move", 0);
                if (isMoved == 1) {
                    ArrayList<String> resultList = data.getStringArrayListExtra("list_result");
                    if (resultList != null) {
                        myAlbum.remove(resultList);
                        spanAction();
                    }
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SECRET) {
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PIC) {
            String path_img = data.getStringExtra("path_img");
            if (isSecret == 1) {
                myAlbum.remove(path_img);
                spanAction();
            } else if (duplicateImg == 2) {
                myAlbum.remove(path_img);
                spanAction();
            }
        }
    }

    private void spanAction() {
        if (spanCount == 1) {
            ryc_list_album.setAdapter(new ItemAlbumAdapter3(myAlbum));
        } else if (spanCount == 2) {
            ryc_list_album.setAdapter(new ItemAlbumAdapter2(myAlbum));
        } else {
            ryc_list_album.setAdapter(new ItemAlbumAdapter(myAlbum));
        }
    }

    private void setRyc() {
        album_name = intent.getStringExtra("name");
        ryc_list_album.setLayoutManager(new GridLayoutManager(this, spanCount));
        itemAlbumAdapter = new ItemAlbumAdapter(myAlbum);
        if (spanCount == 1)
            ryc_list_album.setAdapter(new ItemAlbumAdapter3(myAlbum));
        else if (spanCount == 2)
            ryc_list_album.setAdapter(new ItemAlbumAdapter2(myAlbum));
        else
            ryc_list_album.setAdapter(new ItemAlbumAdapter(myAlbum));
    }




    private void events() {

    }



    private void spanCountEvent() {

    }

    private void slideShowEvents() {
        Intent intent = new Intent(ItemAlbumActivity.this, SlideShowActivity.class);
        intent.putStringArrayListExtra("data_slide", myAlbum);
        intent.putExtra("name", album_name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ItemAlbumActivity.this.startActivity(intent);
    }

    private void setData() {
        myAlbum = intent.getStringArrayListExtra("data");
        path_folder = intent.getStringExtra("path_folder");
        isSecret = intent.getIntExtra("isSecret", 0);
        duplicateImg = intent.getIntExtra("duplicateImg", 0);
        itemAlbumAdapter2 = new ItemAlbumAdapter2(myAlbum);
        isAlbum = intent.getIntExtra("ok", 0);
        itemAlbumAdapter3 = new ItemAlbumAdapter3(myAlbum);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    private void mappingControls() {
        ryc_list_album = findViewById(R.id.ryc_list_album);
        toolbar_item_album = findViewById(R.id.toolbar_item_album);
    }

    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < myAlbum.size(); i++) {
                File file = new File(myAlbum.get(i));
                if (!file.exists()) {
                    myAlbum.remove(i);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            spanAction();
        }
    }

}
