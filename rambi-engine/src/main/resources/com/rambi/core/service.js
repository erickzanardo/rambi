importClass(java.lang.System);

function doService(service, req, resp, type) {
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
        if (val != null) {
            _resp.getWriter().print(val.toString());
        }
    }

    this.println = function(val) {
        if (val != null) {
            _resp.getWriter().println(val.toString());
        }
    }

}

console = {
        info: function(v) {
            if (v != null) {
                System.out.println(v.toString());
            }
        }
};