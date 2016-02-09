package com.beuth.ebp.smartshop;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface GithubService {

    String ENDPOINT = "http://141.64.163.251:8080/SmartShopServer/rest";

    @GET("/api/get")
    Items listItems();

    @GET("/order/get")
    Orders listOrder();

    @GET("/order/confirm/{number}")
    Response ConfirmOrder(@Path("number") int number);

    @POST("/api/getItems")
    Items getItemsList(@Header("token") String token);

    @GET("/api/getSessionID")
    Response sessionIDResponse();

    @GET("/api/getToken/{sessionid}")
    Response getTokenResponse(@Path("sessionid") String sessionid);

    @POST("/addItem/put")
    Response getAddItemResponse(@Header("token") String token, @Header("title") String title, @Header("description") String description, @Header("price") String price);

}
