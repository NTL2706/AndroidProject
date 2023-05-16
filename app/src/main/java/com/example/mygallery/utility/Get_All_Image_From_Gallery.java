package com.example.mygallery.utility;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mygallery.models.Image;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Get_All_Image_From_Gallery {
    public static List<Image> allImages;

    private static boolean allImagesPresent = false;
    private static boolean addNewestImagesOnly = false;

    public static List<Image> getAllImages() {

        return allImages;
    }
    public static void refreshAllImages(){
        allImagesPresent = false;
    }
    public static void updateNewImages(){
        addNewestImagesOnly = true;
    }
    public static void removeImageFromAllImages(String path) {  // remove deleted photo from "database"
        for(int i=0;i<allImages.size();i++) {
            if(allImages.get(i).getPath().equals(path)) {
                Log.d("My-Photo","GetAllPhotosFromGallery -> Image removed from allImages. Breaking");
                allImages.remove(i);
                break;
            }
        }
    }
    public static final List<Image> getAllImageFromGallery(Context context) {
        Log.d("My-Photo","GetAllPhotoFromGallery->getAllImageFromGallery()");
        if(!allImagesPresent) { // Do not fetch photos between Activity switching.
            // MASSIVE performance improvement. Like over 9000.
            Uri uri;
            Cursor cursor;
            int columnIndexData, thumb, dateIndex;
            List<Image> listImage = new ArrayList<>();

            String absolutePathImage = null;
            String thumbnail = null;
            Long dateTaken = null;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN
            };

            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;


            cursor = context.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
            dateIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            Calendar myCal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyyy");
            while (cursor.moveToNext()) {
                try {
                    absolutePathImage = cursor.getString(columnIndexData);
                    File file = new File(absolutePathImage);

                    if (!file.canRead()) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                thumbnail = cursor.getString(thumb);
                dateTaken = cursor.getLong(dateIndex);
                System.out.println("Date" + dateTaken);
                myCal.setTimeInMillis(dateTaken);
                String dateText = formatter.format(myCal.getTime());
                Image image = new Image();
                image.setPath(absolutePathImage);
                System.out.println(dateText);
                image.setThumb(thumbnail);
                image.setDateTaken(dateText);
                if (image.getPath() == "") {
                    continue;
                }
                Log.d("Path123", image.getPath());
                Log.d("Path", listImage.size() + "");
                if(addNewestImagesOnly){
                    boolean iscontained = false; // in the "database"
                    for(Image i : allImages){
                        if(i.getPath().equals(image.getPath())){
                            iscontained = true;
                            break;
                        }
                    }
                    if(iscontained){
                        Log.d("MyPhoto","GetAllPhotosFromGallery -> Image already in allImages. Breaking");
                        addNewestImagesOnly = false;
                        allImagesPresent = true;
                        cursor.close(); // Android Studio suggestion
                        return allImages;
                    } else{
                        Log.d("Simple-Gallery", allImages.size() + "");
                        if(allImages.size()>1200){
                            addNewestImagesOnly = false;
                            allImagesPresent = true;
                            cursor.close(); // Android Studio suggestion
                            return allImages;
                        }
                        allImages.add(0, image);
                    }
                } else {
                    listImage.add(image);
                }

                if(listImage.size()>1000) { // Just for testing.
                    break;                  // I don't want to load 10 000 photos at once.
                }
            }
            cursor.close(); // Android Studio suggestion
            allImages = listImage;
            addNewestImagesOnly = false;
            allImagesPresent = true;
            return listImage;
        }
        else{
            return allImages;
        }
    }
}
