package com.example.muhammadsalah.recognizerservice.clapper;

import android.util.Log;

import com.example.muhammadsalah.recognizerservice.clapper.interfaces.AmplitudeClipListener;

public class SingleClapDetector implements AmplitudeClipListener
{
    private static final String TAG = "SingleClapDetector";
    /**
     * required loudness to determine it is a clap
     */
    private int amplitudeThreshold;
    /**
     * requires a little of noise by the user to trigger, background noise may
     * trigger it
     */
    public static final int AMPLITUDE_DIFF_LOW = 10000;
    public static final int AMPLITUDE_DIFF_MED = 18000;
    /**
     * requires a lot of noise by the user to trigger. background noise isn't
     * likely to be this loud
     */
    public static final int AMPLITUDE_DIFF_HIGH = 25000;
    private static final int DEFAULT_AMPLITUDE_DIFF = AMPLITUDE_DIFF_MED;
    public SingleClapDetector() {
        this(DEFAULT_AMPLITUDE_DIFF);
    }
    public SingleClapDetector(int amplitudeThreshold) {
        this.amplitudeThreshold = amplitudeThreshold;
    }
    @Override
    public boolean heard(int maxAmplitude)
    {
        boolean clapDetected = false;
        if (maxAmplitude >= amplitudeThreshold) {
            //Log.d(TAG, "heard a clap");
            clapDetected = true;
        }
        return clapDetected;
    }
}