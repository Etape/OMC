package com.davinci.etone.omc;

public class Filter {
    String type="none";
    String region="none";
    String departement="none";
    String pays="none";
    String commune="none";
    long startDate = 0;
    long endDate = 0;
    String sexe="none";
    long startAge= 0;
    long endAge= 0;
    String departement_org="none";

    public Filter(String type, String region, String departement, String pays, String commune,
                  long startDate, long endDate, String sexe, long startAge, long endAge, String
                          departement_org) {
        this.type = type;
        this.region = region;
        this.departement = departement;
        this.pays = pays;
        this.commune = commune;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sexe = sexe;
        this.startAge = startAge;
        this.endAge = endAge;
        this.departement_org = departement_org;
    }

    public Filter() {
        this.type = "none";
        this.region = "none";
        this.departement = "none";
        this.pays = "none";
        this.commune = "none";
        this.startDate = 0;
        this.endDate = 0;
        this.sexe = "none";
        this.startAge = 0;
        this.endAge = 0;
        this.departement_org = "none";
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

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public long getStartAge() {
        return startAge;
    }

    public void setStartAge(long startAge) {
        this.startAge = startAge;
    }

    public long getEndAge() {
        return endAge;
    }

    public void setEndAge(long endAge) {
        this.endAge = endAge;
    }

    public String getDepartement_org() {
        return departement_org;
    }

    public void setDepartement_org(String departement_org) {
        this.departement_org = departement_org;
    }
}
