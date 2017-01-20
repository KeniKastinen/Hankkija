package com.keni.hankkija;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Keni on 2016-12-28.
 */

public class LueChatAsync extends AsyncTask<String, String, String> {
    private Context context;
    private InputStream in;
    private BufferedReader reader;
    private String line = "";
    private Hankkijat hankkijat = Hankkijat.getInstance();
    private Hankkija hankkija;
    private ArrayList<Viesti> feedi;
    private DatabaseReference mDatabase;




    public LueChatAsync(Context mContext) {
        context = mContext;

    }

    @Override
    protected String doInBackground(String... strings) {
        feedi = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy, hh:mm");
        int index = 0;

        try {
            in = context.getResources().openRawResource(R.raw.hankkijachat);
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {

                if(line.length()>19 && line.substring(line.indexOf('-')+2).indexOf(':')>=0 &&line.substring(15, 19).indexOf('-')>=0 && Character.isDigit(line.charAt(0))) {

                    Date aika = sdf.parse(line.substring(0, line.indexOf('-')-1));
                    String loput = line.substring(line.indexOf('-')+2);
                    //System.out.println(index + "  " +line.substring(0, line.indexOf('-')-1));
                    Viesti viesti = new Viesti(aika, loput.substring(0, loput.indexOf(':')), loput.substring(loput.indexOf(':')+2)+"\n", index);
                    mDatabase.child("viestit").child(index+"").child("aika").setValue(aika.getTime());
                    mDatabase.child("viestit").child(index+"").child("lahetteja").setValue(loput.substring(0, loput.indexOf(':')));
                    mDatabase.child("viestit").child(index+"").child("sisalto").setValue(loput.substring(loput.indexOf(':')+2)+"\n");
                    mDatabase.child("viestit").child(index+"").child("id").setValue(index);
                    feedi.add(viesti);
                    index++;
                } else if(line.length()>19 && line.substring(line.indexOf('-')+2).indexOf(':')<0 &&line.substring(15, 19).indexOf('-')>=0 && Character.isDigit(line.charAt(0))) {
                    //System.out.println(line);
                    index++;
                }else{
                    feedi.get(feedi.size() - 1).lisaaViestiin(line + "\n");
                    mDatabase.child("viestit").child(feedi.get(feedi.size() - 1).getId()+"").child("sisalto").setValue(feedi.get(feedi.size() - 1).getSisalto());
                    index++;
                    }
            }


        } catch (Exception e) {
            System.out.println("virhe chatin hakemisessa");
            e.printStackTrace();
        } finally {

            try {
                in.close();
                hankkijat.addViestiHistoria(feedi);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //parsiViestit();
        return null;
    }



    public int etsi(String sisältö){
        ArrayList<Integer> kaikkiAliaksetId = hankkijat.getKaikkiAliaksetId();
        ArrayList<String> kaikkiAliaksetString = hankkijat.getKaikkiAliaksetString();
        int palautus = 0;
        int i = 0;
        for (String alias : kaikkiAliaksetString) {
            if(alias.toLowerCase().equals(sisältö.toLowerCase())){
                palautus = kaikkiAliaksetId.get(i);
            }
            i++;
        }
        return palautus;
    }
    public void parsiViestit(){

        String filePath = context.getFilesDir().getPath().toString() + "/parsitut.txt";
        File file = new File(filePath);
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.print("");

            for (Viesti viesti : feedi) {

                String str = viesti.getSisalto();
                String[] sp = str.split("\\s");
                for (int i = 0; i < sp.length; i++) {
                    if (sp[i].matches("\\d") || sp[i].matches("\\d{2}") || sp[i].matches("\\d\\S") || sp[i].matches("\\d{2}\\S")) {

                        if(i>0) {
                            int hankkijaNro1 = etsi(sp[i - 1]);
                            int hankkijaNro2 = 0;
                            if (hankkijaNro1>0){
                                String kellotus = sp[i].replaceAll("[^0-9]","");
                                pw.println(hankkijaNro1-1+"#"+ viesti.getId() +"#" + kellotus);
                                System.out.println(hankkijaNro1-1+"#"+ viesti.getId() +"#" + kellotus);
                            }

                            if(i>1){
                                hankkijaNro2 = etsi(sp[i - 2]+" "+sp[i - 1]);
                                if (hankkijaNro2>0) {
                                    String kellotus = sp[i].replaceAll("[^0-9]","");
                                    pw.println(hankkijaNro2-1+"#"+ viesti.getId() +"#" + kellotus);
                                    System.out.println(hankkijaNro2-1+"#"+ viesti.getId() +"#" + kellotus);
                                }
                            }
                            if(hankkijaNro1==0&&hankkijaNro2==0&&sp[i - 1].indexOf('"')<0){
                                //System.out.println("-"+sp[i - 1] + "-  -" + sp[i]);
                            }
                        }
                    }
                }
            }
            pw.flush();
            pw.close();
            f.close();


        }catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("jtn", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Toast.makeText(context, "Chat haettu", Toast.LENGTH_SHORT).show();
        //new LueParsitutKellotukset(context).execute();


    }
}
