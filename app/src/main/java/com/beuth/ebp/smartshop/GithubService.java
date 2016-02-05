package com.beuth.ebp.smartshop;

import retrofit.http.GET;
import retrofit.http.Path;

public interface GithubService {

 String ENDPOINT = "http://192.168.2.123:8080/SmartShopServer";


    @GET("/rest/api/get")
    Items listItems();

    @GET("/rest/api/getSessionID")
    Response sessionIDResponse();

    @GET("/rest/api/getToken/{sessionid}")
    Response getTokenResponse(@Path("sessionid") String sessionid);

}
