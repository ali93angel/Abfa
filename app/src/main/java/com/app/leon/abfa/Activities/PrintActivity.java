package com.app.leon.abfa.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.abfa.Infrastructure.EscapeSequence;
import com.app.leon.abfa.Infrastructure.PrintTwoColumns;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.ViewModels.PrintableObject;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.BluetoothManagement;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;
import com.bxl.config.editor.BXLConfigLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.config.JposEntry;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;

public class PrintActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener, StatusUpdateListener, OutputCompleteListener, ErrorListener {
    private static final int REQUEST_CODE_BLUETOOTH = 1;
    private static final String DEVICE_ADDRESS_START = " (";
    private static final String DEVICE_ADDRESS_END = ")";
    private ArrayList<CharSequence> bondedDevices = new ArrayList<>();
    private ArrayAdapter<CharSequence> arrayAdapter;
    private BXLConfigLoader bxlConfigLoader;
    private POSPrinter posPrinter;
    private String logicalName;
    private FontManager fontManager;
    View view;
    String theme = "1";
    SharedPreferenceManager sharedPreferenceManager;
    String bill_Id;
    int trackNumber;
    PrintTwoColumns printTwoColumns;
    BluetoothManagement bluetoothManagement;
    ProgressDialog progressDialog;
    @BindView(R.id.editTextData)
    EditText editTextData;
    @BindView(R.id.print_activity)
    RelativeLayout relativeLayout;
    @BindView(R.id.spinnerEscapeSequences)
    Spinner spinnerEscapeSequences;
    @BindView(R.id.listViewPairedDevices)
    ListView listViewPairedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = new SharedPreferenceManager(getApplication());
        if (sharedPreferenceManager.CheckIsNotEmpty(SharedReferenceKeys.THEME_STABLE.getValue())) {
            theme = sharedPreferenceManager.get(SharedReferenceKeys.THEME_STABLE.getValue());
        }
        onActivitySetTheme(Integer.valueOf(theme));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_activity);
        ButterKnife.bind(this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            bill_Id = bundle.getString(BundleEnum.BILL_ID.getValue());
            trackNumber = bundle.getInt(BundleEnum.TRACK_NUMBER.getValue());
            Log.e(bill_Id, String.valueOf(trackNumber));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BLUETOOTH) {
            setBondedDevices();
        }
    }

    protected void initialize() {
        fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(relativeLayout);
        progressDialog = new ProgressDialog(this);
        setBondedDevices();
        initializeEditText();
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonPrint).setOnClickListener(this);
        bxlConfigLoader = new BXLConfigLoader(this);
        try {
            bxlConfigLoader.openFile();
        } catch (Exception e) {
            e.printStackTrace();
            bxlConfigLoader.newFile();
        }
        posPrinter = new POSPrinter(this);
        posPrinter.addErrorListener(this);
        posPrinter.addOutputCompleteListener(this);
        posPrinter.addStatusUpdateListener(this);
        printTwoColumns = new PrintTwoColumns(posPrinter, getApplicationContext());
    }


    protected void initializeEditText() {
        editTextData.setSelection(editTextData.getText().length());
        editTextData.setText("با پرداخت به موقع قبوض خود، ما را در ارائه خدمات یاری فرمایید.\nمشترک گرامی");
    }

    protected void initializeListView() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, bondedDevices);
        listViewPairedDevice.setAdapter(arrayAdapter);
        listViewPairedDevice.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewPairedDevice.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String device = ((TextView) view).getText().toString();
        String name = device.substring(0, device.indexOf(DEVICE_ADDRESS_START));
        String address = device.substring(device.indexOf(DEVICE_ADDRESS_START)
                        + DEVICE_ADDRESS_START.length(),
                device.indexOf(DEVICE_ADDRESS_END));
        try {
            for (Object entry : bxlConfigLoader.getEntries()) {
                JposEntry jposEntry = (JposEntry) entry;
                bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            logicalName = printTwoColumns.setProductName(name);
            bxlConfigLoader.addEntry(logicalName,
                    BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER,
                    logicalName,
                    BXLConfigLoader.DEVICE_BUS_BLUETOOTH, address);

            bxlConfigLoader.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                String text = EscapeSequence.getString(spinnerEscapeSequences.getSelectedItemPosition());
                editTextData.getText().insert(editTextData.getSelectionStart(), text);
                break;
            case R.id.buttonPrint:
                String[] menu = {"سطر اول", "سطردوم", "سطر سوم"};
                String[] price = {"17.00", "31.50", "17.00"};
                ArrayList<PrintableObject> printableObjectsBody = new ArrayList<>();
                for (int i = 0; i < menu.length; i++) {
                    PrintableObject printableObject = new PrintableObject(price[i], menu[i]);
                    printableObjectsBody.add(printableObject);
                }
                String subTotal = "192.15";
                String serviceCharge = "-21.35";
                String total = "213.50";
                String subTotalTitle = "زیر کل";
                String serviceChargeTitle = "هزینه سرویس ";
                String totalTitle = "جمع";

                ArrayList<PrintableObject> printableObjectsFooter = new ArrayList<>();
                printableObjectsFooter.add(new PrintableObject(subTotal, subTotalTitle));
                printableObjectsFooter.add(new PrintableObject(serviceCharge, serviceChargeTitle));
                printableObjectsFooter.add(new PrintableObject(total, totalTitle));

                String address = editTextData.getText().toString();
                printTwoColumns.printReceiptForFarsi(printableObjectsBody, address, logicalName, printableObjectsFooter);
                break;
        }
    }

    private void setBondedDevices() {
        logicalName = null;
        bondedDevices.clear();
        bluetoothManagement = new BluetoothManagement(getApplicationContext());
        if (bluetoothManagement.getBluetoothAdapter() == null) {
            Log.e("status", "null");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_BLUETOOTH);
        } else if (!bluetoothManagement.getBluetoothAdapter().isEnabled()) {
            Log.e("status", "disable");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_BLUETOOTH);

        } else {
            Log.e("status", "pair");
            bluetoothManagement.setBondedDeviceSet();
            bondedDevices = bluetoothManagement.getBondedDevice();

            if (bondedDevices.size() > 0) {
                Log.e("status", "pair not null");
                initializeListView();
            } else {
                Log.e("status", "pair null");
//                bluetoothManagement.pairBluetooth(broadcastReceiver);
                setBondedDevices();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            posPrinter.close();
        } catch (JposException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void outputCompleteOccurred(final OutputCompleteEvent e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PrintActivity.this, "complete print", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void errorOccurred(final ErrorEvent arg0) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(PrintActivity.this, "Error status : " + printTwoColumns.getERMessage(arg0.getErrorCodeExtended()), Toast.LENGTH_SHORT).show();
                if (printTwoColumns.getERMessage(arg0.getErrorCodeExtended()).equals("Power off")) {
                    try {
                        posPrinter.close();
                    } catch (JposException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (printTwoColumns.getERMessage(arg0.getErrorCodeExtended()).equals("Cover open")) {
                } else if (printTwoColumns.getERMessage(arg0.getErrorCodeExtended()).equals("Paper empty")) {
                }
            }
        });
    }

    @Override
    public void statusUpdateOccurred(final StatusUpdateEvent arg0) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(PrintActivity.this, "printer status : " + printTwoColumns.getSUEMessage(arg0.getStatus()), Toast.LENGTH_SHORT).show();
                if (printTwoColumns.getSUEMessage(arg0.getStatus()).equals("Power off")) {
                    Toast.makeText(PrintActivity.this, "check the printer - Power off", Toast.LENGTH_SHORT).show();
                } else if (printTwoColumns.getSUEMessage(arg0.getStatus()).equals("Cover Open")) {
                    Toast.makeText(PrintActivity.this, "check the printer - Cover Open", Toast.LENGTH_SHORT).show();
                } else if (printTwoColumns.getSUEMessage(arg0.getStatus()).equals("Cover OK")) {
                } else if (printTwoColumns.getSUEMessage(arg0.getStatus()).equals("Receipt Paper Empty")) {
                    Toast.makeText(PrintActivity.this, "check the printer - Receipt Paper Empty", Toast.LENGTH_SHORT).show();
                } else if (printTwoColumns.getSUEMessage(arg0.getStatus()).equals("Receipt Paper OK")) {
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