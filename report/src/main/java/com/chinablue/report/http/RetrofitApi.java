package com.chinablue.report.http;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;
import rx.Observable;

public interface RetrofitApi {
    @Headers(RetrofitClient.CACHE_CONTROL_AGE + RetrofitClient.CACHE_STALE_SHORT)
    @GET
    Call<String> getCallMap(@Url String path, @QueryMap() Map<String, String> map);

    @FormUrlEncoded
    @POST
    Call<String> postCallMap(@Url String path, @FieldMap Map<String, String> map);

    @Headers(RetrofitClient.CACHE_CONTROL_AGE + RetrofitClient.CACHE_STALE_SHORT)
    @GET
    Observable<Response<String>> getObservableMap(@Url String path, @QueryMap() Map<String, String> map);

    @FormUrlEncoded
    @POST
    Observable<Response<String>> postObservableMap(@Url String path, @FieldMap Map<String, String> map);

    @POST
    Observable<Response<String>> postObservableRequestBody(@Url String path, @Body RequestBody requestBody);

    @POST
    Observable<Response<String>> postObservableObject(@Url String path, @Body Object jsonObject);

    @HTTP(method = "PUT", hasBody = true)
    Observable<Response<String>> putObject(@Url String path, @Body Object jsonObject);

    @HTTP(method = "PUT", hasBody = true)
    Observable<Response<String>> putObservableRequestBody(@Url String path, @Body RequestBody jsonObject);

    @HTTP(method = "DELETE", hasBody = true)
    Observable<Response<String>> deletObservableObject(@Url String path, @Body Object jsonObject);

    @HTTP(method = "DELETE", hasBody = true)
    Observable<Response<String>> deleteObservableMap(@Url String path, @QueryMap() Map<String, String> map);

    @HTTP(method = "DELETE", hasBody = true)
    Observable<Response<String>> deletObservableRequestBody(@Url String path, @Body RequestBody jsonObject);

    @Multipart
    @POST
    Observable<Response<String>> fromObservableMap(@Url String path, @Part List<MultipartBody.Part> partLis);
}
