package com.davinci.etone.omc;

public class Preinscription {

    String id="none";
    String nom="none";
    String prenom="none";
    String sexe="none";
    String date_naissance="none";
    String telephone="none";
    String email="none";
    String region="none";
    String pays="none";
    String parti="none";
    String matricule_parti="none";
    String departement="none";
    String commune="none";
    String departement_org="none";
    String cni="none";
    String parrain="none";
    String sympatisant="none";
    long creation_date=0;

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public String getParrain() {
        return parrain;
    }

    public void setParrain(String parrain) {
        this.parrain = parrain;
    }

    public String getParti() {
        return parti;
    }

    public void setParti(String parti) {
        this.parti = parti;
    }

    public String getMatricule_parti() {
        return matricule_parti;
    }

    public void setMatricule_parti(String matricule_parti) {
        this.matricule_parti = matricule_parti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDepartement_org() {
        return departement_org;
    }

    public void setDepartement_org(String departement_org) {
        this.departement_org = departement_org;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public String getSympatisant() {
        return sympatisant;
    }

    public void setSympatisant(String sympatisant) {
        this.sympatisant = sympatisant;
    }

    public Preinscription(String id, String nom, String prenom, String sexe, String date_naissance, String
            telephone, String email, String region, String pays,
                String departement, String commune, String departement_org, String cni, String
                                  sympatisant,String parti,String matricule_parti) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.date_naissance = date_naissance;
        this.telephone = telephone;
        this.email = email;
        this.region = region;
        this.pays = pays;
        this.departement = departement;
        this.commune = commune;
        this.parti = parti;
        this.matricule_parti = matricule_parti;
        this.departement_org = departement_org;
        this.cni = cni;
        this.sympatisant = sympatisant;
    }
    public Preinscription() {
        this.id = "id";
        this.nom = "none";
        this.prenom = "none";
        this.sexe = "none";
        this.date_naissance = "none";
        this.telephone = "none";
        this.email = "none";
        this.region = "none";
        this.pays = "none";
        this.departement = "none";
        this.commune = "none";
        this.parti = "none";
        this.matricule_parti = "none";
        this.departement_org = "none";
        this.cni = "none";
        this.sympatisant = "none";
    }

}
