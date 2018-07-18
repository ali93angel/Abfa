package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class KarbariRateType {
    int id;
    String title;

    public KarbariRateType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
