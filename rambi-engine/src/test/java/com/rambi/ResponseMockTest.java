package com.rambi;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ResponseMockTest {
    @Test
    public void test() throws IOException {
        ResponseMock resp = new ResponseMock();
        resp.getOutputStream().write("test".getBytes("UTF-8"));
        assertEquals("test", resp.getOutData());

        resp = new ResponseMock();
        resp.getWriter().print("test");
        resp.getWriter().flush();
        assertEquals("test", resp.getOutData());

        resp = new ResponseMock();
        resp.getWriter().println("test");
        resp.getWriter().println("test");
        resp.getWriter().flush();

        assertEquals("test\ntest\n", resp.getOutData());

        resp = new ResponseMock();
        resp.getWriter().print(1);
        resp.getWriter().flush();
        assertEquals("1", resp.getOutData());

        resp = new ResponseMock();
        resp.getWriter().print(true);
        resp.getWriter().flush();
        assertEquals("true", resp.getOutData());

    }
}
