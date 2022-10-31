package com.davinci.etone.omc;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Discussion {
    String id;
    String title;
    String type;
    String initiateur;
    String interlocuteur;
    long last_date;
    String last_message;
    String last_writer;

    public String getLast_writer() {
        return last_writer;
    }

    public void setLast_writer(String last_writer) {
        this.last_writer = last_writer;
    }

    public Discussion(String id, String title, String type, String initiateur, String
            interlocuteur2, long
            last_date, String last_message) {
        this.id = id;
        this.title=title;
        this.type=type;
        this.initiateur = initiateur;
        this.interlocuteur = interlocuteur2;
        this.last_date = last_date;
        this.last_message = last_message;
    }
    public Discussion() {
        this.id = "id";
        this.title="none";
        this.type="tchat";
        this.initiateur = "id";
        this.interlocuteur = "id";
        this.last_date = 0;
        this.last_message = "none";
        this.last_writer = "none";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInitiateur() {
        return initiateur;
    }

    public void setInitiateur(String initiateur) {
        this.initiateur = initiateur;
    }

    public String getInterlocuteur() {
        return interlocuteur;
    }

    public void setInterlocuteur(String interlocuteur) {
        this.interlocuteur = interlocuteur;
    }

    public long getLast_date() {
        return last_date;
    }

    public void setLast_date(long last_date) {
        this.last_date = last_date;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }



}
