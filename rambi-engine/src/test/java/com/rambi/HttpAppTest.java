package com.rambi;

import static org.junit.Assert.*;

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
		HttpServletRequest req = new RequestMock() {
			@Override
			public String getRequestURI() {
				return "mock/mock";
			}

			@Override
			public String getParameter(String param) {
				return param + " - Mock Value";
			}

			@Override
			public String getMethod() {
				return "GET";
			}
		};

		ResponseMock responseMock = new ResponseMock();
		RambiScriptMachine.getInstance().executeHttpRequest(
				req, responseMock);

		assertEquals("param - Mock Value", responseMock.getOutData());
	}
}
