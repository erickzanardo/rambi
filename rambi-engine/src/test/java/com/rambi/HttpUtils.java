package com.rambi;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpUtils {
    public static String get(String url) {
        HttpClient client = new HttpClient();

        HttpMethod method = new GetMethod(url);
        try {
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
