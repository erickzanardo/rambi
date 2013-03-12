package com.rambi;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DatastoreServlet extends HttpServlet {
    private static final long serialVersionUID = -4181276104088504445L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String kind = req.getParameter("kind");
        Integer id = Integer.parseInt(req.getParameter("key"));

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = service.get(KeyFactory.createKey(kind, id));
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(entity.getProperties());

            resp.setContentType("application/json");
            resp.getOutputStream().print(json);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String kind = req.getParameter("kind");
        String data = req.getParameter("data");

        JsonObject o = (JsonObject) new JsonParser().parse(data);
        Set<Entry<String, JsonElement>> entrySet = o.entrySet();

        Entity e = new Entity(kind);
        for (Entry<String, JsonElement> entry : entrySet) {
            Object val = null;
            if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                val = entry.getValue().getAsInt();
            } else {
                val = entry.getValue().getAsString();
            }
            e.setProperty(entry.getKey(), val);
        }
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(e);
    }
}
