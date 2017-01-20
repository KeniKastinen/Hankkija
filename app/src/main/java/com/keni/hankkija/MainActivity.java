package com.keni.hankkija;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView sisältö;
    private Hankkijat hankkijat;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "hankkijaPref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        hankkijat = Hankkijat.getInstance();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBar));
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        /**String hankkijaId = sharedpreferences.getString("id", null);
         sisältö = (TextView) findViewById(R.id.teksti);
         ArrayList <Hankkija> uusiHankkijaLista = hankkijat.getHankkijaLista();
         Collections.sort(uusiHankkijaLista,new Comparator<Hankkija>() {

        @Override public int compare(Hankkija h2, Hankkija h1) {
        //return Integer.compare(h1.getKellotukset().size(),h2.getKellotukset().size()); //määrä
        return Double.compare(h2.getKeskiarvo(), h1.getKeskiarvo());

        }
        });
         for (Hankkija hankkija : uusiHankkijaLista) {
         if(hankkija.getKellotukset().size()>0){
         sisältö.append(hankkija.getHankkijaNimi()+" ");
         sisältö.append(hankkija.getKeskiarvo()+"\n");
         }

         //sisältö.append(hankkijat.getHankkijaLista().get(Integer.parseInt(hankkijaId) - 1).getHankkijaNimi() + " ");
         //sisältö.append(hankkijat.getHankkijaLista().get(Integer.parseInt(hankkijaId) - 1).getKellotukset().size() + "");
         }**/


    }
}
