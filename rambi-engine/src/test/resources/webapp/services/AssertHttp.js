var service = {
    get : function(req, resp) {

        var ret = {};
        // Resp attrs
        resp.characterEncoding("UTF-8");
        resp.contentType("application/json");
        resp.contentLength(10000);
        resp.dateHeader("Date-Header", 0);
        resp.intHeader("Int-Header", 1);
        resp.header("Test-Header", "Test");

        ret.characterEncoding = resp.characterEncoding();
        ret.contentType = resp.contentType();
        ret.hasHeader = resp.containsHeader("Test-Header");

        // Req attrs
        ret.localAddr = req.localAddr();
        ret.localName = req.localName();
        ret.remoteAddr = req.remoteAddr();
        ret.remoteHost = req.remoteHost();
        ret.protocol = req.protocol();
        ret.method = req.method();
        ret.requestURI = req.requestURI();
        ret.queryString = req.queryString();
        ret.contextPath = req.contextPath();
        ret.paramMap = req.paramMap();
        ret.paramNames = req.paramNames();
        ret.paramValues = req.paramValues("ble");
        ret.cookies = req.cookies();
        ret.headerTest = req.header("headerTest");

        resp.addCookie("My-Cookie", "CookieTestValue");

        resp.print(JSON.stringify(ret));
        resp.status(403);
    }
};