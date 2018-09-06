package com.shuding.fastnetworklibrary.net.common;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.shuding.fastnetworklibrary.dialog.LoadingDialog;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tangpeng on 2018/6/12.
 * 进度条
 */

public class ProgressUtils {

    public static <T> ObservableTransformer<T,T> applyProgressBar(@NonNull final Activity activity, String msg){
        final WeakReference<Activity> activityWeakReferenc=new WeakReference<Activity>(activity);
        final LoadingDialog loadingDialog=new LoadingDialog(activityWeakReferenc.get());
        loadingDialog.show(msg);
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return  upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }).doOnTerminate(new Action() {//订阅即将被终止时的监听，无论是正常终止还是异常终止
                    @Override
                    public void run() throws Exception {
                        Activity context;
                        if ((context = activityWeakReferenc.get()) != null
                                && !context.isFinishing()) {
                            loadingDialog.Dismiss();
                        }
                    }
                }) .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(
            @NonNull final Activity activity) {
        return applyProgressBar(activity, "");
    }
}
