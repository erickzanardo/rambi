package com.rambi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpRequestAndResponseTest {
    private TestServer server = new TestServer();

    @Before
    public void setup() throws Exception {
        server.init();
    }

    @Test
    public void test() throws Exception {
        // Check server
        Response resp = HttpUtils.get("http://localhost:8080/services/AssertHttp.js?bla=true&ble=false&ble=false");

        JsonObject obj = (JsonObject) new JsonParser().parse(resp.getAsString());

        // Request assert
        assertEquals("127.0.0.1", obj.get("localAddr").getAsString());
        assertEquals("127.0.0.1", obj.get("localName").getAsString());

        assertEquals("127.0.0.1", obj.get("remoteAddr").getAsString());
        assertEquals("127.0.0.1", obj.get("remoteHost").getAsString());
        assertEquals("HTTP/1.1", obj.get("protocol").getAsString());
        assertEquals("GET", obj.get("method").getAsString());
        assertEquals("/services/AssertHttp.js", obj.get("requestURI").getAsString());
        assertEquals("bla=true&ble=false&ble=false", obj.get("queryString").getAsString());
        assertEquals("", obj.get("contextPath").getAsString());

        JsonObject paramsMap = obj.get("paramsMap").getAsJsonObject();
        assertEquals("true", paramsMap.get("bla").getAsString());
        assertTrue(paramsMap.get("ble").isJsonArray());
        assertEquals("false", paramsMap.get("ble").getAsJsonArray().get(0).getAsString());
        assertEquals("false", paramsMap.get("ble").getAsJsonArray().get(1).getAsString());

        // Response assert
        assertEquals("UTF-8", obj.get("characterEncoding").getAsString());
        assertEquals("application/json;charset=UTF-8", obj.get("contentType").getAsString());
        assertTrue(obj.get("hasHeader").getAsBoolean());

        assertEquals("application/json;charset=UTF-8", resp.getHeader("Content-Type"));

        assertEquals("10000", resp.getHeader("Content-Length"));

        assertTrue(resp.getHeader("Date-Header").startsWith("Thu, 01 Jan 1970"));
        assertEquals("1", resp.getHeader("Int-Header"));
        assertEquals("Test", resp.getHeader("Test-Header"));

        assertEquals("CookieTestValue", resp.getCookie("My-Cookie").getValue());

        assertEquals(403, resp.getStatus());

        // Test redirect
        resp = HttpUtils.get("http://localhost:8080/services/Redirect.js");
        JsonObject o = (JsonObject) new JsonParser().parse(resp.getAsString());
        assertEquals("OK", o.get("status").getAsString());

        // Test forward
        resp = HttpUtils.get("http://localhost:8080/services/Forward.js");
        o = (JsonObject) new JsonParser().parse(resp.getAsString());
        assertEquals("OK", o.get("status").getAsString());

    }

    @After
    public void after() throws Exception {
        server.end();
    }

}
