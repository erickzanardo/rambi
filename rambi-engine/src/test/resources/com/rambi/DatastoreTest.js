var db = importModule('com/rambi/core/datastore.js', 'db');

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
            value : "PUT - value"
        };
        var key = db().put("Kind", data, key);
        resp.print(key);
    },
    post : function(req, resp) {

        var d = new Date();
        d.setDate(9);
        d.setMonth(0);
        d.setYear(2013);

        var data = {
            value : "POST - value",
            numberValue : 1,
            decimalValue : 0.1,
            values : [ 1, 2, 3 ],
            valid : true,
            date : d
        };

        var key = db().put("Kind", data);
        resp.print(key);
    },
};