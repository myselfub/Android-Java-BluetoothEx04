package com.example.bluetoothex04.models.network;

import com.example.bluetoothex04.models.model.JsonResultModel;
import com.example.bluetoothex04.models.model.VentilationTimeModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitRestAPI {
    @GET("/air/ventilationtime")
    Observable<JsonResultModel<VentilationTimeModel>> ventilationtime();

    @GET("/air/ventilationtime")
    Observable<JsonResultModel<VentilationTimeModel>> ventilationtime(@QueryMap Map<String, String> queryMap);
}
