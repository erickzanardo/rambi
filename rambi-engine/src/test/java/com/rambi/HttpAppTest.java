package com.rambi;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.rambi.core.RambiScriptMachine;

public class HttpAppTest {

    @Test
    public void testHttpRequest() {
        HttpServletRequest req = new RequestMock("mock/mock", "GET") {
            @Override
            public String getParameter(String param) {
                return param + " - Mock Value";
            }
        };

        ResponseMock responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock, getServiceFile(), "HttpAppTest");

        assertEquals("param - Mock Valueparam - Mock Value\na a", responseMock.getOutData());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHttpRequestUnsupported() {
        HttpServletRequest req = new RequestMock("mock/mock", "DELETE");

        ResponseMock responseMock = new ResponseMock();
        RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock, getServiceFile(), "HttpAppTest");
    }

    public InputStream getServiceFile() {
        return HttpAppTest.class.getClassLoader().getResourceAsStream("com/rambi/HttpAppTest.js");
    }

}
