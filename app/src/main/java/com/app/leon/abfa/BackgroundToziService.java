package com.app.leon.abfa;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.abfa.Activities.BillActivity;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.LocationUpdateModel;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.Utils.GeoTracker;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.IGeoTracker;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.MakeNotification;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;

public class BackgroundToziService extends Service {
    public static int counter;
    public Context context = this;
    SendLocation sendLocation;
    private IGeoTracker geoTracker;
    private ISharedPreferenceManager sharedPreferenceManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        counter = 0;
        sharedPreferenceManager = new SharedPreferenceManager(context);
        sendLocation = new SendLocation();

    }

    @Override
    public void onDestroy() {
        sendLocation.stopSendLocationToServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && Objects.equals(intent.getAction(), getString(R.string.stop_tozi_background_service))) {
            super.onStartCommand(intent, flags, startId);
            stopService(new Intent(this, BackgroundToziService.class));
            BillActivity billActivity = BillActivity.instance;
            billActivity.setButtonBill(getString(R.string.start), false);
            counter = 0;
            return START_NOT_STICKY;
        }
        sendLocation.sendLocationToServer();
//        makeNotification();
        MakeNotification.makeAboveNotification(context, BackgroundToziService.class,
                getString(R.string.stop_tozi_background_service), getString(R.string.tozing),
                getString(R.string.tozing), getString(R.string.stop),
                R.drawable.ic_menu_send, R.drawable.ic_menu_send);
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    class SendLocation implements ICallback<SimpleMessage> {
        SendLocation() {
            geoTracker = new ExtendedGeo("background", context);
            if (geoTracker.checkPlayServices()) {
                geoTracker.buildGoogleApiClient();
                geoTracker.createLocationRequest();
            }
        }

        void sendLocationToServer() {
            geoTracker.start();
            geoTracker.resume();
        }

        void stopSendLocationToServer() {
            geoTracker.pause();
            geoTracker.stop();
        }

        @Override
        public void execute(SimpleMessage simpleMessage) {
            counter++;
            BillActivity billActivity = BillActivity.instance;
            billActivity.setTextViewCounter(counter);
        }

        class ExtendedGeo extends GeoTracker {
            ExtendedGeo(String TAG, Context appContext) {
                super(TAG, appContext);
            }

            @Override
            public void onLocationChanged(Location location) {
                super.onLocationChanged(location);
                new sendLocation();
            }

            @SuppressLint("Registered")
            class sendLocation extends AppCompatActivity {
                sendLocation() {
                    Location lastLocation = geoTracker.getLastLocation();
                    BillActivity billActivity = BillActivity.instance;
                    if (lastLocation != null) {
                        if (lastLocation.hasAccuracy()) {
                            Integer accuracy = Math.round(lastLocation.getAccuracy());
                            sharedPreferenceManager = new SharedPreferenceManager(billActivity);
                            String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
//                            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxYzRkODM5Ny1iYWEwLTRlNjEtOTQ5NS1mZWY4NzkzM2E5NzkiLCJpc3MiOiJodHRwOi8vYXV0aHNlcnZlci8iLCJpYXQiOjE1MjY3MTE0NzIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWVpZGVudGlmaWVyIjoiZDZkMTQzODktODBjMi00OTU2LWE5MDQtODY2MDhkNjkyY2E4IiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI6InRzd185NSIsImRpc3BsYXlOYW1lIjoi2LHYs9iq2YUiLCJ1c2VySWQiOiJkNmQxNDM4OS04MGMyLTQ5NTYtYTkwNC04NjYwOGQ2OTJjYTgiLCJ1c2VyQ29kZSI6Ijk1IiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9zZXJpYWxudW1iZXIiOiIwZTBjYjA0ODJmNjI0ZDRkYTliMzFhZDRkN2JlODBmMSIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdXNlcmRhdGEiOiJkNmQxNDM4OS04MGMyLTQ5NTYtYTkwNC04NjYwOGQ2OTJjYTgiLCJ6b25lSWQiOlsiMTIwOTEzIiwiMTIwOTA2IiwiMTIwOTEyIiwiMTIwOTA3IiwiMTIwOTEwIiwiMTIwOTA1IiwiMTIwOTAyIiwiMTIwOTExIiwiMTIwOTA5IiwiMTIwOTA0IiwiMTIwMDAwIiwiMTIwOTAzIiwiMTIwOTAxIiwiMTIwOTA4Il0sImFjdGlvbiI6WyJPZmZsb2FkaW5nLk9mZmxvYWRFbXB0eUJvZHkiLCJPZmZsb2FkaW5nLlNldENvdW50ZXJQb3NpdGlvbiIsIk9mZmxvYWRpbmcuT2ZmbG9hZCIsIk9mZmxvYWRpbmcuVXBsb2FkSW1hZ2UiLCJPZmZsb2FkaW5nLlJlZ2lzdGVyRGVzY3JpcHRpb24iLCJRZWlyZU1vamF6LlJlZ2lzdGVyIiwiTG9hZGluZy5SZWxvYWQiLCJMb2FkaW5nLkxvYWQiLCJBcGsuR2V0TmV3ZXN0SW5mbyIsIkFway5HZXROZXdlc3QiLCJQcm9maWxlLkluZGV4IiwiVG96aUdoYWJzTWFuYWdlci5BZGQiXSwibmJmIjoxNTI2NzExNDcxLCJleHAiOjE1MjY3NTQ2NzEsImF1ZCI6IjQxNGUxOTI3YTM4ODRmNjhhYmM3OWY3MjgzODM3ZmQxIn0.gxpvvFDGNZXl0f07lwDSf4F9BX7fFbFtY-QbPypDnOM";
                            Retrofit retrofit = NetworkHelper.getInstance(true, token);
                            final IAbfaService sendLocation = retrofit.create(IAbfaService.class);
                            Call<SimpleMessage> call = sendLocation.toziGhabs(new LocationUpdateModel(lastLocation, accuracy));
                            HttpClientWrapper.callHttpAsync(call, SendLocation.this,
                                    billActivity, ProgressType.NOT_SHOW.getValue());
                        }
                    }
                }
            }
        }
    }
}