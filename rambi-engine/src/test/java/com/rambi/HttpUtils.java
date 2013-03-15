package com.rambi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

public class HttpUtils {

    public static Response get(String url) {
        return service(new Request().url(url).method("GET"));
    }

    public static Response put(String url) {
        return service(new Request().url(url).method("PUT"));
    }

    public static Response delete(String url) {
        return service(new Request().url(url).method("DELETE"));
    }

    public static Response post(String url, Map<String, String> params) {

        Request req = new Request();
        req.url(url).method("POST");

        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                req.param(entry.getKey(), entry.getValue());
            }
        }

        return service(req);
    }

    public static Response service(Request req) {
        HttpClient client = new HttpClient();

        String m = req.getMethod();
        String url = req.getUrl();

        HttpMethod method = null;
        if ("GET".equals(m)) {
            method = new GetMethod(url);
        } else if ("PUT".equals(m)) {
            method = new PutMethod(url);
        } else if ("DELETE".equals(m)) {
            method = new DeleteMethod(url);
        } else if ("POST".equals(m)) {
            PostMethod postMethod = new PostMethod(url);

            Set<Entry<String, String>> entrySet = req.getParams().entrySet();
            for (Entry<String, String> entry : entrySet) {
                postMethod.addParameter(entry.getKey(), entry.getValue());
            }

            method = postMethod;
        }

        try {
            client.executeMethod(method);
            return new Response(method, client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

class Request {
    private String url;
    private String method = "GET";
    private Map<String, String> params = new HashMap<String, String>();

    public Request url(String url) {
        this.url = url;
        return this;
    }

    public Request method(String method) {
        this.method = method;
        return this;
    }

    public Request param(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

}

class Response {

    private HttpMethod method;
    private HttpClient client;

    public Response(HttpMethod method, HttpClient client) {
        this.method = method;
        this.client = client;
    }

    public String getHeader(String key) {
        Header responseHeader = method.getResponseHeader(key);
        if (responseHeader != null) {
            return responseHeader.getValue();
        }
        return null;
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

    public Cookie getCookie(String cookie) {
        Cookie[] cookies = client.getState().getCookies();
        for (Cookie c : cookies) {
            if (cookie.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }
}
