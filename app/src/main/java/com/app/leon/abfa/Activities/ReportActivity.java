package com.app.leon.abfa.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.app.leon.abfa.Adapters.ViewPagerAdapterTab;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.DBService.CounterStateValueKeyService;
import com.app.leon.abfa.Fragments.ReportNotReadFragment;
import com.app.leon.abfa.Fragments.ReportTemporaryFragment;
import com.app.leon.abfa.Fragments.ReportTotalFragment;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.DbTables.CounterStateValueKey;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.DepthPageTransformer;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadActiveSize;
import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadAllIsManeSize;
import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadReadUnReadSize;
import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class ReportActivity extends BaseActivity {
    public static long onOffLoadSize;
    public static long onOffLoadsStandard = 0, onOffLoadsHigh = 0, onOffLoadsLow = 0,
            onOffLoadsZero = 0, notRead = 0, allIsMane = 0;
    public static int selected;
    public static List<CounterStateValueKey> counterStateValueKeys = new ArrayList<>();
    public Context context;
    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.textViewNotRead)
    TextView textViewNotRead;
    @BindView(R.id.textViewTemporary)
    TextView textViewTemporary;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Snackbar.make(viewPager, getString(R.string.side_bar), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            String theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
            onActivitySetTheme(Integer.valueOf(theme));
        }
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.report_activity);
        return uiElementInActivity;
    }

    void setTextViewTotalClickListener() {
        textViewTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTotal.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewTotal.setTextColor(getResources().getColor(R.color.black1));
                textViewTemporary.setBackgroundColor(Color.TRANSPARENT);
                textViewTemporary.setTextColor(getResources().getColor(R.color.white));
                textViewNotRead.setBackgroundColor(Color.TRANSPARENT);
                textViewNotRead.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(0);
            }
        });
    }

    void setTextViewTemporaryClickListener() {
        textViewTemporary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTemporary.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewTemporary.setTextColor(getResources().getColor(R.color.black1));
                textViewTotal.setBackgroundColor(Color.TRANSPARENT);
                textViewTotal.setTextColor(getResources().getColor(R.color.white));
                textViewNotRead.setBackgroundColor(Color.TRANSPARENT);
                textViewNotRead.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(2);
            }
        });
    }

    void setTextViewNotReadClickListener() {
        textViewNotRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewNotRead.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewNotRead.setTextColor(getResources().getColor(R.color.black1));
                textViewTemporary.setBackgroundColor(Color.TRANSPARENT);
                textViewTemporary.setTextColor(getResources().getColor(R.color.white));
                textViewTotal.setBackgroundColor(Color.TRANSPARENT);
                textViewTotal.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new ReportTotalFragment(), "آمار کلی");
        adapter.addFragment(new ReportNotReadFragment(), "قرائت نشده");
        adapter.addFragment(new ReportTemporaryFragment(), "علی الحساب");
        viewPager.setAdapter(adapter);
        setOnPageChangeListener();
    }

    private void setOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    textViewTotal.callOnClick();
                } else if (position == 1) {
                    textViewNotRead.callOnClick();
                } else if (position == 2) {
                    textViewTemporary.callOnClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = this;
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        setTextViewTotalClickListener();
        setTextViewNotReadClickListener();
        setTextViewTemporaryClickListener();
        new FillReadReport(context).execute();
    }


    private class FillReadReport extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        Context context;

        FillReadReport(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... s) {
            onOffLoadSize = getOnOffLoadActiveSize(context);
            notRead = getOnOffLoadReadUnReadSize(context);
            counterStateValueKeys = CounterStateValueKeyService.getCounterStateValueKeyIsMane();
            allIsMane = getOnOffLoadAllIsManeSize(context, counterStateValueKeys);
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            viewPager.setPageTransformer(true, new DepthPageTransformer());
            setupViewPager(viewPager);
            progressDialog.dismiss();
        }
    }
}
