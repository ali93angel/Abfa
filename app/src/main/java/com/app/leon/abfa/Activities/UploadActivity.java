package com.app.leon.abfa.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.app.leon.abfa.Adapters.ViewPagerAdapterTab;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.DBService.ReadingConfigService;
import com.app.leon.abfa.Fragments.UploadFragment;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.DbTables.MultimediaData;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.DbTables.QeireMojaz;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.Models.DbTables.Report;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.OffLoadParams;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.DepthPageTransformer;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class UploadActivity extends BaseActivity {
    public static String trackNumber;
    public List<QeireMojaz> qeireMojazs = new ArrayList<>();
    public long onOffLoadSize;
    public List<ReadingConfig> readingConfigs = new ArrayList<>();
    public List<OffLoadParams> offLoads = new ArrayList<>();
    public List<Report> reports = new ArrayList<>();
    public List<MultimediaData> multimediaDatas = new ArrayList<>();
    public List<OnOffLoad> onOffLoads = new ArrayList<>();
    Context context;
    private SharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.textViewUpload)
    TextView textViewUpload;
    @BindView(R.id.textViewUploadOff)
    TextView textViewUploadOff;
    @BindView(R.id.textViewUploadMultimedia)
    TextView textViewMultimedia;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.pager)
    ViewPager viewPager;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            String theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
            onActivitySetTheme(Integer.valueOf(theme));
        }
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.upload_activity);
        return uiElementInActivity;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Snackbar.make(viewPager, getString(R.string.side_bar), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = this;
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        setTextViewUploadOnClickListener();
        setTextViewUploadOffClickListener();
        setTextViewMultimediaClickListener();
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(viewPager);
        setOnPageChangeListener();
        new getNeedUploadDate(context).execute();
    }

    void setTextViewUploadOnClickListener() {
        textViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewUpload.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewUpload.setTextColor(getResources().getColor(R.color.black1));
                textViewUploadOff.setBackgroundColor(0x00000000);
                textViewUploadOff.setTextColor(getResources().getColor(R.color.white));
                textViewMultimedia.setBackgroundColor(0x00000000);
                textViewMultimedia.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(0);
            }
        });
    }

    void setTextViewUploadOffClickListener() {
        textViewUploadOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewUploadOff.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewUploadOff.setTextColor(getResources().getColor(R.color.black1));
                textViewUpload.setBackgroundColor(0x00000000);
                textViewUpload.setTextColor(getResources().getColor(R.color.white));
                textViewMultimedia.setBackgroundColor(0x00000000);
                textViewMultimedia.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(1);
            }
        });
    }

    void setTextViewMultimediaClickListener() {
        textViewMultimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewMultimedia.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewMultimedia.setTextColor(getResources().getColor(R.color.black1));
                textViewUpload.setBackgroundColor(0x00000000);
                textViewUpload.setTextColor(getResources().getColor(R.color.white));
                textViewUploadOff.setBackgroundColor(0x00000000);
                textViewUploadOff.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(2);
            }
        });
    }

    private void setOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    textViewUpload.callOnClick();
                } else if (position == 1) {
                    textViewUploadOff.callOnClick();
                } else if (position == 2) {
                    textViewMultimedia.callOnClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(UploadFragment.newInstance(1), "تخلیه");
        adapter.addFragment(UploadFragment.newInstance(2), "تخلیه offline");
        adapter.addFragment(UploadFragment.newInstance(3), "تخلیه چندرسانه ای");
        viewPager.setAdapter(adapter);
    }


    public class getNeedUploadDate extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        Context context;

        getNeedUploadDate(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            readingConfigs = ReadingConfigService.getReadingConfigsNoArchive(false);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }
}
