package com.test.doan.adapters;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.test.doan.fragments.mainFragments.AlbumFragment;
import com.test.doan.fragments.mainFragments.FavoriteFragment;
import com.test.doan.fragments.mainFragments.PhotoFragment;
import com.test.doan.fragments.mainFragments.SecretFragment;
import com.test.doan.models.Image;
import com.test.doan.util.GetAllPhotoFromGallery;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Image> data;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
        data = GetAllPhotoFromGallery.getAllImageFromGallery(context);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PhotoFragment();
            case 1:
                return new AlbumFragment();
            case 2:
                return new SecretFragment();
            case 3:
                return new FavoriteFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }


}
