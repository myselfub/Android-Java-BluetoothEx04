package com.example.bluetoothex04.viewmodels;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.example.bluetoothex04.models.model.DeviceModel;

public class DeviceViewModel extends ViewModel {

    private ObservableArrayList<DeviceModel> observableArrayList;

    public DeviceViewModel() {
        if (observableArrayList == null) {
            observableArrayList = new ObservableArrayList<>();
        }
    }

    public void setObservableArrayList(String name, String macAddress) {
        DeviceModel deviceModel = new DeviceModel();
        if (name == null || name.trim().equals("")) {
            name = "unknown";
        }
        deviceModel.setName(name);
        deviceModel.setMacAddress(macAddress);
        observableArrayList.add(deviceModel);
    }

    public ObservableArrayList<DeviceModel> getObservableArrayList() {
        return observableArrayList;
    }
}