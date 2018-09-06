package com.shuding.fastnetworklibrary.net.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shuding.fastnetworklibrary.BuildConfig;
import com.shuding.fastnetworklibrary.net.Interceptor.HttpCacheInterceptor;
import com.shuding.fastnetworklibrary.net.Interceptor.HttpHeaderInterceptor;
import com.shuding.fastnetworklibrary.net.converter.GsonConverterFactory;
import com.shuding.fastnetworklibrary.utils.Utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by tangpeng on 2018/6/12.
 */

public class RetrofitUtils {


    public static OkHttpClient.Builder getOkHttpClientBuilder() {

        File file=new File(Utils.getContext().getCacheDir(),"cache");

        Cache cache=new Cache(file,1024 * 1024 * 100);//100M

        return new OkHttpClient.Builder()
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(getInterceptor())   //调试网络请求日志
//                .addInterceptor(new HttpHeaderInterceptor()) //在此处增加http请求头
                .addNetworkInterceptor(new HttpCacheInterceptor()) //配置缓存
                .cache(cache);

    }

    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        OkHttpClient okHttpClient = RetrofitUtils.getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl);
    }

    private static  HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }
}
