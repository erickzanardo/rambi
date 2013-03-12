package com.rambi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.naming.NamingException;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DatastoreTest {

    private TestServer server = new TestServer();

    @Before
    public void init() throws NamingException {
        server.init();
    }

    @After
    public void tearDown() throws NamingException {
        server.end();
    }

    @Test
    public void testDatastore() throws Exception {

        // PUT
        assertPut();

        // // POST
        long id = assertPost();
        //
        // // GET
        // assertGet(service, id);

        // Queries
        // createQueryMockEntities();
        //
        // // Query 1
        // // LESS THAN
        // assertLessThan(false);
        // assertLessThan(true);
        //
        // // Query 2
        // // LESS THAN OR EQUALS
        // assertLessThanOrEquals(false);
        // assertLessThanOrEquals(true);
        //
        // // Query 3
        // // GREATER THAN
        // assertGreatThan(false);
        // assertGreatThan(true);
        //
        // // Query 4
        // // GREATER THAN OR EQUALS
        // assertGreaterOtEqualsThan(false);
        // assertGreaterOtEqualsThan(true);
        //
        // // Query 5
        // // EQUAL
        // assertEqual(false);
        // assertEqual(true);
        //
        // // Query 6
        // // NOT EQUAL
        // assertNotEqual(false);
        // assertNotEqual(true);
        //
        // // Query 7
        // // IN
        // assertIn(false);
        // assertIn(true);
        //
        // // Query 8
        // // MULTIPLE OPERATOR
        // assertMultiple(false);
        // assertMultiple(true);
        //
        // // Query 9
        // // MULTIPLE OPERATOR ON SAME FIELD
        // assertRange(false);
        // assertRange(true);
        //
        // // Query 10
        // // NO FILTERS
        // assertNoFilter(false);
        // assertNoFilter(true);
        //
        // // Query 11
        // // SORT
        // createSortMockEntities();
        // assertSort(false);
        // assertSort(true);
        //
        // // Query 12
        // // OFFSET and LIMIT
        // assertLimitOffset(false);
        // assertLimitOffset(true);

    }

    // private void assertLimitOffset(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query12");
    // } else {
    // req = createQueryMockRequest("query12", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(1, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertSort(boolean b) {
    //
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query11");
    // } else {
    // req = createQueryMockRequest("query11", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(4, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(3, asJsonObject.get("number").getAsInt());
    // assertEquals(1, asJsonObject.get("number2").getAsInt());
    //
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // assertEquals(1, asJsonObject.get("number2").getAsInt());
    //
    // asJsonObject = result.get(2).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // assertEquals(1, asJsonObject.get("number2").getAsInt());
    //
    // asJsonObject = result.get(3).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // assertEquals(2, asJsonObject.get("number2").getAsInt());
    //
    // }
    //
    private long assertPost() {
        Response response = HttpUtils.post("http://localhost:8080/services/DatastoreTest.js", null);

        long id = Long.parseLong(response.getAsString());

        response = HttpUtils.get("http://localhost:8080/datastore?key=" + id + "&kind=Kind");
        JsonObject entity = (JsonObject) new JsonParser().parse(response.getAsString());

        assertEquals("POST - value", entity.get("value").getAsString());
        return id;
    }

    //
    // private void assertGet(DatastoreService service, final long id)
    // throws EntityNotFoundException {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // Entity entity;
    // req = new RequestMock("mock/mock", "GET") {
    //
    // private Map<String, String> params = new HashMap<String, String>();
    // {
    // // nonexistent ID
    // params.put("key", "656565656565656565");
    // params.put("kind", "Kind");
    // }
    //
    // @Override
    // public String getParameter(String param) {
    // return params.get(param);
    // }
    // };
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    // assertEquals("null", responseMock.getOutData());
    //
    // req = new RequestMock("mock/mock", "GET") {
    //
    // private Map<String, String> params = new HashMap<String, String>();
    // {
    // params.put("key", String.valueOf(id));
    // params.put("kind", "Kind");
    // }
    //
    // @Override
    // public String getParameter(String param) {
    // return params.get(param);
    // }
    // };
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // // Testing datatypes and embed _id
    // JsonObject resp = (JsonObject) new JsonParser().parse(responseMock
    // .getOutData());
    // assertEquals("POST - value", resp.get("value").getAsString());
    // assertEquals(1, resp.get("numberValue").getAsInt());
    // assertEquals(0.1d, resp.get("decimalValue").getAsDouble(), 0);
    //
    // // embed id
    // assertNotNull(resp.get("_id"));
    // assertEquals(id, resp.get("_id").getAsLong());
    //
    // JsonArray asJsonArray = resp.get("values").getAsJsonArray();
    // assertEquals(1, asJsonArray.get(0).getAsInt());
    // assertEquals(2, asJsonArray.get(1).getAsInt());
    // assertEquals(3, asJsonArray.get(2).getAsInt());
    //
    // assertTrue(resp.get("valid").getAsBoolean());
    // assertTrue(resp.get("date").getAsString().startsWith("2013-01-09"));
    //
    // entity = service.get(KeyFactory.createKey("Kind", id));
    // assertNotNull(entity);
    //
    // assertNull(entity.getProperty("_id"));
    // assertTrue(entity.getProperty("numberValue") instanceof Long);
    // assertTrue(entity.getProperty("decimalValue") instanceof Double);
    // assertTrue(entity.getProperty("values") instanceof ArrayList);
    // assertTrue(entity.getProperty("valid") instanceof Boolean);
    // assertTrue(entity.getProperty("date") instanceof Date);
    // }

    private void assertPut() throws UnsupportedEncodingException {
        Response response = HttpUtils.put("http://localhost:8080/services/DatastoreTest.js?key=5");

        JsonObject obj = (JsonObject) new JsonParser().parse(response.getAsString());
        assertEquals(5, obj.get("_id").getAsLong());

        response = HttpUtils.get("http://localhost:8080/datastore?key=5&kind=Kind");
        JsonObject dsObj = (JsonObject) new JsonParser().parse(response.getAsString());
        assertEquals("PUT - value", dsObj.get("value").getAsString());

        // Updated
        obj.addProperty("value", "PUT - value updated");
        response = HttpUtils.put("http://localhost:8080/services/DatastoreTest.js?data="
                + URLEncoder.encode(obj.toString(), "UTF-8"));

        response = HttpUtils.get("http://localhost:8080/datastore?key=5&kind=Kind");
        obj = (JsonObject) new JsonParser().parse(response.getAsString());
        assertEquals("PUT - value updated", obj.get("value").getAsString());
    };

    // private void assertNoFilter(boolean b) {
    //
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query10");
    // } else {
    // req = createQueryMockRequest("query10", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(3, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // assertEquals(2, asJsonObject.get("_id").getAsInt());
    //
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // assertEquals(3, asJsonObject.get("_id").getAsInt());
    //
    // asJsonObject = result.get(2).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // assertEquals(4, asJsonObject.get("_id").getAsInt());
    //
    // }
    //
    // private void assertRange(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query9");
    // } else {
    // req = createQueryMockRequest("query9", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(1, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertMultiple(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query8");
    // } else {
    // req = createQueryMockRequest("query8", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(1, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertIn(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query7");
    // } else {
    // req = createQueryMockRequest("query7", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(2, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertNotEqual(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query6");
    // } else {
    // req = createQueryMockRequest("query6", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(2, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertEqual(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query5");
    // } else {
    // req = createQueryMockRequest("query5", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(1, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertGreaterOtEqualsThan(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query4");
    // } else {
    // req = createQueryMockRequest("query4", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(3, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(2).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertGreatThan(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query3");
    // } else {
    // req = createQueryMockRequest("query3", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(2, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertLessThanOrEquals(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    // JsonArray result;
    // JsonObject asJsonObject;
    // if (b) {
    // req = createQueryMockRequest("query2");
    // } else {
    // req = createQueryMockRequest("query2", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // result = (JsonArray) new JsonParser().parse(responseMock.getOutData());
    //
    // assertEquals(3, result.size());
    //
    // asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(2).getAsJsonObject();
    // assertEquals(2, asJsonObject.get("number").getAsInt());
    // }
    //
    // private void assertLessThan(boolean b) {
    // HttpServletRequest req;
    // ResponseMock responseMock;
    //
    // if (b) {
    // req = createQueryMockRequest("query1");
    // } else {
    // req = createQueryMockRequest("query1", "builder");
    // }
    //
    // responseMock = new ResponseMock();
    // RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock,
    // getServiceFile(), "DatastoreTest", null);
    //
    // JsonArray result = (JsonArray) new JsonParser().parse(responseMock
    // .getOutData());
    //
    // assertEquals(2, result.size());
    //
    // JsonObject asJsonObject = result.get(0).getAsJsonObject();
    // assertEquals(0, asJsonObject.get("number").getAsInt());
    // asJsonObject = result.get(1).getAsJsonObject();
    // assertEquals(1, asJsonObject.get("number").getAsInt());
    // }
    //
    // private RequestMock createQueryMockRequest(final String... param) {
    // return new RequestMock("mock/mock", "GET") {
    //
    // private Map<String, String> params = new HashMap<String, String>();
    // {
    // for (String string : param) {
    // params.put(string, "");
    // }
    // }
    //
    // @Override
    // public String getParameter(String param) {
    // return params.get(param);
    // }
    // };
    // }
    //
    // private void createSortMockEntities() {
    // DatastoreService service = DatastoreServiceFactory
    // .getDatastoreService();
    //
    // Entity e = new Entity("Mock2");
    // e.setProperty("number", 0);
    // e.setProperty("number2", 1);
    // service.put(e);
    //
    // e = new Entity("Mock2");
    // e.setProperty("number", 1);
    // e.setProperty("number2", 1);
    // service.put(e);
    //
    // e = new Entity("Mock2");
    // e.setProperty("number", 2);
    // e.setProperty("number2", 2);
    // service.put(e);
    //
    // e = new Entity("Mock2");
    // e.setProperty("number", 3);
    // e.setProperty("number2", 1);
    // service.put(e);
    //
    // }
    //
    // private void createQueryMockEntities() {
    // DatastoreService service = DatastoreServiceFactory
    // .getDatastoreService();
    //
    // for (int i = 0; i < 3; i++) {
    // Entity e = new Entity("Mock");
    // e.setProperty("number", i);
    // e.setProperty("string", "test" + i);
    // service.put(e);
    // }
    // }
    //
    // public InputStream getServiceFile() {
    // return DatastoreTest.class.getClassLoader().getResourceAsStream(
    // "com/rambi/DatastoreTest.js");
    // }
}
