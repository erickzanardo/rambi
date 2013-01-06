package com.rambi;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.rambi.core.RambiScriptMachine;

public class HttpAppTest {
	private String appConfig = "com/rambi/HttpAppTestConfig.js";

	@Before
	public void init() {
		RambiScriptMachine.getInstance().init(appConfig);
	}

	@Test
	public void testHttpRequest() {
		HttpServletRequest req = new RequestMock("mock/mock", "GET") {
			@Override
			public String getParameter(String param) {
				return param + " - Mock Value";
			}
		};

		ResponseMock responseMock = new ResponseMock();
		RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);

		assertEquals("param - Mock Valueparam - Mock Value\na a",
				responseMock.getOutData());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testHttpRequestUnsupported() {
		HttpServletRequest req = new RequestMock("mock/mock", "DELETE");

		ResponseMock responseMock = new ResponseMock();
		RambiScriptMachine.getInstance().executeHttpRequest(req, responseMock);
	}

}
