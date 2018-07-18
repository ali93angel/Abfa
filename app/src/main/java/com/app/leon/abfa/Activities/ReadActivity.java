package com.app.leon.abfa.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.leon.abfa.Adapters.ViewPagerAdapter;
import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.DBService.CounterStateValueKeyService;
import com.app.leon.abfa.DBService.HighLowConfigService;
import com.app.leon.abfa.DBService.KarbariService;
import com.app.leon.abfa.DBService.OnOffLoadService;
import com.app.leon.abfa.DBService.ReadingConfigService;
import com.app.leon.abfa.DBService.ReportService;
import com.app.leon.abfa.Fragments.AreYouSureFragment;
import com.app.leon.abfa.Fragments.ContactUsFragment;
import com.app.leon.abfa.Fragments.SearchFragment;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.DbTables.CounterStateValueKey;
import com.app.leon.abfa.Models.DbTables.HighLowConfig;
import com.app.leon.abfa.Models.DbTables.Karbari;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.Models.DbTables.Report;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.Models.Enums.HighLowStateEnum;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.ReadStatusEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.OffLoadParams;
import com.app.leon.abfa.Models.InterCommunation.UploadReadData;
import com.app.leon.abfa.Models.InterCommunation.UploadReadFeedback;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.CustomDialog;
import com.app.leon.abfa.Utils.FlashLightManager;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.GeoTracker;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.IFlashLightManager;
import com.app.leon.abfa.Utils.IGeoTracker;
import com.app.leon.abfa.Utils.MakeNotification;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.app.leon.abfa.Activities.ReportActivity.selected;
import static com.app.leon.abfa.DBService.ReportService.UpdateReportByState;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.UpdateOnOffLoadSend;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadByCounterNumber;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadByCounterSerialKey;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadWithoutCounterNumber;
import static com.app.leon.abfa.Fragments.ReportTotalFragment.state;

