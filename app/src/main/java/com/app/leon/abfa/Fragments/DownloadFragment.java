package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.BuildConfig;
import com.app.leon.abfa.DBService.FillOnOffLoadService;
import com.app.leon.abfa.DBService.ReadingConfigService;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.CounterStateValueKey;
import com.app.leon.abfa.Models.InterCommunation.CrReport;
import com.app.leon.abfa.Models.InterCommunation.HighLowWrapper;
import com.app.leon.abfa.Models.InterCommunation.KarbariWrapper;
import com.app.leon.abfa.Models.InterCommunation.MobileInput;
import com.app.leon.abfa.Models.InterCommunation.MyWorks;
import com.app.leon.abfa.Models.InterCommunation.ReadingConfig;
import com.app.leon.abfa.Models.InterCommunation.ReportValueKey;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Retrofit;

public class DownloadFragment extends BaseFragment {
    Unbinder unbinder;
    Context context;
    View view;
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<MyWorks> myWorks = new ArrayList<MyWorks>();
    ArrayList<ReadingConfig> readingConfigs;
    ArrayList<CounterStateValueKey> counterStateValueKeys = new ArrayList<CounterStateValueKey>();
    ArrayList<ReportValueKey> reportValueKeys = new ArrayList<ReportValueKey>();
    ArrayList<CrReport> reports = new ArrayList<CrReport>();
    KarbariWrapper karbariWrapper;
    HighLowWrapper highLowWrapper;
    @BindView(R.id.buttonDownload)
    Button buttonDownload;
    @BindView(R.id.imageViewLoad)
    ImageView imageViewLoad;
    private int type;
    private int[] imageSrc = {R.drawable.img_reload, R.drawable.img_reload_retry,
            R.drawable.img_reload_off, R.drawable.img_reload_special};

    public static DownloadFragment newInstance(int type) {
        DownloadFragment downloadFragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleEnum.TYPE.getValue(), type);
        downloadFragment.setArguments(bundle);
        return downloadFragment;
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if (getArguments() != null) {
            type = getArguments().getInt(BundleEnum.TYPE.getValue());
        }
        view = inflater.inflate(R.layout.download_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageViewLoad.setImageDrawable(null);
        unbinder.unbind();
        view = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
        sharedPreferenceManager = null;
        view = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public void initialize() {
        context = getActivity();
        imageViewLoad.setImageResource(imageSrc[type - 1]);
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        setOnButtonDownloadClickListener();
    }

    void setOnButtonDownloadClickListener() {
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case 1:
                        download();
                        break;
                    case 2:
                        downloadRetry();
                        break;
                    case 3:
                        downloadOff();
                        break;
                    case 4:
                        downloadSpecially();
                        break;
                }
            }
        });
    }

    void download() {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService getDate = retrofit.create(IAbfaService.class);
        Download download = new Download();
        Call<MobileInput> call = getDate.download(BuildConfig.VERSION_CODE);
        HttpClientWrapper.callHttpAsync(call, download, getActivity(), ProgressType.SHOW.getValue());
    }

    void downloadOff() {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(false, token);
        final IAbfaService getDate = retrofit.create(IAbfaService.class);
        Download download = new Download();
        Call<MobileInput> call = getDate.downloadOff(BuildConfig.VERSION_CODE);
        HttpClientWrapper.callHttpAsync(call, download, getActivity(), ProgressType.SHOW.getValue());
    }

    void downloadSpecially() {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService getDate = retrofit.create(IAbfaService.class);
        Download download = new Download();
        Call<MobileInput> call = getDate.downloadSpecial(BuildConfig.VERSION_CODE);
        HttpClientWrapper.callHttpAsync(call, download, getActivity(), ProgressType.SHOW.getValue());
    }

    void downloadRetry() {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService getDate = retrofit.create(IAbfaService.class);
        Download download = new Download();
        List<com.app.leon.abfa.Models.DbTables.ReadingConfig> readingConfigs =
                ReadingConfigService.getReadingConfigsNoArchive(false);
        List<Integer> trackNumbers = new ArrayList<>();
        for (com.app.leon.abfa.Models.DbTables.ReadingConfig readingConfig : readingConfigs)
            trackNumbers.add(readingConfig.getTrackNumber());
        Call<MobileInput> call = getDate.downloadRetry(trackNumbers);
        HttpClientWrapper.callHttpAsync(call, download, getActivity(), ProgressType.SHOW.getValue());
    }

    public void fillDB(MobileInput mobileInput) {
        myWorks = mobileInput.getMyWorks();
        readingConfigs = mobileInput.getReadingConfigs();
        karbariWrapper = mobileInput.getKarbariWrapper();
        counterStateValueKeys = mobileInput.getCounterStateValueKeys();
        reportValueKeys = mobileInput.getReportValueKeys();
        highLowWrapper = mobileInput.getHighLowWrapper();
        reports = mobileInput.getReports();
        new FillOnOffLoadService(getActivity(), mobileInput).execute();
    }

    class Download
            implements ICallback<MobileInput> {
        @Override
        public void execute(MobileInput mobileInput) {
            fillDB(mobileInput);
        }
    }
}
