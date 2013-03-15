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

        resp.addCookie("My-Cookie", "CookieTestValue");

        resp.print(JSON.stringify(ret));
        resp.status(403);
    }
};