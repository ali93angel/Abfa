package com.app.leon.abfa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.leon.abfa.Adapters.ViewPagerAdapterTab;
import com.app.leon.abfa.Fragments.LocationFragment;
import com.app.leon.abfa.Fragments.PlaceFragment;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.DepthPageTransformer;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.BuildTypeManager.isGisBuild;

public class LocationActivity extends AppCompatActivity {
    public static String theme = "1";
    public OnOffLoad onOffLoad;
    ISharedPreferenceManager sharedPreferenceManager;
    Context context;
    @BindView(R.id.location_activity)
    RelativeLayout relativeLayout;
    @BindView(R.id.textViewLocation)
    TextView textViewLocation;
    @BindView(R.id.textViewPlace)
    TextView textViewPlace;
    @BindView(R.id.pager)
    ViewPager viewPager;
    FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        GpsEnabled();
        context = this;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            String jsonBundle = bundle.getString(BundleEnum.ON_OFFLOAD.getValue());
            onOffLoad = new Gson().fromJson(jsonBundle, OnOffLoad.class);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }


    @Override
    protected void onNewIntent(Intent intent) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    void initialize() {
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);
        setTextViewPlaceClickListener();
        setTextViewLocationClickListener();
        setupViewPager();
    }

    void setTextViewPlaceClickListener() {
        textViewPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewPlace.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewPlace.setTextColor(getResources().getColor(R.color.black1));
                textViewLocation.setBackgroundColor(0x00000000);
                textViewLocation.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(0);
            }
        });
    }

    void setTextViewLocationClickListener() {
        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewLocation.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewLocation.setTextColor(getResources().getColor(R.color.black1));
                textViewPlace.setBackgroundColor(0x00000000);
                textViewPlace.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(1);
            }
        });
    }

    void setupViewPager() {
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new PlaceFragment(), "مکان کنتور");
        if (isGisBuild()) {
            adapter.addFragment(new LocationFragment(), "لایه ها ");
        }
        viewPager.setAdapter(adapter);
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    textViewPlace.callOnClick();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else if (position == 1) {
                    textViewLocation.callOnClick();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onActivitySetTheme(int theme) {
        if (theme == 1) {
            setTheme(R.style.AppTheme);
        } else if (theme == 2) {
            setTheme(R.style.AppTheme_GreenBlue);
        } else if (theme == 3) {
            setTheme(R.style.AppTheme_Indigo);
        } else if (theme == 4) {
            setTheme(R.style.AppTheme_DarkGrey);
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
