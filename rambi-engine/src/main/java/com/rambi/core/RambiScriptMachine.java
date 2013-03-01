package com.rambi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
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

        Context cx = Context.enter();

        global = new ImporterTopLevel(cx);
        global.defineFunctionProperties(new String[] { "importModule" },
                RambiScriptMachine.class, ScriptableObject.DONTENUM);

        String services = RambiScriptMachine
                .readFile("com/rambi/core/service.js");

        Script servicesScript = cx.compileString(services, "services.js", 1,
                null);
        servicesScript.exec(cx, global);

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

    public void executeHttpRequest(HttpServletRequest req,
            HttpServletResponse resp) {

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
            String method = req.getMethod().toLowerCase();

            String serviceScript = readFile(service);

            try {

                ScriptableObject scriptableObject = (ScriptableObject) cx
                        .newObject(global);
                scriptableObject.setParentScope(global);

                // TODO cache this
                Script script = cx.compileString(serviceScript, service, 1,
                        null);
                script.exec(cx, scriptableObject);

                Function f = (Function) global.get("doService");
                f.call(cx, global, scriptableObject, new Object[] {
                        scriptableObject.get("service"), req, resp, method });

            } catch (Exception e) {
                if (e.getMessage().startsWith("unsupported")) {
                    throw new UnsupportedOperationException("Method " + method
                            + " not allowed in this url " + req.getRequestURI());
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                Context.exit();
            }
        }
    }

    public static ScriptableObject importModule(Context cx, Scriptable thisObj,
            Object[] args, Function funObj) {
        ScriptableObject object = null;

        if (args.length != 2) {
            throw new RuntimeException(
                    "Must inform a path and module to import");
        }

        String path = (String) args[0];
        String module = (String) args[1];
        String file = RambiScriptMachine.readFile(path);

        object = (ScriptableObject) cx.newObject(thisObj);

        // TODO cache
        Script script = cx.compileString(file, path, 1, null);
        script.exec(cx, object);

        return (ScriptableObject) ScriptableObject.getProperty(object, module);
    }

    protected static String readFile(String file) {
        BufferedReader br = null;
        StringBuilder ret = new StringBuilder();

        try {
            InputStream resourceAsStream = RambiScriptMachine.class
                    .getClassLoader().getResourceAsStream(file);
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
