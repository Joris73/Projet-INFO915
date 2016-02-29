package com.joris.projetinfo915;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jobos on 05/02/2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TemperatorFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private int permissionRequestCode = 42;
    private static ValueMapper valueMapper;
    static private TextView textTemp;
    static private TextView textLumiere;
    private static TemperatorFragment caca;

    public TemperatorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        caca = this;

        mHandler = new Handler();
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, permissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == permissionRequestCode) {
            scanLeDevice(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperator, container, false);
        Button onScan = (Button) view.findViewById(R.id.btn_on_scan);
        Button offScan = (Button) view.findViewById(R.id.btn_off_scan);

        textTemp = (TextView) view.findViewById(R.id.tvTemp);
        textLumiere = (TextView) view.findViewById(R.id.tvAir);

        onScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(true);
            }
        });

        offScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(false);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    @Override
    public void onDestroy() {
        if (mGatt == null) {
            return;
        }
        mGatt.close();
        mGatt = null;
        super.onDestroy();
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);
                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            BluetoothDevice btDevice = result.getDevice();
            if (btDevice.toString().equals("A0:14:3D:7D:94:0A")) {
                valueMapper = ValueMapper.getInstance(getContext());
                Log.wtf("Connect to ", btDevice.toString());
                connectToDevice(btDevice);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.wtf("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            mGatt = device.connectGatt(getActivity(), false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        } else {
            Log.wtf("lol", "dfsf");
        }
    }

    private static final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        boolean toto = true;

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            for (BluetoothGattService bluetoothGattService : services) {
                if (toto && String.valueOf(bluetoothGattService.getUuid()).equals("39e1fa00-84a8-11e2-afba-0002a5d5c51b")) {
                    gatt.readCharacteristic(bluetoothGattService
                            .getCharacteristic(UUID.fromString("39e1fa04-84a8-11e2-afba-0002a5d5c51b")));
                } else if (String.valueOf(bluetoothGattService.getUuid()).equals("39e1fa00-84a8-11e2-afba-0002a5d5c51b")) {
                    gatt.readCharacteristic(bluetoothGattService
                            .getCharacteristic(UUID.fromString("39e1fa01-84a8-11e2-afba-0002a5d5c51b")));
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            if (characteristic.getUuid().equals(UUID.fromString("39e1fa04-84a8-11e2-afba-0002a5d5c51b"))) {
                double airTemp = ByteUtils.convertByteArrayToInt((ByteUtils.convertLeToBe(characteristic.getValue())));
                airTemp = valueMapper.mapTemperature((int) airTemp);
                Log.wtf("onCharacteristicRead", "airTemp : " + airTemp);
                final double finalAirTemp = airTemp;
                caca.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        caca.setTemp(finalAirTemp);
                    }
                });
                toto = false;
                gatt.discoverServices();
            } else if (String.valueOf(characteristic.getUuid()).equals("39e1fa01-84a8-11e2-afba-0002a5d5c51b")) {
                double sunlight = ByteUtils.convertByteArrayToInt((ByteUtils.convertLeToBe(characteristic.getValue())));
                sunlight = valueMapper.mapSunlight((int) sunlight);
                Log.wtf("onCharacteristicRead", "SUNLIGHT : " + sunlight);
                final double finalSunlight = sunlight;
                caca.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        caca.setLi(finalSunlight);
                    }
                });
                gatt.disconnect();
            }
        }
    };


    public void setTemp(double airTemp) {
        textTemp.setText("Température : " + airTemp + "°c");
    }

    public void setLi(double sunlight) {
        textLumiere.setText("Lumière : " + sunlight + " lum");
    }
}