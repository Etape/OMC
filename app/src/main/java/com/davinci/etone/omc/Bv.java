package com.davinci.etone.omc;

public class Bv {
    String bv_name;
    String bv_region;
    String bv_pays="Cameroun";
    String bv_dep;
    String bv_commune;
    int bv_attente;
    int bv_recolte;
    String bv_vol1_name;
    String bv_vol1_tel;
    String bv_vol2_name;
    String bv_vol2_tel;
    String bv_vol3_name;
    String bv_vol3_tel;
    String bv_vol4_name;
    String bv_vol4_tel;

    public Bv(String bv_name, String bv_region, String bv_dep, String bv_commune, int
            bv_attente,
              int bv_recolte, String bv_vol1_name, String bv_vol1_tel, String bv_vol2_name,
              String bv_vol2_tel, String bv_vol3_name, String bv_vol3_tel, String bv_vol4_name,
              String bv_vol4_tel) {
        this.bv_name = bv_name;
        this.bv_region = bv_region;
        this.bv_dep = bv_dep;
        this.bv_commune = bv_commune;
        this.bv_attente = bv_attente;
        this.bv_recolte = bv_recolte;
        this.bv_vol1_name = bv_vol1_name;
        this.bv_vol1_tel = bv_vol1_tel;
        this.bv_vol2_name = bv_vol2_name;
        this.bv_vol2_tel = bv_vol2_tel;
        this.bv_vol3_name = bv_vol3_name;
        this.bv_vol3_tel = bv_vol3_tel;
        this.bv_vol4_name = bv_vol4_name;
        this.bv_vol4_tel = bv_vol4_tel;
    }

    public Bv() {
        this.bv_name = "none";
        this.bv_region = "none";
        this.bv_dep = "none";
        this.bv_commune = "none";
        this.bv_attente = 0;
        this.bv_recolte = 0;
        this.bv_vol1_name = "none";
        this.bv_vol1_tel = "none";
        this.bv_vol2_name = "none";
        this.bv_vol2_tel = "none";
        this.bv_vol3_name = "none";
        this.bv_vol3_tel = "none";
        this.bv_vol4_name = "none";
        this.bv_vol4_tel = "none";
        this.bv_pays = "Cameroun";
    }

    public String getBv_pays() {
        return bv_pays;
    }

    public void setBv_pays(String bv_pays) {
        this.bv_pays = bv_pays;
    }

    public String getBv_name() {
        return bv_name;
    }

    public void setBv_name(String bv_name) {
        this.bv_name = bv_name;
    }

    public String getBv_region() {
        return bv_region;
    }

    public void setBv_region(String bv_region) {
        this.bv_region = bv_region;
    }

    public String getBv_dep() {
        return bv_dep;
    }

    public void setBv_dep(String bv_dep) {
        this.bv_dep = bv_dep;
    }

    public String getBv_commune() {
        return bv_commune;
    }

    public void setBv_commune(String bv_commune) {
        this.bv_commune = bv_commune;
    }

    public int getBv_attente() {
        return bv_attente;
    }

    public void setBv_attente(int bv_attente) {
        this.bv_attente = bv_attente;
    }

    public int getBv_recolte() {
        return bv_recolte;
    }

    public void setBv_recolte(int bv_recolte) {
        this.bv_recolte = bv_recolte;
    }

    public String getBv_vol1_name() {
        return bv_vol1_name;
    }

    public void setBv_vol1_name(String bv_vol1_name) {
        this.bv_vol1_name = bv_vol1_name;
    }

    public String getBv_vol1_tel() {
        return bv_vol1_tel;
    }

    public void setBv_vol1_tel(String bv_vol1_tel) {
        this.bv_vol1_tel = bv_vol1_tel;
    }

    public String getBv_vol2_name() {
        return bv_vol2_name;
    }

    public void setBv_vol2_name(String bv_vol2_name) {
        this.bv_vol2_name = bv_vol2_name;
    }

    public String getBv_vol2_tel() {
        return bv_vol2_tel;
    }

    public void setBv_vol2_tel(String bv_vol2_tel) {
        this.bv_vol2_tel = bv_vol2_tel;
    }

    public String getBv_vol3_name() {
        return bv_vol3_name;
    }

    public void setBv_vol3_name(String bv_vol3_name) {
        this.bv_vol3_name = bv_vol3_name;
    }

    public String getBv_vol3_tel() {
        return bv_vol3_tel;
    }

    public void setBv_vol3_tel(String bv_vol3_tel) {
        this.bv_vol3_tel = bv_vol3_tel;
    }

    public String getBv_vol4_name() {
        return bv_vol4_name;
    }

    public void setBv_vol4_name(String bv_vol4_name) {
        this.bv_vol4_name = bv_vol4_name;
    }

    public String getBv_vol4_tel() {
        return bv_vol4_tel;
    }

    public void setBv_vol4_tel(String bv_vol4_tel) {
        this.bv_vol4_tel = bv_vol4_tel;
    }
}
