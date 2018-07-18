package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class MyWorks {
    String id;
    OnLoad onLoad;
    OffLoad offLoad;

    public MyWorks(String id, OnLoad onLoad, OffLoad offLoad) {
        this.id = id;
        this.onLoad = onLoad;
        this.offLoad = offLoad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OnLoad getOnLoad() {
        return onLoad;
    }

    public void setOnLoad(OnLoad onLoad) {
        this.onLoad = onLoad;
    }

    public OffLoad getOffLoad() {
        return offLoad;
    }

    public void setOffLoad(OffLoad offLoad) {
        this.offLoad = offLoad;
    }

}
