package com.example.muhammadsalah.recognizerservice.movement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.movement.interfaces.MovementDetectionListener;

public class MovementDetector
{
    private static final String TAG = "MovementDetector";

    private static final int RATE = SensorManager.SENSOR_DELAY_NORMAL;

    private SensorManager sensorManager;
    private AccelerationEventListener sensorListener;
    private boolean readingAccelerationData;
    private boolean useHighPassFilter;
    private boolean useAccelerometer;

    public  MovementDetector(Context context) {
        this(context, true);
    }

    /**
     * @param useAccelerometer otherwise use linear acceleration
     */
    public  MovementDetector(Context context, boolean useAccelerometer) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.useAccelerometer = useAccelerometer;
    }

    /**
     * @param resultCallback Movement Activator
     */
    public  void startReadingAccelerationData(MovementDetectionListener resultCallback) {
        //stop anything that is currently happening
        stopReadingAccelerationData();

        if (!readingAccelerationData) {
            // Data files are stored on the external cache directory so they can
            // be pulled off of the device by the user
            if (useAccelerometer) {
                sensorListener = new AccelerationEventListener(useHighPassFilter, resultCallback);
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        RATE);
            } else {
                sensorListener = new AccelerationEventListener(useHighPassFilter, resultCallback);
                sensorManager.registerListener(sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                        RATE);
            }

            readingAccelerationData = true;

            Log.d(TAG, "Started reading acceleration data");
        }
    }

    public  void stopReadingAccelerationData() {
        if (readingAccelerationData && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
            readingAccelerationData = false;
            Log.d(TAG, "Stopped reading acceleration data");
        }
    }
}