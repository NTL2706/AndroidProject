package com.example.mygallery.mainActivities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.mygallery.MyAdapter.SlideImageAdapter;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.data_favor.DataLocalManager;
import com.example.mygallery.utility.AlbumUtility;
import com.example.mygallery.utility.FileUtility;
import com.example.mygallery.utility.Get_All_Image_From_Gallery;
import com.example.mygallery.utility.PictureInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;


public class PictureActivity extends AppCompatActivity implements PictureInterface {
    ActivityResultLauncher<Intent> mSActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyyy");

            Intent data = result.getData();

            Uri image = data.getData();

            String[] projection = {
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.DATE_TAKEN,

            };

            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            try {


                Cursor cursor = getContentResolver().query(image, projection, null, null, null);
                if (cursor != null) {
                    cursor.moveToNext();
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DATA, cursor.getString(0));
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    //         getContentResolver().update(image, values,null,null);
                    Uri state = getContentResolver().insert(imageUri, values);
                    System.out.println(state);
                    Get_All_Image_From_Gallery.refreshAllImages();
                    Get_All_Image_From_Gallery.updateNewImages();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });


    private ViewPager viewPager_picture;
    private Toolbar toolbar_picture;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frame_viewPager;
    private ArrayList<String> imageListThumb;
    private ArrayList<String> imageListPath;
    private Intent intent;
    private int pos;
    private SlideImageAdapter slideImageAdapter;
    private PictureInterface activityPicture;
    private String imgPath;
    private String imageName;
    private String thumb;
    private Bitmap imageBitmap;
    private String title, link, displayedLink, snippet;
    private RecyclerView resultsRV;

    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView ryc_album;
    public static Set<String> imageListFavor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        mappingControls();
        events();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageListFavor = DataLocalManager.getListSet();
    }


    private void events() {
        setDataIntent();
        setUpToolBar();
        setUpSilder();
        bottomNavigationViewEvents();
    }

    private void mappingControls() {
        viewPager_picture = findViewById(R.id.viewPager_picture);
        bottomNavigationView = findViewById(R.id.bottom_picture);
        toolbar_picture = findViewById(R.id.toolbar_picture);
        frame_viewPager = findViewById(R.id.frame_viewPager);
    }

    private void bottomNavigationViewEvents() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Uri targetUri = Uri.parse("file://" + thumb);
                switch (item.getItemId()) {
                    case R.id.sharePic:
                        if (thumb.contains("gif")) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, targetUri);
                            startActivity(Intent.createChooser(share, "Share this image to your friends!"));
                        } else {
                            Drawable mDrawable = Drawable.createFromPath(imgPath);
                            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
                            thumb = thumb.replaceAll(" ", "");

                            Uri uri = Uri.parse(path);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(shareIntent, "Share Image"));
                        }
                        break;
                    case R.id.editPic:

                        Intent editIntent = new Intent(PictureActivity.this, DsPhotoEditorActivity.class);
                        System.out.println(imgPath);


                        if (imgPath.contains("gif")) {
                            Toast.makeText(PictureActivity.this, "Cannot edit GIF", Toast.LENGTH_LONG).show();
                        } else {

                            editIntent.setData(Uri.fromFile(new File(imgPath)));

                            editIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "My_Gallery");
                            editIntent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, Color.parseColor("#FF000000"));
                            editIntent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, Color.parseColor("#FF000000"));
