function doService(req, resp, type) {
	if (service[type]) {
		service[type](new RambiRequest(req), new RambiResponse(resp));
	} else {
		throw "unsupported";
	}
}

function RambiRequest(req) {
	var _req = req;
	
	this.param = function(key) {
		return _req.getParameter(key);
	};
}

function RambiResponse(resp) {
	var _resp = resp;

	this.print = function(val) {
		_resp.getWriter().print(val);
		_resp.getWriter().flush();
	}

	this.println = function(val) {
		_resp.getWriter().println(val);
		_resp.getWriter().flush();
	}

}