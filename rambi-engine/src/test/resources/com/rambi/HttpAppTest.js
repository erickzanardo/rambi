var service = {
	get: function(req, resp) {
		var p = req.param("param");
		resp.print(p);
	}
};