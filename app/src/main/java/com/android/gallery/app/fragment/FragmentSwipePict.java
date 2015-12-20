package com.android.gallery.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.gallery.app.R;
import com.android.gallery.app.activity.SwipePictActivity;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class FragmentSwipePict extends Fragment {
    public static final String ARG_OBJECT = "object";
    Fragment frag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_swiped_picture, container, false);
        Bundle args = getArguments();
        String photoPath = args.getString(ARG_OBJECT);
        frag = this;

        final File imgFile = new File(photoPath);
        if(imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(photoPath);
//            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
//            imageView.setImageBitmap(myBitmap);
            //((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
            //imageView.invalidate();

            final SimpleDraweeView simpleDraweeView = ((SimpleDraweeView) rootView.findViewById(R.id.image_view));
            simpleDraweeView
                    .getHierarchy()
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            simpleDraweeView.setImageURI(Uri.fromFile(imgFile));

            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwipePictActivity.getHandler().removeCallbacks(SwipePictActivity.getThread());
                    Toast.makeText(getActivity(), "Long press to continue", Toast.LENGTH_SHORT).show();
                }
            });
            simpleDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SwipePictActivity.getHandler().removeCallbacks(SwipePictActivity.getThread());
                    SwipePictActivity.getHandler().postDelayed(SwipePictActivity.getThread(),
                            SwipePictActivity.getSlideInterval());
                    return true;
                }
            });
        }

        return rootView;
    }
}
