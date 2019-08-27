package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Infrastructure.Counting;
import com.app.leon.abfa.Models.DbTables.CounterStateValueKey;
import com.app.leon.abfa.Models.DbTables.Karbari;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.HighLowStateEnum;
import com.app.leon.abfa.Models.ViewModels.SimpleErrorViewModel;
import com.app.leon.abfa.R;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReadActivity.focusOnEditText;
import static com.app.leon.abfa.Activities.ReadActivity.showSerialBox;

public class ReadFragment extends BaseFragment {
    public static OnOffLoad onOffLoad;
    Context context;
    int counterStatePosition;
    int counterStateCode;
    int number;
    View view, viewFocus;
    @BindView(R.id.editTextNumber)
    EditText editTextNumber;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.textViewPreNumber)
    TextView textViewPreNumber;
    @BindView(R.id.textViewPreDate)
    TextView textViewPreDate;
    @BindView(R.id.textViewBranch)
    TextView textViewBranch;
    @BindView(R.id.textViewSiphon)
    TextView textViewSiphon;
    @BindView(R.id.textViewAhadForosh)
    TextView textViewAhadForosh;
    @BindView(R.id.textViewAhadMasraf)
    TextView textViewAhadMasraf;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewSerial)
    TextView textViewSerial;
    @BindView(R.id.textViewCode)
    TextView textViewCode;
    @BindView(R.id.textViewKarbari)
    TextView textViewKarbari;
    @BindView(R.id.textViewAhadAsli)
    TextView textViewAhadAsli;
    @BindView(R.id.textViewRadif)
    TextView textViewRadif;
    @BindView(R.id.expandableTextViewAddress)
    ExpandableTextView expandableTextView;
    boolean canEmpty = false;
    int pagePosition, spinnerPosition;
    Unbinder unbinder;

    public static ReadFragment newInstance(OnOffLoad onOffLoad, int position, int spinnerPosition) {
        ReadFragment readFragment = new ReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleEnum.ON_OFFLOAD.getValue(), new Gson().toJson(onOffLoad));
        bundle.putInt(BundleEnum.POSITION.getValue(), position);
        bundle.putInt(BundleEnum.SPINNER_POSITION.getValue(), spinnerPosition);
        readFragment.setArguments(bundle);
        return readFragment;
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.read_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            String jsonBundle = getArguments().getString(BundleEnum.ON_OFFLOAD.getValue());
            onOffLoad = new Gson().fromJson(jsonBundle, OnOffLoad.class);
            pagePosition = getArguments().getInt(BundleEnum.POSITION.getValue());
            spinnerPosition = getArguments().getInt(BundleEnum.SPINNER_POSITION.getValue());
        }
        return view;
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

    @Override
    public void initialize() {
        context = getActivity();
        showSerialBox = false;
        initializeTextView();
        viewFocus = editTextNumber;
        viewFocus.requestFocus();
        if (focusOnEditText) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            viewFocus = editTextNumber;
            viewFocus.requestFocus();
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        setupSpinner();
        setSpinnerOnItemSelectedListener();
        editTextNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editTextNumber.setText("");
                return false;
            }
        });
        setupSpinner();
        setSpinnerOnItemSelectedListener();
        setButtonSubmitOnClickListener();
    }

    private void initializeTextView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onOffLoad.preNumber != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewPreNumber.setText(String.valueOf(onOffLoad.preNumber));
                        }
                    });
                }
                final String date = onOffLoad.preDate.substring(0, 2) + "/" + onOffLoad.preDate.substring(2, 4) + "/" + onOffLoad.preDate.substring(4, 6);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewPreDate.setText(date);
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewBranch.setText(onOffLoad.getQotrCustom());
                        textViewSiphon.setText(onOffLoad.getSifoonQotrCustom());
                        textViewName.setText(onOffLoad.name.trim() + " " + onOffLoad.family.trim());
                        expandableTextView.setText(onOffLoad.address);
                        textViewSerial.setText(onOffLoad.counterSerial);
                        textViewRadif.setText(String.valueOf(onOffLoad.radif));
                    }
                });
                if (onOffLoad.tedadNonMaskooni != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewAhadForosh.setText(String.valueOf(onOffLoad.tedadNonMaskooni));
                        }
                    });
                }
                for (final Karbari karbari : ((ReadActivity) (getActivity())).karbaris) {
                    if (onOffLoad.karbariCode != null && onOffLoad.karbariCode == karbari.getIdCustom()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewKarbari.setText(karbari.getTitle());
                            }
                        });
                    }
                }
                if (onOffLoad.tedadMaskooni != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewAhadAsli.setText(String.valueOf(onOffLoad.tedadMaskooni));
                        }
                    });
                }
                if (onOffLoad.ahadMasraf != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewAhadMasraf.setText(String.valueOf(onOffLoad.ahadMasraf));
                        }
                    });
                }
                for (ReadingConfig readingConfig : ((ReadActivity) (getActivity())).readingConfigs) {
                    if (readingConfig.getTrackNumber() == onOffLoad.trackNumber) {
                        if (readingConfig.isOnQeraatCode()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewCode.setText(onOffLoad.qeraatCode);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewCode.setText(onOffLoad.eshterak);
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("avg pre", String.valueOf(onOffLoad.preNumber));
                if (!canEmpty) {
                    canNotEmpty();
                } else {
                    canEmpty();
                }
            }
        });
    }

    private SimpleErrorViewModel isModelValid(Integer currentNumber, Integer preNumber, CounterStateValueKey counterStateValueKey) {
        SimpleErrorViewModel simpleErrorViewModel = new SimpleErrorViewModel();
        simpleErrorViewModel.isValid = true;
        String counterNumberString = editTextNumber.getText().toString();
        if (counterStateValueKey.isMane() && counterNumberString.length() > 0) {
            //todo can not enter number
            simpleErrorViewModel.isValid = false;
        }
        number = Integer.valueOf(counterNumberString);
        if (editTextNumber.getText().length() < 1 && counterStateValueKey.shouldEnterNumber()) {
            simpleErrorViewModel.errorMessage = getString(R.string.counter_empty);
            simpleErrorViewModel.isValid = false;
        }
        if (!counterStateValueKey.isCanNumberBeLessThanPre() && currentNumber < preNumber) {
            simpleErrorViewModel.isValid = false;
            simpleErrorViewModel.errorMessage = getString(R.string.less_than_pre);
        }
        return simpleErrorViewModel;
    }

    private void displayErrorMessage(SimpleErrorViewModel simpleErrorViewModel) {
        viewFocus = editTextNumber;
        view.requestFocus();
        editTextNumber.setError(simpleErrorViewModel.errorMessage);
    }

    void canNotEmpty() {
        if (editTextNumber.getText().length() > 0) {
            noEmpty();
        } else {
            viewFocus = editTextNumber;
            view.requestFocus();
            editTextNumber.setError(getString(R.string.counter_empty));
        }
    }

    void canEmpty() {
        if (editTextNumber.getText().length() > 0) {
            number = Integer.valueOf(editTextNumber.getText().toString());
            if (onOffLoad.preNumber == number) {
                ((ReadActivity) (getActivity())).zeroUse(onOffLoad, counterStatePosition, counterStateCode, number);
            } else {
                ((ReadActivity) (getActivity())).updateOnOffLoadByCounter(onOffLoad, counterStatePosition,
                        counterStateCode, number, HighLowStateEnum.NORMAL.getValue());
            }
        } else {
            ((ReadActivity) (getActivity())).updateOnOffLoadWithoutCounter(onOffLoad,
                    counterStatePosition, counterStateCode);
        }
    }

    void noEmpty() {
        number = Integer.valueOf(editTextNumber.getText().toString());
        if (canLessThanPre()) {
            ((ReadActivity) (getActivity())).canLessThanPre(onOffLoad, counterStateCode,
                    counterStatePosition, number, textViewPreDate.getText().toString());
        } else {
            if (onOffLoad.preNumber > number) {
                viewFocus = editTextNumber;
                view.requestFocus();
                editTextNumber.setError(getString(R.string.less_than_pre));
            } else if (onOffLoad.preNumber == number) {
                ((ReadActivity) (getActivity())).zeroUse(onOffLoad, counterStatePosition, counterStateCode, number);
            } else {
                canNotLessThanPre();
            }
        }
    }

    boolean canLessThanPre() {
        return ((ReadActivity) (getActivity())).counterStateValueKeys.get(
                spinner.getSelectedItemPosition()).isCanNumberBeLessThanPre();
    }

    void canNotLessThanPre() {
        int status = Counting.highLowCounter(number, onOffLoad,
                textViewPreDate.getText().toString(), ((ReadActivity) (getActivity())).highLowConfigs);
        if (status == 0) {
            ((ReadActivity) (getActivity())).updateOnOffLoadByCounter(onOffLoad, counterStatePosition,
                    counterStateCode, number, HighLowStateEnum.NORMAL.getValue());
        } else if (status > 0) {
            ((ReadActivity) (getActivity())).highUse(onOffLoad, counterStatePosition, counterStateCode, number);
        } else if (status < 0) {
            ((ReadActivity) (getActivity())).lowUse(onOffLoad, counterStatePosition, counterStateCode, number);
        }
    }

    void setupSpinner() {
        spinner.setAdapter(((ReadActivity) (getActivity())).adapter);
        spinner.setSelection(spinnerPosition);
    }

    private void setSpinnerOnItemSelectedListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (onOffLoad.counterStatePosition != null && onOffLoad.counterStatePosition == spinner.getSelectedItemPosition()) {
                    return;
                }
                if (((ReadActivity) (getActivity())).viewPager.getCurrentItem() != pagePosition) {
                    return;
                }
                counterStatePosition = i;
                CounterStateValueKey counterStateValueKey = getCounterStateValueKey(i);
                counterStateCode = counterStateValueKey.getIdCustom();
                if (counterStateValueKey.canEnterNumber() || counterStateValueKey.shouldEnterNumber()) {
                    editTextNumber.setEnabled(true);
                } else {
                    editTextNumber.setEnabled(false);
                }
                canEmpty = !counterStateValueKey.shouldEnterNumber();
                if (counterStateValueKey.isXarab() || counterStateValueKey.isTavizi()) {
                    SerialFragment serialFragment = SerialFragment.newInstance(
                            onOffLoad, counterStatePosition, counterStateCode);
                    serialFragment.show(getFragmentManager(), "سریال کنتور");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private CounterStateValueKey getCounterStateValueKey(int counterStatePosition) {
        CounterStateValueKey counterStateValueKey = ((ReadActivity) (getActivity())).getCounterStateValueKey(counterStatePosition);
        return counterStateValueKey;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
