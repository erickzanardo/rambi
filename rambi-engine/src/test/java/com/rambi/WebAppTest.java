package com.rambi;

import static org.junit.Assert.assertEquals;

import org.bitumenframework.jettify.JettyServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebAppTest {

    private JettyServer server;

    @Before
    public void setup() throws Exception {
        server = new JettyServer(8080);
        server.setResourceBase("src/test/resources/webapp");
        server.start();
    }

    @Test
    public void test() throws Exception {
        // Check server
        String string = HttpUtils.get("http://localhost:8080/check.json");
        JsonParser jsonParser = new JsonParser();
        JsonObject o = (JsonObject) jsonParser.parse(string);
        assertEquals("OK", o.get("status").getAsString());
    }

    @After
    public void end() throws Exception {
        server.stop();
    }
}
