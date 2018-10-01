package com.example.muhammadsalah.recognizerservice.record;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.muhammadsalah.recognizerservice.MainActivity;
import com.example.muhammadsalah.recognizerservice.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class recorderService extends Service {
    private final static String TAG = "recorderServiceTags";

    private MediaRecorder audioRecorder;
    private String outputFile = new String();
    private List<File> files = new LinkedList<File>();
    private boolean continueRecording ;
    private final static int SILENCE_THRESHOLD = 1000;

    SilenceTask silenceTask = null;

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent !=null ){
            if (intent.hasExtra("stop")) {
                stop();
                intent.removeExtra("stop");
                Toast.makeText(getApplicationContext(), "stopped recording", Toast.LENGTH_LONG).show();

            } else if (intent.hasExtra("pause")) {
                if (Build.VERSION.SDK_INT >= 24) {
                    audioRecorder.pause();
                    Toast.makeText(getApplicationContext(), "paused", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "pause is  not available", Toast.LENGTH_LONG).show();
                }
                intent.removeExtra("pause");

            } else if (intent.hasExtra("resume")) {
                if (Build.VERSION.SDK_INT >= 24) {
                    audioRecorder.resume();
                    Toast.makeText(getApplicationContext(), "resumed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "resume is  not available", Toast.LENGTH_LONG).show();
                }
               intent.removeExtra("resume");

            } else {
                prepareRecorder();
                buildNotification();
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * set the Recorded file name ,mediaRecorders parameters and start recording
     */
    private void prepareRecorder(){
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH-mm-ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        File rootPath = new File(Environment.getExternalStorageDirectory(), "recordings");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        String date = sdf.format(cal.getTime());
        Log.d(TAG, date);
        outputFile = rootPath+ "/recording "+date.toString()+".3gp";
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(outputFile);
        try{
            audioRecorder.prepare();
            audioRecorder.start();

            silenceTask =  new SilenceTask(audioRecorder,this);
            silenceTask.execute();
            Toast.makeText(getApplicationContext(),"started recording",Toast.LENGTH_LONG).show();
          } catch (Exception e){
                 Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * build the corresponding notification bar with its actions
     */
    private void buildNotification(){
        Intent i = new Intent(this,MainActivity.class);
        PendingIntent intent =
                PendingIntent.getService(this, 0,i ,
                        0);
        Notification  notification ;
        Notification.Builder builder = new Notification.Builder(this);

        Intent intentStop = new Intent(this,recorderService.class);
        intentStop.putExtra("stop", true);
        PendingIntent pendingIntentStop =  PendingIntent.getService(this,0,intentStop,0);


       Intent intentpause = new Intent(this,recorderService.class);
        intentpause.putExtra("pause", true);
        PendingIntent pendingIntentPause =  PendingIntent.getService(this,1,intentpause,0);

        Intent intentresume = new Intent(this,recorderService.class);
        intentresume.putExtra("resume", true);
        PendingIntent pendingIntentresume =  PendingIntent.getService(this,2,intentresume,0);

        builder.setSmallIcon(R.drawable.ic_mic_black_24dp).setWhen(System.currentTimeMillis()).setTicker("recording")
                .setContentTitle("smartRecorder").setContentText("recording").setContentIntent(intent);

                builder.addAction(R.drawable.ic_stop_black_24dp,"stop",pendingIntentStop);
                builder.addAction(R.drawable.ic_pause_circle_filled_black_24dp,"pause",pendingIntentPause);
                builder.addAction(R.drawable.ic_play_circle_filled_black_24dp,"resume",pendingIntentresume);
        notification = builder.getNotification();
        startForeground(10298,notification);
    }

    /**
     * stop recording and prepare for the next recording
     */
    private void stop(){
        if(audioRecorder != null){
            audioRecorder.stop();
            audioRecorder.reset();
            audioRecorder.release();
            audioRecorder = null;
        }

        if(silenceTask != null){
            silenceTask.cancel(true);
        }
        destroyNotification();
    }
    @Override
    public void onDestroy() {
        if(audioRecorder != null){
            audioRecorder.stop();
            audioRecorder.reset();
            audioRecorder.release();
            audioRecorder = null;
        }
        if(silenceTask != null){
            silenceTask.cancel(true);
        }
        destroyNotification();
        super.onDestroy();
    }

    /**
     * remove the notification bar when stop recording
     */
    private void destroyNotification(){
        stopForeground(true);
        //startForeground(0,null);
    }

    /**
     * merge recorded files into one file
     * @param mergedFile
     * @param mp3Files
     */
    private void mergeSongs(File mergedFile,File...mp3Files){
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mergedFile);
            fileInputStream = new FileInputStream(mergedFile);
            for(File mp3File:mp3Files){
                if(!mp3File.exists())
                    continue;
                FileInputStream fileInputStream1 = new FileInputStream(mp3File);
                SequenceInputStream sequenceInputStream = new SequenceInputStream(fileInputStream, fileInputStream1);
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fileInputStream1.read(buf)) != -1;)
                        fileOutputStream.write(buf, 0, readNum);
                } finally {
                    if(fileInputStream1!=null){
                        fileInputStream1.close();
                    }
                    if(sequenceInputStream!=null){
                        sequenceInputStream.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(fileOutputStream!=null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if(fileInputStream!=null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SilenceTask extends AsyncTask <Void, Void, Void>{
        MediaRecorder mediaRecorder;
        recorderService service ;
        SilenceTask(MediaRecorder m , recorderService service){
            mediaRecorder = m;
            this.service = service;
        }

        private void silenceDetector(MediaRecorder mediaRecorder){

            mediaRecorder.getMaxAmplitude();
            int timeOfSilence = 0;
            while (timeOfSilence<10){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    Log.d(TAG,e.getMessage());
                }

                int maxAmp = mediaRecorder.getMaxAmplitude();
                Log.d(TAG, String.valueOf(maxAmp));
                if (maxAmp<SILENCE_THRESHOLD){
                    timeOfSilence ++;

                }else {
                    timeOfSilence = 0;
                }
            }
            service.stop();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            silenceDetector(mediaRecorder);
            return null;
        }

        @Override
        protected void onCancelled() {
            service.stop();
            super.onCancelled();
        }
    }

}
