package com.keni.hankkija;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Keni on 2016-12-28.
 */

public class Hankkijat implements Serializable {

    private ArrayList<Hankkija> hankkijatLista;
    private ArrayList<Viesti> viestiHistoria;
    private ArrayList<Integer> kaikkiAliaksetId;
    private ArrayList<String> kaikkiAliaksetString;
    private transient SuggestionsDatabase database;
    private transient Context context;

    private static Hankkijat ourInstance = new Hankkijat();

    public static final Hankkijat getInstance() {
        return ourInstance;
    }

    private Hankkijat() {
        this.hankkijatLista = new ArrayList<>();
        this.viestiHistoria = new ArrayList<>();
    }

    public void addHankkija(Hankkija person) {
        if (!hankkijatLista.contains(person)) {
            hankkijatLista.add(person);
        }
    }

    public void addContext(Context context) {
        this.context = context;
        this.database = new SuggestionsDatabase(this.context);
    }

    public void addViestiHistoria(ArrayList<Viesti> viestihistoria) {
        this.viestiHistoria = viestihistoria;
    }

    public ArrayList<Hankkija> getHankkijaLista() {
        return hankkijatLista;
    }

    public ArrayList<Viesti> getViestiHistoria() {
        return viestiHistoria;
    }

    public SuggestionsDatabase getDatabase() {
        return database;
    }

    public ArrayList<Integer> getKaikkiAliaksetId() {
        return kaikkiAliaksetId;
    }

    public ArrayList<String> getKaikkiAliaksetString() {
        return kaikkiAliaksetString;
    }

    public void setHankkijatLista(ArrayList<Hankkija> hankkijatLista) {
        this.hankkijatLista = hankkijatLista;
    }

    public void setViestiHistoria(ArrayList<Viesti> viestiHistoria) {
        this.viestiHistoria = viestiHistoria;
    }

    public String getNimiById(int id) {
        return hankkijatLista.get(id).getHankkijaNimi();
    }

    public Viesti getViestiById(int id) {
        Viesti palautusViesti = new Viesti();
        for (Viesti viesti : viestiHistoria) {
            if (viesti.getId() == id) {
                palautusViesti = viesti;
            }
        }
        return palautusViesti;
    }

    public void luoKaikkiAliakset() {
        this.kaikkiAliaksetId = new ArrayList<>();
        this.kaikkiAliaksetString = new ArrayList<>();
        ArrayList<String> aliakset;
        int i = 0;
        for (Hankkija hankkija : hankkijatLista) {
            aliakset = hankkija.getAliakset();
            for (String alias : aliakset) {
                kaikkiAliaksetId.add(hankkija.getId());
                kaikkiAliaksetString.add(alias);
                //kaikkiAliakset.put(hankkija.getId(), alias);
                i++;
                //System.out.println(i+ " " + hankkija.getHankkijaNimi() + " " + alias);
            }
        }
    }

    public double getKeskiarvo(int id) {
        int yhteensä = 0;
        int kpl = 0;

        double ka = 0;
        if (hankkijatLista.get(id).getKellotustenMaara() != 0)
            for (Kellotus kellotus : hankkijatLista.get(id).getKellotukset()) {
                if (kellotus.getTulos() < 99) {
                    yhteensä += kellotus.getTulos();
                    kpl++;
                }

            }
            ka = (double) yhteensä / (double) kpl;
        return Math.round(ka * 100d) / 100d;
    }

    public int getYleisinTulos(int id) {
        int popular = 0;
        if (hankkijatLista.get(id).getKellotustenMaara() != 0) {


            int[] a = new int[hankkijatLista.get(id).getKellotustenMaara()];
            int i = 0;
            for (Kellotus kellotus : hankkijatLista.get(id).getKellotukset()) {
                a[i] = kellotus.getTulos();
                i++;
            }
            int count = 1, tempCount;
            popular = a[0];
            int temp = 0;
            for (i = 0; i < (a.length - 1); i++) {
                temp = a[i];
                tempCount = 0;
                for (int j = 1; j < a.length; j++) {
                    if (temp == a[j])
                        tempCount++;
                }
                if (tempCount > count) {
                    popular = temp;
                    count = tempCount;
                }
            }
        }
        return popular;
    }


}
