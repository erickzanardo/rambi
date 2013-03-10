var cache = importModule('com/rambi/core/memcache.js', 'cache');

var service = {
    get : function(req, resp) {
        var value = cache().get("value");
        resp.print(value);
    },
    put : function(req, resp) {
        if (req.param("expirationInMillis")) {
            var e = parseInt(req.param("expirationInMillis"));
            cache().put("value", req.param("value"), e);
        } else {
            cache().put("value", req.param("value"));
        }
    },
    delete : function(req, resp) {
        cache().delete("value");
    }
};