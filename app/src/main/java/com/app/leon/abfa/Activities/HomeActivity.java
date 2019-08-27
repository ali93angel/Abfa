package com.app.leon.abfa.Activities;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.abfa.BaseItem.BaseActivity;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.ViewModels.UiElementInActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.CustomDialog;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getActiveCompanyName;

public class HomeActivity extends BaseActivity {
    ISharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.imageViewDownload)
    ImageView imageViewDownload;
    @BindView(R.id.imageViewHelp)
    ImageView imageViewHelp;
    @BindView(R.id.imageViewReadSetting)
    ImageView imageViewReadSetting;
    @BindView(R.id.imageViewReadReport)
    ImageView imageViewReadReport;
    @BindView(R.id.imageViewBill)
    ImageView imageViewBill;
    @BindView(R.id.imageViewAppSetting)
    ImageView imageViewAppSetting;
    @BindView(R.id.imageViewExit)
    ImageView imageViewExit;
    @BindView(R.id.imageViewUpload)
    ImageView imageViewUpload;
    @BindView(R.id.imageViewRead)
    ImageView imageViewRead;
    @BindView(R.id.textViewFooter)
    TextView textViewFooter;
    String theme = "1";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageViewDownload.setImageDrawable(null);
        imageViewHelp.setImageDrawable(null);
        imageViewReadSetting.setImageDrawable(null);
        imageViewReadReport.setImageDrawable(null);
        imageViewBill.setImageDrawable(null);
        imageViewAppSetting.setImageDrawable(null);
        imageViewExit.setImageDrawable(null);
        imageViewUpload.setImageDrawable(null);
        imageViewRead.setImageDrawable(null);
        sharedPreferenceManager = null;
    }

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.home_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        manage_M_permissions();
        initializeImageViewListener();
        textViewFooter.setText(DifferentCompanyManager.getCompanyName(getActiveCompanyName()));
    }

    void initializeImageViewListener() {
        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        imageViewReadSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewReadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewAppSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BillActivity.class);
                startActivity(intent);
            }
        });
        imageViewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    public void manage_M_permissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "مجوز ها داده شده", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "مجوز رد شد \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                forceClose();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("جهت استفاده بهتر از برنامه مجوز های پیشنهادی را قبول فرمایید")
                .setDeniedMessage("در صورت رد این مجوز قادر با استفاده از این دستگاه نخواهید بود" + "\n" +
                        "لطفا با فشار دادن دکمه" + " " + "اعطای دسترسی" + " " + "و سپس در بخش " + " دسترسی ها" + " " + " با این مجوز هاموافقت نمایید")
                .setGotoSettingButtonText("اعطای دسترسی")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CALL_PHONE
                        //,Manifest.permission.READ_LOGS
                )
                .check();
    }

    private void forceClose() {
        new CustomDialog(DialogType.Red, getApplicationContext(),
                getApplicationContext().getString(R.string.permission_not_completed),
                getApplicationContext().getString(R.string.dear_user),
                getApplicationContext().getString(R.string.call_operator),
                getApplicationContext().getString(R.string.force_close));
        finishAffinity();
    }

    public boolean CheckIsNotEmpty() {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(
                getApplicationContext());
        if (!sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.USERNAME.getValue()))
            return false;
        else if (!sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.PASSWORD.getValue()))
            return false;
        else if (!sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.TOKEN.getValue()))
            return false;
        else
            return sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.REFRESH_TOKEN.getValue());
    }

}
