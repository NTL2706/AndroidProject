package com.example.mygallery.MyFragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyAdapter.CategoryAdapter;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.ItemAlbumActivity;
import com.example.mygallery.mainActivities.MainActivity;
import com.example.mygallery.mainActivities.PictureActivity;
import com.example.mygallery.models.Category;
import com.example.mygallery.models.Image;
import com.example.mygallery.utility.AlbumUtility;
import com.example.mygallery.utility.Get_All_Image_From_Gallery;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PhotoFragment extends Fragment implements View.OnLongClickListener {
    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if (result.getResultCode() == Activity.RESULT_OK){
//                    System.out.println("Chup anh thanh cong")
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    });
    ActivityResultLauncher<String> LauncherCamera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                Toast.makeText(context, "permission is granted", Toast.LENGTH_LONG).show();
            }
        }
    });
    private int CAMERA_PERMISSION_CODE = 1;
    private int CAMERA = 2;
    private Context context;
    private CategoryAdapter categoryAdapter;
    private Toolbar toolbar_photo;
    private List<Image> imageList;
    private RecyclerView recyclerView;
    private Uri imageUri;
    private Bitmap thumbnail;
    private List<Image>multiPick;


    private String imageurl;
    public static boolean selected;
    private PhotoFragment photoFragment;
    private List<Image> listAdvancedSearch = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout photo_layout = (RelativeLayout)inflater.inflate(R.layout.fragment_show_photo, container, false);
        System.out.println("HEllo đây là photo fragment");
        context = photo_layout.getContext();
        recyclerView = photo_layout.findViewById(R.id.show_photo);
        toolbar_photo = photo_layout.findViewById(R.id.toolbar_photo);
        multiPick = new ArrayList<>();
        photoFragment = PhotoFragment.this;
        setting_event_toolbar();
        SetRecycleView();
        return photo_layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryAdapter.setData(getListCategory());

    }

    private void SetRecycleView(){
        categoryAdapter = new CategoryAdapter(context,photoFragment, multiPick);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(getListCategory());
        recyclerView.setAdapter(categoryAdapter);
    }
    private int count  = 0;
    private void setting_event_toolbar (){
        toolbar_photo.inflateMenu(R.menu.menu_of_top);
        toolbar_photo.setTitle("Photo");

        toolbar_photo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.menuCamera:
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "Take a picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "New picture");
                            imageUri = getActivity().getApplicationContext().getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            mLauncher.launch(intent);

                            Get_All_Image_From_Gallery.refreshAllImages();
                            Get_All_Image_From_Gallery.updateNewImages();

                        }else {
                            LauncherCamera.launch(Manifest.permission.CAMERA);
                        }
                        break;
                    case R.id.search_image:
                        SearchImage();
                        break;
                    case R.id.menuSearch_Advanced:
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        EditText editText = new EditText(context);
                        alert.setTitle("Type your word");
                        alert.setView(editText);

                        try {
                            alert.setPositiveButton("Find", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    List<Image> all_image = Get_All_Image_From_Gallery.getAllImageFromGallery(context);
                                    RnAdvancedSearch rnAdvancedSearch = new RnAdvancedSearch(alert, editText);
                                    rnAdvancedSearch.execute(all_image);
                                    if (listAdvancedSearch.size() > 0){
                                        for (int i = 0; i < listAdvancedSearch.size();i++){
                                            listAdvancedSearch.remove(i);
                                        }
                                    }
                                };





                            });


                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, "CANCELED", Toast.LENGTH_SHORT).show();

                                }
                            });


                            alert.show();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case R.id.menuSettings:
                        break;

                }
                return true;
            }
        });
    }

    private class RnAdvancedSearch extends AsyncTask<List<Image>,Void, List<Image>>{
        private AlertDialog.Builder alert;
        private EditText text;
        public RnAdvancedSearch(AlertDialog.Builder alert, EditText text){
            this.alert = alert;
            this.text = text;
        }
        @Override
        protected List<Image> doInBackground(List<Image> ...all_image) {
            final CountDownLatch countDownLatch = new CountDownLatch(all_image[0].size());
            for (int i = 0; i < all_image[0].size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(all_image[0].get(i).getPath());
                ImageLabeler imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
                InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
                Image image = all_image[0].get(i);
                System.out.println(all_image[0].get(i).getPath());
                imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> imageLabels) {
                        String imageRecognization = "";
                        if (text.getText().length() > 0) {
                            for (ImageLabel imageLabel : imageLabels) {

                                String label = imageLabel.getText();
                                Float Confidence = imageLabel.getConfidence();
                                String str1 = label.toLowerCase();
                                String str2 = text.getText().toString().toLowerCase();


                                if (str1.equals(str2)) {
                                    System.out.println(str2);
                                    listAdvancedSearch.add(image);
                                }

                            }
                        }
                        countDownLatch.countDown();
                    }
                });

            }
            try {
                countDownLatch.await(); // Wait for all image labeling processes to complete
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          return  listAdvancedSearch;
        }


        @Override
        protected void onPostExecute(List<Image> all_image) {



                    ArrayList<String> listStringImage = new ArrayList<>();
                    System.out.println(listAdvancedSearch.size());
                    for (Image image : listAdvancedSearch) {
                        listStringImage.add(image.getPath());
                    }
                    Intent intent = new Intent(context, ItemAlbumActivity.class);
                    intent.putStringArrayListExtra("data", listStringImage);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }


    }

    private void SearchImage (){
        Calendar calendar = Calendar.getInstance();
        int day =calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String pickdate = simpleDateFormat.format(calendar.getTime());
                showImageByDate(pickdate);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    private void showImageByDate(String date) {
        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
        List<Image> imageList = Get_All_Image_From_Gallery.getAllImageFromGallery(getContext());
        List<Image> listImageSearch = new ArrayList<>();

        for (Image image : imageList) {
            if (image.getDateTaken().contains(date)) {
                listImageSearch.add(image);
            }
        }
        System.out.println(listImageSearch.size());
        if (listImageSearch.size() == 0) {
            Toast.makeText(getContext(), "have not any image", Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> listStringImage = new ArrayList<>();
            for (Image image : listImageSearch) {
                listStringImage.add(image.getPath());
            }
            Intent intent = new Intent(context, ItemAlbumActivity.class);
            intent.putStringArrayListExtra("data", listStringImage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
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

    public void takeImage (){

    }

    @Override
    public boolean onLongClick(View view) {
        toolbar_photo.getMenu().clear();
        toolbar_photo.inflateMenu(R.menu.multi_selected_menu);
        toolbar_photo.setTitle("Select image");

        toolbar_photo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.CancelSelect:
                        selected = false;
                        toolbar_photo.getMenu().clear();
                        setting_event_toolbar();
                        recyclerView.setAdapter(categoryAdapter);
                        toolbar_photo.setTitle("Photo");
                        break;
                    case R.id.toAlbum:
                        for (int i = 0; i < multiPick.size();i++){
                            System.out.println(multiPick.get(i).getPath());
                        }
                        View addToAlbumView = LayoutInflater.from(context).inflate(R.layout.choose_album_form, null);
                        ListView chooseAlbumListView = addToAlbumView.findViewById(R.id.chooseAlbumListView);

                        ArrayList<String> albums = AlbumUtility.getInstance(context).getAllAlbums();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_list_item_multiple_choice, albums);
                        chooseAlbumListView.setAdapter(adapter);

                        AlertDialog.Builder addToAlbumDialog = new AlertDialog.Builder(context, R.style.AlertDialog);
                        addToAlbumDialog.setView(addToAlbumView);
                        ArrayList<String> chosen = new ArrayList<String>();
                        addToAlbumDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                //String picturePath = pictureFiles[mViewPager.getCurrentItem()].getAbsolutePath();
                                for (int index = 0; index < chooseAlbumListView.getCount(); ++index) {
                                    if (chooseAlbumListView.isItemChecked(index))
                                        chosen.add(chooseAlbumListView.getItemAtPosition(index).toString());
                                }
                                for (String s : chosen) {
                                    for (int i = 0; i < multiPick.size();i++){
                                        AlbumUtility.getInstance(context).addPictureToAlbum(s, multiPick.get(i).getPath());
                                    }
                                }
//                                Toast.makeText(PictureActivity.this, "Added to selected albums", Toast.LENGTH_SHORT).show();
                            }
                        });

                        addToAlbumDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "CANCELED", Toast.LENGTH_SHORT).show();
                            }
                        });
                        addToAlbumDialog.create();
                        addToAlbumDialog.show();
                        break;
