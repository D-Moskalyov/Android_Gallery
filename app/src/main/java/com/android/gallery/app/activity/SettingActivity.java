package com.android.gallery.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.*;
import com.android.gallery.app.R;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

public class SettingActivity extends Activity implements AdapterView.OnItemSelectedListener {

    SettingActivity settingActivity;
    SharedPreferences settings;
    int currentPos;
    ArrayList<String> colors;
    Spinner spinner;

    private static final int START_EXPLORER = 2;
    private static final int LOAD_PICTURE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingActivity = this;
        currentPos = 0;
        colors = new ArrayList<String>();
        settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Button selFolder = (Button) findViewById(R.id.sel_folder);
        spinner = (Spinner) findViewById(R.id.snipper);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek);
        final TextView interval = (TextView) findViewById(R.id.interval);

        Set<String> histDir = settings.getStringSet("historyDir", new ArraySet<String>());
        LinkedList<String> historyDir = new LinkedList<String>();
        String currDir = settings.getString("currentDir", "def");

        if(currDir != "def") {
            histDir.remove(currDir);
            historyDir.addAll(histDir);
            historyDir.addFirst(currDir);
        }

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
                editor.putInt("intervalSH", seekBar.getVerticalScrollbarPosition() + 1);
                editor.apply();
            }
        });

        selFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingActivity.setResult(START_EXPLORER);
                settingActivity.finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(currentPos != position){
            File path = new File(colors.get(position));
            if(!path.exists()){
                Toast.makeText(this, "Path not exist", Toast.LENGTH_SHORT).show();
                //delete from prefs
                colors.remove(colors.get(position));
                spinner.setSelection(0, true);

                return;
            }
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getAbsolutePath().matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)");
                }
            };
            File[] files = path.listFiles(filter);

            if(files == null || files.length == 0){
                Toast.makeText(this, "Picture not exist in folder", Toast.LENGTH_SHORT).show();
                //delete from prefs
                colors.remove(colors.get(position));
                spinner.setSelection(0, true);

                return;
            }
            Intent intent = new Intent();
            intent.putExtra("path", colors.get(position));
            this.setResult(LOAD_PICTURE, intent);
            currentPos = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        int i = 0;
    }
}
