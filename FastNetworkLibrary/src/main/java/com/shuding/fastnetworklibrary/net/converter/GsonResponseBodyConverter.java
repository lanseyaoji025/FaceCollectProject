/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shuding.fastnetworklibrary.net.converter;

import com.google.gson.TypeAdapter;
import com.shuding.fastnetworklibrary.net.common.BasicResponse;
import com.shuding.fastnetworklibrary.net.exception.NoDataExceptionException;
import com.shuding.fastnetworklibrary.net.exception.ServerResponseException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {

    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {
            BasicResponse response = (BasicResponse) adapter.fromJson(value.charStream());
            //在这里根据后台返回的json修改返回的逻辑
            if (response.getCode()!=200) {
                throw new ServerResponseException(response.getCode(), response.getMsg());
            }else {
                if(response.getContent()!=null){
                    return response.getContent();
                } else{
                    throw new NoDataExceptionException();
                }
            }
        } finally {
            value.close();
        }
    }
}
