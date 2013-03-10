package com.rambi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcacheServlet extends HttpServlet {
    private static final long serialVersionUID = 3305193393004630874L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
        Object object = memcacheService.get(req.getParameter("key"));

        if (object != null) {
            resp.getOutputStream().print(object.toString());
        }
    }
}
