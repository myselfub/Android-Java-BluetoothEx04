package com.example.bluetoothex04.util;

import android.util.Log;

// https://www.programmersought.com/article/58692736550/
// http://gnujava.com/board/article_view.jsp?article_no=4175&board_no=1&table_cd=EPAR01&table_no=01

public class CRC16Utils {
    private static final String TAG = "CRC16Utils";
    private final byte[] STX = new byte[]{(byte) 0xFF};
    private int crc16_ccitt_table[] = {0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7,
            0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c, 0xd1ad, 0xe1ce, 0xf1ef,
            0x1231, 0x0210, 0x3273, 0x2252, 0x52b5, 0x4294, 0x72f7, 0x62d6,
            0x9339, 0x8318, 0xb37b, 0xa35a, 0xd3bd, 0xc39c, 0xf3ff, 0xe3de,
            0x2462, 0x3443, 0x0420, 0x1401, 0x64e6, 0x74c7, 0x44a4, 0x5485,
            0xa56a, 0xb54b, 0x8528, 0x9509, 0xe5ee, 0xf5cf, 0xc5ac, 0xd58d,
            0x3653, 0x2672, 0x1611, 0x0630, 0x76d7, 0x66f6, 0x5695, 0x46b4,
            0xb75b, 0xa77a, 0x9719, 0x8738, 0xf7df, 0xe7fe, 0xd79d, 0xc7bc,
            0x48c4, 0x58e5, 0x6886, 0x78a7, 0x0840, 0x1861, 0x2802, 0x3823,
            0xc9cc, 0xd9ed, 0xe98e, 0xf9af, 0x8948, 0x9969, 0xa90a, 0xb92b,
            0x5af5, 0x4ad4, 0x7ab7, 0x6a96, 0x1a71, 0x0a50, 0x3a33, 0x2a12,
            0xdbfd, 0xcbdc, 0xfbbf, 0xeb9e, 0x9b79, 0x8b58, 0xbb3b, 0xab1a,
            0x6ca6, 0x7c87, 0x4ce4, 0x5cc5, 0x2c22, 0x3c03, 0x0c60, 0x1c41,
            0xedae, 0xfd8f, 0xcdec, 0xddcd, 0xad2a, 0xbd0b, 0x8d68, 0x9d49,
            0x7e97, 0x6eb6, 0x5ed5, 0x4ef4, 0x3e13, 0x2e32, 0x1e51, 0x0e70,
            0xff9f, 0xefbe, 0xdfdd, 0xcffc, 0xbf1b, 0xaf3a, 0x9f59, 0x8f78,
            0x9188, 0x81a9, 0xb1ca, 0xa1eb, 0xd10c, 0xc12d, 0xf14e, 0xe16f,
            0x1080, 0x00a1, 0x30c2, 0x20e3, 0x5004, 0x4025, 0x7046, 0x6067,
            0x83b9, 0x9398, 0xa3fb, 0xb3da, 0xc33d, 0xd31c, 0xe37f, 0xf35e,
            0x02b1, 0x1290, 0x22f3, 0x32d2, 0x4235, 0x5214, 0x6277, 0x7256,
            0xb5ea, 0xa5cb, 0x95a8, 0x8589, 0xf56e, 0xe54f, 0xd52c, 0xc50d,
            0x34e2, 0x24c3, 0x14a0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405,
            0xa7db, 0xb7fa, 0x8799, 0x97b8, 0xe75f, 0xf77e, 0xc71d, 0xd73c,
            0x26d3, 0x36f2, 0x0691, 0x16b0, 0x6657, 0x7676, 0x4615, 0x5634,
            0xd94c, 0xc96d, 0xf90e, 0xe92f, 0x99c8, 0x89e9, 0xb98a, 0xa9ab,
            0x5844, 0x4865, 0x7806, 0x6827, 0x18c0, 0x08e1, 0x3882, 0x28a3,
            0xcb7d, 0xdb5c, 0xeb3f, 0xfb1e, 0x8bf9, 0x9bd8, 0xabbb, 0xbb9a,
            0x4a75, 0x5a54, 0x6a37, 0x7a16, 0x0af1, 0x1ad0, 0x2ab3, 0x3a92,
            0xfd2e, 0xed0f, 0xdd6c, 0xcd4d, 0xbdaa, 0xad8b, 0x9de8, 0x8dc9,
            0x7c26, 0x6c07, 0x5c64, 0x4c45, 0x3ca2, 0x2c83, 0x1ce0, 0x0cc1,
            0xef1f, 0xff3e, 0xcf5d, 0xdf7c, 0xaf9b, 0xbfba, 0x8fd9, 0x9ff8,
            0x6e17, 0x7e36, 0x4e55, 0x5e74, 0x2e93, 0x3eb2, 0x0ed1, 0x1ef0
    };

