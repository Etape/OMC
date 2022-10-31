package com.davinci.etone.omc;

import java.util.ArrayList;

public class Commune {
    String com_name;
    String com_dec;
    int activite;
    ArrayList<Bv> updatedBvlist;

    public int getActivite() {
        return activite;
    }

    public void setActivite(int activite) {
        this.activite = activite;
    }

    public Commune(String com_name, String com_dec) {
        this.com_name = com_name;
        this.com_dec = com_dec;
    }
    public Commune(String com_name) {
        this.com_name = com_name;
    }
    public Commune() {
        this.com_name = "none";
        this.com_dec = "Aucun";
        this.activite=0;
        this.updatedBvlist = new ArrayList<>();
    }

    public ArrayList<Bv> getUpdatedBvlist() {
        return updatedBvlist;
    }

    public void setUpdatedBvlist(ArrayList<Bv> updatedBvlist) {
        this.updatedBvlist = updatedBvlist;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getCom_dec() {
        return com_dec;
    }

    public void setCom_dec(String com_dec) {
        this.com_dec = com_dec;
    }
}
