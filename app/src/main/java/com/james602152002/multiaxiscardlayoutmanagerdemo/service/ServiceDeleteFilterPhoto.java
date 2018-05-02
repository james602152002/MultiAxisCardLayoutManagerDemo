package com.james602152002.multiaxiscardlayoutmanagerdemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ServiceDeleteFilterPhoto extends IntentService {


    public ServiceDeleteFilterPhoto() {
        super("ServiceDeleteFilterPhoto");
    }

    public ServiceDeleteFilterPhoto(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final File file = new File(((Uri) intent.getParcelableExtra("uri")).getPath());
        final CompositeDisposable disposable = new CompositeDisposable();
        Observable.interval(5000, TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(Long aLong) {
                if (file != null && file.exists()) {
                    file.delete();
                    disposable.dispose();
                    disposable.clear();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
