package com.davinci.etone.omc;

public class Action {
    String type;
    String titre;
    String cible;
    int audienceMedia;
    int audiencePresentiel;
    long debut;
    long fin;
    long time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCible() {
        return cible;
    }

    public void setCible(String cible) {
        this.cible = cible;
    }

    public long getDebut() {
        return debut;
    }

    public void setDebut(long debut) {
        this.debut = debut;
    }

    public long getFin() {
        return fin;
    }

    public void setFin(long fin) {
        this.fin = fin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAudienceMedia() {
        return audienceMedia;
    }

    public void setAudienceMedia(int audienceMedia) {
        this.audienceMedia = audienceMedia;
    }

    public int getAudiencePresentiel() {
        return audiencePresentiel;
    }

    public void setAudiencePresentiel(int audiencePresentiel) {
        this.audiencePresentiel = audiencePresentiel;
    }

    public Action(String type, String titre,int audienceMedia,int audiencePresentiel, String cible, long debut,
                  long fin, long time) {
        this.type = type;
        this.titre = titre;
        this.cible = cible;
        this.audienceMedia = audienceMedia;
        this.audiencePresentiel = audiencePresentiel;
        this.debut = debut;
        this.fin = fin;
        this.time = time;
    }

    public Action() {
        this.type = "none";
        this.titre = "none";
        this.cible = "none";
        this.audienceMedia = 0;
        this.audiencePresentiel = 0;
        this.debut = 0;
        this.fin = 0;
        this.time = 0;
    }
}
