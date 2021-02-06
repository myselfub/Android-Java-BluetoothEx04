package com.example.bluetoothex04.models.repository;

import com.example.bluetoothex04.models.model.VentilationTimeModel;
import com.example.bluetoothex04.models.network.RetrofitRestAPI;
import com.example.bluetoothex04.models.network.RetrofitRestAPIService;
import com.example.bluetoothex04.viewmodels.VentilationTimeViewModel;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VentilationTimeRepository {
    private RetrofitRestAPI retrofitRestAPIService;
    private CompositeDisposable disposable;
    private VentilationTimeViewModel ventilationTimeViewModel;

    private VentilationTimeRepository() {
    }

    public VentilationTimeRepository(String url, VentilationTimeViewModel ventilationTimeViewModel) {
        retrofitRestAPIService = RetrofitRestAPIService.getInstance().getRetrofitRestAPI(url);
        this.ventilationTimeViewModel = ventilationTimeViewModel;
    }

    public void callVentilationTime(Map<String, String> queryMap) {
        if (disposable == null || disposable.isDisposed()) {
//            disposable.clear();
            disposable = new CompositeDisposable();
        }
        disposable.add(retrofitRestAPIService.ventilationtime(queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> onNext(result.getData()),
                        e -> onError(e), () -> onComplete()));
    }

    private void onNext(ArrayList<VentilationTimeModel> arrayList) {
        if (arrayList != null) {
            ventilationTimeViewModel.setMutableLiveData(arrayList);
        }
    }

    private void onError(Throwable t) {
        t.printStackTrace();
    }

    private void onComplete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}