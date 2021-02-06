package com.example.bluetoothex04.views.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothex04.R;
import com.example.bluetoothex04.models.model.DeviceModel;
import com.example.bluetoothex04.views.ui.DeviceInfoActivity;

public class DevicePairedHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private ObservableArrayList<DeviceModel> arrayList;

    public DevicePairedHorizontalAdapter(Activity context, ObservableArrayList<DeviceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.card_device_paired_horizontal, parent, false);
        return new RecyclerViewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeviceModel deviceModel = arrayList.get(position);
        RecyclerViewViewHolder viewHolder = (RecyclerViewViewHolder) holder;
        viewHolder.tv_device_name.setText(deviceModel.getName());
        viewHolder.tv_device_mac_address.setText(deviceModel.getMacAddress());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView tv_device_name;
        TextView tv_device_mac_address;
        View view;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device_name = itemView.findViewById(R.id.card_tv_device_name);
            tv_device_mac_address = itemView.findViewById(R.id.card_tv_device_mac_address);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv_device_name = v.findViewById(R.id.card_tv_device_name);
                    TextView tv_device_mac_address = v.findViewById(R.id.card_tv_device_mac_address);
                    Intent intent = new Intent(context, DeviceInfoActivity.class);
                    intent.putExtra("name", (String) tv_device_name.getText());
                    intent.putExtra("macAddress", (String) tv_device_mac_address.getText());
                    context.startActivity(intent);
                }
            });
        }
    }
}