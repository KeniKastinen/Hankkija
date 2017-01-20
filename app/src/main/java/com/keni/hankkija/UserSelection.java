package com.keni.hankkija;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Created by Keni on 2016-12-27.
 */

public class UserSelection extends Activity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private RelativeLayout rela;
    private SuggestionsDatabase database;
    private SearchView searchView;
    private int screenWidth, screenHeight;
    private Hankkijat hankkijat = Hankkijat.getInstance();
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "hankkijaPref";
    private LinearLayout wrapper;
    private TextView tervehdys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        database = hankkijat.getDatabase();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.userselection);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBar));
        }
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        System.out.println("screen dimensions: " + screenHeight + " x " + screenWidth);

        rela = (RelativeLayout) findViewById(R.id.rela);
        ImageView tikit = new ImageView(this);


        tikit.setImageResource(R.drawable.tikit);
        int scaledScreenHeight = (int) (screenHeight * 0.4);
        int scaledScreenWidth = (int) (screenWidth * 0.4);
        System.out.println("scaled screen dimensions: " + scaledScreenHeight + " x " + scaledScreenWidth);
        tikit.setY((int) (screenHeight * 0.6));
        tikit.setX((screenWidth - scaledScreenWidth) / 2);
        rela.addView(tikit, scaledScreenWidth, (int) (scaledScreenWidth * 0.75));


        LayoutInflater inflater = LayoutInflater.from(this);
        wrapper = (LinearLayout) inflater.inflate(R.layout.annanimi, null, false);
        wrapper.setY((int) (screenHeight * 0.3));
        wrapper.setX((int) (screenWidth * 0.2));
        wrapper.setAlpha(0f);
        rela.addView(wrapper, (int) (screenWidth * 0.6), 300);

        tervehdys = new TextView(this);
        tervehdys.setAlpha(0f);
        tervehdys.setTextSize((int)(screenWidth/30));
        tervehdys.setGravity(Gravity.CENTER);
        tervehdys.setTextColor(Color.BLACK);
        tervehdys.setY((int) (screenHeight * 0.3));
        tervehdys.setX((int) (screenWidth * 0.2));
        rela.addView(tervehdys, (int) (screenWidth * 0.6), 300);

        searchView = (SearchView) findViewById(R.id.annaNimi);
        searchView.setIconified(false);

        tikit.animate()
                .setDuration(500)
                .x((screenWidth - scaledScreenWidth) / 2)
                .y((int) (screenHeight * 0.1))
                .setInterpolator(new FastOutSlowInInterpolator()).
                withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        wrapper.animate()
                                .alpha(1f)
                                .setDuration(500);
                    }
                });
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);


    }

    public void jatka() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(wrapper.getWindowToken(), 0);
        tervehdys.setText("Tervetuloa " + sharedpreferences.getString("nimi", null) + "!");



        Thread timer = new Thread() {
            public void run() {
                try {
                    wrapper.animate()
                            .alpha(0f)
                            .setDuration(500);
                    sleep(200);
                    tervehdys.animate()
                            .alpha(1f).
                            setDuration(500);
                    sleep(2000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    Intent i = new Intent(UserSelection.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };
        timer.start();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        //long result = database.insertSuggestion(s);
        //return result != -1;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Cursor cursor = database.getSuggestions(s);
        if (cursor.getCount() != 0) {
            String[] columns = new String[]{SuggestionsDatabase.HANKKIJA_NIMI};
            int[] columnTextId = new int[]{android.R.id.text1};

            SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(getBaseContext(),
                    android.R.layout.simple_list_item_1, cursor,
                    columns, columnTextId
                    , 0);

            searchView.setSuggestionsAdapter(simple);
            return true;
        } else {

            return false;
        }
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int i) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(i);
        int indexColumnSuggestion = cursor.getColumnIndex(SuggestionsDatabase.HANKKIJA_NIMI);
        int nimiId = cursor.getColumnIndex(SuggestionsDatabase.HANKKIJA_ID);
        editor.putString("nimi", cursor.getString(indexColumnSuggestion));
        editor.putString("id", cursor.getString(nimiId));
        System.out.println("TÄMÄ: "+cursor.getString(nimiId));
        editor.commit();
        jatka();


        return true;
    }
}
