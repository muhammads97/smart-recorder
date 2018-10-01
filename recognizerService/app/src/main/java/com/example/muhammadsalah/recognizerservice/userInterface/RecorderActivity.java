/*
package com.example.muhammadsalah.recognizerservice.userInterface;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
*/
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;*//*

import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.muhammadsalah.recognizerservice.MainActivity;
import com.example.muhammadsalah.recognizerservice.R;

import ak.sh.ay.musicwave.MusicWave;

public class RecorderActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 9976;
    FloatingActionButton fab;
    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private MusicWave musicWave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
*/
/*
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
*//*

        } else {
            initialise();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initialise() {
        musicWave = (MusicWave) findViewById(R.id.musicWave);
        mMediaPlayer = MediaPlayer.create(this, R.raw.music);
        prepareVisualizer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
                fab.setImageResource(android.R.drawable.ic_media_play);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status;
                if (mMediaPlayer.isPlaying()) {
                    status = "Sound Paused";
                    mMediaPlayer.pause();
                    fab.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    status = "Sound Started";
                    mVisualizer.setEnabled(true);
                    mMediaPlayer.start();
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                }
                Snackbar.make(view, status, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    private void prepareVisualizer() {
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        musicWave.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_color_solid) {
            Log.e("action_color_solid", "" + musicWave.getConfig().getColorGradient());
            musicWave.getConfig().setColorGradient(false);
            return true;
        }
        if (id == R.id.action_color_gradient) {
            Log.e("action_color_gradient", "" + musicWave.getConfig().getColorGradient());
            musicWave.getConfig().setColorGradient(true);
            return true;
        }
        if (id == R.id.action_sound_usage) {
            Snackbar.make(fab, "Sound used “music” by Jay Man www.ourmusicbox.com", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialise();
                } else {
                    Toast.makeText(RecorderActivity.this, "Allow Permission from settings", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
*/
