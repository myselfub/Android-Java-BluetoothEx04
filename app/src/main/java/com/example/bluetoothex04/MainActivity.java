package com.example.bluetoothex04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bluetoothex04.views.ui.DeviceRegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickBtn1(View view) {
        Intent intent = new Intent(this, DeviceRegisterActivity.class);
        startActivity(intent);
    }
}