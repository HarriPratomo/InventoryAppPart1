package com.example.appsdata.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("loginuser/user/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
