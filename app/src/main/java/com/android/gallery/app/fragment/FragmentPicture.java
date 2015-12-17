package com.android.gallery.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.gallery.app.R;
import com.android.gallery.app.activity.MainActivity;
import com.android.gallery.app.activity.SwipePictActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentPicture extends Fragment {
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = getArguments();
        return inflater.inflate(R.layout.fragment_picture, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        String imgPath = bundle.getString("path");
        final File imgFile = new File(imgPath);
        if(imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            ImageView imageView = (ImageView) this.getView().findViewById(R.id.image_view);
//            imageView.setImageBitmap(myBitmap);
            //((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
            //imageView.invalidate();

            SimpleDraweeView simpleDraweeView =
                    (SimpleDraweeView) this.getView().findViewById(R.id.image_view);
            simpleDraweeView.setImageURI(Uri.fromFile(imgFile));

            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SwipePictActivity.class);
                    List<String> photosPath = ((MainActivity) getActivity()).getPictures();
                    int startPict = 0;
                    for(int i = 0; i < photosPath.size(); i++){
                        if(photosPath.get(i) == imgFile.getAbsolutePath()) {
                            startPict = i;
                            break;
                        }
                    }

                    intent.putStringArrayListExtra("photosPath", new ArrayList<String>(photosPath));
                    intent.putExtra("startPict", startPict);
                    startActivity(intent);

                }
            });
        }
    }
}
