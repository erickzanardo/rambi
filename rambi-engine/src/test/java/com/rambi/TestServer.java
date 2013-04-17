package com.rambi;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.mycontainer.gae.web.LocalServiceTestHelperFilter;
import com.googlecode.mycontainer.kernel.ShutdownCommand;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.FilterDesc;
import com.googlecode.mycontainer.web.WebServerDeployer;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

public class TestServer {
    public void init() throws NamingException {
        System.setProperty("java.naming.factory.initial",
                "com.googlecode.mycontainer.kernel.naming.MyContainerContextFactory");
        ContainerBuilder builder = new ContainerBuilder();

        WebServerDeployer server = builder.createDeployer(JettyServerDeployer.class);
        server.setName("WebServer");
        server.bindPort(8080);

        ContextWebServer web = server.createContextWebServer();
        web.setContext("/");
        web.setResources("src/test/resources/webapp");

        LocalServiceTestHelper helper = new LocalServiceTestHelper(
                new LocalDatastoreServiceTestConfig(), new LocalMemcacheServiceTestConfig(),
                new LocalUserServiceTestConfig());

        helper.setEnvIsLoggedIn(true);
        helper.setEnvIsAdmin(false);
        helper.setEnvEmail("test@example.com");
        helper.setEnvAuthDomain("example.com");

        LocalServiceTestHelperFilter gae = new LocalServiceTestHelperFilter(helper);
        web.getFilters().add(new FilterDesc(gae, "/*"));
        server.deploy();

    }

    public void end() throws NamingException {
        ShutdownCommand shutdown = new ShutdownCommand();
        shutdown.setContext(new InitialContext());
        shutdown.shutdown();

    }
}
