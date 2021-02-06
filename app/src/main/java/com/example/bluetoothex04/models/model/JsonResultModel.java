package com.example.bluetoothex04.models.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class JsonResultModel<T> {
    private String code;
    private String status;
    private String message;
    private int count;
    private ArrayList<T> data;
}
