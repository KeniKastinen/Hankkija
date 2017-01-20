package com.keni.hankkija;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Keni on 2017-01-01.
 */

public class Viesti implements Serializable {
    private Date date;
    private String lahetteja;
    private String sisalto;
    private int id;
    Boolean kellotus;

    public Viesti(){

    }

    public Viesti(Date date, String lahetteja, String sisalto, int id) {
        this.date = date;
        this.sisalto = sisalto;
        this.lahetteja = lahetteja;
        this.id = id;

    }

    public Date getDate() {
        return this.date;
    }
    public String getSisalto(){
        return this.sisalto;
    }
    public String getLahetteja(){
        return this.lahetteja;
    }
    public int getId() {
        return id;
    }
    public void lisaaViestiin(String lisaViesti){
        this.sisalto += lisaViesti;
    }

}
