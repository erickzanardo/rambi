var fetcher = importModule('com/rambi/core/urlfetcher.js', 'urlfetcher');

var service = {
    get : function(req, resp) {
    	var str = fetcher().get("http://localhost:8080/check.json");
    	var ret = JSON.parse(str);
    	ret.echo = req.param("echo");
    	resp.print(JSON.stringify(ret));
    }
};