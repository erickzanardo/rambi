package com.rambi;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;

public class HttpUtils {

    public static Response get(String url) {
        return service(url, "GET");
    }

    public static Response put(String url) {
        return service(url, "PUT");
    }

    public static Response delete(String url) {
        return service(url, "DELETE");
    }

    private static Response service(String url, String m) {
        HttpClient client = new HttpClient();

        HttpMethod method = null;
        if ("GET".equals(m)) {
            method = new GetMethod(url);
        } else if ("PUT".equals(m)) {
            method = new PutMethod(url);
        } else if ("DELETE".equals(m)) {
            method = new DeleteMethod(url);
        }

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
