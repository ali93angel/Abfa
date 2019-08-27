package com.app.leon.abfa.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.abfa.DBService.QeireMojazService;
import com.app.leon.abfa.Fragments.ReadFragment;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.DbTables.QeireMojaz;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.CustomDialog;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.GeoTracker;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.IGeoTracker;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.app.leon.abfa.Utils.TakePhoto;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.app.leon.abfa.Utils.TakePhoto.getOutputMediaFile;

public class ReportForbidActivity extends AppCompatActivity
        implements ICallback<SimpleMessage> {
    Uri file;
    View viewFocus;
    String theme = "1", bill_Id;
    int trackNumber;
    TakePhoto takePhoto;
    IGeoTracker geoTracker;
    Location lastLocation;
    Integer accuracy;
    QeireMojaz qeireMojaz;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.buttonPhoto)
    Button buttonPhoto;
    @BindView(R.id.editTextHome)
    EditText editTextHome;
    @BindView(R.id.editTextAddress)
    EditText editTextAddress;
    @BindView(R.id.editTextAccountNext)
    EditText editTextAccountNext;
    @BindView(R.id.editTextAccountPre)
    EditText editTextAccountPre;
    @BindView(R.id.textViewHome)
    TextView textViewHome;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewAccountNext)
    TextView textViewAccountNext;
    @BindView(R.id.textViewAccountPre)
    TextView textViewAccountPre;
    @BindView(R.id.linearLayoutHome)
    LinearLayout linearLayoutHome;
    @BindView(R.id.linearLayoutAddress)
    LinearLayout linearLayoutAddress;
    @BindView(R.id.linearLayoutAccountNext)
    LinearLayout linearLayoutAccountNext;
    @BindView(R.id.linearLayoutAccountPre)
    LinearLayout linearLayoutAccountPre;
    @BindView(R.id.report_forbid_activity)
    RelativeLayout relativeLayout;
    private SharedPreferenceManager sharedPreferenceManager;
    private Context context;
    private FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_forbid_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StrictMode.VmPolicy.Builder newBuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newBuilder.build());
        context = this;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            bill_Id = bundle.getString(BundleEnum.BILL_ID.getValue());
            trackNumber = bundle.getInt(BundleEnum.TRACK_NUMBER.getValue());
        }
        initialize();
        setGeoTracker();
    }

    private void setGeoTracker() {
        geoTracker = new GeoTracker("viewPager", context);
        if (geoTracker.checkPlayServices()) {
            geoTracker.buildGoogleApiClient();
            geoTracker.createLocationRequest();
        }
        lastLocation = geoTracker.getLastLocation();
        if (lastLocation != null) {
            if (lastLocation.hasAccuracy()) {
                accuracy = Math.round(lastLocation.getAccuracy());
            }
        }
        geoTracker.displayLocation();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GpsEnabled()) {
                    if (editTextAccountPre.getText().length() < 1 && editTextAccountNext.getText().length() < 1
                            && editTextAddress.getText().length() < 1) {
                        if (editTextAccountPre.getText().length() < 1) {
                            viewFocus = editTextAccountPre;
                            viewFocus.requestFocus();
                            editTextAccountPre.setError(getString(R.string.error_empty));
                            return;
                        }
                        if (editTextAccountNext.getText().length() < 1) {
                            viewFocus = editTextAccountNext;
                            viewFocus.requestFocus();
                            editTextAccountNext.setError(getString(R.string.error_empty));
                            return;
                        }
                        if (editTextAddress.getText().length() < 1) {
                            viewFocus = editTextAddress;
                            viewFocus.requestFocus();
                            editTextAddress.setError(getString(R.string.error_empty));
                            return;
                        }
                    }
                    if (editTextHome.getText().length() < 1) {
                        viewFocus = editTextHome;
                        viewFocus.requestFocus();
                        editTextHome.setError(getString(R.string.error_empty));
                        return;
                    }
                    String filePath = "";
                    if (file != null) filePath = file.getPath();
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
                    qeireMojaz = QeireMojazService.addQeireMojaz(1,
                            accuracy,
                            lastLocation.getLatitude(),
                            lastLocation.getLongitude(),
                            filePath, editTextAccountNext.getText().toString(),
                            OffloadStateEnum.INSERTED.getValue(),
                            editTextAddress.getText().toString(),
                            editTextAccountPre.getText().toString(),
                            Integer.valueOf(editTextHome.getText().toString()),
                            ReadFragment.onOffLoad.trackNumber,
                            editTextAddress.getText().toString());
                    attemptReport();
                }
            }
        });
    }

    private void setButtonPhotoOnCLickListener() {
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                TakePhoto.file = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, TakePhoto.file);
                startActivityForResult(intent, 100);
            }
        });
    }

    void initialize() {
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);
        initializeEditText();
        takePhoto = new TakePhoto(buttonPhoto);
        setButtonSubmitOnClickListener();
        setButtonPhotoOnCLickListener();
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

    public boolean GpsEnabled() {
        LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("GPS IS:", enabled + "");
        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("تنظیمات جی پی اس");
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
        return enabled;
    }

    void initializeEditText() {

        setEditTextAccountPreFocusListener();
        setEditTextAccountNextFocusListener();
        setEditTextAddressFocusListener();
        setEditTextHomeFocusListener();
    }

    void setEditTextAccountPreFocusListener() {
        editTextAccountPre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutAccountPre.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewAccountPre.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutAccountPre.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewAccountPre.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextAccountNextFocusListener() {

        editTextAccountNext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutAccountNext.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewAccountNext.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutAccountNext.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewAccountNext.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextAddressFocusListener() {
        editTextAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutAddress.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewAddress.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutAddress.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewAddress.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextHomeFocusListener() {
        editTextHome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutHome.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewHome.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutHome.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewHome.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                file = takePhoto.getFile();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        geoTracker.start();
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        geoTracker.resume();
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
        geoTracker.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        geoTracker.pause();
    }

    void attemptReport() {
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService uploadForbid = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call;
        if (file != null) {
            call = uploadForbid.uploadForbid(
                    "Bearer " + sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue()),
                    editTextAccountPre.getText().toString(), editTextAccountNext.getText().toString(),
                    editTextAddress.getText().toString(), Integer.valueOf(editTextHome.getText().toString()),
                    lastLocation.getLatitude(), lastLocation.getLongitude(), trackNumber,
                    takePhoto.preparePicToSend(file));
        } else {
            call = uploadForbid.uploadForbid(
                    "Bearer " + sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue()),
                    editTextAccountPre.getText().toString(), editTextAccountNext.getText().toString(),
                    editTextAddress.getText().toString(), Integer.valueOf(editTextHome.getText().toString()),
                    lastLocation.getLatitude(), lastLocation.getLongitude(), trackNumber);
        }
        HttpClientWrapper.callHttpAsync(call, ReportForbidActivity.this, this, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        qeireMojaz.setOffLoadState(OffloadStateEnum.SENT.getValue());
        qeireMojaz.save();
        new CustomDialog(DialogType.Green, context,
                simpleMessage.getMessage(),
                getString(R.string.dear_user), getString(R.string.report_forbid), getString(R.string.accepted));
    }
}
