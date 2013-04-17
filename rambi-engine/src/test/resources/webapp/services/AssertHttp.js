var service = {
    get : function(req, resp) {

        var jsonFromReq = req.paramAsJson("json");

        var ret = {};
        ret.jsonFromReq = jsonFromReq;

        // Resp attrs
        resp.characterEncoding("UTF-8")
            .contentType("application/json")
            .contentLength(10000)
            .dateHeader("Date-Header", 0)
            .intHeader("Int-Header", 1)
            .header("Test-Header", "Test")
            .addCookie("My-Cookie", "CookieTestValue")

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

        resp.printJson(ret)
            .status(403);
    }
};