import com/rambi/core/datastore.js;
importClass(java.lang.Integer);

var service = {
    get : function(req, resp) {
        var kind = req.param("kind");
        var key = parseInt(req.param("key"));
        var data = db().get(kind, key);
        resp.print(JSON.stringify(data));
    },
    put : function(req, resp) {
        var key = parseInt(req.param("key"));

        var data = {
                value: "PUT - value"
        };
        var key = db().put("Kind", data, key);
        resp.print(key);
    },
    post : function(req, resp) {
        var data = {
                value: "POST - value"
        };

        var key = db().put("Kind", data);
        resp.print(key);
    },

};