package com.keni.hankkija;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Keni on 2017-01-03.
 */

public class LueParsitutKellotukset extends AsyncTask<String, String, String> {
    private transient Context context;
    private InputStream in;
    private BufferedReader reader;
    private String line = "";
    private Hankkijat hankkijat = Hankkijat.getInstance();
    private Hankkija suorittaja;
    private SingletonSerialized ss;
    private DatabaseReference mDatabase;



    public LueParsitutKellotukset(Context mContext) {
        context = mContext;
        ss = new SingletonSerialized(context);


    }

    @Override
    protected String doInBackground(String... strings) {
        int index;
        String loput = "";
        mDatabase = FirebaseDatabase.getInstance().getReference();

        try {
            in = context.getResources().openRawResource(R.raw.parsitut_kellotukset);
            reader = new BufferedReader(new InputStreamReader(in));
            line = reader.readLine();
            while (line != null) {
                index = line.indexOf('#');
                int hankkija = Integer.parseInt(line.substring(0,index).trim());
                loput = line.substring(index+1);
                index = loput.indexOf('#');
                int viestiId = Integer.parseInt(loput.substring(0, index).trim())-1;
                int tulos = Integer.parseInt(loput.substring(index+1).trim());
                //System.out.println(hankkija + " " +viesti+ " " +tulos);

                suorittaja = hankkijat.getHankkijaLista().get(hankkija);

                Kellotus tamaKellotus = new Kellotus(hankkija, tulos, viestiId);
                suorittaja.setKellotus(tamaKellotus);




                line = reader.readLine();

            }


        } catch (IOException e){
            System.out.println("virhe kelotusten hakemissa");
            e.printStackTrace();
        }finally {

            try {
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Hankkija hankkija : hankkijat.getHankkijaLista()) {
           mDatabase.child("hankkijat").child(hankkija.getId()+"").child("kellotukset").setValue(hankkija.getKellotukset());
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Toast.makeText(context, "kellotukset haettu", Toast.LENGTH_SHORT).show();



    }
}

