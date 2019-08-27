package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.Activities.UploadActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.DBService.OnOffLoadService;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.DbTables.MultimediaData;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.DbTables.QeireMojaz;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.MultimediaTypeEnum;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.ReadStatusEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.Models.InterCommunation.Upload;
import com.app.leon.abfa.Models.InterCommunation.UploadMultimedia;
import com.app.leon.abfa.Models.InterCommunation.UploadReadData;
import com.app.leon.abfa.Models.InterCommunation.UploadReadFeedback;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.RecordVoice;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.app.leon.abfa.Utils.TakePhoto;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.app.leon.abfa.Activities.UploadActivity.trackNumber;
import static com.app.leon.abfa.DBService.MultimediaService.getMultimediaDataUnsent;
import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadReadManeSize;
import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadReadUnseenSize;
import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadReadUnsentSize;
import static com.app.leon.abfa.DBService.QeireMojazService.getQeireMojazUnsent;
import static com.app.leon.abfa.DBService.ReportService.UpdateReportByState;
import static com.app.leon.abfa.DBService.ReportService.getReportByStateTrackNumber;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.UpdateOnOffLoadSend;


public class UploadFragment extends BaseFragment {
    @BindView(R.id.buttonUpload)
    Button buttonUpload;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.imageViewUpload)
    ImageView imageView;
    Unbinder unbinder;
    private Context context;
    private long onOffLoadManeSize;
    private long onOffLoadUnseenSize;
    private View view;
    private SharedPreferenceManager sharedPreferenceManager;
    private String[] items;
    private ArrayAdapter<String> adapter;
    private int position;
    private QeireMojaz qeireMojaz;
    private int type;
    private int[] imageSrc = {R.drawable.img_load_on, R.drawable.img_load_off, R.drawable.img_multimedia};

    public static UploadFragment newInstance(int type) {
        UploadFragment uploadFragment = new UploadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleEnum.TYPE.getValue(), type);
        uploadFragment.setArguments(bundle);
        return uploadFragment;
    }

    public static void setQeireMojaz(QeireMojaz qeireMojaz) {
        qeireMojaz.setOffLoadState(OffloadStateEnum.SENT.getValue());
        qeireMojaz.save();
    }

    public static void setMultimedia(MultimediaData multimediaData) {
        multimediaData.setSent(true);
        multimediaData.save();
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if (getArguments() != null) {
            type = getArguments().getInt(BundleEnum.TYPE.getValue());
        }
        view = inflater.inflate(R.layout.upload_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageView.setImageDrawable(null);
        unbinder.unbind();
        view = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
//        imageView.setImageDrawable(null);
        sharedPreferenceManager = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public void initialize() {
        imageView.setImageResource(imageSrc[type - 1]);
        setButtonUploadClickListener();
        setupSpinner();
        setSpinnerOnItemSelectedListener();
    }

    void setButtonUploadClickListener() {
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferenceManager = new SharedPreferenceManager(getActivity());
                switch (type) {
                    case 1:
                        upload(items[spinner.getSelectedItemPosition()], true);
                        break;
                    case 2:
                        upload(items[spinner.getSelectedItemPosition()], false);
                        break;
                    case 3:
                        uploadMultimedia(items[spinner.getSelectedItemPosition()], true);
                        break;
                }
            }
        });
    }

    void setSpinnerOnItemSelectedListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                trackNumber = items[i];
                position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSpinner() {
        items = new String[((UploadActivity) (getActivity())).readingConfigs.size()];
        int counter = 0;
        for (ReadingConfig readingConfig : ((UploadActivity) (getActivity())).readingConfigs) {
            items[counter] = String.valueOf(readingConfig.getTrackNumber());
            counter++;
        }
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(typeface);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeMedium));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeSmall));
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
//                ((TextView) v).setWidth(4 * size.x / 5);
                return v;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

    }

    private void upload(String trackNumber, boolean mood) {
        onOffLoadUnseenSize = getOnOffLoadReadUnseenSize(getActivity(), trackNumber);
        if (onOffLoadUnseenSize < 1) {
            ((UploadActivity) (getActivity())).onOffLoadSize = getOnOffLoadReadUnsentSize(getActivity(), trackNumber);
            onOffLoadManeSize = getOnOffLoadReadManeSize(getActivity(), trackNumber);
            float alalPercent = Float.valueOf(((UploadActivity) (getActivity())).onOffLoadSize *
                    ((UploadActivity) (getActivity())).readingConfigs.get(position).getAlalPercent() / 100);
            if (onOffLoadManeSize < alalPercent) {
                ((UploadActivity) (getActivity())).multimediaDatas = getMultimediaDataUnsent(trackNumber);
                for (MultimediaData multimediaData : ((UploadActivity) (getActivity())).multimediaDatas) {
                    if (String.valueOf(multimediaData.getMultimediaTypeId()).equals(MultimediaTypeEnum.IMAGE.getValue())) {
                        uploadImage(multimediaData, mood);
                    } else if (String.valueOf(multimediaData.getMultimediaTypeId()).equals(MultimediaTypeEnum.AUDIO.getValue())) {
                        uploadVoice(multimediaData, mood);
                    }
                }
                ((UploadActivity) (getActivity())).qeireMojazs = getQeireMojazUnsent(trackNumber);
                for (QeireMojaz qeireMojaz : ((UploadActivity) (getActivity())).qeireMojazs) {
                    uploadReportForbid(qeireMojaz, mood);
                }
                ((UploadActivity) (getActivity())).onOffLoads = OnOffLoadService.getOnOffLoadByOffLoadUnsent(trackNumber);
                for (OnOffLoad onOffLoad : ((UploadActivity) (getActivity())).onOffLoads) {
                    ((UploadActivity) (getActivity())).offLoads.add(onOffLoad.getOffLoad());
                }
                ((UploadActivity) (getActivity())).reports = getReportByStateTrackNumber(OffloadStateEnum.REGISTERD.getValue(), trackNumber);
                uploadOnOffLoad(mood);
            } else {
                Toast.makeText(getActivity(), getString(R.string.alal_percent), Toast.LENGTH_SHORT).show();
            }
        } else {
            goToRead();
        }
    }

    private void uploadMultimedia(String trackNumber, boolean mood) {
        ((UploadActivity) (getActivity())).multimediaDatas = getMultimediaDataUnsent(trackNumber);
        for (MultimediaData multimediaData : ((UploadActivity) (getActivity())).multimediaDatas) {
            if (String.valueOf(multimediaData.getMultimediaTypeId()).equals(MultimediaTypeEnum.IMAGE.getValue())) {
                uploadImage(multimediaData, mood);
            } else if (String.valueOf(multimediaData.getMultimediaTypeId()).equals(MultimediaTypeEnum.AUDIO.getValue())) {
                uploadVoice(multimediaData, mood);
            }
        }
    }

    void goToRead() {
        Toast.makeText(getActivity(),
                "همکار گرامی تعداد " + onOffLoadUnseenSize + " اشتراک قرائت نشده است",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ReadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("readStatus", ReadStatusEnum.TRACK_NUMBER.getValue());
        intent.putExtra("readStatus", bundle);
        startActivity(intent);
    }

    void uploadImage(MultimediaData multimediaData, boolean mood) {
        Uri uri = Uri.fromFile(new File(multimediaData.getPath()));
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(mood, token);
        final IAbfaService uploadImage = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call = uploadImage.uploadImage(multimediaData.getTrackNumber(),
                multimediaData.getBillId(), new TakePhoto(buttonUpload).preparePicToSend(uri));
        UploadMultimedia upload = new UploadMultimedia(multimediaData);
        HttpClientWrapper.callHttpAsync(call, upload, getActivity(), ProgressType.SHOW.getValue());
    }

    void uploadReportForbid(QeireMojaz qeireMojaz, boolean mood) {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(mood, token);
        final IAbfaService uploadForbid = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call;
        if (qeireMojaz.getImagePath().length() > 0) {
            Uri uri = Uri.fromFile(new File(qeireMojaz.getImagePath()));
            call = uploadForbid.uploadForbid(
                    "Bearer " + sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue()),
                    qeireMojaz.getPreEshterak(), qeireMojaz.getNextEshterak(), qeireMojaz.getAddress(),
                    Integer.valueOf(qeireMojaz.getTedadVahed()), qeireMojaz.getLatitude(),
                    qeireMojaz.getLongitude(), qeireMojaz.getTrackNumber(), new TakePhoto(buttonUpload).preparePicToSend(uri));
        } else {
            call = uploadForbid.uploadForbid(
                    "Bearer " + sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue()),
                    qeireMojaz.getPreEshterak(), qeireMojaz.getNextEshterak(), qeireMojaz.getAddress(),
                    Integer.valueOf(qeireMojaz.getTedadVahed()), qeireMojaz.getLatitude(),
                    qeireMojaz.getLongitude(), qeireMojaz.getTrackNumber());
        }
        this.qeireMojaz = qeireMojaz;
        Upload upload = new Upload(qeireMojaz);
        HttpClientWrapper.callHttpAsync(call, upload, getActivity(), ProgressType.SHOW.getValue());
    }

    void uploadVoice(MultimediaData multimediaData, boolean mood) {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(mood, token);
        final IAbfaService uploadDescription = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call;
        File file = new File(multimediaData.getPath());
        call = uploadDescription.uploadDescription(multimediaData.getTrackNumber(),
                multimediaData.getBillId(), RecordVoice.prepareVoiceToSend(file));
        UploadMultimedia upload = new UploadMultimedia(multimediaData);
        HttpClientWrapper.callHttpAsync(call, upload, getActivity(), ProgressType.SHOW.getValue());

    }

    public void uploadOnOffLoad(boolean mood) {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(mood, token);
        final IAbfaService uploadRead = retrofit.create(IAbfaService.class);
        if (((UploadActivity) (getActivity())).onOffLoads.size() > 0) {
            UploadRead uploadRead1 = new UploadRead();
            UploadReadData uploadReadData = new UploadReadData(((UploadActivity) (getActivity())).offLoads,
                    ((UploadActivity) (getActivity())).reports,
                    ((UploadActivity) (getActivity())).onOffLoads.get(0).idCusotm, true);
            Call<ArrayList<UploadReadFeedback>> call = uploadRead.uploadRead(uploadReadData);
            HttpClientWrapper.callHttpAsync(call, uploadRead1, getActivity(), ProgressType.SHOW.getValue());
        } else {
            UploadEmptyBody uploadEmptyBody = new UploadEmptyBody();
            Call<SimpleMessage> call = uploadRead.uploadEmpty(((UploadActivity) (getActivity())).trackNumber);
            HttpClientWrapper.callHttpAsync(call, uploadEmptyBody, getActivity(), ProgressType.SHOW.getValue());
        }
    }

    public void readHandel(ArrayList<UploadReadFeedback> uploadReadFeedback) {
        UpdateOnOffLoadSend(((UploadActivity) (getActivity())).onOffLoads, OffloadStateEnum.SENT.getValue());
        for (UploadReadFeedback uploadReadFeedback1 : uploadReadFeedback) {
            if (uploadReadFeedback1.isHasError()) {
                for (OnOffLoad onOffLoad : ((UploadActivity) (getActivity())).onOffLoads) {
                    Log.e(onOffLoad.idCusotm, uploadReadFeedback1.getId());
                    if (onOffLoad.idCusotm == uploadReadFeedback1.getId())
                        UpdateOnOffLoadSend(onOffLoad, OffloadStateEnum.SENT_WITH_ERROR.getValue());
                }
            }
        }
        UpdateReportByState(((UploadActivity) (getActivity())).reports, OffloadStateEnum.SENT.getValue());
    }

    public void showMessage(SimpleMessage simpleMessage) {
        Toast.makeText(getActivity(), simpleMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }

    class UploadRead
            implements ICallback<ArrayList<UploadReadFeedback>> {
        @Override
        public void execute(ArrayList<UploadReadFeedback> uploadReadFeedback) {
            readHandel(uploadReadFeedback);
        }
    }

    class UploadEmptyBody
            implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            showMessage(simpleMessage);
        }
    }
}
