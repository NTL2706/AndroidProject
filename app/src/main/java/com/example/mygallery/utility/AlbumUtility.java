package com.example.mygallery.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.mygallery.models.Album;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class AlbumUtility {
    private SharedPreferences sharedPreferences;
    private static AlbumUtility instance;
    private static final String ALL_ALBUM_KEY = "album_list";
    private static final String ALL_ALBUM_DATA_KEY = "album_data";

    private AlbumUtility(Context context) {
        sharedPreferences = context.getSharedPreferences("albums_database", Context.MODE_PRIVATE);
        if (getAllAlbums() == null) {
            initAlbums();
        }
        if (getAllAlbumData() == null) {
            initAlbumData();
        }
    }

    public static AlbumUtility getInstance(Context context) {
        if (null == instance)
            instance = new AlbumUtility(context);
        return instance;
    }

    public ArrayList<String> getAllAlbums() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_ALBUM_KEY, null), type);
    }

    public ArrayList<Album> getAllAlbumData() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Album>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_ALBUM_DATA_KEY, null), type);
    }

    public void setAllAlbums(ArrayList<String> albums) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ALL_ALBUM_KEY);
        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
        editor.apply();
    }

    public void setAllAlbumData(ArrayList<Album> data) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ALL_ALBUM_DATA_KEY);
        editor.putString(ALL_ALBUM_DATA_KEY, gson.toJson(data));
        editor.apply();
    }

    public void editAlbumName(String oldName, String newName) {
        ArrayList<Album> data = getAllAlbumData();
        if (data != null) {
            for (Album d: data) {
                if (d.getAlbumName().equals(oldName))
                    d.setAlbumName(newName);
            }
            setAllAlbumData(data);
        }
    }

    private void initAlbums() {
        ArrayList<String> albums = new ArrayList<String>();
        albums.add("Animals");
        albums.add("Food");
        albums.add("Holiday");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_ALBUM_KEY, gson.toJson(albums));
        editor.apply();
    }

    private void initAlbumData() {
        ArrayList<Album> albumData = new ArrayList<Album>();
        albumData.add(new Album("Animals", new ArrayList<String>()));
        albumData.add(new Album("Food", new ArrayList<String>()));
        albumData.add(new Album("Holiday", new ArrayList<String>()));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_ALBUM_DATA_KEY, gson.toJson(albumData));
        editor.apply();
    }

    public boolean addNewAlbum(String albumName) {
        ArrayList<String> albums = getAllAlbums();
        ArrayList<Album> data = getAllAlbumData();
        if (albums != null && data != null)
            if (albums.add(albumName) && data.add(new Album(albumName, new ArrayList<String>()))) {
                setAllAlbums(albums);
                setAllAlbumData(data);
                return true;
            }
        return false;
    }

    public boolean addPictureToAlbum(String albumName, String picturePath) {
        ArrayList<Album> data = getAllAlbumData();
        if (null != data) {
            Album selectedAlbum = findDataByAlbumName(albumName);
            if (selectedAlbum != null) {
                if (selectedAlbum.addNewPath(picturePath)){
                    data.removeIf(d -> d.getAlbumName().equals(selectedAlbum.getAlbumName()));
                    data.add(selectedAlbum);
                }
            }
            setAllAlbumData(data);
            return true;
        }
        return false;
    }
    public boolean deleteAlbum(String albumName) {
        ArrayList<String> albums = getAllAlbums();
        ArrayList<Album> data = getAllAlbumData();
        if (albums != null && data != null)
            for (String album: albums)
                if (album.equals(albumName))
                    if (albums.remove(album)) {
                        data.removeIf(d->d.getAlbumName().equals(albumName));
                        setAllAlbums(albums);
                        setAllAlbumData(data);
                        return true;
                    }
        return false;
    }

//    public boolean deletePictureInAlbum(String albumName, String picturePath) {
//        // Get all album data
//        ArrayList<Album> data = getAllAlbumData();
//        // Get AlbumData object matching the name
//        Album albumData = findDataByAlbumName(albumName);
//        if (albumData != null) {
//            // Remove required path in AlbumData object
//            ArrayList<String> paths = albumData.getPicturePaths();
//            paths.removeIf(s -> s.equals(picturePath));
//            // Set new paths for AlbumData object
//            albumData.setPicturePaths(paths);
//            // Remove that AlbumData in total album data
//            data.removeIf(d -> d.getAlbumName().equals(albumName));
//            // Add modified AlbumData object to data
//            data.add(albumData);
//
//            // Apply changes to shared preferences
//            setAllAlbumData(data);
//            return true;
//        }
//        return false;
//    }



//    public boolean deletePictureInAllAlbums(String picturePath) {
//        ArrayList<Album> data = getAllAlbumData();
//        if (data != null) {
//            for(Album d : data) {
//                ArrayList<String> paths = d.getPicturePaths();
//                paths.removeIf(path -> path.equals(picturePath));
//                d.setPicturePaths(paths);
//            }
//            setAllAlbumData(data);
//            return true;
//        }
//        return false;
//    }

    public Album findDataByAlbumName(String albumName) {
        ArrayList<Album> data = getAllAlbumData();
        if (null != data)
            for (Album d : data)
                if (d.getAlbumName().equals(albumName))
                    return d;
        return null;
    }

}