package com.app.leon.abfa.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.leon.abfa.DBService.MultimediaService;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.DbTables.MultimediaData;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.Models.Enums.MultimediaTypeEnum;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.CustomDialog;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.ISharedPreferenceManager;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.RecordVoice;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadByDescription;


public class DescriptionActivity extends AppCompatActivity
        implements ICallback<SimpleMessage> {
    final double[] startTime = {0};
    final double[] finalTime = {0};
    final int[] oneTimeOnly = {0};
    ISharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.imageViewR)
    ImageView imageViewRecord;
    @BindView(R.id.imageViewP)
    ImageView imageViewP;
    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.textViewCurrent)
    TextView textViewCurrent;
    @BindView(R.id.editTextMessage)
    EditText editTextMessage;
    @BindView(R.id.seekBar)
    SeekBar seekBarMediaPlayer;
    @BindView(R.id.record_voice_activity)
    RelativeLayout relativeLayoutRecordVoice;
    File file;
    List<MultimediaData> multimediaAudioList = new ArrayList<>();
    MultimediaData multimediaData;
    boolean play = false;
    RecordVoice recordVoice;
    Context context;
    String theme = "1", bill_Id, descriptionText = "";
    int trackNumber;
    boolean hasVoice = false;

    FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        setBundle();
        initialize();
    }

    void setBundle() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            bill_Id = bundle.getString(BundleEnum.BILL_ID.getValue());
            trackNumber = bundle.getInt(BundleEnum.TRACK_NUMBER.getValue());
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setSeekBarChangeListener() {
        seekBarMediaPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                recordVoice.Player.seekTo(progressChangedValue);
                startTime[0] = progressChangedValue;
                if (progressChangedValue == finalTime[0]) {
                    imageViewP.setImageResource(R.drawable.img_play);
                    play = false;
                }
            }
        });
    }

    void initialize() {
        imageViewP.setEnabled(false);
        recordVoice = new RecordVoice(context);
        setImageViewPausePlayClickListener();
        setImageViewRecordClickListener();
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayoutRecordVoice);
        setSeekBarChangeListener();
        setButtonSendClickListener();
        checkMultimediaAndToggle();
    }

    private void checkMultimediaAndToggle() {
        multimediaAudioList = MultimediaService.getMultimediaData(bill_Id, MultimediaTypeEnum.AUDIO.getValue());
        if (multimediaAudioList.size() > 0) {
            MultimediaData multimediaData = multimediaAudioList.get(0);
            recordVoice.FileName = multimediaData.getPath();
            if (!multimediaData.isSent())
                buttonSend.setEnabled(true);
            buttonSend.setEnabled(false);
            imageViewP.setEnabled(true);
            imageViewP.setImageResource(R.drawable.img_play);
            imageViewRecord.setEnabled(false);
        }
    }

    private void setButtonSendClickListener() {
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasVoice)
                    multimediaData = MultimediaService.addMultimedia(Integer.valueOf(MultimediaTypeEnum.AUDIO.getValue()),
                            bill_Id, trackNumber, recordVoice.FileName);
                if (editTextMessage.getText().toString().length() > 0) {
                    descriptionText = editTextMessage.getText().toString();
                    updateOnOffLoadByDescription(bill_Id, trackNumber, descriptionText);
                }
                sendDescription();
            }
        });
    }

    void setImageViewRecordClickListener() {
        imageViewRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                play = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonSend.setEnabled(false);
                    imageViewP.setEnabled(false);
                    recordVoice.onRecord(true);
                    imageViewP.setImageResource(R.drawable.img_play_pause);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    recordVoice.onRecord(false);
                    buttonSend.setEnabled(true);
                    imageViewP.setEnabled(true);
                    imageViewP.setImageResource(R.drawable.img_play);
                    hasVoice = true;
                }
                return true;
            }
        });
    }

    void setImageViewPausePlayClickListener() {
        imageViewP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!play) {
                    recordVoice.onPlay(true);
                    finalTime[0] = recordVoice.Player.getDuration();
                    startTime[0] = recordVoice.Player.getCurrentPosition();
                    if (oneTimeOnly[0] == 0) {
                        seekBarMediaPlayer.setMax((int) finalTime[0]);
                        oneTimeOnly[0] = 1;
                    }
                    final Handler myHandler = new Handler();
                    seekBarMediaPlayer.setProgress((int) startTime[0]);
                    Runnable UpdateSongTime = new Runnable() {
                        public void run() {
                            startTime[0] = recordVoice.Player.getCurrentPosition();
                            textViewCurrent.setText(String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime[0]),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime[0]) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                    toMinutes((long) startTime[0])))
                            );
                            seekBarMediaPlayer.setProgress((int) startTime[0]);
                            myHandler.postDelayed(this, 100);
                            if (startTime[0] == finalTime[0]) {
                                final ImageView imageView = (ImageView) findViewById(R.id.imageViewP);
                                imageView.setImageResource(R.drawable.img_pause);
                                play = false;

                            }
                        }
                    };
                    textViewTotal.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime[0]),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime[0]) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime[0])))
                    );
                    textViewCurrent.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime[0]),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime[0]) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            startTime[0])))
                    );
                    myHandler.postDelayed(UpdateSongTime, 100);

                    imageViewP.setImageResource(R.drawable.img_pause);
                    play = true;
                } else {
//                    recordVoice.Player.pause();
                    recordVoice.onPlay(false);
                    imageViewP.setImageResource(R.drawable.img_play);
                    play = false;
                }
            }
        });
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

    private void sendDescription() {
        if (!hasVoice && descriptionText.length() < 1) {
            return;
        }
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        Retrofit retrofit = NetworkHelper.getInstance(true,
                sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue()).toString());
        final IAbfaService uploadDescription = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call;
        if (recordVoice.getFileName().length() > 0)
            file = new File(recordVoice.getFileName());
        if (hasVoice) {
            if (descriptionText.length() > 0) {
                call = uploadDescription.uploadDescription(trackNumber, bill_Id, descriptionText,
                        RecordVoice.prepareVoiceToSend(file));
            } else {
                call = uploadDescription.uploadDescription(trackNumber, bill_Id,
                        RecordVoice.prepareVoiceToSend(file));
            }
        } else {
            call = uploadDescription.uploadDescription(trackNumber, bill_Id, descriptionText);
        }
        HttpClientWrapper.callHttpAsync(call, DescriptionActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        new CustomDialog(DialogType.Green, context, getString(R.string.description_sent), getString(R.string.dear_user), getString(R.string.sign_description), getString(R.string.accepted));
        if (hasVoice) {
            multimediaData.setSent(true);
            multimediaData.save();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        imageViewP.setImageDrawable(null);
        imageViewRecord.setImageDrawable(null);
        sharedPreferenceManager = null;
        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onStop() {
        super.onStop();
        context = null;
        imageViewP.setImageDrawable(null);
        imageViewRecord.setImageDrawable(null);
        sharedPreferenceManager = null;
        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        context = this;
    }
}