//                            startActivity(editIntent);
                            mSActivityResultLauncher.launch(editIntent);
                        }
                        break;
                    case R.id.starPic:

                        // if imageListFavor have imgPath then click remove it
                        if (!imageListFavor.add(imgPath)) {
                            imageListFavor.remove(imgPath);
                        }

                        // if imageListFavor have not imgPath then click add it
                        DataLocalManager.setListImg(imageListFavor);
                        Toast.makeText(PictureActivity.this, imageListFavor.size() + "", Toast.LENGTH_SHORT).show();

                        if (!check(imgPath)) {
                            bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star);
                        } else {
                            bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star_red);
                        }
                        break;
                    case R.id.deletePic:
                        AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Do you want to delete this image?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(targetUri.getPath());

                                if (file.exists()) {
                                    if (file.delete()) {
                                        Get_All_Image_From_Gallery.removeImageFromAllImages(targetUri.getPath());
                                        Toast.makeText(PictureActivity.this, "Delete successfully: " + targetUri.getPath(), Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(PictureActivity.this, "Delete failed: " + targetUri.getPath(), Toast.LENGTH_SHORT).show();
                                }
                                finish();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                }
                return true;
            }
        });
    }


    public void setTitleToolbar(String imageName) {
        this.imageName = imageName;
        toolbar_picture.setTitle(imageName);

    }

    //TODO
    public Boolean check(String Path) {
        for (String img : imageListFavor) {
            if (img.equals(Path)) {
                return true;
            }
        }
        return false;
    }

    private void setUpSilder() {
        slideImageAdapter = new SlideImageAdapter();
        slideImageAdapter.setData(imageListThumb, imageListPath);
        slideImageAdapter.setContext(getApplicationContext());
        slideImageAdapter.setPictureInterface(activityPicture);
        viewPager_picture.setAdapter(slideImageAdapter);
        viewPager_picture.setCurrentItem(pos);

        viewPager_picture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                thumb = imageListThumb.get(position);
                imgPath = imageListPath.get(position);

                setTitleToolbar(thumb.substring(thumb.lastIndexOf("/") + 1));
                if (!check(imgPath)) {
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star);
                } else {
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_star_red);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setUpToolBar() {
        // Toolbar events
        toolbar_picture.inflateMenu(R.menu.menu_top_picture);
        setTitleToolbar("Hello");

        // Show and custom action back button
        toolbar_picture.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar_picture.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Show and custom click menu
        toolbar_picture.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int idItem = item.getItemId();

                switch (idItem) {
                    case R.id.menuAddSecret:
                        // show dialog to notification
                        AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Do you want to hide/show this image?");

                        // if dialog click "YES"
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String scrPath = Environment.getExternalStorageDirectory() + File.separator + ".secret";
                                File scrDir = new File(scrPath);
                                // if Dir not exit
                                if (!scrDir.exists()) {
                                    Toast.makeText(PictureActivity.this, "You haven't created secret album", Toast.LENGTH_SHORT).show();
                                }
                                // if Dir is created
                                else {
                                    FileUtility fu = new FileUtility();
                                    File img = new File(imgPath);

                                    System.out.println(scrPath + File.separator + img.getName());
                                    //check storage have image secret (check path)
                                    // if false: add image to album secret
                                    // else true: remote image form album secret
                                    if (!(scrPath + File.separator + img.getName()).equals(imgPath)) {
                                        fu.moveFile(imgPath, img.getName(), scrPath);
                                        Toast.makeText(PictureActivity.this, "Your image is hidden", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String outputPath = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Restore";
                                        File folder = new File(outputPath);
                                        File imgFile = new File(img.getPath());
                                        File desImgFile = new File(outputPath, imgFile.getName());

                                        if (!folder.exists()) {
                                            folder.mkdir();
                                        }
                                        imgFile.renameTo(desImgFile);
                                        imgFile.deleteOnExit();
                                        desImgFile.getPath();
                                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{outputPath + File.separator + desImgFile.getName()}, null, null);
                                    }
                                }
                                Intent intentResult = new Intent();
                                intentResult.putExtra("path_img", imgPath);
                                setResult(RESULT_OK, intentResult);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                        break;

                    case R.id.menuAddAlbum:
                        addPictureToAlbum();
                        break;
                }

                return false;
            }
        });
    }

    private void setDataIntent() {
        intent = getIntent();
        imageListPath = intent.getStringArrayListExtra("data_list_path");
        imageListThumb = intent.getStringArrayListExtra("data_list_thumb");

        pos = intent.getIntExtra("pos", 0);
        activityPicture = this;
    }

    private void showNavigation(boolean flag) {
        if (!flag) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
            toolbar_picture.setVisibility(View.INVISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar_picture.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void actionShow(boolean flag) {
        showNavigation(flag);
    }

    private void addPictureToAlbum() {
        View addToAlbumView = LayoutInflater.from(this).inflate(R.layout.choose_album_form, null);
        ListView chooseAlbumListView = addToAlbumView.findViewById(R.id.chooseAlbumListView);

        ArrayList<String> albums = AlbumUtility.getInstance(this).getAllAlbums();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, albums);
        chooseAlbumListView.setAdapter(adapter);

        AlertDialog.Builder addToAlbumDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        addToAlbumDialog.setView(addToAlbumView);
        ArrayList<String> chosen = new ArrayList<String>();

        addToAlbumDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //String picturePath = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                for (int index = 0; index < chooseAlbumListView.getCount(); ++index) {
                    if (chooseAlbumListView.isItemChecked(index))
                        chosen.add(chooseAlbumListView.getItemAtPosition(index).toString());
                }
                for (String s : chosen) {
                    AlbumUtility.getInstance(PictureActivity.this).addPictureToAlbum(s, imgPath);
                }
                Toast.makeText(PictureActivity.this, "Added to selected albums", Toast.LENGTH_SHORT).show();
            }
        });
        addToAlbumDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PictureActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
            }
        });

        addToAlbumDialog.create();
        addToAlbumDialog.show();
    }
}
