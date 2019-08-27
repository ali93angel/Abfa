package com.app.leon.abfa.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class AboutActivity extends AppCompatActivity {
    Context context;
    String theme = "1";
    ISharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.linearLayoutLogo)
    LinearLayout linearLayoutLogo;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    @BindView(R.id.about_activity)
    RelativeLayout relativeLayout;
    FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        initialize();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void initialize() {
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        linearLayoutLogo.setBackground(null);
        relativeLayout.setBackground(null);
        sharedPreferenceManager = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
        linearLayoutLogo.setBackground(null);
        relativeLayout.setBackground(null);
        sharedPreferenceManager = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        context = this;
        initialize();
    }


    private void onActivitySetTheme(int theme) {
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
}
