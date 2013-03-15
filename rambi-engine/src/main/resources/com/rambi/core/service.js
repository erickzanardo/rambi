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

    this.param = function(key) {
        return _req.getParameter(key);
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

    /* TODO methods to include
     * getCharacterEncoding
     * getContentLength
     * getContentType
     * getParameterMap
     * getParameterNames
     * getParameterValues
     * getProtocol
     * getRemoteAddr
     * getRemoteHost
     * getRemotePort
     * getContextPath
     * getCookies
     * getDateHeader
     * getHeader
     * getHeaders
     * getIntHeader
     * getMethod
     * getPathInfo
     * getQueryString
     * getRequestURI
     */
}

function RambiResponse(resp) {
    var _resp = resp;

    this.print = function(val) {
        if (val != null) {
            _resp.getWriter().print(val.toString());
        }
    };

    this.println = function(val) {
        if (val != null) {
            _resp.getWriter().println(val.toString());
        }
    };

    this.characterEncoding = function(e) {
        if (e) {
            var ret = utils.jsonToJavaType(e);
            _resp.setCharacterEncoding(ret);
        } else {
            return utils.javaToJsonType(_resp.getCharacterEncoding());
        }
    };

    this.contentType = function(c) {
        if (c) {
            var ret = utils.jsonToJavaType(c);
            _resp.setContentType(c);
        } else {
            return utils.javaToJsonType(_resp.getContentType());
        }
    };

    this.contentLength = function (l) {
        if (l && typeof(l) === 'number') {
            var ret = utils.jsonToJavaType(l);
            _resp.setContentLength(ret);
        }
    };

    this.dateHeader = function(key, l) {
        if (key && typeof(l) === 'number') {
            var ret = utils.jsonToJavaType(l);
            _resp.addDateHeader(key, ret);
        }
    };

    this.intHeader = function(key, l) {
        if (key && typeof(l) === 'number') {
            var ret = utils.jsonToJavaType(l);
            _resp.addIntHeader(key, ret);
        }
    };

    this.header = function(key, l) {
        if (key && typeof(l) === 'string') {
            var ret = utils.jsonToJavaType(l);
            _resp.addHeader(key, ret);
        }
    };

    this.status = function (s) {
        if (s && typeof(s) === 'number') {
            var ret = utils.jsonToJavaType(s);
            _resp.setStatus(ret);
        }
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
    };

    this.addCookie = function(key, value) {
        if (typeof(key) === 'string' && typeof(value) === 'string') {
            var cookie = new Cookie(utils.jsonToJavaType(key), utils.jsonToJavaType(value));
            _resp.addCookie(cookie);
        }
    };
};

console = {
        info: function(v) {
            if (v != null) {
                System.out.println(v.toString());
            }
        }
};