package com.example.muhammadsalah.recognizerservice.clapper.interfaces;

public interface AmplitudeClipListener
{
    /**
     * return true if recording should stop
     */
    boolean heard(int maxAmplitude);
}