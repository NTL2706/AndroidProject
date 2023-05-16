package com.example.mygallery.MyFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygallery.MyAdapter.AlbumAdapter;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.MainActivity;
import com.example.mygallery.utility.AlbumUtility;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    private FloatingActionButton btnAdd;
    private RecyclerView albumRecView;
    private AlbumAdapter albumAdapter;

    private ArrayList<String> albums;
    private final int CAMERA_CAPTURED = 1;
    private Context context;

    public AlbumFragment(Context context) {
        this.context = context;
    }

    public static AlbumFragment getInstance(Context context) {
        return new AlbumFragment(context);
    }

    public ArrayList<String> getAlbums() {
        return AlbumUtility.getInstance(getActivity()).getAllAlbums();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View albumsFragment = inflater.inflate(R.layout.fragment_album, container, false);

        albumRecView = albumsFragment.findViewById(R.id.albumsRecView);
        albums = AlbumUtility.getInstance(context).getAllAlbums();
        albumAdapter = new AlbumAdapter(context, albums);
        albumRecView.setAdapter(albumAdapter);
        albumRecView.setLayoutManager(new LinearLayoutManager(albumsFragment.getContext()));
        btnAdd = (FloatingActionButton) albumsFragment.findViewById(R.id.btnAdd_AlbumsFragment);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAlbum();
            }
        });


        return albumsFragment;
    }

    private void addNewAlbum() {

        View addNewAlbumForm = LayoutInflater.from(context).inflate(R.layout.add_album_form, null);
        EditText edtAlbumName = addNewAlbumForm.findViewById(R.id.edtAlbumName);

        AlertDialog.Builder addDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        addDialog.setView(addNewAlbumForm);

        addDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAlbumName = edtAlbumName.getText().toString();
                if (newAlbumName.length() != 0) {
                    if (AlbumUtility.getInstance(context).addNewAlbum(newAlbumName)) {
                        albumAdapter.addAlbum(newAlbumName);
                        Toast.makeText(context, "New album created successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Error: Failed to create new album!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Empty Album Name!", Toast.LENGTH_LONG).show();
                }
            }
        });
        addDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(context, "Canceled!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        addDialog.create();
        addDialog.show();
    }
}
