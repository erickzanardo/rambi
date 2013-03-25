package com.rambi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rambi.core.RambiScriptMachine;

public class WebAppTest {

    private TestServer server = new TestServer();

    @Before
    public void setup() throws Exception {
        server.init();
    }

    @Test
    public void test() throws Exception {
        System.setProperty(RambiScriptMachine.RAMBI_DEVEL_PARAM, "true");

        // Check server
        Response resp = HttpUtils.get("http://localhost:8080/check.json");
        JsonParser jsonParser = new JsonParser();
        JsonObject o = (JsonObject) jsonParser.parse(resp.getAsString());
        assertEquals("OK", o.get("status").getAsString());

        // Test webapp
        assertEquals("Hello!", HttpUtils.get("http://localhost:8080/services/echo.js?param=Hello!").getAsString());

        assertEquals("Received:Hello!", HttpUtils.get("http://localhost:8080/services/webModule.js?param=Hello!")
                .getAsString());

        // Invalid import webmodule
        resp = HttpUtils.get("http://localhost:8080/services/invalidWebModule.js?param=Hello!");
        assertEquals(500, resp.getStatus());
        assertTrue(resp.getAsString().contains("File not found to import (/services/invalidWebModule.js#1"));

        // Invalid import module
        resp = HttpUtils.get("http://localhost:8080/services/invalidModule.js?param=Hello!");
        assertEquals(500, resp.getStatus());
        assertTrue(resp.getAsString().contains("File not found to import (/services/invalidModule.js#1"));

        assertEquals(404, HttpUtils.get("http://localhost:8080/services/thisDoesNotExist.css").getStatus());
        assertEquals(404, HttpUtils.get("http://localhost:8080/services/thisShouldNotBeHere.css").getStatus());

        // Testing cache
        assertNull(RambiScriptMachine.getInstance().getCache().get("/services/echo.js"));

        System.clearProperty(RambiScriptMachine.RAMBI_DEVEL_PARAM);

        HttpUtils.get("http://localhost:8080/services/echo.js?param=Hello!");
        assertNotNull(RambiScriptMachine.getInstance().getCache().get("/services/echo.js"));
    }

    @After
    public void after() throws Exception {
        server.end();
    }
}