public class ReadActivity extends BaseActivity {
    public static boolean focusOnEditText = true, showSerialBox = false;
    public List<CounterStateValueKey> counterStateValueKeys;
    public List<Karbari> karbaris = new ArrayList<>();
    public List<ReadingConfig> readingConfigs;
    public List<HighLowConfig> highLowConfigs;
    public ArrayList<OnOffLoad> onOffLoads = new ArrayList<>();
    public ArrayAdapter<String> adapter;
    private Context context;
    private List<Integer> spinnerItemSelected = new ArrayList<>();
    private String[] items;
    private List<Report> reports;
    private List<OnOffLoad> registeredOnOffLoads;
    private final int aboveIconCount = 11;
    private int[] imageSrc = new int[aboveIconCount];
    private int[] imageSrcCurrentHighLow;
    private int themeCounter, currentPage = 0;
    private int readStatus = 0;
    private boolean[] currentRead;
    private boolean isFlashOn = false;
    private IFlashLightManager flashLightManager;
    private IGeoTracker geoTracker;
    private Location lastLocation;
    private Integer accuracy;
    private FontManager fontManager;
    private String theme;
    @BindView(R.id.pager)
    public ViewPager viewPager;
    @BindView(R.id.imageViewHighLowState)
    ImageView imageViewHighLowState;
    @BindView(R.id.imageViewOffLoadState)
    ImageView imageViewOffLoadState;
    @BindView(R.id.imageViewReadingType)
    ImageView imageViewReadingType;
    @BindView(R.id.imageViewFlash)
    ImageView imageViewFlash;
    @BindView(R.id.imageViewReverse)
    ImageView imageViewReverse;
    @BindView(R.id.imageViewCamera)
    ImageView imageViewCamera;
    @BindView(R.id.imageViewCheck)
    ImageView imageViewCheck;
    @BindView(R.id.imageViewSearch)
    ImageView imageViewSearch;
    @BindView(R.id.textViewNumber)
    TextView textViewNumber;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        GpsEnabled();
        context = this;
        setTheme();
        setReadStatus();
        setAboveIcons();
        setGeoTracker();
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.read_activity);
        return uiElementInActivity;
    }

    private void setTheme() {
        Bundle extras = getIntent().getExtras();
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        boolean hasStableTheme = sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue());
        boolean hasBundleParams = getIntent().getExtras() != null;
        boolean hasSelfTheme = hasBundleParams && extras.getString(BundleEnum.THEME.getValue()) != null;
        if (hasSelfTheme) {
            theme = extras.getString(BundleEnum.THEME.getValue());
            currentPage = extras.getInt(BundleEnum.CURRENT_PAGE.getValue());
        } else if (hasStableTheme) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        } else {
            theme = "1";
        }
        int themeInt = Integer.valueOf(theme);
        themeCounter = themeInt;
        onActivitySetTheme(themeInt);
    }

    private void setReadStatus() {
        boolean hasExtraParams = getIntent().getExtras() != null;
        Bundle reportBundle = getIntent().getBundleExtra(BundleEnum.READ_STATUS.getValue());
        boolean isFromReports = reportBundle != null;
        if (hasExtraParams && isFromReports) {
            readStatus = reportBundle.getInt(BundleEnum.READ_STATUS.getValue());
        }
    }

    private void setAboveIcons() {
        boolean isThemeDark = theme == "4";
        if (isThemeDark) {
            imageSrc[0] = R.drawable.btn_default_level_;
            imageSrc[1] = R.drawable.btn_low_level_;
            imageSrc[2] = R.drawable.btn_high_level_;
            imageSrc[3] = R.drawable.btn_normal_level_;
            imageSrc[4] = R.drawable.btn_visit_default_;
            imageSrc[5] = R.drawable.btn_visit_;
            imageSrc[6] = R.drawable.btn_writing_;
            imageSrc[7] = R.drawable.btn_successful_default_;
            imageSrc[8] = R.drawable.btn_successful_;
            imageSrc[9] = R.drawable.btn_mistake_;
            imageSrc[10] = R.drawable.btn_failure_;
        } else {
            imageSrc[0] = R.drawable.btn_default_level;
            imageSrc[1] = R.drawable.btn_low_level;
            imageSrc[2] = R.drawable.btn_high_level;
            imageSrc[3] = R.drawable.btn_normal_level;
            imageSrc[4] = R.drawable.btn_visit_default;
            imageSrc[5] = R.drawable.btn_visit;
            imageSrc[6] = R.drawable.btn_writing;
            imageSrc[7] = R.drawable.btn_successful_default;
            imageSrc[8] = R.drawable.btn_successful;
            imageSrc[9] = R.drawable.btn_mistake;
            imageSrc[10] = R.drawable.btn_failure;
        }
    }

    private void setGeoTracker() {
        geoTracker = new GeoTracker("viewPager", context);
        if (geoTracker.checkPlayServices()) {
            geoTracker.buildGoogleApiClient();
            geoTracker.createLocationRequest();
        }
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = this;
        fontManager = new FontManager(getApplicationContext());
        showSerialBox = false;
        flashLightManager = new FlashLightManager(getApplicationContext());
        readingConfigs = ReadingConfigService.getReadingConfigsNoArchive(false);
        counterStateValueKeys = CounterStateValueKeyService.getCounterStateValueKey();
        karbaris = KarbariService.getKarbaris();
        setupSpinner();
        setImageViewSearchClickListener();
        setImageViewReverseClickListener();
        setImageViewCheckClickListener();
        setImageViewFlashClickListener();
        setImageViewCameraClickListener();
        setupViewPager();
        setOnPageChangeListener();
    }

    private void setupSpinner() {
        items = new String[counterStateValueKeys.size()];
        for (int i = 0; i < counterStateValueKeys.size(); i++) {
            items[i] = counterStateValueKeys.get(i).getTitle();
        }
        adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                fontManager.setFont(v);
                ((TextView) v).setTextSize(getResources().getDimension(R.dimen.textSizeMedium));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                fontManager.setFont(v);
                ((TextView) v).setTextSize(getResources().getDimension(R.dimen.textSizeSmall));
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                ((TextView) v).setWidth(4 * size.x / 5);
                return v;
            }
        };
    }

    private void setOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                new Thread(new Runnable() {
                    public void run() {
                        showSerialBox = false;
                        final String number = String.valueOf(position + 1) + "/" + String.valueOf(onOffLoads.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewNumber.setText(number);
                            }
                        });
                        setAboveIconsSrc(position);
                    }
                }).start();
            }

            @Override
            public void onPageSelected(final int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setAboveIconsSrc(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setHighLowImage(position);
                setIsBazdidImage(position);
                setReadStatusImage(position);
            }
        });

    }

    void setReadStatusImage(int position) {
        if (onOffLoads.get(position).offLoadStateId == OffloadStateEnum.INSERTED.getValue()) {
            imageViewOffLoadState.setImageResource(imageSrc[7]);
        } else if (onOffLoads.get(position).offLoadStateId == OffloadStateEnum.REGISTERD.getValue()) {
            imageViewOffLoadState.setImageResource(imageSrc[9]);
        } else if (onOffLoads.get(position).offLoadStateId == OffloadStateEnum.SENT.getValue()) {
            imageViewOffLoadState.setImageResource(imageSrc[8]);
        } else if (onOffLoads.get(position).offLoadStateId == OffloadStateEnum.SENT_WITH_ERROR.getValue()) {
            imageViewOffLoadState.setImageResource(imageSrc[10]);
        }
    }

    void setIsBazdidImage(int position) {
        if (onOffLoads.get(position).isBazdid)
            imageViewReadingType.setImageResource(imageSrc[5]);
        else imageViewReadingType.setImageResource(imageSrc[6]);
    }

    void setHighLowImage(int position) {
        if (!currentRead[viewPager.getCurrentItem()]) {
            if (onOffLoads.get(position).highLowStateId == null)
                imageViewHighLowState.setImageResource(imageSrc[0]);
            else if (onOffLoads.get(position).highLowStateId == HighLowStateEnum.LOW.getValue())
                imageViewHighLowState.setImageResource(imageSrc[1]);
            else if (onOffLoads.get(position).highLowStateId == HighLowStateEnum.HIGH.getValue())
                imageViewHighLowState.setImageResource(imageSrc[2]);
            else if (onOffLoads.get(position).highLowStateId == HighLowStateEnum.NORMAL.getValue())
                imageViewHighLowState.setImageResource(imageSrc[3]);
        } else imageViewHighLowState.setImageResource(imageSrcCurrentHighLow[position]);
    }

    void setupViewPager() {
        new FillReadFragment(this).execute();
    }

    private void setImageViewReverseClickListener() {
        imageViewReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTheme(ReadActivity.this);
            }
        });
    }

    private void setImageViewFlashClickListener() {
        imageViewFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashOn)
                    flashLightManager.turnOff();
                else
                    flashLightManager.turnOn();
                isFlashOn = !isFlashOn;
            }
        });
    }

    private void setImageViewCameraClickListener() {
        imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOffLoads.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), TakePhotoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleEnum.BILL_ID.getValue(), onOffLoads.get(viewPager.getCurrentItem()).billId);
                    bundle.putInt(BundleEnum.TRACK_NUMBER.getValue(), onOffLoads.get(viewPager.getCurrentItem()).trackNumber);
                    intent.putExtra(BundleEnum.DATA.getValue(), bundle);
                    startActivity(intent);
                } else {
                    showNoEshterakFound();
                }
            }
        });
    }

    private void setImageViewCheckClickListener() {
        imageViewCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOffLoads.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), ReadReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleEnum.BILL_ID.getValue(), onOffLoads.get(viewPager.getCurrentItem()).billId);
                    bundle.putInt(BundleEnum.TRACK_NUMBER.getValue(), onOffLoads.get(viewPager.getCurrentItem()).trackNumber);
                    intent.putExtra(BundleEnum.DATA.getValue(), bundle);
                    startActivity(intent);
                } else {
                    showNoEshterakFound();
                }
            }
        });
    }

    private void setImageViewSearchClickListener() {
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOffLoads.size() > 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.show(fragmentManager, getString(R.string.search));
                } else {
                    showNoEshterakFound();
                }
            }
        });
    }

    public void changeTheme(Activity activity) {
        theme = String.valueOf(themeCounter % 4 + 1);
        themeCounter++;
        Intent intent = new Intent(activity, activity.getClass());
        intent.putExtra(BundleEnum.THEME.getValue(), theme);
        intent.putExtra(BundleEnum.CURRENT_PAGE.getValue(), viewPager.getCurrentItem());
        activity.finish();
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater infl = getMenuInflater();
        infl.inflate(R.menu.activity_read_drawer, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            int end = spanString.length();
            spanString.setSpan(new RelativeSizeSpan(1.5f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            fontManager.setFont(spanString);
            item.setTitle(spanString);
        }
        return true;
    }

    private void showNoEshterakFound() {
        new CustomDialog(DialogType.Green, context, getString(R.string.no_eshterak_found),
                getString(R.string.dear_user), getString(R.string.eshterak), getString(R.string.accepted));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (onOffLoads.size() < 1) {
            showNoEshterakFound();
            return false;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent;
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.nav_show_last:
                currentPage = 0;
                viewPager.setCurrentItem(currentPage);
                break;
            case R.id.nav_forbidden:
                intent = new Intent(getApplicationContext(), ReportForbidActivity.class);
                bundle.putString(BundleEnum.BILL_ID.getValue(), onOffLoads.get(viewPager.getCurrentItem()).billId);
                bundle.putInt(BundleEnum.TRACK_NUMBER.getValue(), onOffLoads.get(viewPager.getCurrentItem()).trackNumber);
                intent.putExtra(BundleEnum.DATA.getValue(), bundle);
                startActivity(intent);
                break;
            case R.id.nav_scaling:
                intent = new Intent(getApplicationContext(), NavigationActivity.class);
                bundle.putString(BundleEnum.BILL_ID.getValue(), onOffLoads.get(viewPager.getCurrentItem()).billId);
                bundle.putInt(BundleEnum.TRACK_NUMBER.getValue(), onOffLoads.get(viewPager.getCurrentItem()).trackNumber);
                intent.putExtra(BundleEnum.DATA.getValue(), bundle);
                startActivity(intent);
                break;
            case R.id.nav_comment:
                intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                bundle.putString(BundleEnum.BILL_ID.getValue(), onOffLoads.get(viewPager.getCurrentItem()).billId);
                bundle.putInt(BundleEnum.TRACK_NUMBER.getValue(), onOffLoads.get(viewPager.getCurrentItem()).trackNumber);
                intent.putExtra(BundleEnum.DATA.getValue(), bundle);
                startActivity(intent);
                break;
            case R.id.nav_show_keyboard:
                focusOnEditText = !focusOnEditText;
                if (focusOnEditText) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                break;
            case R.id.nav_connect:
                ContactUsFragment dialogFragment = new ContactUsFragment();
                dialogFragment.show(fragmentManager, getString(R.string.contact_us));
                break;
            case R.id.nav_location:
                intent = new Intent(getApplicationContext(), LocationActivity.class);
                bundle.putString(BundleEnum.ON_OFFLOAD.getValue(), new Gson().toJson(onOffLoads.get(viewPager.getCurrentItem())));
                intent.putExtra(BundleEnum.DATA.getValue(), bundle);

                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readData() {
        if (readStatus == ReadStatusEnum.ALL.getValue()) {
            onOffLoads = (ArrayList<OnOffLoad>) OnOffLoadService.getOnOffLoadRead();
        } else if (readStatus == ReadStatusEnum.UNREAD.getValue()) {
            onOffLoads = (ArrayList<OnOffLoad>) OnOffLoadService.getOnOffLoadReadUnRead();
        } else if (readStatus == ReadStatusEnum.ALL_MANE.getValue()) {
            onOffLoads = (ArrayList<OnOffLoad>) OnOffLoadService.getOnOffLoadAllIsMane(
                    ReportActivity.counterStateValueKeys);
        } else if (readStatus == ReadStatusEnum.CUSTOM_MANE.getValue()) {
            onOffLoads = (ArrayList<OnOffLoad>) OnOffLoadService.getOnOffLoadIsMane((long) selected);
        } else if (readStatus == ReadStatusEnum.STATE.getValue()) {
            onOffLoads = (ArrayList<OnOffLoad>) OnOffLoadService.getOnOffLoadByReadState(state);
        } else if (readStatus == ReadStatusEnum.TRACK_NUMBER.getValue()) {
            onOffLoads = (ArrayList<OnOffLoad>) OnOffLoadService.getOnOffLoadReadUnRead(UploadActivity.trackNumber);
        }
        for (OnOffLoad onOffLoad : onOffLoads) {
            if (onOffLoad.counterStatePosition != null)
                spinnerItemSelected.add(onOffLoad.counterStatePosition);
            else spinnerItemSelected.add(0);
        }
        highLowConfigs = HighLowConfigService.getHighLowConfig();
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
    protected void onStart() {
        super.onStart();
        geoTracker.start();
//        initialize();
        context = this;
        Log.e("start", "Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        geoTracker.resume();
        Log.e("resume", "Activity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        geoTracker.stop();
        Log.e("stop", "Activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageViewReadingType.setImageDrawable(null);
        imageViewReverse.setImageDrawable(null);
        imageViewCheck.setImageDrawable(null);
        imageViewCamera.setImageDrawable(null);
        imageViewFlash.setImageDrawable(null);
        imageViewHighLowState.setImageDrawable(null);
        imageViewOffLoadState.setImageDrawable(null);
        imageViewSearch.setImageDrawable(null);
//        context = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        geoTracker.pause();
        Log.e("pause", "Activity");
    }

    public void attemptSend(String UUID, int src) {
        currentRead[viewPager.getCurrentItem()] = true;
        imageSrcCurrentHighLow[viewPager.getCurrentItem()] = src;
        imageViewHighLowState.setImageResource(src);
        reports = ReportService.getReportByState(OffloadStateEnum.REGISTERD.getValue());
        registeredOnOffLoads = OnOffLoadService.getOnOffLoadByOffLoadState(
                OffloadStateEnum.REGISTERD.getValue());
        List<OffLoadParams> offLoads = new ArrayList<>();
        for (OnOffLoad onOffLoad : registeredOnOffLoads) {
            offLoads.add(onOffLoad.getOffLoad());
        }
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(
                getApplicationContext());
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService uploadRead = retrofit.create(IAbfaService.class);
        UploadRead uploadRead1 = new UploadRead();
        UploadReadData uploadReadData = new UploadReadData(offLoads, reports, UUID, false);
        Call<ArrayList<UploadReadFeedback>> call = uploadRead.uploadRead(uploadReadData);
        HttpClientWrapper.callHttpAsync(call, uploadRead1, context, ProgressType.NOT_SHOW.getValue());
    }

    public void readHandel(ArrayList<UploadReadFeedback> uploadReadFeedback) {
        UpdateOnOffLoadSend(registeredOnOffLoads, OffloadStateEnum.SENT.getValue());
        for (UploadReadFeedback uploadReadFeedback1 : uploadReadFeedback) {
            if (uploadReadFeedback1.isHasError()) {
                for (OnOffLoad onOffLoad : registeredOnOffLoads) {
                    if (onOffLoad.idCusotm.equals(uploadReadFeedback1.getId()))
                        UpdateOnOffLoadSend(onOffLoad, OffloadStateEnum.SENT_WITH_ERROR.getValue());
                }
            }
        }
        UpdateReportByState(reports, OffloadStateEnum.SENT.getValue());
    }

    class UploadRead
            implements ICallback<ArrayList<UploadReadFeedback>> {
        @Override
        public void execute(ArrayList<UploadReadFeedback> uploadReadFeedback) {
            readHandel(uploadReadFeedback);
        }
    }

    public void zeroUse(OnOffLoad onOffLoad, int counterStatePosition, int counterStateCode, int number) {
        MakeNotification.makeVibrate(getApplicationContext());
        MakeNotification.makeRing(getApplicationContext());
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).
                getSupportFragmentManager().beginTransaction();
        AreYouSureFragment areYouSureFragment = AreYouSureFragment.newInstance(onOffLoad, counterStatePosition,
                counterStateCode, number, HighLowStateEnum.ZERO.getValue());
        areYouSureFragment.show(fragmentTransaction, getString(R.string.use_out_of_range));
        //// TODO: Server request in Fragment
    }

    public void lowUse(OnOffLoad onOffLoad, int counterStatePosition, int counterStateCode, int number) {
        MakeNotification.makeVibrate(getApplicationContext());
        MakeNotification.makeRing(getApplicationContext());
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).
                getSupportFragmentManager().beginTransaction();
        AreYouSureFragment areYouSureFragment = AreYouSureFragment.newInstance(onOffLoad, counterStatePosition,
                counterStateCode, number, HighLowStateEnum.LOW.getValue());
        areYouSureFragment.show(fragmentTransaction, getString(R.string.use_out_of_range));
        //// TODO: Server request in Fragment
    }

    public void highUse(OnOffLoad onOffLoad, int counterStatePosition, int counterStateCode, int number) {
        MakeNotification.makeVibrate(getApplicationContext());
        MakeNotification.makeRing(getApplicationContext());
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).
                getSupportFragmentManager().beginTransaction();

        AreYouSureFragment areYouSureFragment = AreYouSureFragment.newInstance(onOffLoad, counterStatePosition,
                counterStateCode, number, HighLowStateEnum.HIGH.getValue());
        areYouSureFragment.show(fragmentTransaction, getString(R.string.use_out_of_range));
        //// TODO: Server request in Fragment
    }

    public void updateOnOffLoadByCounter(OnOffLoad onOffLoad, int counterStatePosition,
                                         int counterStateCode, int number, int state) {
        lastLocation = geoTracker.getLastLocation();
        if (lastLocation != null) {
            if (lastLocation.hasAccuracy()) {
                accuracy = Math.round(lastLocation.getAccuracy());
            } else {
                accuracy = 0;
            }
        } else {
            lastLocation = new Location("");
            lastLocation.setLatitude(0);
            lastLocation.setLongitude(0);
            accuracy = 0;
        }
        updateOnOffLoadByCounterNumber(counterStateCode, counterStatePosition, number, onOffLoad,
                state, accuracy, lastLocation.getLatitude(), lastLocation.getLongitude());
        attemptSend(onOffLoad.idCusotm, imageSrc[1]);
        //// TODO: Server request
        if (viewPager.getCurrentItem() < onOffLoads.size()) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public CounterStateValueKey getCounterStateValueKey(int counterStatePosition) {
        CounterStateValueKey counterStateValueKey = counterStateValueKeys.get(counterStatePosition);
        return counterStateValueKey;
    }

    public void updateOnOffLoadWithoutCounter(OnOffLoad onOffLoad, int counterStatePosition,
                                              int counterStateCode) {
        lastLocation = geoTracker.getLastLocation();
        if (lastLocation != null) {
            if (lastLocation.hasAccuracy()) {
                accuracy = Math.round(lastLocation.getAccuracy());
            } else {
                accuracy = 0;
            }
        } else {
            lastLocation = new Location("");
            lastLocation.setLatitude(0);
            lastLocation.setLongitude(0);
            accuracy = 0;
        }
        updateOnOffLoadWithoutCounterNumber(counterStateCode, counterStatePosition, onOffLoad,
                accuracy, lastLocation.getLatitude(), lastLocation.getLongitude());
        attemptSend(onOffLoad.idCusotm, imageSrc[3]);
        //TODO Server request
        if (viewPager.getCurrentItem() < onOffLoads.size()) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void updateOnOffLoadByCounterSerial(OnOffLoad onOffLoad, int counterStatePosition,
                                               int counterStateCode, String counterSerial) {
        lastLocation = geoTracker.getLastLocation();
        if (lastLocation != null) {
            if (lastLocation.hasAccuracy()) {
                accuracy = Math.round(lastLocation.getAccuracy());
            } else {
                accuracy = 0;
            }
        } else {
            lastLocation = new Location("");
            lastLocation.setLatitude(0);
            lastLocation.setLongitude(0);
            accuracy = 0;
        }
        updateOnOffLoadByCounterSerialKey(counterSerial, counterStateCode, counterStatePosition,
                onOffLoad, accuracy, lastLocation.getLatitude(), lastLocation.getLongitude());
        attemptSend(onOffLoad.idCusotm, imageSrc[3]);
        if (viewPager.getCurrentItem() < onOffLoads.size()) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void canLessThanPre(OnOffLoad onOffLoad, int counterStateCode, int counterStatePosition,
                               int number, String preDate) {
        lastLocation = geoTracker.getLastLocation();
        if (lastLocation != null) {
            if (lastLocation.hasAccuracy()) {
                accuracy = Math.round(lastLocation.getAccuracy());
            } else {
                accuracy = 0;
            }
        } else {
            lastLocation = new Location("");
            lastLocation.setLatitude(0);
            lastLocation.setLongitude(0);
            accuracy = 0;
        }
        updateOnOffLoadByCounterNumber(counterStateCode, counterStatePosition, number,
                onOffLoad, HighLowStateEnum.NORMAL.getValue(), accuracy,
                lastLocation.getLatitude(), lastLocation.getLongitude());

        attemptSend(onOffLoad.idCusotm, imageSrc[3]);
        if (viewPager.getCurrentItem() < onOffLoads.size()) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private class FillReadFragment extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        Context context;

        FillReadFragment(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... s) {
            readData();
            if (onOffLoads != null && onOffLoads.size() > 0) {
                pagerAdapter = new ViewPagerAdapter(context, getSupportFragmentManager(), onOffLoads, spinnerItemSelected);
//                pagerAdapter = new ViewPagerAdapterRead(getSupportFragmentManager(), onOffLoads,
//                        karbaris, adapter, context, readingConfigs);
                currentRead = new boolean[onOffLoads.size()];
                imageSrcCurrentHighLow = new int[onOffLoads.size()];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onOffLoads.get(0).highLowStateId == null)
                            imageViewHighLowState.setImageResource(imageSrc[0]);
                        else if (onOffLoads.get(0).highLowStateId == HighLowStateEnum.LOW.getValue())
                            imageViewHighLowState.setImageResource(imageSrc[1]);
                        else if (onOffLoads.get(0).highLowStateId == HighLowStateEnum.HIGH.getValue())
                            imageViewHighLowState.setImageResource(imageSrc[2]);
                        else if (onOffLoads.get(0).highLowStateId == HighLowStateEnum.NORMAL.getValue())
                            imageViewHighLowState.setImageResource(imageSrc[3]);

                        if (onOffLoads.get(0).isBazdid)
                            imageViewReadingType.setImageResource(imageSrc[5]);
                        else imageViewReadingType.setImageResource(imageSrc[6]);


                        if (onOffLoads.get(0).offLoadStateId == OffloadStateEnum.INSERTED.getValue()) {
                            imageViewOffLoadState.setImageResource(imageSrc[7]);
                        } else if (onOffLoads.get(0).offLoadStateId == OffloadStateEnum.REGISTERD.getValue()) {
                            imageViewOffLoadState.setImageResource(imageSrc[9]);
                        } else if (onOffLoads.get(0).offLoadStateId == OffloadStateEnum.SENT.getValue()) {
                            imageViewOffLoadState.setImageResource(imageSrc[8]);
                        } else if (onOffLoads.get(0).offLoadStateId == OffloadStateEnum.SENT_WITH_ERROR.getValue()) {
                            imageViewOffLoadState.setImageResource(imageSrc[10]);
                        }
                    }
                });
            }
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
            if (onOffLoads.size() > 0) {
                if (pagerAdapter != null)
                    viewPager.setAdapter(pagerAdapter);
                textViewNumber.setText("1/" + String.valueOf(onOffLoads.size()));
//                if (adapter != null)
//                    spinnerRead.setAdapter(adapter);
//
//                if (onOffLoads.get(0).counterStatePosition != null) {
//                    spinnerRead.setSelection(onOffLoads.get(0).counterStatePosition);
//                } else
//                    spinnerRead.setSelection(0);
                boolean b = true;
                for (int i = 0; i < onOffLoads.size(); i++) {
                    OnOffLoad onOffLoad = onOffLoads.get(i);
                    if (b && onOffLoad.counterStateCode == null) {
                        currentPage = i;
                        b = false;
                        i = onOffLoads.size();
                    }
                }
                viewPager.setOffscreenPageLimit(0);
                viewPager.setCurrentItem(currentPage);
//                setSpinnerOnItemSelectedListener();
//                viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            }
//            else {
//                spinnerRead.setVisibility(View.GONE);
//            }
            viewPager.setOffscreenPageLimit(0);
            viewPager.setCurrentItem(currentPage);
            progressDialog.dismiss();
        }
    }

    public void setOnOffloads(OnOffLoad onOffLoad) {
        ((ReadActivity) (context)).onOffLoads.set(
                ((ReadActivity) (context)).viewPager.getCurrentItem(), onOffLoad);
    }

    public int getCurrentPosition() {
        return ((ReadActivity) (context)).viewPager.getCurrentItem();
    }

    private void GpsEnabled() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("GPS IS:", enabled + "");
        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("تنظیمات جی پی اس");
            // Setting Dialog Message
            alertDialog.setMessage("مکان یابی شما غیر فعال است ،آیا مایلید به قسمت تنظیمات مکان یابی منتقل شوید");
            alertDialog.setPositiveButton("تنظیمات", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            alertDialog.show();
        }
    }
}
