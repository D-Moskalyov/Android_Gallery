package com.android.gallery.app.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.gallery.app.R;
import com.android.gallery.app.activity.MainActivity;
import com.android.gallery.app.activity.SwipePictActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
        //int heightScreen = ((MainActivity)(getActivity().getParent())).getHeightScreen();
        //int widthScreen = ((MainActivity)(getActivity().getParent())).getWidthScreen();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int heightScreen = displaymetrics.heightPixels;
//        int widthScreen = displaymetrics.widthPixels;

        String imgPath = bundle.getString("path");
        final File imgFile = new File(imgPath);
        if(imgFile.exists()) {

            SimpleDraweeView simpleDraweeView =
                    (SimpleDraweeView) this.getView().findViewById(R.id.image_view);

            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.fromFile(imgFile))
                    .setImageType(ImageRequest.ImageType.SMALL)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();

            simpleDraweeView.setController(controller);

//            LinearLayout linearLayout = (LinearLayout) this.getView().findViewById(R.id.layoutA);
//            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(heightScreen / 10, widthScreen / 10));
//            if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
//                simpleDraweeView.setMaxWidth(widthScreen / 6);
//                simpleDraweeView.setMaxHeight(heightScreen / 6);
//            }
//            else {
//                simpleDraweeView.setMaxWidth(widthScreen / 10);
//                simpleDraweeView.setMaxHeight(heightScreen / 10);
//            }


            //simpleDraweeView.setImageURI(Uri.fromFile(imgFile));

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
