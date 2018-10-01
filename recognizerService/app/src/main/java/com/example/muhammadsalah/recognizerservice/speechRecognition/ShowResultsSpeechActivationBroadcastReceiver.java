package com.example.muhammadsalah.recognizerservice.speechRecognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.MainActivity;
import com.example.muhammadsalah.recognizerservice.record.recorderService;

public class ShowResultsSpeechActivationBroadcastReceiver extends
        BroadcastReceiver
{
    private static final String TAG =
            "ActivatBroadcastReceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive","on receive");
        if (intent.getAction().equals(
                SpeechActivationService.ACTIVATION_RESULT_BROADCAST_NAME)){
            if (intent
                    .getBooleanExtra(
                            SpeechActivationService.ACTIVATION_RESULT_INTENT_KEY,
                            false))
            {
                Log.d(TAG, "ShowResultsSpeechActivationBroadcastReceiver taking action");
                // launch something that prompts the user...
                /*
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("stopService", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                */
                Intent recorderIntent = new Intent(context, recorderService.class);
                context.startService(recorderIntent);
            }
        }
    }
}