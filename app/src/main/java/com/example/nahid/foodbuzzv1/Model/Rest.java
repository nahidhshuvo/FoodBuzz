package com.example.nahid.foodbuzzv1.Model;

/**
 * Created by Nahid on 4/23/2019.
 */

public class Rest {

    private String rname,rimage,rdescription,rprice;
    public Rest(){

    }

    public Rest(String rname, String rimage, String rdescription, String rprice) {
        this.rname = rname;
        this.rimage = rimage;
        this.rdescription = rdescription;
        this.rprice = rprice;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRimage() {
        return rimage;
    }

    public void setRimage(String rimage) {
        this.rimage = rimage;
    }

    public String getRdescription() {
        return rdescription;
    }

    public void setRdescription(String rdescription) {
        this.rdescription = rdescription;
    }

    public String getRprice() {
        return rprice;
    }

    public void setRprice(String rprice) {
        this.rprice = rprice;
    }
}
