package com.example.muhammadsalah.recognizerservice.clapper;

import android.content.Context;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivator;

public class ClapperActivator implements SpeechActivator {
    private static final String TAG = "ClapperActivator";
    /**
     * asyncTask to detect claps without interrupting the application
     */
    private ClapperSpeechActivationTask activeTask;
    /**
     * recognition service
     */
    private SpeechActivationListener listener;
    /**
     * app context
     */
    private Context context;

    public  ClapperActivator(Context context, SpeechActivationListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public  void detectActivation()
    {
        Log.d(TAG, "started clapper activation");
        activeTask = new ClapperSpeechActivationTask(context, listener);
        activeTask.execute();
    }

    @Override
    public  void stop()
    {
        if (activeTask != null)
        {
            activeTask.cancel(true);
        }
    }
}
