package com.example.muhammadsalah.recognizerservice.word;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.speechRecognition.SoundsLikeWordMatcher;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivator;
import com.example.muhammadsalah.recognizerservice.speechRecognition.SpeechRecognitionUtil;

import java.util.List;

public class WordActivator implements SpeechActivator, RecognitionListener
{
    private static final String TAG = "WordActivator";

    private Context context;
    private SpeechRecognizer recognizer;
    private SoundsLikeWordMatcher matcher;
    private SpeechActivationListener resultListener;

    public WordActivator(Context context,
                         SpeechActivationListener resultListener, String... targetWords) {
        this.context = context;
        this.matcher = new SoundsLikeWordMatcher(targetWords);
        this.resultListener = resultListener;
    }

    @Override
    public void detectActivation()
    {
        recognizeSpeechDirectly();
    }

    /**
     * Start speech recognition using speechRecognitionUtil and recognizerIntent
     */
    private void recognizeSpeechDirectly()
    {
        Intent recognizerIntent =
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        // accept partial results if they come
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        SpeechRecognitionUtil.recognizeSpeechDirectly(recognizerIntent, this, getSpeechRecognizer());
    }

    /**
     * stop the recognition
     */
    public void stop()
    {
        if (getSpeechRecognizer() != null)
        {
            getSpeechRecognizer().stopListening();
            getSpeechRecognizer().cancel();
            getSpeechRecognizer().destroy();
        }
    }

    @Override
    public void onResults(Bundle results)
    {
        Log.d(TAG, "full results");
        receiveResults(results);
    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {
        Log.d(TAG, "partial results");
        receiveResults(partialResults);
    }

    /**
     * common method to process any results bundle
     */
    private void receiveResults(Bundle results)
    {
        if ((results != null)
                && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION))
        {
            List<String> heard =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            float[] scores =
                    results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
            receiveWhatWasHeard(heard, scores);
        }
        else
        {
            Log.d(TAG, "no results");
        }
    }

    /**
     * @param heard List of strings Heard by the recognizer
     * @param scores recognition scores
     */
    private void receiveWhatWasHeard(List<String> heard, float[] scores)
    {
        boolean heardTargetWord = false;
        // find the target word
        for (String possible : heard)
        {
            WordList wordList = new WordList(possible);
            if (matcher.isIn(wordList.getWords()))
            {
                Log.d(TAG, "HEARD IT!");
                heardTargetWord = true;
                break;
            }
        }

        if (heardTargetWord)
        {
            stop();
            resultListener.activated(true);
        }
        else
        {
            // keep going
            recognizeSpeechDirectly();
        }
    }

    @Override
    public void onError(int errorCode)
    {
        if ((errorCode == SpeechRecognizer.ERROR_NO_MATCH)
                || (errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT))
        {
            Log.d(TAG, "didn't recognize anything");
            // keep going
            recognizeSpeechDirectly();
        }
        else
        {
            Log.d(TAG,
                    "FAILED "
                            + SpeechRecognitionUtil
                            .diagnoseErrorCode(errorCode));
        }
    }

    /**
     * lazy initialize the speech recognizer
     */
    private SpeechRecognizer getSpeechRecognizer()
    {
        if (recognizer == null)
        {
            recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        }
        return recognizer;
    }

    // other unused methods from RecognitionListener...

    @Override
    public void onReadyForSpeech(Bundle params)
    {
        Log.d(TAG, "ready for speech " + params);
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}