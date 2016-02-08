package com.beuth.ebp.smartshop;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface GithubService {

    String ENDPOINT = "http://192.168.2.123:8080/SmartShopServer/rest";

    @GET("/api/get")
    Items listItems();

    @GET("/order/od")
    Orders listOrder();

    @POST("/api/getItems")
    Items getItemsList(@Header("token") String token);

    @GET("/api/getSessionID")
    Response sessionIDResponse();

    @GET("/api/getToken/{sessionid}")
    Response getTokenResponse(@Path("sessionid") String sessionid);

    @GET("/addItem/put")
    Response getAddItemResponse();

}
