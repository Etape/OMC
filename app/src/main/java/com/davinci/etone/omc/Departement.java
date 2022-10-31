package com.davinci.etone.omc;

import java.util.ArrayList;

public class Departement {
    String dep_name;
    String dep_dep;
    ArrayList<Bv> updatedBvlist;

    public ArrayList<Bv> getUpdatedBvlist() {
        return updatedBvlist;
    }

    public void setUpdatedBvlist(ArrayList<Bv> updatedBvlist) {
        this.updatedBvlist = updatedBvlist;
    }

    public Departement(String dep_name, String dep_dep) {
        this.dep_name = dep_name;
        this.dep_dep = dep_dep;
    }
    public Departement() {
        this.dep_name = "none";
        this.dep_dep = " Aucun";
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getDep_dep() {
        return dep_dep;
    }

    public void setDep_dep(String dep_dep) {
        this.dep_dep = dep_dep;
    }
}
