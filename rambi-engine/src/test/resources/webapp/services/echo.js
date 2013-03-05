var service = {
    get: function(req, resp) {
        var value = req.param("param");
        resp.print(value);
    }
};