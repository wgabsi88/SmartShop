package com.beuth.ebp.smartshop;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface GithubService {

 String ENDPOINT = "http://192.168.2.125:8080/SmartShopServer";


    @GET("/rest/api/get")
    Items listItems();

    @GET("/rest/order/od")
    Orders listOrder();



   @POST("/rest/api/getItems/1")
   Items getItemsList(@Header("token") String token);

    @GET("/rest/api/getSessionID")
    Response sessionIDResponse();

    @GET("/rest/api/getToken/{sessionid}")
    Response getTokenResponse(@Path("sessionid") String sessionid);

}
