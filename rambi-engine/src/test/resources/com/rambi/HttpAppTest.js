// redundant import
import com/rambi/HttpAppTestImports2.js;
import com/rambi/HttpAppTestImports.js;

var service = {
	get: function(req, resp) {
		var p = req.param("param");
		resp.print(p);
		resp.println(p);
		resp.print(testObject.echo("a"));
	}
};