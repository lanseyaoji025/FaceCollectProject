package com.shuding.fastnetworklibrary.net.common;

import retrofit2.Retrofit;

/**
 * Created by tangpeng on 2018/6/12.
 */

public class GetNetworkApi {

    public static  <T> T getApiSerivce(Class<T> cls,String baseUrl){

        Retrofit build = RetrofitUtils.getRetrofitBuilder(baseUrl).build();

        return build.create(cls);
    }
}
