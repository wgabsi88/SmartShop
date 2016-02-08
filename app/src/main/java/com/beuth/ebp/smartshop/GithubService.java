package com.beuth.ebp.smartshop;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface GithubService {

    String ENDPOINT = "http://192.168.2.123:8080/SmartShopServer/rest";

    @GET("/api/get")
    Items listItems();

    @GET("/order/get")
    Orders listOrder();

    @GET("/order/confirm/{number}")
    Void ConfirmOrder(@Path("number") int number);

    @POST("/api/getItems")
    Items getItemsList(@Header("token") String token);

    @GET("/api/getSessionID")
    Response sessionIDResponse();

    @GET("/api/getToken/{sessionid}")
    Response getTokenResponse(@Path("sessionid") String sessionid);

    @POST("/addItem/put")
    Response getAddItemResponse(@Header("token") String token, @Header("title") String title, @Header("description") String description, @Header("startPrice") String startPrice);

}
