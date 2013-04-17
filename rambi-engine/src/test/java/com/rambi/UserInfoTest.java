package com.rambi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserInfoTest {

    private TestServer server = new TestServer();

    @Before
    public void setup() throws Exception {
        server.init();
    }

    @Test
    public void test() throws Exception {
        Response resp = HttpUtils.get("http://localhost:8080/services/UserInfo.js");
        resp.assertStatus(200);
        JsonParser jsonParser = new JsonParser();
        JsonObject o = (JsonObject) jsonParser.parse(resp.getAsString());

        assertTrue(o.get("logged").getAsBoolean());
        assertFalse(o.get("admin").getAsBoolean());
        assertEquals("example.com", o.get("authDomain").getAsString());
        assertEquals("test@example.com", o.get("email").getAsString());
    }

    @After
    public void after() throws Exception {
        server.end();
    }
}
