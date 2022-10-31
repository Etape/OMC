package com.davinci.etone.omc;

import java.util.ArrayList;

public class Region {
    String reg_name;
    String reg_der;
    ArrayList<Bv> updatedBvlist;

    public ArrayList<Bv> getUpdatedBvlist() {
        return updatedBvlist;
    }

    public void setUpdatedBvlist(ArrayList<Bv> updatedBvlist) {
        this.updatedBvlist = updatedBvlist;
    }

    public Region(String reg_name, String reg_der) {
        this.reg_name = reg_name;
        this.reg_der = reg_der;
    }
    public Region() {
        this.reg_name = "none";
        this.reg_der = " Aucun";
    }

    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
    }

    public String getReg_der() {
        return reg_der;
    }

    public void setReg_der(String reg_der) {
        this.reg_der = reg_der;
    }
}
