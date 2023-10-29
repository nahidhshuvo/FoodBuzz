package com.example.nahid.foodbuzzv1.Model;

/**
 * Created by Nahid on 5/2/2019.
 */

public class Rev {
    private String restname,revcaption,revimage,revdescription,revuid;

    public Rev() {
    }



    public Rev(String restname, String revcaption, String revimage, String revdescription) {
        this.restname = restname;
        this.revcaption = revcaption;
        this.revimage = revimage;
        this.revdescription = revdescription;
        this.revuid = revuid;
    }

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public String getRevcaption() {
        return revcaption;
    }

    public void setRevcaption(String revcaption) {
        this.revcaption = revcaption;
    }

    public String getRevuid() {
        return revuid;
    }

    public void setRevuid(String revuid) {
        this.revuid = revuid;
    }

    public String getRevimage() {
        return revimage;
    }

    public void setRevimage(String revimage) {
        this.revimage = revimage;
    }

    public String getRevdescription() {
        return revdescription;
    }

    public void setRevdescription(String revdescription) {
        this.revdescription = revdescription;
    }
}
