package com.juanfruto.app;

import android.util.Log;

import com.juanfruto.model.MessageUI;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIFetching {
    private IApiCallback context;
    private static final String BASE_URL = "http://192.168.1.6:8080/api/v1/";
    private static final int TIMEOUT_SECONDS = 1800;
    public APIFetching(IApiCallback context){
        this.context = context;
    }

    private Message[] messageListToMessageArray(List<Message> messageList) {
        Message[] messageArray = new Message[messageList.size()];
        for(int i = 0; i < messageList.size(); i++) {
            messageArray[i] = messageList.get(i);
        }
        return messageArray;
    }
    public void message(String conversationContext, String userRole, String botRole, String userMessage, List<MessageUI> history, String gender, String language){
        // request body
        Message systemMessage = new Message().setRole("system").setContent(conversationContext);
        Message assistantRoleMessage = new Message().setRole("assistant").setContent(botRole);
        Message userRoleMessage = new Message().setRole("user").setContent(userRole);
        Message message = new Message().setRole("user").setContent(userMessage);

        List<Message> conversation = new ArrayList<>();
        conversation.add(systemMessage);
        conversation.add(assistantRoleMessage);
        conversation.add(userRoleMessage);

        for(MessageUI messageUI : history) {
            conversation.add(new Message().setRole(messageUI.isUserMessage() ? "user" : "assistant").setContent(messageUI.getText()));
        }

        conversation.add(message);

        ChatRequest chatRequest = new ChatRequest()
                .setPayload(messageListToMessageArray(conversation))
                .setGender(gender)
                .setLanguage(language);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(APIFetching.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(APIFetching.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(APIFetching.TIMEOUT_SECONDS, TimeUnit.SECONDS);

        OkHttpClient httpClient = httpClientBuilder.build();

        // send request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient) // new line
                .build();

        IApiService apiService = retrofit.create(IApiService.class);

        Call<ResponseBody> call = apiService.chat(
                //BASE_URL+"/chat/message_array/",
                chatRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String decodedGptResponse = response.headers().get("X-GPT-Response");
                    try {
                        String gptResponse = java.net.URLDecoder.decode(decodedGptResponse, "UTF-8");
                        Log.d("response", gptResponse);
                        context.onApiSuccess(response.body().byteStream(), gptResponse);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
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
