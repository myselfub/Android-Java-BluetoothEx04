package com.example.bluetoothex04.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.example.bluetoothex04.R;
import com.example.bluetoothex04.databinding.ListDeviceSearchBinding;
import com.example.bluetoothex04.viewmodels.DeviceViewModel;

public class DeviceSearchAdapter extends BaseAdapter {

    private MutableLiveData<DeviceViewModel> mutableLiveData;

    public DeviceSearchAdapter() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
    }

    public void setMutableLiveData(DeviceViewModel deviceViewModel) {
        mutableLiveData.setValue(deviceViewModel);
    }

    @Override
    public int getCount() {
        return mutableLiveData.getValue().getObservableArrayList().size();
    }

    @Override
    public Object getItem(int position) {
        return mutableLiveData.getValue().getObservableArrayList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListDeviceSearchBinding listDeviceSearchBinding;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            listDeviceSearchBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_device_search, parent, false);
            convertView = listDeviceSearchBinding.getRoot();
            convertView.setTag(listDeviceSearchBinding);
        } else {
            listDeviceSearchBinding = (ListDeviceSearchBinding) convertView.getTag();
        }
        listDeviceSearchBinding.setDeviceModel(mutableLiveData.getValue().getObservableArrayList().get(position));

        return convertView;
    }
}
