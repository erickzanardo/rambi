package com.rambi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RambiScriptMachine {
    public static RambiScriptMachine instance;

    private ScriptableObject global;

    private static ThreadLocal<ServletContext> servletContext = new ThreadLocal<ServletContext>();

    private RambiScriptMachine() {
        Context cx = Context.enter();

        global = new ImporterTopLevel(cx);
        global.defineFunctionProperties(new String[] { "importModule" }, RambiScriptMachine.class,
                ScriptableObject.DONTENUM);

        global.defineFunctionProperties(new String[] { "importWebModule" }, RambiScriptMachine.class,
                ScriptableObject.DONTENUM);

        String services = RambiScriptMachine.readFile("com/rambi/core/service.js");

        Script servicesScript = cx.compileString(services, "services.js", 1, null);
        servicesScript.exec(cx, global);

        Context.exit();
    }

    public static RambiScriptMachine getInstance() {
        if (instance == null) {
            instance = new RambiScriptMachine();
        }
        return instance;
    }

    public void executeHttpRequest(HttpServletRequest req, HttpServletResponse resp, InputStream jsStream,
            String service, ServletContext servletContext) {

        if (jsStream != null) {
            RambiScriptMachine.servletContext.set(servletContext);

            Context cx = Context.enter();
            String method = req.getMethod().toLowerCase();

            String serviceScript = readStream(jsStream);

            try {

                ScriptableObject scriptableObject = (ScriptableObject) cx.newObject(global);
                scriptableObject.setParentScope(global);

                // TODO cache this
                Script script = cx.compileString(serviceScript, service, 1, null);
                script.exec(cx, scriptableObject);

                Function f = (Function) global.get("doService");
                f.call(cx, global, scriptableObject,
                        new Object[] { scriptableObject.get("service"), req, resp, method });

            } catch (Exception e) {
                if (e.getMessage().startsWith("unsupported")) {
                    throw new UnsupportedOperationException("Method " + method + " not allowed in this url "
                            + req.getRequestURI());
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                Context.exit();
                RambiScriptMachine.servletContext.remove();

            }
        }
    }

    public static ScriptableObject importModule(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        return importScript(cx, thisObj, args, funObj, false);
    }

    public static ScriptableObject importWebModule(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        return importScript(cx, thisObj, args, funObj, true);
    }

    public static ScriptableObject importScript(Context cx, Scriptable thisObj, Object[] args, Function funObj,
            boolean fromServletContext) {

        if (args.length != 2) {
            throw new RuntimeException("Must inform a path and module to import");
        }

        String path = (String) args[0];
        String module = (String) args[1];
        String file = null;

        if (fromServletContext) {
            InputStream inputStream = servletContext.get().getResourceAsStream(path);
            file = readStream(inputStream);
        } else {
            file = readFile(path);
        }

        ScriptableObject object = (ScriptableObject) cx.newObject(thisObj);

        // TODO cache
        Script script = cx.compileString(file, path, 1, null);
        script.exec(cx, object);

        return (ScriptableObject) ScriptableObject.getProperty(object, module);
    }

    protected static String readFile(String file) {
        InputStream resourceAsStream = RambiScriptMachine.class.getClassLoader().getResourceAsStream(file);

        return readStream(resourceAsStream);
    }

    private static String readStream(InputStream resourceAsStream) {
        BufferedReader br = null;
        StringBuilder ret = new StringBuilder();

        try {
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
