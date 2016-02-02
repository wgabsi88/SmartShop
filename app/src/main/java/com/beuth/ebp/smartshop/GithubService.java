package com.beuth.ebp.smartshop;

import retrofit.http.GET;
import retrofit.http.Path;

public interface GithubService {

public static final String ENDPOINT = "http://192.168.1.8:8080/SmartShopServer";


    @GET("/rest/api/get")
    Items listItems();

    @GET("/rest/api/getSessionID")
    Response sessionISResponse();

    @GET("/rest/api/getToken/{sessionid}")
    Response getTokenResponse(@Path("sessionid") String sessionid);

}
