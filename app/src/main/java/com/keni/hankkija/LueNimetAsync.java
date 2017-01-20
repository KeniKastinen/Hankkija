package com.keni.hankkija;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Keni on 2016-12-28.
 */

public class LueNimetAsync extends AsyncTask<String, String, String> {
    private Context context;
    private InputStream in;
    private BufferedReader reader;
    private String line = "";
    private Hankkijat hankkijat = Hankkijat.getInstance();
    private Hankkija hankkija;
    private ArrayList<Hankkija> hankkijatLista;
    private SuggestionsDatabase database;
    private DatabaseReference mDatabase;


    public LueNimetAsync(Context mContext) {
        context = mContext;
        hankkijat.addContext(context);
        database = hankkijat.getDatabase();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        hankkijatLista = new ArrayList<>();


    }

    @Override
    protected String doInBackground(String... strings) {
        int id = 0;
        int index;
        String hNimi = "";
        String loput = "";
        String oNimi = "";
        String alias = "";

        try {
            in = context.getResources().openRawResource(R.raw.hankkijat);
            reader = new BufferedReader(new InputStreamReader(in));
            line = reader.readLine();
            while (line != null) {
                index = line.indexOf('#');

                hNimi = line.substring(0,index-1).trim();
                loput = line.substring(index+2);
                index = loput.indexOf('#');
                if (index>=0){
                    oNimi = loput.substring(0, index-1).trim();
                    loput = loput.substring(index+1);
                }else oNimi = loput.trim();

                //System.out.println("-" + id + " "+hNimi+ "- -" +oNimi + "- -" + loput);

                hankkija = new Hankkija(hNimi,oNimi, id);




                hankkija.setAlias(hNimi);
                if (index>=0&&loput.indexOf(',')>=0) {
                    do {
                        index = loput.indexOf(',');
                        alias = loput.substring(0, index);
                        loput = loput.substring(index + 1);
                        hankkija.setAlias(alias);

                    } while (loput.indexOf(',') >= 0);
                    hankkija.setAlias(loput);
                }else if(index>=0&&loput.indexOf(',')<0){
                    hankkija.setAlias(loput);
                }

                mDatabase.child("hankkijat").child(id+"").setValue(hankkija);
                hankkijatLista.add(hankkija);
                line = reader.readLine();
                database.insertSuggestion(hNimi, oNimi, id+"");
                id++;
            }
            hankkijat.setHankkijatLista(hankkijatLista);


        } catch (Exception e) {
            System.out.println("virhe nimien hakemisessa");
            e.printStackTrace();
        } finally {

            try {
                in.close();
                hankkijat.luoKaikkiAliakset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //Toast.makeText(context, "Nimet haettu", Toast.LENGTH_SHORT).show();
        //new LueChatAsync(context).execute();




    }
}
