package com.example.muhammadsalah.recognizerservice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.muhammadsalah.recognizerservice.R;
import com.example.muhammadsalah.recognizerservice.commands.CommandSetter;
import com.example.muhammadsalah.recognizerservice.fileViewer.ListDisplay;
import com.example.muhammadsalah.recognizerservice.fileViewer.MyFileAdapter;
import com.example.muhammadsalah.recognizerservice.record.recorderService;
import com.example.muhammadsalah.recognizerservice.speechRecognition.ShowResultsSpeechActivationBroadcastReceiver;
import com.example.muhammadsalah.recognizerservice.speechRecognition.SpeechActivationService;
/*import com.example.muhammadsalah.recognizerservice.userInterface.RecorderActivity;*/
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.List;



/*import butterknife.BindView;
import butterknife.ButterKnife;*/

public class MainActivity extends AppCompatActivity {
    ShowResultsSpeechActivationBroadcastReceiver broadcastReceiver;
    Toolbar toolbar;
    FrameLayout root;
    View contentHamburger;
    Button activation;
    private static final long RIPPLE_DURATION = 250;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        new CommandSetter().createCommandFile();
        activation = findViewById(R.id.btn_activation);
        activation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[] {"Voice", "Clapping", "Shaking"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Activation Method");
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case 0 :
                                chooseActivationMethod(R.string.speech_activation_speak);
                                break;
                            case 1 :
                                chooseActivationMethod(R.string.speech_activation_clap);
                                break;
                            case 2
                                    :
                                chooseActivationMethod(R.string.speech_activation_movement);
                                break;
                            default: // never happens
                                break;
                        }

                    }
                });
                builder.show();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        contentHamburger = findViewById(R.id.content_hamburger);
        root = findViewById(R.id.root);
        activation = findViewById(R.id.btn_activation);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.menu, null);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        final LinearLayout recordings = (LinearLayout) findViewById(R.id.folders_group);
        recordings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                Intent intent = new Intent(Intent.ACTION_VIEW);
                String location = Environment.getExternalStorageDirectory().getPath()+"/recordings";
                Uri myidr = Uri.parse(location);
                intent.setDataAndType(myidr,"resource/folder");*/
                Intent intent = new Intent(MainActivity.this, ListDisplay.class);
                startActivity(intent);
            }
        });
        LinearLayout commands = (LinearLayout) findViewById(R.id.commands_group);
        commands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("commands","entered the commands intent ");
                Intent intent = new Intent(MainActivity.this, CommandSetter.class);
                startActivity(intent);
            }
        });
    }

    private void chooseActivationMethod(Integer activationId)
    {

        if(!getIntent().hasExtra("stopService")){
            try
            {
                Intent myServiceIntent = SpeechActivationService.makeStartServiceIntent(MainActivity.this, getString(activationId));
                startService(myServiceIntent);
                Log.d("k",getString(activationId));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }


/*
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
*/


/*
 >>>>>>>>>>>>> MAKE IT ANOTHER OPTION BELOW FOLDERS
new CommandSetter().createCommandFile();
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getIntent().hasExtra("stopService")){
                    try
                    {
                        Intent myServiceIntent = SpeechActivationService.makeStartServiceIntent(MainActivity.this, getString(R.string.speech_activation_speak));
                        startService(myServiceIntent);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });
*/
/* >> BUTTON 2
findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CommandSetter.class);
                startActivity(intent);
            }
        });*/


/*
        ButterKnife.bind(this);
*/




    /*----------------------------------------------------------------------*//*


        //registerMyReceiver();

    }

    *//*private void registerMyReceiver() {

        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            registerReceiver(broadcastReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }*/
/*    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }*/
    /*-====================================================================================================================================*/
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                getPermissions();
            }
            else if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                getPermissions();
            }
            else if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                getPermissions();
            }
            else if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED)){
                getPermissions();
            }
            else if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)){
                getPermissions();
            }
        }
    }
    private void getPermissions(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Permissions Needed!");
        b.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        });
        b.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        });
        b.show();
    }
}

