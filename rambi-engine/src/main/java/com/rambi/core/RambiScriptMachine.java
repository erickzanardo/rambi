package com.rambi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RambiScriptMachine {
	public static RambiScriptMachine instance;
	private JsonParser parser = new JsonParser();
	private JsonArray config;
	private ScriptableObject global;

	public void init(String appConfig) {
		config = parser.parse(readFile(appConfig)).getAsJsonArray();

		Context enter = Context.enter();

		global = enter.initStandardObjects();

		Context.exit();
	}

	private RambiScriptMachine() {
	}

	public static RambiScriptMachine getInstance() {
		if (instance == null) {
			instance = new RambiScriptMachine();
		}
		return instance;
	}

	public void executeHttpRequest(HttpServletRequest req, HttpServletResponse resp) {

		String uri = req.getRequestURI();
		String service = null;

		for (JsonElement e : config) {
			JsonObject o = (JsonObject) e;
			String pattern = o.get("pattern").getAsString();

			if (uri.matches(pattern)) {
				service = o.get("service").getAsString();
			}
		}

		if (service != null) {
			Context cx = Context.enter();

			StringBuilder scriptStr = new StringBuilder();
			scriptStr.append(readFile("com/rambi/core/service.js"));
			scriptStr.append(readFile(service));

			ScriptableObject scriptableObject = cx.initStandardObjects();

			// TODO cache this
			Script script = cx.compileString(scriptStr.toString(), service, 1, null);
			script.exec(cx, scriptableObject);

			Function f = (Function) scriptableObject.get("doService");
			f.call(cx, global, scriptableObject, new Object[] { req, resp, req.getMethod().toLowerCase() });

			try {

			} finally {
				Context.exit();
			}
		}
	}

	protected String readFile(String file) {
		BufferedReader br = null;
		StringBuilder ret = new StringBuilder();

		try {
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(file);
			String line;
			br = new BufferedReader(new InputStreamReader(resourceAsStream));

			while ((line = br.readLine()) != null) {
				ret.append(line).append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return ret.toString();
	}
}
