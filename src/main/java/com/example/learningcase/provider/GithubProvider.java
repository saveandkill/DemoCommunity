package com.example.learningcase.provider;

import com.alibaba.fastjson.JSON;
import com.example.learningcase.dto.AccessTokenDTO;
import com.example.learningcase.dto.GithubUserDTO;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient.Builder().callTimeout(100, TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS).build();

        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string =response.body().string();
            String[] tokenResponseParaArray=string.split("&");
            String[] tokenKeyValue=tokenResponseParaArray[0].split("=");
            String token=tokenKeyValue[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Unknown error");
        return null;
    }
/*
   根据token向Github资源服务器请求User信息
 */
    public GithubUserDTO getUser(String token)
    {
        OkHttpClient client = new OkHttpClient.Builder().callTimeout(100, TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS).build();
        System.out.println("token is "+ token);
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization","token "+token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String userInfoResponse=response.body().string();
            System.out.println(userInfoResponse);
            return JSON.parseObject(userInfoResponse,GithubUserDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

