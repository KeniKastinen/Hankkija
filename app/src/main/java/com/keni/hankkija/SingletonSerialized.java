package com.keni.hankkija;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static com.keni.hankkija.UserSelection.MyPREFERENCES;

/**
 * Created by Keni on 2017-01-03.
 */

public class SingletonSerialized extends Activity {
    private Hankkijat hankkijat;
    private Context context;
    private SharedPreferences sharedpreferences;

    private SharedPreferences.Editor editor;

    public SingletonSerialized(Context mContext)  {
        context = mContext;
        hankkijat = Hankkijat.getInstance();
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    public void Serialize()throws FileNotFoundException, IOException, ClassNotFoundException{
        File path = new File(context.getFilesDir(), "hankkijaSerData");
        path.mkdir();
        File f = new File(path,"hankkija_lista.ser");
        System.out.println(f);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
        out.writeObject(hankkijat.getHankkijaLista());
        out.close();
        //String filePath2 = context.getFilesDir().getPath().toString() + "/viesti_historia.ser";
        File f2 = new File(path,"viesti_historia.ser");
        ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(f2));
        out2.writeObject(hankkijat.getViestiHistoria());
        out.close();
        out2.close();

        editor.putString("serialized", "true");
        editor.commit();
    }
    /**public void Deserialize()throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Hankkija> hankkijatLista;
        ArrayList<Viesti> viestiHistoria;

        String filePath = context.getFilesDir().getPath().toString() + "/hankkija_lista.ser";
        ObjectInput in = new ObjectInputStream(new FileInputStream(filePath));
        hankkijatLista = (ArrayList<Hankkija>) in.readObject();
        hankkijat.setHankkijatLista(hankkijatLista);
        //System.out.println(hankkijatLista.get(1).getHankkijaNimi());

        String filePath2 = context.getFilesDir().getPath().toString() + "/viesti_historia.ser";
        ObjectInput in2 = new ObjectInputStream(new FileInputStream(filePath2));
        viestiHistoria = (ArrayList<Viesti>) in2.readObject();
        hankkijat.setViestiHistoria(viestiHistoria);
        //System.out.println(viestiHistoria.get(1).getSisalto());
        in.close();
        in2.close();
        hankkijat.luoKaikkiAliakset();
        Toast.makeText(context, "serialisation done", Toast.LENGTH_SHORT).show();


    }**/

}



