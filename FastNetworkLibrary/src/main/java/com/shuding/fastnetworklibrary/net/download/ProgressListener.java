package com.shuding.fastnetworklibrary.net.download;

/**
 * Created by tangpeng on 2018/6/14.
 */

public interface ProgressListener {

    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
