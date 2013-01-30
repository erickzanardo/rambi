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
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        LT : 2
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query2")) {
            // LESS THAN OR EQUALS
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        LT_EQ : 2
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query3")) {
            // GREATER THAN
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        GT : 0
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query4")) {
            // GREATER THAN OR EQUALS
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        GT_EQ : 0
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query5")) {
            // EQUAL
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        EQ : 0
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query6")) {
            // NOT EQUAL
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        NOT_EQ : 1
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query7")) {
            // IN
            var query = {
                kind : "Mock",
                filters : {
                    number : {
                        IN : [ 0, 1 ]
                    }
                }
            };
            resp.print(JSON.stringify(db().query(query)));
        } else if (req.param("query_builder1")) {
        } else if (req.param("query_builder2")) {
        } else if (req.param("query_builder3")) {
        } else if (req.param("query_builder4")) {
        } else if (req.param("query_builder5")) {
        } else if (req.param("query_builder6")) {
        } else if (req.param("query_builder7")) {
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