package com.davinci.etone.omc;

public class Message {
    String emetteur;
    String recepteur;
    String contenu;
    String disc_id;
    long date_envoi;

    public String getDisc_id() {
        return disc_id;
    }

    public void setDisc_id(String disc_id) {
        this.disc_id = disc_id;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public String getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(String recepteur) {
        this.recepteur = recepteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public long getDate_envoi() {
        return date_envoi;
    }

    public void setDate_envoi(long date_envoi) {
        this.date_envoi = date_envoi;
    }

    public Message() {
        this.disc_id="none";
        this.emetteur = "auteur";
        this.recepteur = "None";
        this.contenu = "contenu";
        this.date_envoi = 0;
    }

    public Message(String emetteur, String recepteur, String contenu, long date_envoi) {
        this.emetteur = emetteur;
        this.recepteur = recepteur;
        this.contenu = contenu;
        this.date_envoi = date_envoi;
    }
}
