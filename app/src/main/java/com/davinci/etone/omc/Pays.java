package com.davinci.etone.omc;

public class Pays {
 String code;
 String alpha2;
 String alpha3;
 String nom_fr;
 String nom_eng;

    public Pays(String code, String alpha2, String alpha3, String nom_fr, String nom_eng) {
        this.code = code;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.nom_fr = nom_fr;
        this.nom_eng = nom_eng;
    }

    public Pays() {
        this.code = "none";
        this.alpha2 = "none";
        this.alpha3 = "none";
        this.nom_fr = "none";
        this.nom_eng = "none";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }

    public String getNom_fr() {
        return nom_fr;
    }

    public void setNom_fr(String nom_fr) {
        this.nom_fr = nom_fr;
    }

    public String getNom_eng() {
        return nom_eng;
    }

    public void setNom_eng(String nom_eng) {
        this.nom_eng = nom_eng;
    }
}
