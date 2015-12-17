package com.android.gallery.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.gallery.app.R;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class FragmentSwipePict extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_swiped_picture, container, false);
        Bundle args = getArguments();
        String photoPath = args.getString(ARG_OBJECT);

        final File imgFile = new File(photoPath);
        if(imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(photoPath);
//            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
//            imageView.setImageBitmap(myBitmap);
            //((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
            //imageView.invalidate();

            SimpleDraweeView simpleDraweeView = ((SimpleDraweeView) rootView.findViewById(R.id.image_view));
            simpleDraweeView
                    .getHierarchy()
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            simpleDraweeView.setImageURI(Uri.fromFile(imgFile));
        }

        return rootView;
    }
}
