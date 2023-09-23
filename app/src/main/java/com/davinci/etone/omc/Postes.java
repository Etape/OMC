package com.davinci.etone.omc;

import java.util.ArrayList;

public class Postes {
    String zone;
    String intitule;
    String categorie;
    ArrayList<String> postes;

    public Postes(String intitule) {
        this.intitule = intitule;
        this.postes=new ArrayList<>();
        this.postes.add("PN");
        this.postes.add("SG");
        this.postes.add("DEP");
        this.postes.add("Admin");
        this.postes.add("DED");
        this.postes.add("DEC");
        this.postes.add("DECON");
        this.postes.add("DEPAYS");
        this.postes.add("Point Focal Region");
        this.postes.add("SG DEP");
        this.postes.add("Delegue Regional");
        this.postes.add("SG CA");
        this.postes.add("Resp. Comm + Pub");
        this.postes.add("Resp. Lobbying");
        this.postes.add("SG1 DEP");
        this.postes.add("SG2 DEP");
        this.postes.add("SG3 DEP");
        this.postes.add("Mandataire ELECAM");
        this.postes.add("Volontaire");
        this.postes.add("Militant");
        this.postes.add("Citoyen");
        for (int i=0;i<postes.size();i++){
            if (postes.get(i).toUpperCase().equals(intitule.toUpperCase().trim())){
                if (i<4)
                    this.categorie = "A";
                else if(i>=4 & i<14)
                    this.categorie = "B";
                else if(i>=14 & i<postes.size()-1)
                    this.categorie = "C";
                else
                    this.categorie = "D";
            }
        }

        for (int i=0;i<postes.size();i++){
            if (postes.get(i).toUpperCase().equals(intitule.toUpperCase().trim())){
                if (i<4)
                    this.zone = "6";
                else if(i>=4 & i<14){
                    if (intitule.equals("DEC") | intitule.equals("SG CA") )
                        this.zone = "1";
                    else if (intitule.equals("DED"))
                        this.zone = "2";
                    else if (intitule.equals("DER") | intitule.contains("Region"))
                        this.zone = "3";
                    else if (intitule.equals("DEPAYS"))
                        this.zone = "4";
                    else if (intitule.equals("DECON"))
                        this.zone = "5";
                    else
                        this.zone= "1";
                }
                else if(i>=14 & i<postes.size()-1)
                    this.zone = "0";
                else
                    this.zone = "-1";
            }
        }
    }

    public Postes() {
        this.intitule = "Citoyen";
        this.postes=new ArrayList<>();
        this.postes.add("PN");
        this.postes.add("SG");
        this.postes.add("DEP");
        this.postes.add("Admin");
        this.postes.add("DED");
        this.postes.add("DEC");
        this.postes.add("DECON");
        this.postes.add("DEPAYS");
        this.postes.add("Point Focal Region");
        this.postes.add("SG DEP");
        this.postes.add("Delegue Regional");
        this.postes.add("SG CA");
        this.postes.add("Resp. Comm + Pub");
        this.postes.add("Agent Comm");
        this.postes.add("Resp. Lobbying");
        this.postes.add("SG1 DEP");
        this.postes.add("SG2 DEP");
        this.postes.add("SG3 DEP");
        this.postes.add("Mandataire ELECAM");
        this.postes.add("Volontaire");
        this.postes.add("Militant");
        this.postes.add("Citoyen");
        for (int i=0;i<postes.size();i++){
            if (postes.get(i).toUpperCase().equals(intitule.toUpperCase().trim())){
                if (i<4)
                    this.categorie = "A";
                else if(i>=4 & i<15)
                    this.categorie = "B";
                else if(i>=15 & i<postes.size()-1)
                    this.categorie = "C";
                else
                    this.categorie = "D";
            }
        }
        for (int i=0;i<postes.size();i++){
            if (postes.get(i).toUpperCase().equals(intitule.toUpperCase().trim())){
                if (i<4)
                    this.zone = "6";
                else if(i>=4 & i<14){
                    if (intitule.equals("DEC") | intitule.equals("SG CA") )
                        this.zone = "1";
                    else if (intitule.equals("DED"))
                        this.zone = "2";
                    else if (intitule.equals("DER") | intitule.contains("Region"))
                        this.zone = "3";
                    else if (intitule.equals("DEPAYS"))
                        this.zone = "4";
                    else if (intitule.equals("DECON"))
                        this.zone = "5";
                    else
                        this.zone= "1";
                }
                else if(i>=14 & i<postes.size()-1)
                    this.zone = "0";
                else
                    this.zone = "-1";
            }
        }
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public ArrayList<String> getPostes() {
        return postes;
    }

    public void setPostes(ArrayList<String> postes) {
        this.postes = postes;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
