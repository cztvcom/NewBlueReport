package com.chinablue.report.interceptor;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class StringResponseBodyConverter implements Converter<ResponseBody, String> {


    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            String s = value.string();
            return s;
        } finally {
            value.close();
        }
    }
}