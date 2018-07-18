package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 1/14/2018.
 */

public class UploadReadFeedback {
    String id;
    boolean hasError;

    public UploadReadFeedback(String id, boolean hasError) {
        this.id = id;
        this.hasError = hasError;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}
