package com.example.mygallery.MyAdapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mygallery.MyFragment.AlbumFragment;
import com.example.mygallery.MyFragment.FavoriteFragment;
import com.example.mygallery.MyFragment.PhotoFragment;
import com.example.mygallery.MyFragment.SecretFragment;
import com.example.mygallery.models.Image;
import com.example.mygallery.utility.Get_All_Image_From_Gallery;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Image> data;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
        data = Get_All_Image_From_Gallery.getAllImageFromGallery(context);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PhotoFragment();
            case 1:
                return new AlbumFragment(context);
            case 2:
                return new FavoriteFragment();
            case 3:
                return new SecretFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
