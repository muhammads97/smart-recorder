package com.example.muhammadsalah.recognizerservice.clapper;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.clapper.interfaces.AmplitudeClipListener;

import java.io.IOException;

public class MaxAmplitudeRecorder
{
    private static final String TAG = "MaxAmplitudeRecorder";

    private static final long DEFAULT_CLIP_TIME = 1000;
    private long clipTime = DEFAULT_CLIP_TIME;

    /**
     * single clap detector
     */
    private AmplitudeClipListener clipListener;
    private MediaRecorder recorder;
    private AsyncTask task;
    private String tmpAudioFile;
    private boolean continueRecording;
    /**
     * @param clipTime time to wait in between maxAmplitude checks
     * @param tmpAudioFile should be a file where the MediaRecorder can write temporary audio data
     * @param clipListener called periodically to analyze the max amplitude
     * @param task stop recording if the task is canceled
     */
    public MaxAmplitudeRecorder(long clipTime, String tmpAudioFile,
                                AmplitudeClipListener clipListener, AsyncTask task)
    {
        this.clipTime = clipTime;
        this.clipListener = clipListener;
        this.tmpAudioFile = tmpAudioFile;
        this.task = task;
    }
    /**
     * start recording maximum amplitude and passing it to the clipListener
     * @throws IllegalStateException if there is trouble creating the recorder
     * @throws IOException if the SD card is not available
     * @throws RuntimeException if audio recording channel is occupied
     * @return true if clipListener succeeded in detecting something
     * false if it failed or the recording stopped for some other reason
     */
    public boolean startRecording() throws IOException {
        //Log.d(TAG, "recording maxAmplitude");
        recorder = prepareRecorder(tmpAudioFile);
        // when an error occurs just stop recording
        recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                // stop recording
                stopRecording();
            }
        });
        //possible RuntimeException if Audio recording channel is occupied
        recorder.start();
        continueRecording = true;
        boolean heard = false;
        recorder.getMaxAmplitude();
        while (continueRecording) {
            //Log.d(TAG, "waiting while recording...");
            waitClipTime();
            if ((!continueRecording) || ((task != null) && task.isCancelled())) {
                break;
            }
            int maxAmplitude = recorder.getMaxAmplitude();
            //Log.d(TAG, "current max amplitude: " + maxAmplitude);
            heard = clipListener.heard(maxAmplitude);
            if (heard) {
                stopRecording();
                break;
            }
        }
        //Log.d(TAG, "stopped recording max amplitude");
        done();
        return heard;
    }

    /**
     * sleep clipTime
     */
    private void waitClipTime() {
            try {
                Thread.sleep(clipTime);
            } catch (InterruptedException e) {
                Log.d(TAG, "interrupted");
            }
    }

    /**
     * stop the recorder and clean
     */
    private void done() {
        //Log.d(TAG, "stop recording on done");
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (Exception e) {
                Log.d(TAG, "failed to stop");
                return;
            }
            recorder.release();
        }
    }

    /**
     * @return boolean continue recording
     */
    public boolean isRecording() {
            return continueRecording;
    }

    /**
     * set continue recording to false
     */
    public void stopRecording() {
            continueRecording = false;
    }

    /**
     * @param sdCardPath audio file path to store temp audio
     * @return prepared media recorder
     * @throws IOException for the media recorder
     */
    public static MediaRecorder prepareRecorder(String sdCardPath) throws IOException {
        if (!isStorageReady()) {
            throw new IOException("SD card is not available");
        }
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        Log.d(TAG, "recording to: " + sdCardPath);
        recorder.setOutputFile(sdCardPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.prepare();
        return recorder;
    }

    /**
     * @return boolean to indicate that sd card is available
     */
    private static boolean isStorageReady(){
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }
}


