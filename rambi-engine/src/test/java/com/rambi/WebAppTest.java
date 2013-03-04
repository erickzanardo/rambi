package com.rambi;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.After;
import org.junit.Before;

public class WebAppTest {

    private Server server;

    @Before
    public void setup() throws Exception {
        server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        FilterMapping filterMapping = new FilterMapping();
        filterMapping.setFilterName("");
        filterMapping.setPathSpec("");
        
        handler.addFilterMapping(filterMapping);

        
        server.start();
        server.join();
    }

    @After
    public void end() throws Exception {
        server.stop();
    }
}