    public byte[] getSTX() {
        return STX;
    }

    /**
     * Using the look-up table, generates a CRC checksum based on the data
     *
     * @return
     * @Param message check value for the byte to be verified
     */
    public String CRC_16_X25(byte[] message) {
        int crc_reg = 0xffff; // CRC check when the initial value
        for (int i = 0; i < message.length; i++) {
            crc_reg = (crc_reg >> 8) ^ crc16_ccitt_table[(crc_reg ^ message[i]) & 0xff];
        }
        return Integer.toHexString(~crc_reg & 0xffff).toUpperCase();
    }

    public String CRC_16_CCITT_FALSE(byte[] bytes, int length) {
        int crc = 0xffff; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        // output String word hex
        String strCrc = Integer.toHexString(crc).toUpperCase();
        return strCrc;
    }

    public String CRC_16_XMODEM(byte[] bytes, int length) {
        int crc = 0x0000; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        // output String word hex
        String strCrc = Integer.toHexString(crc).toUpperCase();
        return strCrc;
    }

    public int CRC_16_BigEndian(byte[] bytes, int length) {
        int crc16 = CRC_16_Check(bytes, length);
        return ((crc16 << 8) & 0xFF00) | ((crc16 >> 8) & 0x00FF);
    }

    public int CRC_16_Check(byte[] bytes, int length) {
        int crc16;
        int i = 0;
        for (crc16 = 0; length > 0; length--) {
            crc16 = crc16_ccitt_table[(crc16 ^ bytes[i++]) & 0xff] ^ (crc16 >> 0x0008);
        }
        return crc16;
    }

    // hexadecimal byte array to a hexadecimal string
    public String bytes2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
//            String hex = String.format("%02X", b & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // hexadecimal form a string of hexadecimal byte data transfer
    public byte[] hexStr2BinArr(String hexString) {
        // hexString rounding length of 2, as the length of bytes
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
        }
        len = hexString.length() / 2;
        byte[] bytes = new byte[len];
//        byte high = 0; // the high four byte
//        byte low = 0; // low four bytes
//        for (int i = 0; i < len; i++) {
//            // get the right four high
//            high = (byte) ((hexString.indexOf(hexString.charAt(2 * i))) << 4);
//            low = (byte) hexString.indexOf(hexString.charAt(2 * i + 1));
//            bytes[i] = (byte) (high | low); // do the high-status or operation
//        }
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) ((Character.digit(hexString.charAt(2 * i), 16) << 4) + Character.digit(hexString.charAt(2 * i + 1), 16));
        }
        return bytes;
    }

    public void crc16UtilsExam() {
        byte[] data2 = new byte[]{(byte) 0xAA, 0x0C, 0x01, 0x00, 0x01, 0x00, 0x00, 0x04, 0x05, 0x17, 0x05, 0x01,
                (byte) 0xA0, (byte) 0x86, 0x01, 0x00};
        //E.g:
        //AA 0C 01 00 01 00 00 04 05 17 05 01 A0 86 01 00

        // Use CRC-16 / CCITT-FALSE calculated F2E3
        // Use CRC-16 / XMODEM calculated 98E9
        // Use CRC-16 / X25 calculated A27A
        // verify the results are as follows

        String result1 = CRC_16_CCITT_FALSE(data2, data2.length); //F2E3
        String result2 = CRC_16_XMODEM(data2, data2.length); //98E9
        String result3 = CRC_16_X25(data2); //A27A
        int result4 = CRC_16_Check(data2, data2.length);
        int result5 = CRC_16_BigEndian(data2, data2.length);
        Log.d(TAG, result1);
        Log.d(TAG, result2);
        Log.d(TAG, result3);
        Log.d(TAG, String.valueOf(result4));
        Log.d(TAG, String.valueOf(result5));

        Log.d(TAG, bytes2HexString(data2));
    }

}