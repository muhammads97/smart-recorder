package com.example.muhammadsalah.recognizerservice.speechRecognition;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class SpeechRecognitionUtil {
    /**
     * @param recognizerIntent prev created intent for speeh recognition
     * @param listener the recognition service
     * @param recognizer SpeechRecognizer
     */
    public static void recognizeSpeechDirectly(Intent recognizerIntent,
                                               RecognitionListener listener,
                                               SpeechRecognizer recognizer) {
        //need to have a calling package for it to work
        if (!recognizerIntent.hasExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE))
        {
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.dummy");
        }

        recognizer.setRecognitionListener(listener);
        recognizer.startListening(recognizerIntent);
    }

    /**
     * @param errorCode speechRecognizer error code
     * @return String error message
     */
    public static String diagnoseErrorCode(int errorCode)
    {
        String message;
        switch (errorCode)
        {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
