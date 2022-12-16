package com.davinci.etone.omc;

public class User {
    String id="none";
    String code="none";
    String nom="none";
    String prenom="none";
    String sexe="none";
    String date_naissance="none";
    String telephone="none";
    String email="none";
    String password="none";
    String type="citoyen";
    String region="none";
    String pays="none";
    String departement="none";
    String commune="none";
    String departement_org="none";
    String cni="none";
    String matricule_parti="none";
    String sympatisant="none";
    String parti="none";
    String parrain="none";
    String sous_comite_arr="none";
    String comite_base="none";
    long creation_date=0;
    int activite=0;

    public String getCode() {
        return code;
    }

    public void setCode(String id) {
        if (this.id.length()>10)
            this.code=this.id.substring(this.id.length()-9,this.id.length()-1);
    }

    public String getSous_comite_arr() {
        return sous_comite_arr;
    }

    public void setSous_comite_arr(String sous_comite_arr) {
        this.sous_comite_arr = sous_comite_arr;
    }

    public String getComite_base() {
        return comite_base;
    }

    public void setComite_base(String comite_base) {
        this.comite_base = comite_base;
    }

    public int getActivite() {
        return activite;
    }

    public void setActivite(int activite) {
        this.activite = activite;
    }

    public String getMatricule_parti() {
        return matricule_parti;
    }

    public void setMatricule_parti(String matricule_parti) {
        this.matricule_parti = matricule_parti;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creatio_date) {
        this.creation_date = creatio_date;
    }

    public String getParti() {
        return parti;
    }

    public void setParti(String parti) {
        this.parti = parti;
    }

    public String getParrain() {
        return parrain;
    }

    public void setParrain(String parrain) {
        this.parrain = parrain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if(id.length()>9)
        this.code=id.substring(id.length()-9,id.length()-1);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public User(String id, String nom, String prenom, String sexe, String date_naissance, String
            telephone, String email, String password, String type, String region, String pays,
                String departement, String commune, String departement_org, String cni, String sympatisant) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.date_naissance = date_naissance;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.type = type;
        this.region = region;
        this.pays = pays;
        this.departement = departement;
        this.commune = commune;
        this.departement_org = departement_org;
        this.cni = cni;
        this.sympatisant = sympatisant;
    }
    public User() {
        this.id = "id";
        this.code = "id";
        this.nom = "none";
        this.prenom = "none";
        this.sexe = "none";
        this.date_naissance = "none";
        this.telephone = "none";
        this.email = "none";
        this.password = "none";
        this.type = "none";
        this.region = "none";
        this.pays = "none";
        this.departement = "none";
        this.commune = "none";
        this.departement_org = "none";
        this.cni = "none";
        this.sympatisant = "none";
        this.comite_base = "none";
        this.sous_comite_arr = "none";
    }

    public User(String id) {
        this.id = id;
        this.nom = "none";
        this.prenom = "none";
        this.sexe = "none";
        this.date_naissance = "none";
        this.telephone = "none";
        this.email = "none";
        this.password = "none";
        this.type = "none";
        this.region = "none";
        this.pays = "none";
        this.departement = "none";
        this.commune = "none";
        this.departement_org = "none";
        this.cni = "none";
        this.sympatisant = "none";
    }
}
