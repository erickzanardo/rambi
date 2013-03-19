package com.rambi;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class URLFectherTest {
    private TestServer server = new TestServer();

    @Before
    public void setup() throws Exception {
        server.init();
    }

    @Test
    public void test() throws Exception {
        Response response = HttpUtils.get("http://localhost:8080/services/gaeurlfecther.js?echo=Sentdata");

        JsonObject obj = new JsonParser().parse(response.getAsString()).getAsJsonObject();

        assertEquals("Sentdata", obj.get("echo").getAsString());
        assertEquals("OK", obj.get("status").getAsString());
    }

    @After
    public void after() throws Exception {
        server.end();
    }

}
