package com.example.muhammadsalah.recognizerservice.speechRecognition;

import android.content.Context;

import com.example.muhammadsalah.recognizerservice.R;
import com.example.muhammadsalah.recognizerservice.clapper.ClapperActivator;
import com.example.muhammadsalah.recognizerservice.commands.CommandSetter;
import com.example.muhammadsalah.recognizerservice.movement.MovementActivator;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivator;
import com.example.muhammadsalah.recognizerservice.word.WordActivator;

public class SpeechActivatorFactory {
    /**
     * @param context App Context
     * @param callback speechActivationListener (the service)
     * @param type Activator type
     * @return SpeechActivator Object
     */
    public static SpeechActivator createSpeechActivator(Context context, SpeechActivationListener callback, String type) {
        SpeechActivator speechActivator = null;

        if (type.equals(context.getResources().getString(R.string.speech_activation_button))) {
            speechActivator = null;
        } else if (type.equals(context.getResources().getString(R.string.speech_activation_movement))) {
            speechActivator = new MovementActivator(context, callback);
        } else if (type.equals(context.getResources().getString(R.string.speech_activation_clap))) {
            speechActivator = new ClapperActivator(context, callback);
        } else if (type.equals(context.getResources().getString(R.string.speech_activation_speak))) {
            speechActivator = new WordActivator(context, callback, new CommandSetter().getCommands());
        }
        return speechActivator;
    }

    /**
     * @param context app context
     * @param speechActivator activator
     * @return activator type
     */
    public static String getLabel(Context context, SpeechActivator speechActivator) {
        String label = "";
        if (speechActivator == null) {
            label = context.getString(R.string.speech_activation_button);
        } else if (speechActivator instanceof WordActivator) {
            label = context.getString(R.string.speech_activation_speak);
        } else if (speechActivator instanceof ClapperActivator){
            label = context.getString(R.string.speech_activation_clap);
        } else if (speechActivator instanceof MovementActivator){
            label = context.getString(R.string.speech_activation_movement);
        } else {
            label = context.getString(R.string.speech_activation_button);
        }
        return label;
    }
}
