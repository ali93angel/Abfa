package com.app.leon.abfa.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.abfa.Activities.SettingActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.BuildConfig;
import com.app.leon.abfa.Infrastructure.DifferentCompanyManager;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.AppInfo;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SettingUpdateFragment extends BaseFragment {
    Context context;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewVersion)
    TextView textViewVersion;
    @BindView(R.id.textViewSize)
    TextView textViewSize;
    @BindView(R.id.textViewPossibility)
    TextView textViewPossibility;
    @BindView(R.id.buttonReceive)
    Button buttonReceive;
    ProgressDialog progressDialogDownloading;
    View view;
    SharedPreferenceManager sharedPreferenceManager;
    Unbinder unbinder;

    class ApkInfoFeedBack implements ICallback<AppInfo> {
        @Override
        public void execute(AppInfo appInfo) {
            setTextView(appInfo);
        }
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_update_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initialize() {
        context = getActivity();
        progressDialogDownloading = new ProgressDialog(getActivity());
        progressDialogDownloading.setCancelable(false);
        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService appInfo = retrofit.create(IAbfaService.class);
        Call<AppInfo> call = appInfo.getLastApkInfo();
        ApkInfoFeedBack apkInfoFeedBack = new ApkInfoFeedBack();
        HttpClientWrapper.callHttpAsync(call, apkInfoFeedBack, getActivity(), ProgressType.SHOW_CANCELABLE.getValue());
        buttonReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchVisibility();
                doUpdateApp();
            }
        });
    }

    private void switchVisibility() {
        if (!progressDialogDownloading.isShowing()) {
            progressDialogDownloading.show();
            buttonReceive.setVisibility(View.GONE);
        } else {
            progressDialogDownloading.cancel();
            buttonReceive.setVisibility(View.VISIBLE);
        }
    }

    private void doUpdateApp() {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService apkDownloader = retrofit.create(IAbfaService.class);
        Call<ResponseBody> call = apkDownloader.getNewestAppVersion(BuildConfig.VERSION_CODE);
        ApkDownload apkDownload = new ApkDownload();
        HttpClientWrapper.callHttpAsync(call, apkDownload, getActivity(), ProgressType.SHOW.getValue());
    }

    void setTextView(AppInfo appInfo) {
        textViewDate.setText(appInfo.getInsertDateJalali());
        textViewPossibility.setText(appInfo.getDescription());
        float size = Integer.parseInt(appInfo.getFileSizeBytes());
        size = size / (1024 * 1024);
        String size_ = String.valueOf(size);
        size_ = size_.substring(0, size_.indexOf('.') + 2);
        textViewSize.setText(size_ + "مگابایت");
        textViewVersion.setText(appInfo.getVersionName());
    }

    class ApkDownload
            implements ICallback<ResponseBody> {
        @Override
        public void execute(ResponseBody responseBody) {
            writeResponseBodyToDisk(responseBody);
        }
    }

    boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            String fileName = DifferentCompanyManager.getActiveCompanyName().toString();
            File futureStudioIconFile = new File(root + "/Download" + File.separator + fileName + ".apk");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[7168];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    progressDialogDownloading.setMessage(".apk file :" + "file download: " +
                            String.valueOf(fileSizeDownloaded) + " of " + String.valueOf(fileSize));
                    Log.d(".apk file", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                progressDialogDownloading.dismiss();
                Toast.makeText(context, getString(R.string.file_downloaded), Toast.LENGTH_SHORT).show();
                SettingActivity.runFile();

                return true;
            } catch (IOException e) {
                progressDialogDownloading.dismiss();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            progressDialogDownloading.dismiss();
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }
}
