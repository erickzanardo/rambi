var o2 = importModule('com/rambi/HttpAppTestImports2.js', 'module');
var testObject = importModule('com/rambi/HttpAppTestImports.js', 'module');

var service = {
	get: function(req, resp) {
		var p = req.param("param");
		resp.print(p);
		resp.println(p);
		resp.print(testObject.echo("a"));
	}
};