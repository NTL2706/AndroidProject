package com.example.mygallery.mainActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mygallery.MyAdapter.ViewPagerAdapter;
import com.example.mygallery.MyFragment.PhotoFragment;
import com.example.mygallery.R;
import com.example.mygallery.mainActivities.data_favor.DataLocalManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.karan.churi.PermissionManager.PermissionManager;


public class MainActivity extends AppCompatActivity {
    private static final int MY_READ_PERMISSION_CODE = 101;
    private PhotoFragment photoFragment;
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;
    private PermissionManager permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);

        DataLocalManager.init(getApplicationContext());
        permission = new PermissionManager() {


            @Override
            public void ifCancelledAndCannotRequest(Activity activity) {

            }
        };
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            load();
        }else {
            permission.checkAndRequestPermissions(this);
        }

//        setUpViewPager();
//        loadSettings();
//
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.photo:
//                        viewPager.setCurrentItem(0);
//                        break;
//
//                    case R.id.album:
//
//                        viewPager.setCurrentItem(1);
//                        break;
//
//                    case R.id.favorite:
//
//                        viewPager.setCurrentItem(2);
//                        break;
//
//                    case R.id.scret:
//                        viewPager.setCurrentItem(3);
//                        break;
//
//                }
//                return true;
//            }
//        });
    }

    private void setUpViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.setContext(getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

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

                        bottomNavigationView.getMenu().findItem(R.id.scret).setChecked(true);
                        break;
                    case 3:

                        bottomNavigationView.getMenu().findItem(R.id.favorite).setChecked(true);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("Đang đi chơi");

    }

    private void load(){
        setUpViewPager();
        loadSettings();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permission.checkResult(requestCode,permissions,grantResults);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            load();
        }

    }

    private void loadSettings(){
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

}