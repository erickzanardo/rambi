package com.rambi;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpUtils {
    public static Response get(String url) {
        HttpClient client = new HttpClient();

        HttpMethod method = new GetMethod(url);
        try {
            client.executeMethod(method);
            return new Response(method);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

class Response {

    private HttpMethod method;

    public Response(HttpMethod method) {
        this.method = method;
    }

    public int getStatus() {
        return method.getStatusCode();
    }

    public String getAsString() {
        try {
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
