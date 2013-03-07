package com.rambi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.rambi.core.RambiScriptMachine;

public class MemcacheTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalMemcacheServiceTestConfig());

    @Before
    public void init() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testMemcache() throws InterruptedException {
        // PUT on cache
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("value", "BLA");

        RequestMock req = createMockRequest("PUT", paramsMap);
        ResponseMock responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
                getServiceFile(), "MemcacheTest", null);

        MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
        assertNotNull(cache.get("value"));
        assertTrue(cache.get("value") instanceof String);
        assertEquals("BLA", cache.get("value"));

        // GET on cache
        req = createMockRequest("GET", new HashMap<String, String>());
        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
                getServiceFile(), "MemcacheTest", null);

        String data = responseMock.getOutData();
        assertNotNull(data);
        assertEquals("BLA", data);

        // DELETE on cache
        req = createMockRequest("DELETE", new HashMap<String, String>());
        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
                getServiceFile(), "MemcacheTest", null);
        assertNull(cache.get("value"));

        // PUT on cache with expiration
        paramsMap = new HashMap<String, String>();
        paramsMap.put("value", "BLA");
        paramsMap.put("expirationInMillis", "10");

        req = createMockRequest("PUT", paramsMap);
        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
                getServiceFile(), "MemcacheTest", null);

        cache = MemcacheServiceFactory.getMemcacheService();

        // Make sure data is expired
        Thread.sleep(15);
        assertNull(cache.get("value"));

    }

    private RequestMock createMockRequest(String method,
            final Map<String, String> paramsMap) {
        return new RequestMock("mock/mock", method) {

            private Map<String, String> params = paramsMap;

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };
    }

    public InputStream getServiceFile() {
        return MemcacheTest.class.getClassLoader().getResourceAsStream(
                "com/rambi/MemcacheTest.js");
    }

}
