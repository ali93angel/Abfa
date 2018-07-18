package com.app.leon.abfa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.leon.abfa.BackgroundToziService;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.ErrorHandlerType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.LocationUpdateModel;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.GeoTracker;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.IGeoTracker;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class BillActivity extends BaseActivity
        implements ICallback<SimpleMessage> {
    private Context context;
    @BindView(R.id.imageViewBill)
    ImageView imageViewBill;
    @BindView(R.id.textViewCounter)
    TextView textViewCounter;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.buttonBill)
    Button buttonBill;
    private ISharedPreferenceManager sharedPreferenceManager;
    private Integer accuracy;
    private IGeoTracker geoTracker;
    private boolean start = true;
    private int counter = 0;
    public static BillActivity instance;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            String theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
            onActivitySetTheme(Integer.valueOf(theme));

        }
        GpsEnabled();
        instance = this;
        context = this;
        geoTracker = new ExtendedGeo("billActivity", context);
        if (geoTracker.checkPlayServices()) {
            geoTracker.buildGoogleApiClient();
            geoTracker.createLocationRequest();
        }
        geoTracker.displayLocation();
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.bill_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        if (BackgroundToziService.counter > 0) {
            counter = BackgroundToziService.counter;
            setTextViewCounter(counter);
            buttonBill.setText(getString(R.string.end));
            start = false;
        }
        startAnimationOnTextViewCounter();
        setButtonBillClickListener();
    }

    private void startAnimationOnTextViewCounter() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textViewCounter.startAnimation(anim);
    }

    private void setButtonBillClickListener() {
        buttonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                if (start) {
                    buttonBill.setText(getString(R.string.end));
                    start = false;
//                    geoTracker.start();
//                    geoTracker.resume();

                    Intent intent = new Intent(context, BackgroundToziService.class);
                    startService(intent);

                } else {
                    buttonBill.setText(getString(R.string.start));
                    start = true;
//                    geoTracker.pause();
//                    geoTracker.stop();

                    Intent intent = new Intent(context, BackgroundToziService.class);
                    stopService(intent);
                }
            }
        });
    }

    public void setTextViewCounter(int counter) {
        Log.e("number", String.valueOf(counter));
        textViewCounter.setText(String.valueOf(counter));
    }

    public void setButtonBill(String text, boolean stop) {
        buttonBill.setText(text);
        start = !stop;
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        counter++;
        textViewCounter.setText(String.valueOf(counter));
    }

    @Override
    protected void onStart() {
        super.onStart();
        context = this;
        imageViewBill.setImageResource(R.drawable.img_temporary3);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
        imageViewBill.setImageDrawable(null);
        sharedPreferenceManager = null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Snackbar.make(imageViewBill, getString(R.string.side_bar), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    class ExtendedGeo extends GeoTracker {
        ExtendedGeo(String TAG, Context appContext) {
            super(TAG, appContext);
        }

        @Override
        public void onLocationChanged(Location location) {
            super.onLocationChanged(location);
            if (location.hasAccuracy()) {
                accuracy = Math.round(location.getAccuracy());
            }
            sendLocation();
        }

        private void sendLocation() {
            Location lastLocation = geoTracker.getLastLocation();
            if (lastLocation != null) {
                if (lastLocation.hasAccuracy()) {
                    accuracy = Math.round(lastLocation.getAccuracy());
                    sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
                    String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
                    Retrofit retrofit = NetworkHelper.getInstance(true, token);
                    final IAbfaService sendLocation = retrofit.create(IAbfaService.class);
                    Call<SimpleMessage> call = sendLocation.toziGhabs(new LocationUpdateModel(lastLocation, accuracy));
                    HttpClientWrapper.callHttpAsync(call, BillActivity.this, context, ErrorHandlerType.ordinary);
                }
            }
        }
    }

    private void GpsEnabled() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("GPS IS:", enabled + "");
        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(getString(R.string.setting_gps));
            alertDialog.setMessage(getString(R.string.gps_question));
            alertDialog.setPositiveButton(getString(R.string.setting_), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton(getString(R.string.close_app), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            alertDialog.show();
        }
    }
}
