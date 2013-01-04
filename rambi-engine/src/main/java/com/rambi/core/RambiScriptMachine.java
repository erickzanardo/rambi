package com.rambi.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RambiScriptMachine {
	public static RambiScriptMachine instance;

	public void init(String appConfig) {
	}

	private RambiScriptMachine() {
	}

	public static RambiScriptMachine getInstance() {
		if (instance == null) {
			instance = new RambiScriptMachine();
		}
		return instance;
	}

	public String executeHttpRequest(HttpServletRequest req, HttpServletResponse resp) {
		return null;
	}
}
