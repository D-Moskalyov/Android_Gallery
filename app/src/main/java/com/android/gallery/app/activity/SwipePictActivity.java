package com.android.gallery.app.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.android.gallery.app.R;
import com.android.gallery.app.service.PhotoCollectionPagerAdapter;

import java.util.ArrayList;

public class SwipePictActivity extends FragmentActivity {

    PhotoCollectionPagerAdapter pCollPagerAdapter;
    ViewPager viewPager;
    static int startPict;
    static ArrayList<String> photosPath;
    boolean firstScreen;

    static Handler handler;
    static Thread thread;
    static int slideInterval;
    SwipePictActivity swipePictActivity;
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_picture);

        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        slideInterval = settings.getInt("intervalSH", 3000) * 1000;
        handler = new Handler();
        firstScreen = true;

        swipePictActivity = this;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        startPict = bundle.getInt("startPict");
        photosPath = bundle.getStringArrayList("photosPath");

        pCollPagerAdapter = new PhotoCollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pCollPagerAdapter);
        viewPager.setCurrentItem(startPict);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Toast.makeText(swipePictActivity, "Long press to continue", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(thread);
                return false;
            }
        });

        thread = new Thread() {
            @Override
            public void run() {
                if(firstScreen){
                    firstScreen = false;
                    handler.postDelayed(this, slideInterval);
                }
                else {
                    if (viewPager.getCurrentItem() == pCollPagerAdapter.getCount() - 1)
                        viewPager.setCurrentItem(0);
                    else
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    //Toast.makeText(swipePictActivity, "1000", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this, slideInterval);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null)
            handler.removeCallbacks(thread);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (handler != null)
//            handler.postDelayed(thread, slideInterval);
    }

    static public int getStartPict() {
        return startPict;
    }

    static public Handler getHandler() {
        return handler;
    }

    static public Thread getThread() {
        return thread;
    }

    static public int getSlideInterval() {
        return slideInterval;
    }

    public static ArrayList<String> getPhotosPath() {
        return photosPath;
    }
}
