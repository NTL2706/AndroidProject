package com.example.photolibrary;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.photo).setChecked(true);
                        break;
                    case 1:

                        bottomNavigationView.getMenu().findItem(R.id.album).setChecked(true);
                        break;
                    case 2:

                        bottomNavigationView.getMenu().findItem(R.id.favorite).setChecked(true);
                        break;
                    case 3:

                        bottomNavigationView.getMenu().findItem(R.id.scret).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.photo:

                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.album:

                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.favorite:

                        viewPager.setCurrentItem(2);
                        break;

                    case R.id.scret:

                        viewPager.setCurrentItem(3);
                        break;

                }
                return true;
            }
        });

    }




}
