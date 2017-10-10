package app.com.appsamples.frag.BluetoothPager;

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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import app.com.appsamples.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Reena on 8/27/2017.
 */

public class BluetoothLETest extends Fragment {

    private static final String TAG = BluetoothLETest.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanSettings mScanSettings;
    private List<ScanFilter> mFilters;
    private BluetoothGatt mBluetoothGatt;

    Unbinder unbinder;
    @BindView(R.id.listView1)
    ListView listView1;
    private Set<BluetoothDevice> pairedDevices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bluetooth_test, container, false);
        unbinder = ButterKnife.bind(this, view);

        mHandler = new Handler();
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "Bluetooth LE Not available !!!", Toast.LENGTH_SHORT).show();
        }

        //bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        return view;
    }

    public void on() {

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                mScanSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_OPPORTUNISTIC)
                        .build();
                mFilters = new ArrayList<>();
            }
            scanLeDevice(true);
        }
    }

    void scanLeDevice(boolean value) {
        if (value) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                    } else {
                        mBluetoothLeScanner.stopScan(scanCallback);
                    }
                }
            }, SCAN_PERIOD);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.startLeScan(leScanCallback);
            } else {
                mBluetoothLeScanner.startScan(mFilters, mScanSettings, scanCallback);
            }

        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter.stopLeScan(leScanCallback);
            } else {
                mBluetoothLeScanner.stopScan(scanCallback);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.e(TAG, "callbackType: " + callbackType);
            Log.e(TAG, "result: " + result.toString());
            BluetoothDevice bluetoothDevice = result.getDevice();
            //connectToDevice(bluetoothDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.e("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("onLeScan", bluetoothDevice.toString());
                    //connectToDevice(bluetoothDevice);
                }
            });
        }
    };

    void connectToDevice(BluetoothDevice device) {
        if (mBluetoothGatt == null) {
            mBluetoothGatt = device.connectGatt(getActivity(), false, bluetoothGattCallback);
            scanLeDevice(false); // will stop after first device detection
        }
    }

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
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
            List<BluetoothGattService> servers = gatt.getServices();
            for (BluetoothGattService service : servers) {
                Log.i(TAG, "onServicesDiscovered: " + service.toString());
            }
            gatt.readCharacteristic(servers.get(0).getCharacteristics().get(0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            gatt.disconnect();
        }
    };

    void list() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();
        for (BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView1.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(mBluetoothGatt != null){
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    @OnClick({R.id.btnOn, R.id.btnVisible, R.id.btnList, R.id.btnOff})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnOn:
                on();
                break;
            case R.id.btnVisible:
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnList:
                list();
                break;
            case R.id.btnOff:
                mBluetoothAdapter.disable();
                break;
        }
    }
}
