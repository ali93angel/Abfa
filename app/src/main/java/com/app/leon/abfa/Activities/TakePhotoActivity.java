package com.app.leon.abfa.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.leon.abfa.DBService.MultimediaService;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.DbTables.MultimediaData;
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
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.app.leon.abfa.Utils.TakePhoto;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TakePhotoActivity extends AppCompatActivity
        implements ICallback<SimpleMessage> {
    final double WIDTH = 0.4, HEIGHT = 0.2;
    @BindView(R.id.buttonTakePhoto)
    Button buttonTakePhoto;
    @BindView(R.id.take_photo_activity)
    RelativeLayout relativeLayout;
    ImageView[] imageViews = new ImageView[4];
    Uri file;
    Uri[] files = new Uri[4];
    TakePhoto takePhoto;
    String bill_Id, theme = "1";
    SharedPreferenceManager sharedPreferenceManager;
    int trackNumber, counter = 0;
    List<MultimediaData> multimediaDataS = new ArrayList<>();
    Context context;
    MultimediaData multimediaData;
    private FontManager fontManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setBundle();
        context = this;
        initialize();
    }

    void setBundle() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            bill_Id = bundle.getString("bill_Id");
            trackNumber = bundle.getInt("trackNumber");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void initialize() {
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);

        StrictMode.VmPolicy.Builder newBuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newBuilder.build());
        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);

        takePhoto = new TakePhoto(buttonTakePhoto);

        setButtonTakePhotoOncClickListener();
        initializeImageView();
        setMultimediaData();
    }

    void setButtonTakePhotoOncClickListener() {
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.takePicture(TakePhotoActivity.this);
            }
        });
    }

    void setImageSrc(ImageView imageSrc, Uri uri) {
        Display display = getWindowManager().getDefaultDisplay();
        takePhoto.setImageSrc(display, imageSrc, uri, WIDTH, HEIGHT);
    }

    private void setMultimediaData() {
        multimediaDataS = MultimediaService.getMultimediaData(bill_Id, MultimediaTypeEnum.IMAGE.getValue());
        if (multimediaDataS.size() > 0) {
            for (int i = 0; i < multimediaDataS.size(); i++) {
                MultimediaData multimediaData = multimediaDataS.get(i);
                file = Uri.parse(multimediaData.getPath());
                files[i] = file;
                counter++;
                if (i == 0) {
                    setImageSrc(imageViews[0], file);
                } else if (i == 1) {
                    setImageSrc(imageViews[1], file);
                } else if (i == 2) {
                    setImageSrc(imageViews[2], file);
                } else if (i == 3) {
                    setImageSrc(imageViews[3], file);
                }
            }
            if (multimediaDataS.size() == 1) {
                imageViews[1].setEnabled(true);
            } else if (multimediaDataS.size() == 2) {
                imageViews[2].setEnabled(true);
            } else if (multimediaDataS.size() == 3) {
                imageViews[3].setEnabled(true);
            }
        } else {
            imageViews[0].setEnabled(true);
        }
    }

    void initializeImageView() {
        imageViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.takePicture(TakePhotoActivity.this);
            }
        });
        imageViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.takePicture(TakePhotoActivity.this);
            }
        });
        imageViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.takePicture(TakePhotoActivity.this);
            }
        });

        imageViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto.takePicture(TakePhotoActivity.this);
            }
        });
        imageViews[0].setEnabled(false);
        imageViews[1].setEnabled(false);
        imageViews[2].setEnabled(false);
        imageViews[3].setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                file = takePhoto.getFile();
                files[counter] = file;
                counter++;
                if (counter == 1) {
                    setImageSrc(imageViews[0], file);
                    imageViews[0].setEnabled(false);
                    imageViews[1].setEnabled(true);
                } else if (counter == 2) {
                    setImageSrc(imageViews[1], file);
                    imageViews[1].setEnabled(false);
                    imageViews[1].setEnabled(true);
                } else if (counter == 3) {
                    setImageSrc(imageViews[2], file);
                    imageViews[2].setEnabled(false);
                    imageViews[3].setEnabled(true);
                } else {
                    setImageSrc(imageViews[3], file);
                    buttonTakePhoto.setEnabled(false);
                    imageViews[4].setEnabled(false);
                }
                multimediaData = MultimediaService.addMultimedia(Integer.valueOf(MultimediaTypeEnum.IMAGE.getValue()),
                        bill_Id, trackNumber, file.getPath());
                uploadImage(file);
            }
        }
    }

    void uploadImage(Uri uri) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService uploadImage = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call = uploadImage.uploadImage(trackNumber, bill_Id, takePhoto.preparePicToSend(uri));
        HttpClientWrapper.callHttpAsync(call, TakePhotoActivity.this, this, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        new CustomDialog(DialogType.Green, context,
                "تصویر با موفقیت ارسال ثبت شد",
                getString(R.string.dear_user), "ارسال عکس", "ادامه");
        multimediaData.setSent(true);
        multimediaData.save();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageViews = null;
    }
}
