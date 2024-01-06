package com.example.app;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IApiService {
    @GET
    Call<ResponseBody> hello(@Url String url);

    @Headers("Content-Type: application/json")
    //@Streaming
    @POST("chat/message_array/")
    Call<ResponseBody> chat(
            //@Url String url,
            @Body ChatRequest requestBody
            );
}
