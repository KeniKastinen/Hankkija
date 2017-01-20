package com.keni.hankkija;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Keni on 2016-12-28.
 */

public class Hankkija implements Serializable {

    private String hankkijaNimi;
    private String oikeaNimi;
    private int id;
    private ArrayList<String> aliakset;
    private ArrayList<Kellotus> kellotukset;

    public Hankkija(){

    }

    public Hankkija(String nimi1, String nimi2, int id) {
        this.hankkijaNimi = nimi1;
        this.oikeaNimi = nimi2;
        this.id = id;
        this.aliakset = new ArrayList<>();
        this.kellotukset = new ArrayList<>();
    }

    public void setHankkijaNimi(String hankkijaNimi) {
        this.hankkijaNimi = hankkijaNimi;
    }
    public void setOikeaNimi(String oikeaNimi) {
        this.oikeaNimi = oikeaNimi;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setAliakset(ArrayList<String> aliakset) {
        this.aliakset = aliakset;
    }
    public void setKellotukset(ArrayList<Kellotus> kellotukset) {
        this.kellotukset = kellotukset;
    }

    public void setAlias(String alias) {
        aliakset.add(alias);
    }
    public void setKellotus(Kellotus kellotus){
        if(kellotukset != null) {
            kellotukset.add(kellotus);
        }else {
            this.kellotukset = new ArrayList<>();
            kellotukset.add(kellotus);

        }

    }

    public String getHankkijaNimi() {
        return this.hankkijaNimi;
    }

    public String getOikeaNimi() {
        return this.oikeaNimi;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<String> getAliakset() {
        return this.aliakset;
    }

    public ArrayList<Kellotus> getKellotukset() {
        return kellotukset;
    }
    public int getKellotustenMaara(){
        int palautus = 0;
        if(this.getKellotukset()!=null)palautus = this.getKellotukset().size();
        return palautus;
    }

}
