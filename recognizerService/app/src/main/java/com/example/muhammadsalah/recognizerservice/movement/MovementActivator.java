package com.example.muhammadsalah.recognizerservice.movement;

import android.content.Context;

import com.example.muhammadsalah.recognizerservice.movement.interfaces.MovementDetectionListener;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivator;

public class MovementActivator implements SpeechActivator, MovementDetectionListener {
    private MovementDetector detector;
    /**
     * speech activation service
     */
    private SpeechActivationListener resultListener;

    public MovementActivator(Context context, SpeechActivationListener resultListener) {
        detector = new MovementDetector(context);
        this.resultListener = resultListener;
    }
    @Override
    public void detectActivation() {
        detector.startReadingAccelerationData(this);
    }
    @Override
    public void stop() {
        detector.stopReadingAccelerationData();
    }
    @Override
    public void movementDetected(boolean success)
    {
        stop();
        resultListener.activated(success);
    }
}