package com.example.muhammadsalah.recognizerservice.commands;

import android.content.Intent;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.muhammadsalah.recognizerservice.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CommandSetter extends AppCompatActivity {
    private Button newCommand;
    private TextView command;
    private Button saveCommand;
    private static final String TAG = "CommandSetter";
    private ListView lView;
    private MyCustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_setter);
        setElements();
        final ArrayList<String> list = new ArrayList<String>();
        addItemstoList(getCommands(), list);

        //instantiate custom adapter

        adapter = new MyCustomAdapter(list, this, this);

        //handle listview and assign adapter

        lView = (ListView)findViewById(R.id.commandsList);
        lView.setAdapter(adapter);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null){
                    command.setText(matches.get(0).split("\\s")[0]);
                    saveCommand.setEnabled(true);
                }

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        newCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                command.setText("");
                command.setText("Listening...");
            }
        });

        saveCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCommand.setEnabled(false);
                storeCommand(command.getText().toString());
                addItemstoList(getCommands(),list);
                command.setText("");
            }

        });
    }
    private void addItemstoList(String[] items, ArrayList<String> l){
        l.clear();
        for(String item: items){
            l.add(item);
        }
        adapter = new MyCustomAdapter(l, this, this);

        //handle listview and assign adapter

        lView = (ListView)findViewById(R.id.commandsList);
        lView.setAdapter(adapter);
    }
    private void setElements(){
        command = findViewById(R.id.command);
        newCommand = findViewById(R.id.newCommand);
        saveCommand = findViewById(R.id.saveCommand);
    }

    public void createCommandFile() {
        File f = new File(Environment.getExternalStorageDirectory(), "recordings");

        if(!f.exists()){
            f.mkdir();
        }
        File commands = new File(f, "commands.txt");
        if(!commands.exists()) {
            FileWriter writer;
            try {
                writer = new FileWriter(commands);
                writer.append("record");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void storeCommand(String word){
        File f = new File(Environment.getExternalStorageDirectory(), "recordings");
        if(!f.exists()){
            f.mkdir();
        }
        File commands = new File(f, "commands.txt");
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(commands, true)));
            out.print(","+word);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] getCommands(){
        File f = new File(Environment.getExternalStorageDirectory(), "recordings");
        if(!f.exists()){
            return null;
        }
        File commands = new File(f, "commands.txt");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(commands));
            String str = br.readLine();
            return str.split(",");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteCommand(String word){
        File f = new File(Environment.getExternalStorageDirectory(), "recordings");
        if(!f.exists()){
            return;
        }
        File commands = new File(f, "commands.txt");
        BufferedReader br;
        String str = "";
        try {
            br = new BufferedReader(new FileReader(commands));
            str = br.readLine();
            Log.d(TAG, str);
            str = str.replaceAll(","+ word, "");
            Log.d(TAG, str);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileWriter fr;
        try{
            fr = new FileWriter(commands);
            fr.append(str);
            fr.flush();
            fr.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
