package com.example.muhammadsalah.recognizerservice.clapper;

public interface AmplitudeClipListener
{
    /**
     * return true if recording should stop
     */
    public boolean heard(int maxAmplitude);
}