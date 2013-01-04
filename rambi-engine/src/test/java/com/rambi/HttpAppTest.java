package com.rambi;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.rambi.core.RambiScriptMachine;

public class HttpAppTest {
	private String appConfig = "";

	@Before
	public void init() {
		RambiScriptMachine.getInstance().init(appConfig);
	}

	@Test
	public void testHttpRequest() {
		HttpServletRequest req = new RequestMock() {
			@Override
			public String getQueryString() {
				return "mock/mock?param=param";
			}

			@Override
			public String getParameter(String param) {
				return param + " - Mock Value";
			}
		};

		ResponseMock responseMock = new ResponseMock();
		String response = RambiScriptMachine.getInstance().executeHttpRequest(
				req, responseMock);

		assertEquals("param - Mock Value", response);
	}
}
