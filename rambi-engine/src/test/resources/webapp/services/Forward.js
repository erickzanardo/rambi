var service = {
	get : function(req, resp) {
		req.forward("/check.json");
	}
};