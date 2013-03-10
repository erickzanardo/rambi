package com.rambi;

import static org.junit.Assert.assertEquals;

import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MemcacheTest {

    private TestServer server = new TestServer();

    @Before
    public void init() throws NamingException {
        server.init();
    }

    @Test
    public void testMemcache() throws InterruptedException {
        // PUT on cache
        HttpUtils.put("http://localhost:8080/services/MemcacheTest.js?value=BLA");

        Response response = HttpUtils.get("http://localhost:8080/memcache?key=value");
        assertEquals("BLA", response.getAsString());

        // GET on cache
        response = HttpUtils.get("http://localhost:8080/services/MemcacheTest.js");
        assertEquals("BLA", response.getAsString());

        // DELETE on cache
        HttpUtils.delete("http://localhost:8080/services/MemcacheTest.js");
        response = HttpUtils.get("http://localhost:8080/memcache?key=value");
        assertEquals("", response.getAsString());

        // PUT on cache with expiration
        HttpUtils.put("http://localhost:8080/services/MemcacheTest.js?value=BLA&expirationInMillis=10");

        response = HttpUtils.get("http://localhost:8080/memcache?key=value");
        assertEquals("", response.getAsString());
    }

    @After
    public void end() throws NamingException {
        server.end();
    }
}

