package com.app.leon.abfa.Utils;

import android.location.Location;

import com.app.leon.abfa.Models.ViewModels.LatLang;

public interface IGeoTracker {
    LatLang getLatLang();

    void displayLocation();

    void buildGoogleApiClient();

    boolean checkPlayServices();

    void start();

    void resume();

    void stop();

    void pause();

    void createLocationRequest();

    void startLocationUpdates();

    void stopLocationUpdates();

    void togglePeriodicLocationUpdates();

    Location getLastLocation();
}
