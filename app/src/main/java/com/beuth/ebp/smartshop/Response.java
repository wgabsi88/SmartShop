package com.beuth.ebp.smartshop;

import java.util.List;

/**
 * Created by florentchampigny on 05/03/15.
 */
public class Response {
    private String body;
    private int status;

    public Response(String body, int status) {
        this.body = body;
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}