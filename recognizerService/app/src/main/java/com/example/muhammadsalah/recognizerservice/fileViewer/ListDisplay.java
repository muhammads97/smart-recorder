package com.example.muhammadsalah.recognizerservice.fileViewer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.example.muhammadsalah.recognizerservice.R;

import java.io.File;
import java.util.ArrayList;

public class ListDisplay extends Activity {
     File directory;

    ArrayList<String> filenames = new ArrayList<String>();
    MediaPlayer myMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display);

        directory = new File(Environment.getExternalStorageDirectory(), "recordings");
        final File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].getName().indexOf("commands.txt")==-1) {
                filenames.add(files[i].getName());
            }
        }

        MyFileAdapter adapter = new MyFileAdapter(filenames, this);

        ListView lView = findViewById(R.id.file_list);
        lView.setAdapter(adapter);

     /*   ArrayAdapter adapter = new ArrayAdapter<String>(this,
                        R.layout.activity_listview, filenames);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }

        });
        */
    }


}