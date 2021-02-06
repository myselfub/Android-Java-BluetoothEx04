package com.example.bluetoothex04.views.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.bluetoothex04.R;
import com.example.bluetoothex04.databinding.ActivityDeviceInfoBinding;
import com.example.bluetoothex04.models.model.DeviceModel;
import com.example.bluetoothex04.models.model.VentilationTimeModel;
import com.example.bluetoothex04.models.repository.LocalRepository;
import com.example.bluetoothex04.util.BluetoothThread;
import com.example.bluetoothex04.viewmodels.VentilationTimeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DeviceInfoActivity extends AppCompatActivity {
    private DeviceModel deviceModel;
    private BluetoothThread bluetoothThread;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private LocalRepository localRepository;
    private boolean isLocationUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        localRepository = new LocalRepository(this);
        deviceModel = new DeviceModel();
        deviceModel.setName(intent.getStringExtra("name"));
        deviceModel.setMacAddress(intent.getStringExtra("macAddress"));
        ActivityDeviceInfoBinding activityDeviceInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_info);
        activityDeviceInfoBinding.setDeviceModel(deviceModel);
        setMyLastLocation(this);
    }

    @Override
    protected void onDestroy() {
        if (bluetoothThread != null) {
            bluetoothThread.interrupt();
            bluetoothThread = null;
        }
        super.onDestroy();
    }

    Observer<ArrayList<VentilationTimeModel>> observer = new Observer<ArrayList<VentilationTimeModel>>() {
        @Override
        public void onChanged(ArrayList<VentilationTimeModel> arrayList) {
            Log.d("aaaa", arrayList.get(0).toString());
            if (bluetoothThread.isConnected()) {
                bluetoothThread.sendMessage(arrayList.get(0).toString());
            } else {
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (bluetoothThread.isConnected()) {
                        bluetoothThread.sendMessage(arrayList.get(0).toString());
                    } else {
                        Log.d("aaaa", "오류");
                    }
                }, 3000);
            }
        }
    };

    public void clickData(View view) {
        Button btnDeviceInfoData = view.findViewById(R.id.btn_device_info_data);
        String text = (String) btnDeviceInfoData.getText();
        if (text.equals("소켓 연결")) {
            if (bluetoothThread == null) {
                bluetoothThread = new BluetoothThread(this, deviceModel.getMacAddress());
                bluetoothThread.start();
            }
            btnDeviceInfoData.setText("데이터");
        } else if (text.equals("데이터")) {
            if (bluetoothThread.isConnected()) {
                bluetoothThread.sendMessage("11");
            } else {
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (bluetoothThread.isConnected()) {
                        bluetoothThread.sendMessage("11");
                    } else {
                        Log.d("aaaa", "오류");
                    }
                }, 3000);
            }
//        String[] latlon = localRepository.getLatLon();
//        HashMap<String, String> queryMap = new HashMap<>();
//        queryMap.put("lat", latlon[0]);
//        queryMap.put("lon", latlon[1]);
//        VentilationTimeViewModel ventilationTimeViewModel = new VentilationTimeViewModel("http://192.168.25.5:8000", queryMap);
//        ventilationTimeViewModel.getMutableLiveData().observe(this, observer);
        }
    }

    public void clickUnpairDevice(View view) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        String macAddress = deviceModel.getMacAddress();
        BluetoothDevice bluetoothDevice = null;
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                if (macAddress.equals(device.getAddress())) {
                    bluetoothDevice = device;
                    break;
                }
            }
        }
        if (bluetoothDevice != null) {
            try {
                // 선택한 디바이스 페어링 해제 요청
                Method method = bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null);
                method.invoke(bluetoothDevice, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "연결 해제 완료", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setMyLastLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!checkGPS) {
            Toast.makeText(context, "GPS를 켜야합니다.", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); // REQUEST_CODE_TURN_ON_GPS
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1); // 한 번만 가져옴

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                isLocationUpdated = true;
                List<Location> locationList = locationResult.getLocations();
                if (locationList != null && locationList.size() > 0) {
                    Location location = locationList.get(locationList.size() - 1);
                    String latlon = location.getLatitude() + "," + location.getLongitude();
                    localRepository.setLatLon(latlon);
                    Log.d("aaaa", String.valueOf(location.getTime()));
                    Date date = new Date(location.getTime());
                    Log.d("aaaa", date.toString());
                    Log.d("aaaa", latlon);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        // timeout.
        new Handler(Looper.myLooper()).postDelayed(() -> {
            if (!isLocationUpdated) {
                Toast.makeText(context, "위치를 가져오지 못했습니다.", Toast.LENGTH_LONG).show();
                fusedLocationClient.removeLocationUpdates(locationCallback);
                isLocationUpdated = false;
            }
        }, 3000);
    }
}