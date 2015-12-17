package com.android.gallery.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.android.gallery.app.R;
import com.android.gallery.app.service.PhotoCollectionPagerAdapter;

import java.util.ArrayList;

public class SwipePictActivity extends FragmentActivity {

    PhotoCollectionPagerAdapter pCollPagerAdapter;
    ViewPager viewPager;
    static int startPict;
    static ArrayList<String> photosPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_picture);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        startPict = bundle.getInt("startPict");
        photosPath = bundle.getStringArrayList("photosPath");

        pCollPagerAdapter = new PhotoCollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pCollPagerAdapter);

        viewPager.setCurrentItem(startPict);
    }

    static public int getStartPict() {
        return startPict;
    }

    public static ArrayList<String> getPhotosPath() {
        return photosPath;
    }
}
