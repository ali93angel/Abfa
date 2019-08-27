package com.app.leon.abfa.Activities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.app.leon.abfa.Adapters.ViewPagerAdapterTab;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.Fragments.DownloadFragment;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.DepthPageTransformer;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class DownloadActivity extends BaseActivity {
    @BindView(R.id.textViewDownloadNormal)
    TextView textViewDownloadNormal;
    @BindView(R.id.textViewDownloadRetry)
    TextView textViewDownloadRetry;
    @BindView(R.id.textViewDownloadOff)
    TextView textViewDownloadOff;
    @BindView(R.id.textViewDownloadSpecial)
    TextView textViewDownloadSpecial;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.pager)
    ViewPager viewPager;
    ISharedPreferenceManager sharedPreferenceManager;
    Context context;

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
        uiElementInActivity.setContentViewId(R.layout.download_activity);
        context = this;
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        TextViewDownloadNormal();
        TextViewDownloadSpecial();
        TextViewDownloadOff();
        TextViewDownloadRetry();
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(viewPager);
    }

    void TextViewDownloadOff() {
        textViewDownloadOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDownloadOff.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewDownloadOff.setTextColor(getResources().getColor(R.color.black1));
                textViewDownloadNormal.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadNormal.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadRetry.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadRetry.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadSpecial.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadSpecial.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(2);
            }
        });
    }

    void TextViewDownloadRetry() {
        textViewDownloadRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDownloadRetry.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewDownloadRetry.setTextColor(getResources().getColor(R.color.black1));
                textViewDownloadNormal.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadNormal.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadOff.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadOff.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadSpecial.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadSpecial.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(1);
            }
        });
    }

    void TextViewDownloadSpecial() {
        textViewDownloadSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDownloadSpecial.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewDownloadSpecial.setTextColor(getResources().getColor(R.color.black1));
                textViewDownloadNormal.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadNormal.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadOff.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadOff.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadRetry.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadRetry.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(3);
            }
        });
    }

    void TextViewDownloadNormal() {
        textViewDownloadNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDownloadNormal.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewDownloadNormal.setTextColor(getResources().getColor(R.color.black1));
                textViewDownloadOff.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadOff.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadRetry.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadRetry.setTextColor(getResources().getColor(R.color.white));
                textViewDownloadSpecial.setBackgroundColor(Color.TRANSPARENT);
                textViewDownloadSpecial.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(0);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(DownloadFragment.newInstance(1), "بارگیری");
        adapter.addFragment(DownloadFragment.newInstance(2), "بارگیری مجدد");
        adapter.addFragment(DownloadFragment.newInstance(3), "بارگیری offline");
        adapter.addFragment(DownloadFragment.newInstance(4), "بارگیری ویژه");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    textViewDownloadNormal.callOnClick();
                } else if (position == 1) {
                    textViewDownloadRetry.callOnClick();
                } else if (position == 2) {
                    textViewDownloadOff.callOnClick();
                } else if (position == 3) {
                    textViewDownloadSpecial.callOnClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        sharedPreferenceManager = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
        sharedPreferenceManager = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        context = this;
    }
}
