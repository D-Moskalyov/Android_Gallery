package com.android.gallery.app.activity;

import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.gallery.app.R;
import com.android.gallery.app.fragment.FragmentPicture;
import com.turhanoz.android.reactivedirectorychooser.event.OnDirectoryCancelEvent;
import com.turhanoz.android.reactivedirectorychooser.event.OnDirectoryChosenEvent;
import com.turhanoz.android.reactivedirectorychooser.ui.DirectoryChooserFragment;
import com.turhanoz.android.reactivedirectorychooser.ui.OnDirectoryChooserFragmentInteraction;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements OnDirectoryChooserFragmentInteraction {

    File currentRootDirectory = Environment.getExternalStorageDirectory();
    List<String> pictPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictPath = new ArrayList<String>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            addDirectoryChooserAsFloatingFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void addDirectoryChooserAsFloatingFragment() {
        DialogFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(currentRootDirectory);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        directoryChooserFragment.show(transaction, "RDC");
    }

    @Override
    public void onEvent(OnDirectoryChosenEvent event) {
        File directoryChosenByUser = event.getFile();
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getAbsolutePath().matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)");
            }
        };
        File[] files = directoryChosenByUser.listFiles(filter);
        if(files != null & files.length != 0)
            CreateFragments(files);
        else
            Toast.makeText(this, "Picture not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEvent(OnDirectoryCancelEvent event) {
        int i = 1;
    }

    void CreateFragments(File[] pictures){
        LinearLayout photo_lt = (LinearLayout) findViewById(R.id.photo_lt);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        LinearLayout layout = new LinearLayout(this);

        int i = 0;
        int j;
        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE)
            j = 6;
        else
            j = 3;

        String absolutePath;
        for(File picture : pictures) {
            if (i % j == 0) {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(layoutParams);
                layout.setId(View.generateViewId());

                photo_lt.addView(layout);
            }

            FragmentPicture fragmentPicture = new FragmentPicture();

            Bundle bundle = new Bundle();
            absolutePath = picture.getAbsolutePath();
            pictPath.add(absolutePath);
            bundle.putString("path", absolutePath);

            fragmentPicture.setArguments(bundle);
            ft.add(layout.getId(), fragmentPicture);

            i++;
        }
        ft.commit();
    }

    public List<String> getPictures(){
        return pictPath;
    }
}
