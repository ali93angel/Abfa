package com.app.leon.abfa.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadByNavigation;
import static com.app.leon.abfa.Infrastructure.DifferentCompanyManager.getEshterakMinLength;

public class NavigationActivity extends AppCompatActivity {
    String theme = "1";
    SharedPreferenceManager sharedPreferenceManager;
    String bill_Id;
    int trackNumber;
    @BindView(R.id.buttonNavigation)
    Button buttonNavigation;
    @BindView(R.id.editTextAccount)
    EditText editTextAccount;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.editTextCell)
    EditText editTextCell;
    @BindView(R.id.editTextCounter)
    EditText editTextCounter;
    @BindView(R.id.editTextAddress)
    EditText editTextAddress;
    @BindView(R.id.editTextHome)
    EditText editTextHome;
    @BindView(R.id.textViewAccount)
    TextView textViewAccount;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;
    @BindView(R.id.textViewCell)
    TextView textViewCell;
    @BindView(R.id.textViewCounter)
    TextView textViewCounter;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewHome)
    TextView textViewHome;
    @BindView(R.id.linearLayoutAccount)
    LinearLayout linearLayoutAccount;
    @BindView(R.id.linearLayoutPhone)
    LinearLayout linearLayoutPhone;
    @BindView(R.id.linearLayoutCell)
    LinearLayout linearLayoutCell;
    @BindView(R.id.linearLayoutCounter)
    LinearLayout linearLayoutCounter;
    @BindView(R.id.linearLayoutAddress)
    LinearLayout linearLayoutAddress;
    @BindView(R.id.linearLayoutHome)
    LinearLayout linearLayoutHome;
    private View viewFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            bill_Id = bundle.getString(BundleEnum.BILL_ID.getValue());
            trackNumber = bundle.getInt(BundleEnum.TRACK_NUMBER.getValue());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void initialize() {
        setEditTextAddressClickListener();
        setEditTextPhoneClickListener();
        setEditTextAccountClickListener();
        setEditTextCellClickListener();
        setEditTextCounterClickListener();
        setEditTextHomeClickListener();
        setButtonNavigationClickListener();
    }

    private boolean checkAreInputsEmpty() {
        return editTextAccount.getText().toString().length() < 1 &&
                editTextPhone.getText().toString().length() < 1 &&
                editTextCell.getText().toString().length() < 1 &&
                editTextCounter.getText().toString().length() < 1 &&
                editTextAddress.getText().toString().length() < 1 &&
                editTextHome.getText().toString().length() < 1;
    }

    void setButtonNavigationClickListener() {
        buttonNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAreInputsEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_empty_all), Toast.LENGTH_SHORT).show();
                } else {
                    Integer home = null;
                    if (editTextHome.getText().toString().length() > 0) {
                        home = Integer.valueOf(editTextHome.getText().toString());
                    }
                    if (editTextPhone.getText().length() < 8) {
                        viewFocus = editTextPhone;
                        viewFocus.requestFocus();
                        editTextPhone.setError(getString(R.string.error_in_format));
                        return;
                    }
                    if (editTextAccount.getText().length() < getEshterakMinLength()) {
                        viewFocus = editTextAccount;
                        viewFocus.requestFocus();
                        editTextAccount.setError(getString(R.string.error_in_format));
                        return;
                    }
                    if (editTextCell.getText().length() < 11) {
                        viewFocus = editTextCell;
                        viewFocus.requestFocus();
                        editTextCell.setError(getString(R.string.error_in_format));
                        return;
                    }
                    updateOnOffLoadByNavigation(bill_Id, trackNumber,
                            editTextCounter.getText().toString(), editTextAddress.getText().toString(),
                            editTextAccount.getText().toString(), editTextCell.getText().toString(),
                            editTextPhone.getText().toString(), home);
                }
            }
        });
    }

    void setEditTextHomeClickListener() {
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

    void setEditTextCounterClickListener() {
        editTextCounter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutCounter.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewCounter.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutCounter.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewCounter.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextCellClickListener() {
        editTextCell.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutCell.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewCell.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutCell.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewCell.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextAccountClickListener() {
        editTextAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutAccount.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewAccount.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutAccount.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewAccount.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextPhoneClickListener() {
        editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayoutPhone.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    textViewPhone.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayoutPhone.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    textViewPhone.setTextColor(getResources().getColor(R.color.black1));
                }
            }
        });
    }

    void setEditTextAddressClickListener() {
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
}
