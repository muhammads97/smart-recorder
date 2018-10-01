package com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces;

import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;

public interface SpeechActivator {
    /**
     * listen for speech activation, when heard, call a {@link SpeechActivationListener}
     * and stop listening
     */
    void detectActivation();

    /**
     * stop waiting for activation.
     */
    void stop();
}
