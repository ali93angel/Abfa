package com.app.leon.abfa.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Leon on 1/17/2018.
 */

public class BluetoothManagement {
    private final String DEVICE_ADDRESS_START = " (";
    private final String DEVICE_ADDRESS_END = ")";
    private final ArrayList<CharSequence> bondedDevices = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> bondedDeviceSet;
    private Context context;

    public BluetoothManagement(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBondedDeviceSet() {
        bondedDeviceSet = bluetoothAdapter.getBondedDevices();
    }

    public ArrayList<CharSequence> getBondedDevice() {
        bondedDevices.clear();
        for (BluetoothDevice device : bondedDeviceSet) {
            bondedDevices.add(device.getName() + DEVICE_ADDRESS_START
                    + device.getAddress() + DEVICE_ADDRESS_END);
        }
        return bondedDevices;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void pairBluetooth(BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(broadcastReceiver, filter);
        bluetoothAdapter.startDiscovery();

    }
}
