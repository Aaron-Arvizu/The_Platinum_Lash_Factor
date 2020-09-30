package com.platinumlashes.lashdemomkiv;

public class ClientModel {
    public String lName;
    public String mName;
    public String picture;
    public String idNum;
    public String mostRecentCheck;

    public ClientModel(){}
    public ClientModel(String fName,String mName, String lName, String picture, String idNum, String mostRecentCheck) {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.picture = picture;
        this.idNum = idNum;
        this.mostRecentCheck = mostRecentCheck;
    }

    public String fName;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public void setMostRecentCheck(String mostRecentCheck) {this.mostRecentCheck = mostRecentCheck;}




}