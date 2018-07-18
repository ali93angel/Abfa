package com.app.leon.abfa.Activities;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.app.leon.abfa.Adapters.ViewPagerAdapterTab;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.Fragments.ReadSettingDeleteFragment;
import com.app.leon.abfa.Fragments.ReadSettingFragment;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.DepthPageTransformer;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class ReadSettingActivity extends BaseActivity {
    public static List<ReadingConfig> readingConfigs;
    @BindView(R.id.textViewRead)
    TextView textViewRead;
    @BindView(R.id.textViewChangeSend)
    TextView textViewChangeSend;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.textViewDelete)
    TextView textViewDelete;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private SharedPreferenceManager sharedPreferenceManager;

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
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            String theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
            onActivitySetTheme(Integer.valueOf(theme));
        }
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.read_setting_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        textViewChangeSend = (TextView) findViewById(R.id.textViewChangeSend);

        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(viewPager);
        setOnPageChangeListener();
        setTextViewReadClick();
//     setTextViewChangeSendClick();
        setTextViewDeleteClick();
        setTextViewReadClick();
    }

    void setTextViewReadClick() {
        textViewRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewRead.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewRead.setTextColor(getResources().getColor(R.color.black1));
                textViewChangeSend.setBackgroundColor(Color.TRANSPARENT);
                textViewChangeSend.setTextColor(getResources().getColor(R.color.white));
                textViewDelete.setBackgroundColor(Color.TRANSPARENT);
                textViewDelete.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(0);
            }
        });
    }

    void setTextViewChangeSendClick() {
        textViewChangeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewChangeSend.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewChangeSend.setTextColor(getResources().getColor(R.color.black1));
                textViewRead.setBackgroundColor(Color.TRANSPARENT);
                textViewRead.setTextColor(getResources().getColor(R.color.white));
                textViewDelete.setBackgroundColor(Color.TRANSPARENT);
                textViewDelete.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void setTextViewDeleteClick() {
        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDelete.setBackground(getResources().getDrawable(R.drawable.border_white_));
                textViewDelete.setTextColor(getResources().getColor(R.color.black1));
                textViewRead.setBackgroundColor(Color.TRANSPARENT);
                textViewRead.setTextColor(getResources().getColor(R.color.white));
                textViewChangeSend.setBackgroundColor(Color.TRANSPARENT);
                textViewChangeSend.setTextColor(getResources().getColor(R.color.white));
                viewPager.setCurrentItem(2);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterTab adapter = new ViewPagerAdapterTab(getSupportFragmentManager());
        adapter.addFragment(new ReadSettingFragment(), "تنظیمات قرائت");
//        adapter.addFragment(new ReadSettingSendFragment(), "تنظیمات ارسال");
        adapter.addFragment(new ReadSettingDeleteFragment(), "حذف داده ها");
        viewPager.setAdapter(adapter);
    }

    private void setOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    textViewRead.callOnClick();
                } else if (position == 2) {
                    textViewChangeSend.callOnClick();
                } else if (position == 1) {
                    textViewDelete.callOnClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}