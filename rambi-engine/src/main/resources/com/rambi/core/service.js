importClass(java.lang.System);
importClass(javax.servlet.http.Cookie);
importClass(com.rambi.core.RambiScriptStatus);

var utils = importModule('com/rambi/core/utils.js', 'utils');

function doService(service, _req, _resp, type) {
    if (service[type]) {
        var resp = new RambiResponse(_resp);
        var req = new RambiRequest(_req, _resp);
        service[type](req, resp);

        if (resp.requireChain) {
            return RambiScriptStatus.OK_REQUIRE_CHAIN;
        } else {
            return RambiScriptStatus.OK;
        }
    } else {
        throw "unsupported";
    }
}

function RambiRequest(req, resp) {
    var _req = req;
    var _resp = resp;

    this.paramAsJson = function(key) {
        var paramStr = this.param(key);
        if (paramStr != null) {
            return JSON.parse(paramStr);
        }
        return null;
    }

    this.param = function(key) {
        var p = _req.getParameter(key);
        if (p != null) {
            return utils.javaToJsonType(p);
        }
        return null;
    };

    this.forward = function(url) {
        if (typeof(url) === 'string') {
            _req.getRequestDispatcher(url).forward(_req, _resp);
        }
    };

    this.localAddr = function() {
        return utils.javaToJsonType(_req.getLocalAddr());
    };

    this.localName = function() {
        return utils.javaToJsonType(_req.getLocalName());
    };

    this.localPort = function() {
        return utils.javaToJsonType(_req.getLocalPort());
    };

    this.remoteAddr = function() {
        return utils.javaToJsonType(_req.getRemoteAddr());
    };

    this.remoteHost = function() {
        return utils.javaToJsonType(_req.getRemoteHost());
    };

    this.remotePort = function() {
        return utils.javaToJsonType(_req.getRemotePort());
    };

    this.protocol = function() {
        return utils.javaToJsonType(_req.getProtocol());
    };

    this.method = function() {
        return utils.javaToJsonType(_req.getMethod());
    };

    this.requestURI = function() {
        return utils.javaToJsonType(_req.getRequestURI());
    };

    this.queryString = function() {
        return utils.javaToJsonType(_req.getQueryString());
    };

    this.contextPath = function() {
        return utils.javaToJsonType(_req.getContextPath());
    };

    this.paramMap = function() {
        var params = _req.getParameterMap();
        if (params != null) {
            var ret = {};
            var entries = params.entrySet().toArray();
            for (var i in entries) {
               var entry = entries[i];
               var key = utils.javaToJsonType(entry.getKey());
               var value = null;
               if (entry.getValue().length == 1) {
                   value = utils.javaToJsonType(entry.getValue()[0]);
               } else {
                   value = [];
                   for (var j in entry.getValue()) {
                       value.push(utils.javaToJsonType(entry.getValue()[j]));
                   }
               }
               ret[key] = value;
            }
            return ret;
        }
        return {};
    };

    this.paramNames = function() {
        var names = _req.getParameterNames();

        if (names != null) {
            var ret = [];
            while (names.hasMoreElements()) {
                ret.push(utils.javaToJsonType(names.nextElement()));
            }
            return ret;
        }

        return [];
    };

    this.paramValues = function(key) {
        var values = _req.getParameterValues(utils.jsonToJavaType(key));

        if (values != null) {
            var ret = [];
            for (var i in values) {
                ret.push(utils.javaToJsonType(values[i]));
            }
            return ret;
        }

        return [];
    };

    this.cookies = function() {
        var cookies = _req.getCookies();

        if (cookies != null) {
            var ret = [];
            for (var i in cookies) {
                var c = cookies[i];

                var cookieJson = {
                        name: utils.javaToJsonType(c.getName()),
                        value: utils.javaToJsonType(c.getValue()),
                }

                ret.push(cookieJson);
            }
            return ret;
        }

        return [];
    };

    this.header = function(key) {
        var header = _req.getHeader(utils.jsonToJavaType(key));
        if (header != null) {
            return utils.javaToJsonType(header);
        }
        return null;
    };
}

function RambiResponse(resp) {
    var _resp = resp;

    this.printJson = function(val) {
        this.print(JSON.stringify(val));
        return this;
    }

    this.print = function(val) {
        if (val != null) {
            _resp.getWriter().print(val.toString());
        }
        return this;
    };

    this.println = function(val) {
        if (val != null) {
            _resp.getWriter().println(val.toString());
        }
        return this;
    };

    this.characterEncoding = function(e) {
        if (e) {
            var ret = utils.jsonToJavaType(e);
            _resp.setCharacterEncoding(ret);
            return this;
        } else {
            return utils.javaToJsonType(_resp.getCharacterEncoding());
        }
    };

    this.contentType = function(c) {
        if (c) {
            var ret = utils.jsonToJavaType(c);
            _resp.setContentType(c);
            return this;
        } else {
            return utils.javaToJsonType(_resp.getContentType());
        }
    };

    this.contentLength = function (l) {
        if (l && typeof(l) === 'number') {
            var ret = utils.jsonToJavaType(l);
            _resp.setContentLength(ret);
        }
        return this;
    };

    this.dateHeader = function(key, l) {
        if (key && typeof(l) === 'number') {
            var ret = utils.jsonToJavaType(l);
            _resp.addDateHeader(key, ret);
        }
        return this;
    };

    this.intHeader = function(key, l) {
        if (key && typeof(l) === 'number') {
            var ret = utils.jsonToJavaType(l);
            _resp.addIntHeader(key, ret);
        }
        return this;
    };

    this.header = function(key, l) {
        if (key && typeof(l) === 'string') {
            var ret = utils.jsonToJavaType(l);
            _resp.addHeader(key, ret);
        }
        return this;
    };

    this.status = function (s) {
        if (s && typeof(s) === 'number') {
            var ret = utils.jsonToJavaType(s);
            _resp.setStatus(ret);
        }
        return this;
    };

    this.containsHeader = function (header) {
        if (typeof(header) === 'string') {
            return _resp.containsHeader(utils.jsonToJavaType(header));
        }
        return false;
    };

    this.sendRedirect = function (url) {
        if (typeof(url) === 'string') {
            _resp.sendRedirect(utils.jsonToJavaType(url));
            this.requireChain = true;
        }
        return this;
    };

    this.addCookie = function(key, value) {
        if (typeof(key) === 'string' && typeof(value) === 'string') {
            var cookie = new Cookie(utils.jsonToJavaType(key), utils.jsonToJavaType(value));
            _resp.addCookie(cookie);
        }
        return this;
    };
};

console = {
        info: function(v) {
            if (v != null) {
                System.out.println(v.toString());
            }
        }
};