package com.keni.hankkija;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class Splashscreen extends Activity implements LueFirebase.LoadingTaskFinishedListener {
    private TextView teksti;
    private ImageView tikki1, tikki2, tikki3;
    private RelativeLayout rela;
    private int mShortAnimationDuration;
    private int screenWidth, screenHeight;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "hankkijaPref";




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen_new);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBar));
        }


        DisplayMetrics dm = new DisplayMetrics(); this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        rela = (RelativeLayout) findViewById(R.id.rela);
        teksti = new TextView(this);
        teksti.setText("HANKKIJA");
        teksti.setTextSize((int)(screenWidth/20));
        teksti.setTypeface(teksti.getTypeface(), Typeface.BOLD);
        teksti.setTextColor(Color.BLACK);
        teksti.setGravity(Gravity.CENTER);
        rela.addView(teksti, screenWidth, (int)(screenHeight*0.6));

        tikki1 = new ImageView(this);
        tikki2 = new ImageView(this);
        tikki3 = new ImageView(this);
        tikki1.setImageResource(R.drawable.tikki1);
        tikki2.setImageResource(R.drawable.tikki2);
        tikki3.setImageResource(R.drawable.tikki3);
        tikki1.setAlpha(0f);
        tikki2.setAlpha(0f);
        tikki3.setAlpha(0f);

               int scaledScreenWidth = (int) (screenWidth*0.4);

        tikki1.setY((int)(screenHeight*0.6));
        tikki1.setX((screenWidth-scaledScreenWidth)/2);
        tikki2.setY((int)(screenHeight*0.6));
        tikki2.setX((screenWidth-scaledScreenWidth)/2);
        tikki3.setY((int)(screenHeight*0.6));
        tikki3.setX((screenWidth-scaledScreenWidth)/2);
        rela.addView(tikki1, scaledScreenWidth, (int) (scaledScreenWidth*0.75));
        rela.addView(tikki2, scaledScreenWidth, (int) (scaledScreenWidth*0.75));
        rela.addView(tikki3, scaledScreenWidth, (int) (scaledScreenWidth*0.75));



        /**File file = new File("/data/user/0/com.keni.hankkija/files/hankkijaSerData/hankkija_lista.ser");

        if(file.exists()){
            Toast.makeText(getBaseContext(), "ser file löydetty", Toast.LENGTH_SHORT).show();
            new DeserializeAsync(getBaseContext(), this).execute();
        }
        else{
            Toast.makeText(getBaseContext(), "ser fileä ei löydetty", Toast.LENGTH_SHORT).show();
            new LueNimetAsync(getBaseContext(), this).execute();
            new LueChatAsync(getBaseContext()).execute();
            new LueParsitutKellotukset(getBaseContext()).execute();
        }*/

        //new LueNimetAsync(getBaseContext()).execute();
        //new LueChatAsync(getBaseContext()).execute();
        //new LueParsitutKellotukset(getBaseContext()).execute();





        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        Thread timer = new Thread() {
            public void run() {
                try {

                    //sleep(860);
                    fadeIn(tikki1);
                    sleep(300);
                    fadeIn(tikki2);
                    sleep(800);
                    fadeIn(tikki3);
                    sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //startApp();
                }
            }
        };
        timer.start();
        new LueFirebase(getBaseContext(), this).execute();
    }

    private void fadeIn(ImageView view) {
        view.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

    @Override
    public void onTaskFinished() {

        completeSplash();

    }
    private void completeSplash(){
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }
    private void startApp() {

        String käyttäjä = sharedpreferences.getString("nimi", null);
        System.out.println(käyttäjä);
        Intent i;
        if(käyttäjä != null){
            i = new Intent(Splashscreen.this, MainActivity.class);

        }else{
            i = new Intent(Splashscreen.this, UserSelection.class);
        }
        startActivity(i);

    }
}
