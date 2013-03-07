importClass(java.lang.System);

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
     * getHeaderNames
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
    }

    this.println = function(val) {
        if (val != null) {
            _resp.getWriter().println(val.toString());
        }
    }

    /* TODO methods to include
     * getCharacterEncoding
     * getContentType
     * setCharacterEncoding
     * setContentType
     * setContentLength
     * addCookie
     * addDateHeader
     * addHeader
     * addIntHeader
     * containsHeader
     * encodeRedirectURL
     * encodeURL
     * sendError
     * sendRedirect
     * setDateHeader
     * setHeader
     * setIntHeader
     * setStatus
     */
}

console = {
        info: function(v) {
            if (v != null) {
                System.out.println(v.toString());
            }
        }
};