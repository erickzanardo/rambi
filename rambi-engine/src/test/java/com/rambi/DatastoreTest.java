package com.rambi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rambi.core.RambiScriptMachine;

public class DatastoreTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    private String appConfig = "com/rambi/DatastoreTestConfig.js";

    @Before
    public void init() {
        RambiScriptMachine.getInstance().init(appConfig);
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testDatastore() throws EntityNotFoundException {
        // PUT
        HttpServletRequest req = new RequestMock("mock/mock", "PUT") {

            private Map<String, String> params = new HashMap<String, String>();
            {
                params.put("key", "5");
            }

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };

        ResponseMock responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        DatastoreService service = DatastoreServiceFactory
                .getDatastoreService();

        assertEquals("5", responseMock.getOutData());
        Entity entity = service.get(KeyFactory.createKey("Kind", 5));
        assertEquals("PUT - value", entity.getProperty("value"));
        assertNotNull(entity);

        // POST
        req = new RequestMock("mock/mock", "POST");
        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        final long id = Long.parseLong(responseMock.getOutData());
        entity = service.get(KeyFactory.createKey("Kind", id));
        assertNotNull(entity);
        assertEquals("POST - value", entity.getProperty("value"));

        // GET
        req = new RequestMock("mock/mock", "GET") {

            private Map<String, String> params = new HashMap<String, String>();
            {
                // nonexistent ID
                params.put("key", "656565656565656565");
                params.put("kind", "Kind");
            }

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);
        assertEquals("null", responseMock.getOutData());

        req = new RequestMock("mock/mock", "GET") {

            private Map<String, String> params = new HashMap<String, String>();
            {
                params.put("key", String.valueOf(id));
                params.put("kind", "Kind");
            }

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        // Testing datatypes
        JsonObject resp = (JsonObject) new JsonParser().parse(responseMock
                .getOutData());
        assertEquals("POST - value", resp.get("value").getAsString());
        assertEquals(1, resp.get("numberValue").getAsInt());
        assertEquals(0.1d, resp.get("decimalValue").getAsDouble(), 0);

        JsonArray asJsonArray = resp.get("values").getAsJsonArray();
        assertEquals(1, asJsonArray.get(0).getAsInt());
        assertEquals(2, asJsonArray.get(1).getAsInt());
        assertEquals(3, asJsonArray.get(2).getAsInt());

        assertTrue(resp.get("valid").getAsBoolean());
        assertTrue(resp.get("date").getAsString().startsWith("2013-01-09"));

        entity = service.get(KeyFactory.createKey("Kind", id));
        assertNotNull(entity);

        assertTrue(entity.getProperty("numberValue") instanceof Long);
        assertTrue(entity.getProperty("decimalValue") instanceof Double);
        assertTrue(entity.getProperty("values") instanceof ArrayList);
        assertTrue(entity.getProperty("valid") instanceof Boolean);
        assertTrue(entity.getProperty("date") instanceof Date);

        // Queries
        createMockEntities();

        // Query 1
        // LESS THAN
        req = new RequestMock("mock/mock", "GET") {

            private Map<String, String> params = new HashMap<String, String>();
            {
                params.put("query1", "");
            }

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        JsonArray result = (JsonArray) new JsonParser().parse(responseMock
                .getOutData());

        assertEquals(2, result.size());

        JsonObject asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());

        // Query 2
        // LESS THAN OR EQUALS

        // Query 3
        // GREATER THAN

        // Query 4
        // GREATER THAN OR EQUALS

        // Query 5
        // EQUAL

        // Query 6
        // NOT EQUAL

        // Query 7
        // IN

        // Query Builder 1
        // LESS THAN

        // Query Builder 2
        // LESS THAN OR EQUALS

        // Query Builder 3
        // GREATER THAN

        // Query Builder 4
        // GREATER THAN OR EQUALS

        // Query Builder 5
        // EQUAL

        // Query Builder 6
        // NOT EQUAL

        // Query Builder 7
        // IN

    }

    private void createMockEntities() {
        DatastoreService service = DatastoreServiceFactory
                .getDatastoreService();

        for (int i = 0; i < 3; i++) {
            Entity e = new Entity("Mock");
            e.setProperty("number", i);
            e.setProperty("string", "test" + i);
            service.put(e);
        }
    }
}
