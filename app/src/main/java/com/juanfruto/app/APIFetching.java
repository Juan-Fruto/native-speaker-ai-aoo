package com.juanfruto.app;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIFetching {
    private IApiCallback context;
    private static final String BASE_URL = "http://192.168.1.7:8080/api/v1/";
    public APIFetching(IApiCallback context){
        this.context = context;
    }
    public void message(String userMessage){
        // request body
        Message message = new Message().setRole("user").setContent("Hi there");
        ChatRequest chatRequest = new ChatRequest()
                .setPayload(new Message[]{message})
                .setGender("female")
                .setLanguage("english");

        // send request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiService apiService = retrofit.create(IApiService.class);

        Call<ResponseBody> call = apiService.chat(
                //BASE_URL+"/chat/message_array/",
                chatRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("response", response.body().toString());
                    context.onApiSuccess(response.body().byteStream());
                } else {
                    Log.d("server error", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("server error", t.toString());
            }
        });
    }
}

class Message{
    private String role = null;
    private String content = null;

    public String getRole() {
        return role;
    }

    public Message setRole(String role) {
        this.role = role;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }
}
class ChatRequest {
    private Message[] payload;
    private String language;
    private String gender;

    public Message[] getPayload() {
        return payload;
    }

    public ChatRequest setPayload(Message[] payload) {
        this.payload = payload;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public ChatRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public ChatRequest setGender(String gender) {
        this.gender = gender;
        return this;
    }
}
