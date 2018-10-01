package com.example.muhammadsalah.recognizerservice.fileViewer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadsalah.recognizerservice.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyFileAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> filesnames = new ArrayList<String>();

    private Context context;
    MediaPlayer myMediaPlayer;



    public MyFileAdapter(ArrayList<String> list, Context context) {
        this.filesnames = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filesnames.size();
    }

    @Override
    public Object getItem(int pos) {
        return filesnames.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Log.wtf("error","55555");
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recording, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(filesnames.get(position));

        //Handle buttons and add onClickListeners
        ImageButton play = (ImageButton)view.findViewById(R.id.play);
        final ImageButton pause = (ImageButton)view.findViewById(R.id.pause);

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File directory = new File(Environment.getExternalStorageDirectory(), "recordings");
                String filename = directory+"/"+listItemText.getText();

                myMediaPlayer = new MediaPlayer();
                try {
                    myMediaPlayer.setDataSource(filename);
                    myMediaPlayer.prepare();
                    myMediaPlayer.start();
                    Toast.makeText(context,"audio is  playing", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                    myMediaPlayer.pause();
                    Toast.makeText(context, "audio paused", Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }
}