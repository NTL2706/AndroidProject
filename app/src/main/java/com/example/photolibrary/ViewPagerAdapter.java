package com.example.photolibrary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
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
