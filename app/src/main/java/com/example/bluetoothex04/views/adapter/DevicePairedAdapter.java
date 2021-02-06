package com.example.bluetoothex04.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothex04.R;
import com.example.bluetoothex04.models.model.DeviceModel;

public class DevicePairedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private ObservableArrayList<DeviceModel> arrayList;
    private ObservableArrayList<DeviceModel> divideArrayList;

    public DevicePairedAdapter(Activity context, ObservableArrayList<DeviceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayList.addOnListChangedCallback(onListChangedCallback);
        if (this.divideArrayList == null) {
            this.divideArrayList = new ObservableArrayList<>();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.card_device_paired_vertical, parent, false);
        return new RecyclerViewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        divideArrayList.clear();
        for (int i = position * 2; i <= (position * 2 + 1); i++) {
            if (i >= arrayList.size()) {
                break;
            }
            divideArrayList.add(arrayList.get(i));
        }
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;
        DevicePairedHorizontalAdapter devicePairedHorizontalAdapter = new DevicePairedHorizontalAdapter(context, divideArrayList);
        viewHolder.recyclerView.setHasFixedSize(true);
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.recyclerView.setAdapter(devicePairedHorizontalAdapter);
    }

    @Override
    public int getItemCount() {
        return Math.round((float) arrayList.size() / 2);
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_device_paired_horizontal);
        }
    }

    ObservableArrayList.OnListChangedCallback<ObservableArrayList<DeviceModel>> onListChangedCallback = new ObservableArrayList.OnListChangedCallback<ObservableArrayList<DeviceModel>>() {
        @Override
        public void onChanged(ObservableArrayList<DeviceModel> sender) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<DeviceModel> sender, int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<DeviceModel> sender, int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<DeviceModel> sender, int fromPosition, int toPosition, int itemCount) {
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<DeviceModel> sender, int positionStart, int itemCount) {
        }
    };
}
