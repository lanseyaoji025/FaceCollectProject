package com.shuding.fastnetworklibrary.net.download;

import okhttp3.ResponseBody;

/**
 * Created by tangpeng on 2018/6/14.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess(ResponseBody responseBody);

    void onFail(String message);

    void onComplete();
}