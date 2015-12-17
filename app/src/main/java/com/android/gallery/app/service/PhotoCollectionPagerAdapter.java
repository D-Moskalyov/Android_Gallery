package com.android.gallery.app.service;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.android.gallery.app.activity.SwipePictActivity;
import com.android.gallery.app.fragment.FragmentSwipePict;

import java.util.ArrayList;
import java.util.List;

public class PhotoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    int startPict;
    ArrayList<String> photosPath;

    public PhotoCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        photosPath = SwipePictActivity.getPhotosPath();
        startPict = SwipePictActivity.getStartPict();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FragmentSwipePict();
        Bundle args = new Bundle();
        String photoPath = photosPath.get(position);
        args.putString(FragmentSwipePict.ARG_OBJECT, photoPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return photosPath.size();
    }
}
