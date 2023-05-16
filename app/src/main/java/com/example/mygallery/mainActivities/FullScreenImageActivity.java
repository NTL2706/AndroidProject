package com.example.mygallery.mainActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.data_favor.DataLocalManager;
import com.example.mygallery.utility.AlbumUtility;
import com.example.mygallery.utility.Get_All_Image_From_Gallery;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.util.Set;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_picture_album);
        BottomNavigationView bottom_picture_album = findViewById(R.id.bottom_picture_album);

        PhotoView imageView = findViewById(R.id.imgPicture);
        String imagePath = getIntent().getStringExtra("imagePath");
        String albumName = getIntent().getStringExtra("albumName");
        Glide.with(this).load(imagePath).into(imageView);

        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.update();

        bottom_picture_album.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.deletePicAlbum:
                        deleteOnAlbumByPath(albumName, imagePath);
                        break;
                }
                return true;
            }
        });
    }


    private void deleteOnAlbumByPath(String albumName, String picturePath) {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this, R.style.AlertDialog);

        confirmDialog.setMessage("Are you sure to remove this picture from album?");
        confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (AlbumUtility.getInstance(FullScreenImageActivity.this).deletePictureInAlbum(albumName, picturePath)) {
                    Toast.makeText(FullScreenImageActivity.this, "Picture removed from album", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(FullScreenImageActivity.this, "Cannot remove this from album", Toast.LENGTH_SHORT).show();
            }
        });
        confirmDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        confirmDialog.create().show();
    }
}
