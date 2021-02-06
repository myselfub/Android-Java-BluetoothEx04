package com.example.bluetoothex04.views.ui;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothex04.R;
import com.example.bluetoothex04.viewmodels.DeviceViewModel;
import com.example.bluetoothex04.views.adapter.DevicePairedAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DevicePairedActivity extends AppCompatActivity {
    private final static int BLUETOOTH_REQUEST_CODE = 100;
    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private DeviceViewModel deviceViewModel;
    private RecyclerView recyclerView;
    private DevicePairedAdapter devicePairedAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> bluetoothDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_paired);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }

        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(bluetoothSearchReceiver, searchFilter);

        recyclerView = (RecyclerView) findViewById(R.id.rv_device_paired_vertical);

        if (viewModelFactory == null) {
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        }

        deviceViewModel = new ViewModelProvider(this, viewModelFactory).get(DeviceViewModel.class);
        devicePairedAdapter = new DevicePairedAdapter(DevicePairedActivity.this, deviceViewModel.getObservableArrayList());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DevicePairedActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(devicePairedAdapter);
        bluetoothDevices = new ArrayList<>();

        getDevicePairedList();
    }

    @Override
    protected void onDestroy() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(bluetoothSearchReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BLUETOOTH_REQUEST_CODE:
                // 블루투스 활성화 승인
                if (resultCode == Activity.RESULT_OK) {
                    getDevicePairedList();
                }
                // 블루투스 활성화 거절
                else {
                    Toast.makeText(this, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                break;
        }
    }

    //이미 페어링된 목록 가져오기
    public void getDevicePairedList() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        deviceViewModel.getObservableArrayList().clear();
        bluetoothDevices.clear();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                ParcelUuid[] parcelUuids = device.getUuids();
                for (ParcelUuid uuid : parcelUuids) {
                    Log.d("aaaaaa1", uuid.getUuid().toString());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Log.d("aaaaaa2", device.getAlias());
                }
                Log.d("aaaaaa3", device.getName());
                Log.d("aaaaaa4", device.getAddress());
                deviceViewModel.setObservableArrayList(device.getName(), device.getAddress());
//                ParcelUuid[] uuid = device.getUuids();
//                Log.d("aaaa", uuid[0].toString());
                // type = 무선이어폰:1, 맥:1, 갤럭시S105G:3 (https://developer.android.com/reference/kotlin/android/bluetooth/BluetoothDevice#device_type_classic)
            }
        }
    }

    public void clickSearchDevice(View view) {
        Intent intent = new Intent(this, DeviceSearchActivity.class);
        startActivity(intent);
    }

    // 블루투스 검색결과 BroadcastReceiver
    BroadcastReceiver bluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                // 블루투스 디바이스 페어링 상태 변화
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    recyclerView.removeAllViewsInLayout();
                    recyclerView.setAdapter(devicePairedAdapter);
                    getDevicePairedList();
                    break;
            }
        }
    };
}