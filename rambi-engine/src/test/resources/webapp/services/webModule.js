var echo = importWebModule("/js/echo.js", "echo");

var service = {
    get: function(req, resp) {
        var value = req.param("param");
        resp.print(echo(value));
    }
};