package com.keni.hankkija;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Keni on 2017-01-03.
 */

public class Kellotus implements Serializable {
    private int suorittaja;
    private int tulos;
    private int viesti;
    private Boolean ysiysi;

    public Kellotus(){

    }

    public Kellotus(int hankkijaId, int tulos, int viesti){
        this.suorittaja = hankkijaId;
        this.tulos = tulos;
        this.viesti = viesti;
        if (tulos==99)this.ysiysi = true;
        else this.ysiysi = false;

    }

    public void setSuorittaja(int suorittaja) {
        this.suorittaja = suorittaja;
    }
    public void setTulos(int tulos) {
        this.tulos = tulos;
    }
    public void setViestiId(int viestiId) {
        this.viesti = viestiId;
    }
    public void setYsiysi(Boolean ysiysi) {
        this.ysiysi = ysiysi;
    }

    public int getSuorittaja() {
        return suorittaja;
    }
    public int getTulos() {
        return tulos;
    }
    public int getViesti() {
        return viesti;
    }
    public Boolean getYsiysi() {
        return ysiysi;
    }}
