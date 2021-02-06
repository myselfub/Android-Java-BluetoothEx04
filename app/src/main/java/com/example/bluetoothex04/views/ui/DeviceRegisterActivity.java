package com.example.bluetoothex04.views.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothex04.R;

import java.util.Set;

public class DeviceRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            Intent intent = new Intent(this, DevicePairedActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_device_register);
    }

    public void clickSearchDevice(View view) {
        Intent intent = new Intent(this, DeviceSearchActivity.class);
        startActivity(intent);
    }
}