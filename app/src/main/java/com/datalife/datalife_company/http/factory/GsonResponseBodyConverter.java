package com.datalife.datalife_company.http.factory;

import android.util.Log;

import com.datalife.datalife_company.bean.ResultBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            ResultBean result = gson.fromJson(response, ResultBean.class);
            String code = result.getStatus();
            if (code.equals("success")){
                return gson.fromJson(response, type);
            } else {
                Log.d("HttpManager", "返回err==：" + response);
                ResultBean errResponse = gson.fromJson(response, ResultBean.class);
                if(code.equals("error")){
                    throw new ResultException(errResponse.getDesc(), result.getCode());
                }else{
                    throw new ResultException(errResponse.getDesc(), result.getCode());
                }
            }
        }finally {
            value.close();
        }
    }

}
