package com.app.leon.abfa.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends DialogFragment {
    @BindView(R.id.spinnerSearch)
    Spinner spinner;
    @BindView(R.id.buttonSearch)
    Button buttonSearch;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.fragmentFrameLayout)
    FrameLayout frameLayout;
    Context context;
    View view, viewFocus;
    String[] items = new String[]{"اشتراک", "شماره پرونده", "شماره بدنه", "شناسه ", "شماره صفحه", "نام"};
    int namePosition = -1, status, length;
    boolean found = false;
    String search;
    List<OnOffLoad> onOffLoads;
    ArrayAdapter<String> adapter;
    Unbinder unbinder;

    public SearchFragment() {
    }

    void setViewPagerItem(int i) {
        ((ReadActivity) (getActivity())).viewPager.setCurrentItem(i);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initialize();
        return view;
    }

    void setButtonSearchOnClickListener() {
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                found = false;
                if (editTextSearch.getText().length() > 0) {
                    status = spinner.getSelectedItemPosition();
                    search = editTextSearch.getText().toString();
                    onOffLoads = new ArrayList<>();
                    onOffLoads = ((ReadActivity) (getActivity())).onOffLoads;
                    length = onOffLoads.size();
                    new Search().execute();
                } else {
                    viewFocus = editTextSearch;
                    viewFocus.requestFocus();
                    editTextSearch.setError(getActivity().getString(R.string.error_empty));
                }
            }
        });
    }

    void initialize() {
        FontManager fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        setupSpinner();
        setSpinnerOnItemSelectedListener();
        setButtonSearchOnClickListener();
    }

    void setSpinnerOnItemSelectedListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 5) {
                    editTextSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    editTextSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                editTextSearch.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void setupSpinner() {
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
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeMedium));
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                ((TextView) v).setWidth(3 * size.x / 5);
                return v;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    class Search extends AsyncTask<String, Integer, Integer> {
        ProgressDialog progressDialog;

        @Override
        protected Integer doInBackground(String... strings) {
            if (status == 0) {
                for (int i = 0; i < length; i++) {
                    if (search.equals(onOffLoads.get(i).eshterak)) {
                        found = true;
                        return i;
                    }
                }
            }
            if (status == 1) {
                for (int i = 0; i < length; i++) {
                    if (search.equals(String.valueOf(onOffLoads.get(i).radif))) {
                        found = true;
                        return i;
                    }
                }
            }
            if (status == 2) {
                for (int i = 0; i < length; i++) {
                    if (search.equals(onOffLoads.get(i).counterSerial)) {
                        found = true;
                        return i;
                    }
                }
            }
            if (status == 3) {
                for (int i = 0; i < length; i++) {
                    if (search.equals(onOffLoads.get(i).billId)) {
                        found = true;
                        return i;
                    }
                }
            }
            if (status == 4) {
                if (length >= Integer.valueOf(search)) {
                    found = true;
                    return (Integer.valueOf(search) - 1);
                }
            }
            if (status == 5) {
                for (int i = namePosition + 1; i < length; i++) {
                    if (!found) {
                        String name = onOffLoads.get(i).name + " " + onOffLoads.get(i).family;
                        if (name.contains(search)) {
                            namePosition = i;
                            found = true;
                            return i;
                        }
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer != null) {
                setViewPagerItem(integer);
                if (status != 5)
                    dismiss();
            }
            progressDialog.dismiss();
            if (!found) {
                Toast.makeText(getActivity(), getString(R.string.not_found), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
