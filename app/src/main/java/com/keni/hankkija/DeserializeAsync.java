package com.keni.hankkija;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by Keni on 2017-01-03.
 */

public class DeserializeAsync extends AsyncTask<String, String, String> {
    private Context context;
    private Hankkijat hankkijat;

    public interface LoadingTaskFinishedListener {
        void onTaskFinished(); // If you want to pass something back to the listener add a param to this method
    }
    //private final ProgressBar progressBar;


    public DeserializeAsync(Context mContext) {
        context = mContext;
        hankkijat = Hankkijat.getInstance();


    }
    @Override
    protected String doInBackground(String... strings) {


        try {
            Deserialize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);




    }
    public void Deserialize()throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Hankkija> hankkijatLista;
        ArrayList<Viesti> viestiHistoria;
        File path = new File(context.getFilesDir(), "hankkijaSerData");
        File f = new File(path,"hankkija_lista.ser");

        ObjectInput in = new ObjectInputStream(new FileInputStream(f));
        hankkijatLista = (ArrayList<Hankkija>) in.readObject();
        hankkijat.setHankkijatLista(hankkijatLista);
        //System.out.println(hankkijatLista.get(1).getHankkijaNimi());
        System.out.println("Deserializing ready1");
        File f2 = new File(path,"viesti_historia.ser");

        ObjectInput in2 = new ObjectInputStream(new FileInputStream(f));
        viestiHistoria = (ArrayList<Viesti>) in2.readObject();
        hankkijat.setViestiHistoria(viestiHistoria);
        //System.out.println(viestiHistoria.get(1).getSisalto());
        in.close();
        in2.close();
        hankkijat.luoKaikkiAliakset();
        //Toast.makeText(context, "serialisation done", Toast.LENGTH_SHORT).show();
        System.out.println("Deserializing ready2");


    }

}
