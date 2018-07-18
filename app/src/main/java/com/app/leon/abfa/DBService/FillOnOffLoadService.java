package com.app.leon.abfa.DBService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.leon.abfa.Models.DbTables.CounterStateValueKey;
import com.app.leon.abfa.Models.DbTables.HighLowAlgorithm;
import com.app.leon.abfa.Models.DbTables.HighLowConfig;
import com.app.leon.abfa.Models.DbTables.Karbari;
import com.app.leon.abfa.Models.DbTables.KarbariGroup;
import com.app.leon.abfa.Models.DbTables.KarbariRateType;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.Models.DbTables.Report;
import com.app.leon.abfa.Models.DbTables.ReportValueKey;
import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.Models.InterCommunation.CrReport;
import com.app.leon.abfa.Models.InterCommunation.HighLowAlgorithmViewModel;
import com.app.leon.abfa.Models.InterCommunation.HighLowZoneProiorityViewModel;
import com.app.leon.abfa.Models.InterCommunation.MobileInput;
import com.app.leon.abfa.Models.InterCommunation.MyWorks;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.CustomDialog;
import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;

import java.util.ArrayList;

/**
 * Created by Leon on 12/28/2017.
 */

public class FillOnOffLoadService extends AsyncTask<String, String, String> {
    MobileInput mobileInput;
    Context context;
    ProgressDialog progressDialog;
    ArrayList<OnOffLoad> onOffLoads = new ArrayList<>();
    ArrayList<ReadingConfig> readingConfigs = new ArrayList<>();
    ArrayList<CounterStateValueKey> counterStateValueKeys = new ArrayList<>();
    ArrayList<ReportValueKey> reportValueKeys = new ArrayList<>();
    ArrayList<Report> reports = new ArrayList<>();
    ArrayList<Karbari> karbaries = new ArrayList<>();
    ArrayList<KarbariGroup> karbariGroups = new ArrayList<>();
    ArrayList<KarbariRateType> karbariRateTypes = new ArrayList<>();
    ArrayList<HighLowAlgorithm> highLowAlgorithms = new ArrayList<>();
    ArrayList<HighLowConfig> highLowConfigs = new ArrayList<>();

    public FillOnOffLoadService(Context context, MobileInput mobileInput) {
        this.context = context;
        this.mobileInput = mobileInput;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        new CustomDialog(DialogType.Green, context, s, context.getString(R.string.dear_user),
                context.getString(R.string.attention), context.getString(R.string.accepted));
        progressDialog.dismiss();
    }

    @Override
    protected String doInBackground(String... strings) {
        String message;
        try {
            for (MyWorks myWork : mobileInput.getMyWorks()) {
                OnOffLoad onOffLoad = new OnOffLoad(myWork.getOnLoad(), myWork.getOffLoad(), myWork.getId());
                onOffLoads.add(onOffLoad);
            }
            for (com.app.leon.abfa.Models.InterCommunation.ReadingConfig readingConfig : mobileInput.getReadingConfigs()) {
                ReadingConfig readingConfig1 = new ReadingConfig(readingConfig);
                readingConfigs.add(readingConfig1);
            }
            for (com.app.leon.abfa.Models.InterCommunation.CounterStateValueKey counterStateValueKey : mobileInput.getCounterStateValueKeys()) {
                CounterStateValueKey counterStateValueKey1 = new CounterStateValueKey(counterStateValueKey);
                counterStateValueKeys.add(counterStateValueKey1);
            }
            for (com.app.leon.abfa.Models.InterCommunation.ReportValueKey reportValueKey : mobileInput.getReportValueKeys()) {
                ReportValueKey reportValueKey1 = new ReportValueKey(reportValueKey);
                reportValueKeys.add(reportValueKey1);
            }
            for (CrReport crReport : mobileInput.getReports()) {
                Report report = new Report(crReport);
                reports.add(report);
            }
            for (com.app.leon.abfa.Models.InterCommunation.Karbari karbari : mobileInput.getKarbariWrapper().getKarbaries()) {
                Karbari karbari1 = new Karbari(karbari);
                karbaries.add(karbari1);
            }
            for (com.app.leon.abfa.Models.InterCommunation.KarbariGroup karbariGroup : mobileInput.getKarbariWrapper().getKarbariGroups()) {
                KarbariGroup karbariGroup1 = new KarbariGroup(karbariGroup);
                karbariGroups.add(karbariGroup1);
            }
            for (com.app.leon.abfa.Models.InterCommunation.KarbariRateType karbariRateType : mobileInput.getKarbariWrapper().getKarbariRateTypes()) {
                KarbariRateType karbariRateType1 = new KarbariRateType(karbariRateType);
                karbariRateTypes.add(karbariRateType1);
            }
            for (HighLowAlgorithmViewModel highLowAlgorithmViewModel : mobileInput.getHighLowWrapper().getHighLowAlgorithmViewModels()) {
                HighLowAlgorithm highLowAlgorithm = new HighLowAlgorithm(highLowAlgorithmViewModel);
                highLowAlgorithms.add(highLowAlgorithm);
            }
            for (HighLowZoneProiorityViewModel highLowZoneProiorityViewModel : mobileInput.getHighLowWrapper().getHighLowZoneProiorityViewModels()) {
                HighLowConfig highLowConfig = new HighLowConfig(highLowZoneProiorityViewModel);
                highLowConfigs.add(highLowConfig);
            }
            SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                @Override
                public void manipulateInTransaction() {
                    CounterStateValueKey.deleteAll(CounterStateValueKey.class);
                    Karbari.deleteAll(Karbari.class);
                    KarbariRateType.deleteAll(KarbariRateType.class);
                    KarbariGroup.deleteAll(KarbariGroup.class);
                    HighLowConfig.deleteAll(HighLowConfig.class);
                    HighLowAlgorithm.deleteAll(HighLowAlgorithm.class);
                    ReportValueKey.deleteAll(ReportValueKey.class);

                    SugarRecord.saveInTx(onOffLoads);
                    SugarRecord.saveInTx(readingConfigs);
                    SugarRecord.saveInTx(counterStateValueKeys);
                    SugarRecord.saveInTx(highLowAlgorithms);
                    SugarRecord.saveInTx(highLowConfigs);
                    SugarRecord.saveInTx(reports);
                    SugarRecord.saveInTx(reportValueKeys);
                    SugarRecord.saveInTx(karbaries);
                    SugarRecord.saveInTx(karbariGroups);
                    SugarRecord.saveInTx(karbariRateTypes);

                }
            });
            message = "تعداد" + String.valueOf(readingConfigs.size()) + "مسیر و " +
                    String.valueOf(onOffLoads.size()) + "اشتراک بارگیری شد.";
        } catch (Exception e) {
            message = e.toString();
            return message;
        }
        return message;
    }
}
