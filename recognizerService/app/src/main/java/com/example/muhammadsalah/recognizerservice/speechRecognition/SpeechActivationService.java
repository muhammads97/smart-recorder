package com.example.muhammadsalah.recognizerservice.speechRecognition;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.muhammadsalah.recognizerservice.R;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivationListener;
import com.example.muhammadsalah.recognizerservice.speechRecognition.interfaces.SpeechActivator;

public class SpeechActivationService extends Service implements SpeechActivationListener {

    private static final String TAG = "SpeechActivationService";
    public static final String ACTIVATION_TYPE_INTENT_KEY = "ACTIVATION_TYPE_INTENT_KEY";
    public static final String ACTIVATION_RESULT_INTENT_KEY = "ACTIVATION_RESULT_INTENT_KEY";
    public static final String ACTIVATION_RESULT_BROADCAST_NAME = "Action";
    public static final String ACTIVATION_STOP_INTENT_KEY = "ACTIVATION_STOP_INTENT_KEY";

    public static final int NOTIFICATION_ID = 10298;
    /**
     * Activator flag
     */
    private boolean isStarted;
    private SpeechActivator activator;

    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = false;
    }

    /**
     *
     * @param context app context
     * @param activationType Activation type (word, clap or movement
     * @return intent service
     */
    public static Intent makeStartServiceIntent(Context context, String activationType) {
        Intent i = new Intent(context, SpeechActivationService.class);
        i.putExtra(ACTIVATION_TYPE_INTENT_KEY, activationType);
        return i;
    }

    /**
     * @param context app context
     * @return Stop service intent
     */
    public static Intent makeServiceStopIntent(Context context) {
        Intent i = new Intent(context, SpeechActivationService.class);
        i.putExtra(ACTIVATION_STOP_INTENT_KEY, true);
        return i;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra(ACTIVATION_STOP_INTENT_KEY)) {
                Log.d(TAG, "stop service intent");
                activated(false);
            } else {
                if (isStarted)
                {
                    // the activator is currently started
                    // if the intent is requesting a new activator
                    // stop the current activator and start
                    // the new one
                    if (isDifferentType(intent))
                    {
                        Log.d(TAG, "is differnet type");
                        stopActivator();
                        startDetecting(intent);
                    }
                    else
                    {
                        Log.d(TAG, "already started this type");
                    }
                }
                else
                {
                    // activator not started, start it
                    startDetecting(intent);
                }
            }
        }
        // restart in case the Service gets canceled
        return START_REDELIVER_INTENT;
    }

    /**
     * @param intent intent that started the Service to get the requested Activator
     */
    private void startDetecting(Intent intent) {
        activator = getRequestedActivator(intent);
        Log.d(TAG, "started: " + activator.getClass().getSimpleName());
        isStarted = true;
        activator.detectActivation();
        startForeground(NOTIFICATION_ID, getNotification());
    }

    /**
     * @param intent intent that started the application to get Activation type
     * @return SpeechActivator Object from the SpeechActivatorFactory
     */
    private SpeechActivator getRequestedActivator(Intent intent) {
        String type = intent.getStringExtra(ACTIVATION_TYPE_INTENT_KEY);
        // create based on a type name
        SpeechActivator speechActivator =
                SpeechActivatorFactory.createSpeechActivator(this, this, type);
        return speechActivator;
    }

    /**
     * @param intent intent that started the service
     * @return boolean to indicate the activator changed or not
     */
    private boolean isDifferentType(Intent intent) {
        boolean different = false;
        if (activator == null) {
            return true;
        } else {
            SpeechActivator possibleOther = getRequestedActivator(intent);
            different = !(possibleOther.getClass().getName().
                    equals(activator.getClass().getName()));
        }
        return different;
    }


    @Override
    public void activated(boolean success) {
        // make sure the activator is stopped before doing anything else
        stopActivator();
        // broadcast result
        Intent intent = new Intent();
        intent.setAction(ACTIVATION_RESULT_BROADCAST_NAME);
        intent.putExtra(ACTIVATION_RESULT_INTENT_KEY, success);
        sendBroadcast(intent);
        // always stop after receive an activation
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "On destroy");
        super.onDestroy();
        stopActivator();
        stopForeground(true);
    }

    /**
     * calls Activator.Stop() method to stop the activator
     */
    private void stopActivator() {
        if (activator != null) {
            Log.d(TAG, "stopped: " + activator.getClass().getSimpleName());
            activator.stop();
            isStarted = false;

        }
    }

    /**
     * @return a Notification Object to show
     */
    private Notification getNotification() {
        // determine label based on the class
        String name = SpeechActivatorFactory.getLabel(this, activator);
        String message =
                getString(R.string.speech_activation_notification_listening)
                        + " " + name;
        String title = getString(R.string.speech_activation_notification_title);
        PendingIntent pi =
                PendingIntent.getService(this, 0, makeServiceStopIntent(this),
                        0);
        Notification notification;
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.icon)
                .setWhen(System.currentTimeMillis()).setTicker(message)
                .setContentTitle(title).setContentText(message)
                .setContentIntent(pi);
        notification = builder.getNotification();
        return notification;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
