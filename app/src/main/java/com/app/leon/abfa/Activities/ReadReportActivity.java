package com.app.leon.abfa.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.app.leon.abfa.DBService.ReportService;
import com.app.leon.abfa.DBService.ReportValueKeyService;
import com.app.leon.abfa.DBService.UpdateOnOffLoad;
import com.app.leon.abfa.Fragments.AhadFragment;
import com.app.leon.abfa.Fragments.KarbariGroupFragment;
import com.app.leon.abfa.Fragments.TaviziFragment;
import com.app.leon.abfa.Models.DbTables.Report;
import com.app.leon.abfa.Models.DbTables.ReportValueKey;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadReportActivity extends AppCompatActivity {
    static List<ReportValueKey> reportValueKeys;
    static String bill_Id;
    static int trackNumber;
    @BindView(R.id.listViewReports)
    ListView listViewReports;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.reports_activity)
    RelativeLayout relativeLayout;
    FontManager fontManager;
    private String[] listItems;
    private SharedPreferenceManager sharedPreferenceManager;
    private String theme = "1";
    private List<Report> reports;

    public static void UpdateOnOffLoadByKarbari(int karbari) {
        UpdateOnOffLoad.updateOnOffLoadByKarbari(karbari, bill_Id, trackNumber);
    }

    public static void UpdateOnOffLoadByAhad(int ahadAsli, int ahadGheyreAsli) {
        UpdateOnOffLoad.updateOnOffLoadByAhad(ahadAsli, ahadGheyreAsli, bill_Id, trackNumber);
    }

    public static void UpdateOnOffLoadByCounterSerial(String counterSerial) {
        UpdateOnOffLoad.updateOnOffLoadByCounterSerial(counterSerial, bill_Id, trackNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_report_activity);
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

    void initialize() {
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setupListViewReport();
        listViewClickListener();
    }

    private void listViewClickListener() {
        listViewReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listViewReports.isItemChecked(i)) {
                    final ReportValueKey reportValueKey = reportValueKeys.get(i);
                    if (reportValueKey.isKarbari()) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        KarbariGroupFragment karbariGroupFragment = new KarbariGroupFragment();
                        karbariGroupFragment.show(fragmentManager, "گروه کاربری");
                    } else if (reportValueKey.isTavizi()) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        TaviziFragment taviziFragment = new TaviziFragment();
                        taviziFragment.show(fragmentManager, "تعویضی");
                    } else if (reportValueKey.isAhad()) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        AhadFragment ahadFragment = new AhadFragment();
                        ahadFragment.show(fragmentManager, "آحاد");
                    }
                    ReportService.addReport(i, bill_Id, trackNumber, OffloadStateEnum.REGISTERD.getValue(), reportValueKeys);
                } else {
                    ReportService.removeReportByReportCode(i, bill_Id, trackNumber, reportValueKeys);
                }
            }
        });
    }

    void setupListViewReport() {
        if (ReportValueKeyService.reportValueKeySize() > 0) {
            reportValueKeys = ReportValueKeyService.getReportValueKey();
            int counter = 0;
            listItems = new String[(int) ReportValueKey.count(ReportValueKey.class)];
            for (ReportValueKey reportValueKey : reportValueKeys) {
                listItems[counter] = reportValueKey.getTitle();
                counter++;
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final CheckedTextView textView = view
                        .findViewById(android.R.id.text1);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "font/BYekan_3.ttf");
                textView.setTypeface(typeface);
                textView.setChecked(true);
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        listViewReports.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewReports.setAdapter(arrayAdapter);
        if (ReportService.reportSize() > 0) {
            reports = ReportService.getReportByTrackBillID(bill_Id, trackNumber);
            if (reports.size() > 0) {
                for (int i = 0; i < reportValueKeys.size(); i++) {
                    ReportValueKey reportValueKey = reportValueKeys.get(i);
                    for (Report report : reports) {
                        if (report.getReportCode() == reportValueKey.getCode()) {
                            listViewReports.setItemChecked(i, true);
                        }
                    }
                }
            }
        }
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
