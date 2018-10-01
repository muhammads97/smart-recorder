package com.example.muhammadsalah.recognizerservice.clapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;

import java.io.File;
import java.io.IOException;

public class ClapperSpeechActivationTask extends AsyncTask<Void, Void, Boolean>
{
    private static final String TAG = "ClapperActivationTask";
    /**
     * recognition service
     */
    private SpeechActivationListener listener;
    /**
     * app context
     */
    private Context context;
    /**
     * directory to store temp audio file
     */
    private static final String TEMP_AUDIO_DIRECTORY = "tempaudio";

    /**
     * time between amplitude checks
     */
    private static final int CLIP_TIME = 1000;

    public  ClapperSpeechActivationTask(Context context, SpeechActivationListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected  void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  Boolean doInBackground(Void... params) {
        boolean heard = detectClap();
        return heard;
    }

    /**
     * start detecting a clap, return when done
     * @return a boolean to indicate a clap was detected or not
     */
    private boolean detectClap() {
        SingleClapDetector clapper = new SingleClapDetector(SingleClapDetector.AMPLITUDE_DIFF_MED);
        //Log.d(TAG, "recording amplitude");
        String audioStorageDirectory = context.getExternalFilesDir(TEMP_AUDIO_DIRECTORY)
                        + File.separator + "audio.3gp";

        // pass in this so recording can stop if this task is canceled
        MaxAmplitudeRecorder recorder =
                new MaxAmplitudeRecorder(CLIP_TIME, audioStorageDirectory, clapper, this);

        // start recording
        boolean heard;
        try {
            heard = recorder.startRecording();
        } catch (IOException io) {
            Log.e(TAG, "failed to record", io);
            heard = false;
        } catch (IllegalStateException se) {
            Log.e(TAG, "failed to record, recorder not setup properly", se);
            heard = false;
        } catch (RuntimeException se) {
            Log.e(TAG, "failed to record, recorder already being used", se);
            heard = false;
        }
        return heard;
    }

    @Override
    protected  void onPostExecute(Boolean result)
    {
        listener.activated(result);
        super.onPostExecute(result);
    }

    @Override
    protected  void onCancelled()
    {
        //Log.d(TAG, "cancelled");
        super.onCancelled();
    }
}