//                    case R.id.menuSearch_Advanced:
//                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                        EditText text = new EditText(context);
//                        alert.setTitle("Enter your word");
//                        alert.setView(text);
//                        if (text.getText().length()>0){
//                            for (int i = 0; i < multiPick.size();i++){
//                                Bitmap bitmap = BitmapFactory.decodeFile(multiPick.get(i).getPath());
//                                ImageLabeler imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
//                                InputImage inputImage = InputImage.fromBitmap(bitmap,0);
//                                Image image = multiPick.get(i);
//                                imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
//                                    @Override
//                                    public void onSuccess(List<ImageLabel> imageLabels) {
//                                        String imageRecognization= "";
//                                        for (ImageLabel imageLabel: imageLabels){
//                                            String label = imageLabel.getText();
//                                            Float Confidence = imageLabel.getConfidence();
//
//                                            if (label.toLowerCase().equals(text.getText().toString().toLowerCase())){
//                                                listAdvancedSearch.add(image);
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//                            alert.setPositiveButton("Find", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    ArrayList<String> listStringImage = new ArrayList<>();
//                                    for (Image image : listAdvancedSearch) {
//                                        listStringImage.add(image.getPath());
//                                    }
//                                    Intent intent = new Intent(context, ItemAlbumActivity.class);
//                                    intent.putStringArrayListExtra("data", listStringImage);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(intent);
//                                }
//                            });
//                        }
//
//                        break;


                }
                return false;
            }

        });
        this.selected = true;
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.scrollToPosition(View.generateViewId());
        return false;
    }
}
