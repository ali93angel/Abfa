package com.app.leon.abfa.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.app.leon.abfa.Adapters.ViewPagerAdapterTab;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.Fragments.SettingChangePasswordFragment;
import com.app.leon.abfa.Fragments.SettingChangeThemeFragment;
import com.app.leon.abfa.Fragments.SettingUpdateFragment;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.DepthPageTransformer;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class SettingActivity extends BaseActivity {
    static Context context;
    @BindView(R.id.textViewChangePassword)
    TextView textViewChangePassword;
    @BindView(R.id.textViewChangeTheme)
    TextView textViewChangeTheme;
    @BindView(R.id.textViewUpdate)
    TextView textViewUpdate;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private SharedPreferenceManager sharedPreferenceManager;

    public static void runFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        String fileName = DifferentCompanyManager.getActiveCompanyName().toString();
        StrictMode.VmPolicy.Builder newBuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newBuilder.build());
        File futureStudioIconFile = new File(root + "/Download" + File.separator + fileName + ".apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(futureStudioIconFile), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
    protected UiElementInActivity getUiElementsInActivity() {
        Bundle extras = getIntent().getExtras();
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (extras != null) {
            String theme = extras.getString(BundleEnum.THEME.getValue());
            onActivitySetTheme(Integer.valueOf(theme));
        } else if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            String theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
            onActivitySetTheme(Integer.valueOf(theme));
        }
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.setting_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = getApplicationContext();
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        setTextViewChangeThemeClickListener();
        setTextViewChangePasswordClickListener();
        setTextViewUpdateClickListener();
        setupViewPager();
        setOnPageChangeListener();
    }

    void setTextViewChangeThemeClickListener() {
        textViewChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewChangeTheme.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewChangeTheme.setTextColor(getResources().getColor(R.color.black1));
                textViewUpdate.setBackgroundColor(0x00000000);
                textViewUpdate.setTextColor(getResources().getColor(R.color.white));
                textViewChangePassword.setBackgroundColor(0x00000000);
                textViewChangePassword.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(0);
            }
        });
    }

    void setTextViewChangePasswordClickListener() {
        textViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewChangePassword.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewChangePassword.setTextColor(getResources().getColor(R.color.black1));
                textViewUpdate.setBackgroundColor(0x00000000);
                textViewUpdate.setTextColor(getResources().getColor(R.color.white));
                textViewChangeTheme.setBackgroundColor(0x00000000);
                textViewChangeTheme.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(1);
            }
        });
    }

    void setTextViewUpdateClickListener() {
        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewUpdate.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewUpdate.setTextColor(getResources().getColor(R.color.black1));
                textViewChangeTheme.setBackgroundColor(0x00000000);
                textViewChangeTheme.setTextColor(getResources().getColor(R.color.white));
                textViewChangePassword.setBackgroundColor(0x00000000);
                textViewChangePassword.setTextColor(getResources().getColor(R.color.white));
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
                    textViewChangeTheme.callOnClick();
                } else if (position == 1) {
                    textViewChangePassword.callOnClick();
                } else if (position == 2) {
                    textViewUpdate.callOnClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager() {
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new SettingChangeThemeFragment(), "تغییر پوسته");
        adapter.addFragment(new SettingChangePasswordFragment(), "تغییر گذرواژه");
        adapter.addFragment(new SettingUpdateFragment(), "به روز رسانی");
        viewPager.setAdapter(adapter);

    }

}
