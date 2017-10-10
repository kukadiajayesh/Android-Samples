package app.com.appsamples.frag.BluetoothPager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import app.com.appsamples.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Reena on 8/27/2017.
 */

public class BluetoothTest extends Fragment {

    private static final String TAG = BluetoothTest.class.getSimpleName();
    Unbinder unbinder;
    @BindView(R.id.listView1)
    ListView listView1;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bluetooth_test, container, false);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    public void on() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        } else {
            //Toast.makeText(getActivity(), "Already On", Toast.LENGTH_SHORT).show();
            startDiscovery(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            startDiscovery(true);
        }
    }

    void startDiscovery(boolean value) {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        if (value) {
            list.clear();
            getActivity().registerReceiver(broadcastReceiver, intentFilter);
            bluetoothAdapter.startDiscovery();
        } else {
            getActivity().unregisterReceiver(broadcastReceiver);
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.getProfileProxy(getActivity(), serviceListener, BluetoothProfile.HEADSET);
        bluetoothAdapter.getProfileProxy(getActivity(), serviceListener, BluetoothProfile.A2DP);

    }

    ArrayList list = new ArrayList();
    ArrayAdapter adapter;

    void listPaired() {
        list.clear();
        pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());
        adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView1.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnOn, R.id.btnVisible, R.id.btnList, R.id.btnPaired, R.id.btnOff})
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

                break;
            case R.id.btnPaired:
                listPaired();
                break;
            case R.id.btnOff:
                bluetoothAdapter.disable();
                break;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.e(TAG, "onReceive: " + bluetoothDevice.getName());
                list.add(bluetoothDevice.getName());
                adapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, list);
                listView1.setAdapter(adapter);
            }
        }
    };

    BluetoothProfile.ServiceListener serviceListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile bluetoothProfile) {
            Toast.makeText(getActivity(), bluetoothProfile.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Toast.makeText(getActivity(), profile + "", Toast.LENGTH_SHORT).show();
        }

    };
}
