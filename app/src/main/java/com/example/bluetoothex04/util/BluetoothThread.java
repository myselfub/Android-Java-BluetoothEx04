package com.example.bluetoothex04.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.google.android.gms.common.util.ArrayUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothThread extends Thread {
    private Context context;
    private String macAddress;
    private UUID BT_UUID;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private DataOutputStream dataOutputStream;
    //    private OutputStream dataOutputStream;
    private DataInputStream inputStream;
    //    private InputStream inputStream;
    private BluetoothSocket bluetoothSocket;
    private final byte GET_SETTINGS_REQUEST = (byte) 0x04;
    private final byte GET_SETTINGS_RESPONSE = (byte) 0x14;
    private final byte SET_LED_REQUEST = (byte) 0x06;
    private final byte SET_LED_RESPONSE = (byte) 0x16;

    private BluetoothThread() {
    }

    public BluetoothThread(Context context, String macAddress) {
        this.context = context;
        this.macAddress = macAddress;
        this.BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void run() {
        init();
    }

    private void init() {
        // 소켓생성하기 위해 Blutooth 장치 객체 얻어오기
        if (bluetoothSocket == null) {
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);
            ParcelUuid[] parcelUuid = bluetoothDevice.getUuids();
            ArrayList<String> uuids = new ArrayList<>();
            for (ParcelUuid uuid : parcelUuid) {
                uuids.add(uuid.getUuid().toString());
                Log.d("aaaaaa", uuid.getUuid().toString());
            }
            // 디바이스를 통해 소켓연결
            try {
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(BT_UUID);
                bluetoothSocket.connect(); // 연결 시도

                // 만약 연결이 성공되었다고 메세지
//                Toast.makeText(context, "연결 성공", Toast.LENGTH_SHORT).show();
                Log.d("aaaaa", "연결 성공");
                dataOutputStream = new DataOutputStream(bluetoothSocket.getOutputStream());
//            dataOutputStream = bluetoothSocket.getOutputStream();
                inputStream = new DataInputStream(bluetoothSocket.getInputStream());
//            inputStream = bluetoothSocket.getInputStream();
                try {
                    byte[] buffer = null;
                    while (inputStream != null) {
                        int bytes = inputStream.available();
                        if (bytes != 0) {
                            buffer = new byte[bytes];
                            inputStream.read(buffer, 0, bytes);
                            Log.d("aaaaaaaa", new String(buffer, "UTF-8"));
                        }
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
//                Toast.makeText(context, "연결 중 오류 발생", Toast.LENGTH_SHORT).show();
                Log.d("aaaaa", "연결 중 오류 발생");
                e.printStackTrace();
                try {
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                } catch (IOException ioe) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void interrupt() {
        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.interrupt();
    }

    public boolean isConnected() {
        if (bluetoothSocket == null) {
            init();
        }
        return bluetoothSocket.isConnected();
    }

    public void sendMessage(String message) {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            if (dataOutputStream != null) {
                if (message.trim().length() > 0) {
                    try {
//                        dataOutputStream.writeUTF(message);
//                        String data = message;
//                        String data = "Ecoplay";
//                        String lengthHexString = Integer.toHexString(data.getBytes().length);
                        byte[] dataByte = new byte[]{0, 0, 0};
                        CRC16Utils crc16Utils = new CRC16Utils();
                        String lengthHexString = Integer.toHexString(dataByte.length);
                        byte[] lengthByte = crc16Utils.hexStr2BinArr(lengthHexString);
                        if (lengthByte.length < 2) {
                            lengthByte = ArrayUtils.concatByteArrays(new byte[]{0}, lengthByte);
                        }
                        byte[] header = ArrayUtils.concatByteArrays(new byte[]{SET_LED_REQUEST}, lengthByte);
//                        byte[] body = data.getBytes("UTF-8");
                        byte[] body = dataByte;
                        byte[] CRC = new byte[]{};
                        byte[] sendByte = ArrayUtils.concatByteArrays(crc16Utils.getSTX(), header, body, CRC);
                        dataOutputStream.write(sendByte);
                        dataOutputStream.flush();
                        String sendByteString = crc16Utils.bytes2HexString(sendByte);
//                        Toast.makeText(context, new String(sendByte, "UTF-8"), Toast.LENGTH_SHORT).show();
                        Log.d("aaaaa", sendByteString);
                    } catch (IOException e) {
                        try {
                            if (dataOutputStream != null) {
                                dataOutputStream.close();
                            }
                        } catch (IOException ioe) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
