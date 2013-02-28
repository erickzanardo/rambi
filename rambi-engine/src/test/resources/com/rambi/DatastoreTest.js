var db = importModule('com/rambi/core/datastore.js', 'db');

var service = {
    get : function(req, resp) {
        if (req.param("key")) {
            var kind = req.param("kind");
            var key = parseInt(req.param("key"));
            var data = db().get(kind, key);
            resp.print(JSON.stringify(data));
        } else if (req.param("query1")) {
            // LESS THAN
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").lt("number", 2).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { LT : 2 } }]
                };
                result = db().query(query);
            }

            resp.print(JSON.stringify(result));
        } else if (req.param("query2")) {
            // LESS THAN OR EQUALS
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").ltEq("number", 3).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { LT_EQ : 2 } }]
                };
                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query3")) {
            // GREATER THAN
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").gt("number", 0).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { GT : 0 } }]
                };
                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query4")) {
            // GREATER THAN OR EQUALS
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").gtEq("number", 0).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { GT_EQ : 0 } }]
                };

                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query5")) {
            // EQUAL
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").eq("number", 0).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { EQ : 0 } }]
                    };
                
                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query6")) {
            // NOT EQUAL
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").notEq("number", 1).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { NOT_EQ : 1 } }]
                };
                
                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query7")) {
            // IN
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").in("number", [0, 1]).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [{ number : { IN : [ 0, 1 ] } }]
                };

                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query8")) {
            // MULTIPLE OPERATOR
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").in("number", [0, 1]).eq("string", "test1").result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [
                            {number : { IN : [ 0, 1 ] }},
                            {string: { EQ: "test1" }}
                        ]
                };

                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        } else if (req.param("query9")) {
            // MULTIPLE OPERATOR ON SAME FIELD
            var result;

            // BUILDER
            if (req.param("builder")) {
                result = db().prepareQuery("Mock").lt("number", 2).gt("number", 0).result();
            } else {
                var query = {
                        kind : "Mock",
                        filters : [
                            {number : { LT : 2 }},
                            {number : { GT : 0 }}
                        ]
                };

                result = db().query(query);
            }
            resp.print(JSON.stringify(result));
        }
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