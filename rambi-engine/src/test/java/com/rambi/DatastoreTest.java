package com.rambi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

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
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();

        // PUT
        assertPut(service);

        // POST
        final long id = assertPost(service);

        // GET
        assertGet(service, id);

        // Queries
        createQueryMockEntities();

        // Query 1
        // LESS THAN
        assertLessThan(false);
        assertLessThan(true);

        // Query 2
        // LESS THAN OR EQUALS
        assertLessThanOrEquals(false);
        assertLessThanOrEquals(true);

        // Query 3
        // GREATER THAN
        assertGreatThan(false);
        assertGreatThan(true);

        // Query 4
        // GREATER THAN OR EQUALS
        assertGreaterOtEqualsThan(false);
        assertGreaterOtEqualsThan(true);

        // Query 5
        // EQUAL
        assertEqual(false);
        assertEqual(true);

        // Query 6
        // NOT EQUAL
        assertNotEqual(false);
        assertNotEqual(true);

        // Query 7
        // IN
        assertIn(false);
        assertIn(true);

        // Query 8
        assertMultiple(false);
        assertMultiple(true);

        // Query 9
        assertRange(false);
        assertRange(true);

        // Query 10
        assertNoFilter(false);
        assertNoFilter(true);
    }

    private long assertPost(DatastoreService service)
            throws EntityNotFoundException {
        HttpServletRequest req;
        ResponseMock responseMock;
        Entity entity;
        req = new RequestMock("mock/mock", "POST");
        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        final long id = Long.parseLong(responseMock.getOutData());
        entity = service.get(KeyFactory.createKey("Kind", id));
        assertNotNull(entity);
        assertEquals("POST - value", entity.getProperty("value"));
        return id;
    }

    private void assertGet(DatastoreService service, final long id)
            throws EntityNotFoundException {
        HttpServletRequest req;
        ResponseMock responseMock;
        Entity entity;
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

        // Testing datatypes and embed _id
        JsonObject resp = (JsonObject) new JsonParser().parse(responseMock.getOutData());
        assertEquals("POST - value", resp.get("value").getAsString());
        assertEquals(1, resp.get("numberValue").getAsInt());
        assertEquals(0.1d, resp.get("decimalValue").getAsDouble(), 0);

        // embed id
        assertNotNull(resp.get("_id"));
        assertEquals(id, resp.get("_id").getAsLong());

        JsonArray asJsonArray = resp.get("values").getAsJsonArray();
        assertEquals(1, asJsonArray.get(0).getAsInt());
        assertEquals(2, asJsonArray.get(1).getAsInt());
        assertEquals(3, asJsonArray.get(2).getAsInt());

        assertTrue(resp.get("valid").getAsBoolean());
        assertTrue(resp.get("date").getAsString().startsWith("2013-01-09"));

        entity = service.get(KeyFactory.createKey("Kind", id));
        assertNotNull(entity);

        assertNull(entity.getProperty("_id"));
        assertTrue(entity.getProperty("numberValue") instanceof Long);
        assertTrue(entity.getProperty("decimalValue") instanceof Double);
        assertTrue(entity.getProperty("values") instanceof ArrayList);
        assertTrue(entity.getProperty("valid") instanceof Boolean);
        assertTrue(entity.getProperty("date") instanceof Date);
    }

    private void assertPut(DatastoreService service)
            throws EntityNotFoundException {
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

        final JsonObject obj = (JsonObject) new JsonParser().parse(responseMock.getOutData());

        assertEquals(5, obj.get("_id").getAsLong());

        Entity entity = service.get(KeyFactory.createKey("Kind", 5));
        assertNotNull(entity);
        assertEquals("PUT - value", entity.getProperty("value"));

        // Updated
        req = new RequestMock("mock/mock", "PUT") {

            private Map<String, String> params = new HashMap<String, String>();
            {
                params.put("data", obj.toString());
            }

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        entity = service.get(KeyFactory.createKey("Kind", 5));
        assertNotNull(entity);
        assertEquals("PUT - value updated", entity.getProperty("value"));
    }

    private void assertNoFilter(boolean b) {

        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query10");
        } else {
            req = createQueryMockRequest("query10", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(3, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        assertEquals(2, asJsonObject.get("_id").getAsInt());

        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
        assertEquals(3, asJsonObject.get("_id").getAsInt());

        asJsonObject = result.get(2).getAsJsonObject();
        assertEquals(2, asJsonObject.get("number").getAsInt());
        assertEquals(4, asJsonObject.get("_id").getAsInt());
    
    }

    private void assertRange(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query9");
        } else {
            req = createQueryMockRequest("query9", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(1, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
    }

    private void assertMultiple(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query8");
        } else {
            req = createQueryMockRequest("query8", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(1, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
    }

    private void assertIn(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query7");
        } else {
            req = createQueryMockRequest("query7", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(2, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
    }

    private void assertNotEqual(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query6");
        } else {
            req = createQueryMockRequest("query6", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(2, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(2, asJsonObject.get("number").getAsInt());
    }

    private void assertEqual(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query5");
        } else {
            req = createQueryMockRequest("query5", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(1, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
    }

    private void assertGreaterOtEqualsThan(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query4");
        } else {
            req = createQueryMockRequest("query4", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(3, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(2).getAsJsonObject();
        assertEquals(2, asJsonObject.get("number").getAsInt());
    }

    private void assertGreatThan(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query3");
        } else {
            req = createQueryMockRequest("query3", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(2, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(2, asJsonObject.get("number").getAsInt());
    }

    private void assertLessThanOrEquals(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;
        JsonArray result;
        JsonObject asJsonObject;
        if (b) {
            req = createQueryMockRequest("query2");
        } else {
            req = createQueryMockRequest("query2", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(3, result.size());

        asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(2).getAsJsonObject();
        assertEquals(2, asJsonObject.get("number").getAsInt());
    }

    private void assertLessThan(boolean b) {
        HttpServletRequest req;
        ResponseMock responseMock;

        if (b) {
            req = createQueryMockRequest("query1");
        } else {
            req = createQueryMockRequest("query1", "builder");
        }

        responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

        JsonArray result = (JsonArray) new JsonParser().parse(responseMock.getOutData());

        assertEquals(2, result.size());

        JsonObject asJsonObject = result.get(0).getAsJsonObject();
        assertEquals(0, asJsonObject.get("number").getAsInt());
        asJsonObject = result.get(1).getAsJsonObject();
        assertEquals(1, asJsonObject.get("number").getAsInt());
    }

    private RequestMock createQueryMockRequest(final String... param) {
        return new RequestMock("mock/mock", "GET") {

            private Map<String, String> params = new HashMap<String, String>();
            {
                for (String string : param) {
                    params.put(string, "");
                }
            }

            @Override
            public String getParameter(String param) {
                return params.get(param);
            }
        };
    }

    private void createQueryMockEntities() {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();

        for (int i = 0; i < 3; i++) {
            Entity e = new Entity("Mock");
            e.setProperty("number", i);
            e.setProperty("string", "test" + i);
            service.put(e);
        }
    }
}
