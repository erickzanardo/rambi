importClass(java.lang.System);
importClass(javax.servlet.http.Cookie);

var utils = importModule('com/rambi/core/utils.js', 'utils');

function doService(service, req, resp, type) {
    if (service[type]) {
        service[type](new RambiRequest(req), new RambiResponse(resp));
    } else {
        throw "unsupported";
    }
}

function RambiRequest(req) {
    var _req = req;

    this.param = function(key) {
        return _req.getParameter(key);
    };

    /* TODO methods to include
     * getCharacterEncoding
     * getContentLength
     * getContentType
     * getLocalAddr
     * getLocalName
     * getLocalPort
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
        if (typeof(header) === 'string') {
            return _resp.sendRedirect(utils.jsonToJavaType(url));
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