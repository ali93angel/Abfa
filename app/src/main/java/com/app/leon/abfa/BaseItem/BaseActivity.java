package com.app.leon.abfa.BaseItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.leon.abfa.Activities.AboutActivity;
import com.app.leon.abfa.Activities.BillActivity;
import com.app.leon.abfa.Activities.DownloadActivity;
import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.Activities.ReadSettingActivity;
import com.app.leon.abfa.Activities.ReportActivity;
import com.app.leon.abfa.Activities.SettingActivity;
import com.app.leon.abfa.Activities.UploadActivity;
import com.app.leon.abfa.Adapters.NavigationCustomAdapter;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Typeface typeface;
    public DrawerLayout drawer;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    NavigationCustomAdapter adapter;
    List<NavigationCustomAdapter.DrawerItem> dataList;
    private ListView drawerList;
    private UiElementInActivity uiElementInActivity;
    private FontManager fontManager;

    public void setFont(ViewGroup viewTree, Context context) {
        initializeTypeface();
        Stack<ViewGroup> stackOfViewGroup = new Stack<ViewGroup>();
        stackOfViewGroup.push(viewTree);
        while (!stackOfViewGroup.isEmpty()) {
            ViewGroup tree = stackOfViewGroup.pop();
            for (int i = 0; i < tree.getChildCount(); i++) {
                View child = tree.getChildAt(i);

                if (child instanceof ViewGroup) {
                    stackOfViewGroup.push((ViewGroup) child);
                } else if (child instanceof Button) {
                    ((Button) child).setTypeface(typeface);
                } else if (child instanceof EditText) {
                    ((EditText) child).setTypeface(typeface);
                } else if (child instanceof TextView) {
                    ((TextView) child).setTypeface(typeface);
                } else if (child instanceof ListView) {
                    TextView textView = (TextView) ((ListView) child).getChildAt(0);
                    textView.setTypeface(typeface);
                    textView = (TextView) ((ListView) child).getChildAt(2);
                    textView.setTypeface(typeface);
                }
            }
        }
    }

    void initializeTypeface() {
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/BYekan_3.ttf");
    }

    protected abstract UiElementInActivity getUiElementsInActivity();

    protected abstract void initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        uiElementInActivity = getUiElementsInActivity();
        overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
        setContentView(uiElementInActivity.getContentViewId());
        initializeBase();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        initialize();
    }

    public void onActivitySetTheme(int theme) {
        if (theme == 1) {
            setTheme(R.style.AppTheme_NoActionBar);
        } else if (theme == 2) {
            setTheme(R.style.AppTheme_GreenBlue_NoActionBar);
        } else if (theme == 3) {
            setTheme(R.style.AppTheme_Indigo_NoActionBar);
        } else if (theme == 4) {
            setTheme(R.style.AppTheme_DarkGrey_NoActionBar);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    void setOnDrawerItemClick() {
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {
//                setItemsColor(drawer, position);
                if (position != 0) {
                    for (int i = 0; i < drawerList.getChildCount(); i++) {
                        if (position == i) {
//                            drawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.red2));
                        } else {
//                            drawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                }
                if (position == 1) {
                    Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), ReadActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 3) {
                    Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 4) {
                    Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 5) {
                    Intent intent = new Intent(getApplicationContext(), ReadSettingActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 6) {
                    Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
//                    finish();
                } else if (position == 7) {
                    Intent intent = new Intent(getApplicationContext(), BillActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 8) {
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 9) {
                    finishAffinity();
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    protected void setMenuBackground() {
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                    try {
                        // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView(name, null, attrs);
                        // Kind of apply our own background
                        new Handler().post(new Runnable() {
                            public void run() {
                                view.setBackgroundResource(R.color.black);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                    } catch (ClassNotFoundException e) {
                    }
                }
                return null;
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeBase() {
        initializeTypeface();
        ButterKnife.bind(this);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        dataList = new ArrayList<>();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        fillDrawerListView();
        setOnDrawerItemClick();
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(this.drawer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataList = null;
        adapter = null;
        typeface = null;
    }

    void fillDrawerListView() {
        dataList.add(new NavigationCustomAdapter.DrawerItem("", R.drawable.img_menu_logo));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.download), R.drawable.img_download_information));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.read), R.drawable.img_readings));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.upload), R.drawable.img_data_unloading));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.reading_report), R.drawable.img_my_reading_report));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.reading_setting), R.drawable.img_readout_settings));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.help), R.drawable.img_help));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.bill), R.drawable.img_temporary1));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.setting), R.drawable.img_app_settings));
        dataList.add(new NavigationCustomAdapter.DrawerItem(getString(R.string.exit), R.drawable.img_exit));
        adapter = new NavigationCustomAdapter(this, R.layout.item_navigation, dataList);
        drawerList.setAdapter(adapter);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
