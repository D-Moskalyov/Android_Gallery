package com.android.gallery.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.ArraySet;
import android.view.View;
import android.widget.*;
import com.android.gallery.app.R;
import com.turhanoz.android.reactivedirectorychooser.event.OnDirectoryCancelEvent;
import com.turhanoz.android.reactivedirectorychooser.event.OnDirectoryChosenEvent;
import com.turhanoz.android.reactivedirectorychooser.ui.DirectoryChooserFragment;
import com.turhanoz.android.reactivedirectorychooser.ui.OnDirectoryChooserFragmentInteraction;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

public class SettingActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener, OnDirectoryChooserFragmentInteraction {

    SettingActivity settingActivity;
    SharedPreferences settings;
    File currentRootDirectory;
    //int currentPos;
    Set<String> histDir;
    LinkedList<String> historyDir;
    Spinner spinner;


    private static final int START_EXPLORER = 2;
    private static final int LOAD_PICTURE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingActivity = this;
        //currentPos = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);

        Button selFolder = (Button) findViewById(R.id.sel_folder);
        spinner = (Spinner) findViewById(R.id.snipper);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek);
        final TextView interval = (TextView) findViewById(R.id.interval);

        seekBar.setProgress(settings.getInt("intervalSH", 2) - 1);
        interval.setText(String.valueOf(settings.getInt("intervalSH", 2)) + " sec");

        histDir = settings.getStringSet("historyDir", new HashSet<String>());
        historyDir = new LinkedList<String>();
        String currDir = settings.getString("currentDir", "def");

        if(currDir != "def") {
            //histDir.remove(currDir);
            historyDir.addAll(histDir);
            historyDir.remove(currDir);
            historyDir.addFirst(currDir);

            currentRootDirectory = new File(currDir);
        }
        else
            currentRootDirectory = Environment.getExternalStorageDirectory();

//        colors.add("Red");
//        colors.add("Blue");
//        colors.add("White");
//        colors.add("Yellow");
//        colors.add("Black");
//        colors.add("Green");
//        colors.add("Purple");
//        colors.add("Orange");
//        colors.add("Grey");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, historyDir);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                interval.setText(String.valueOf(progress + 1) + " sec");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("intervalSH", seekBar.getProgress() + 1);
                editor.apply();
            }
        });

        selFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDirectoryChooserAsFloatingFragment();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(currentPos != position){
            File path = new File(historyDir.get(position));
            if(!path.exists()){
                histDir.remove(path.getAbsolutePath());
                SharedPreferences.Editor editor = settings.edit();
                editor.putStringSet("historyDir", histDir);
                editor.apply();

                Toast.makeText(this, "Path not exist", Toast.LENGTH_SHORT).show();

                historyDir.remove(historyDir.get(position));
                spinner.setSelection(0, true);
                //currentPos = 0;
                return;
            }
            if(!ScanDirectory(path)){
//                Intent intent = new Intent();
//                intent.putExtra("path", path.getAbsolutePath());
//                this.setResult(LOAD_PICTURE, intent);
//                //currentPos = position;
//                //spinner.setSelection(position, true);

                histDir.remove(path.getAbsolutePath());
                SharedPreferences.Editor editor = settings.edit();
                editor.putStringSet("historyDir", histDir);
                editor.apply();

                historyDir.remove(path.getAbsolutePath());
                spinner.setSelection(0, true);
                //currentPos = 0;
            }

        //}
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        int i = 0;
    }

    void addDirectoryChooserAsFloatingFragment() {
        DialogFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(currentRootDirectory);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        directoryChooserFragment.show(transaction, "RDC");
    }

    @Override
    public void onEvent(OnDirectoryCancelEvent onDirectoryCancelEvent) {

    }

    @Override
    public void onEvent(OnDirectoryChosenEvent onDirectoryChosenEvent) {
        File directoryChosenByUser = onDirectoryChosenEvent.getFile();
        if(ScanDirectory(directoryChosenByUser)){
            histDir.add(directoryChosenByUser.getAbsolutePath());
            SharedPreferences.Editor editor = settings.edit();
            editor.putStringSet("historyDir", histDir);
            editor.apply();

            this.finish();
        }
    }

    boolean ScanDirectory(File path){
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getAbsolutePath().matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)");
            }
        };
        File[] files = path.listFiles(filter);
        if(files != null & files.length != 0) {
            Intent intent = new Intent();
            intent.putExtra("path", path.getAbsolutePath());
            this.setResult(LOAD_PICTURE, intent);
            return true;
        }
        else {
            Toast.makeText(this, "Picture not found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
