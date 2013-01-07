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

    var toString = function(val) {
        if (val != null) {
            return val.toString();
        }
        return null;
    };

    this.print = function(val) {
        _resp.getWriter().print(toString(val));
    }

    this.println = function(val) {
        _resp.getWriter().println(toString(val));
    }

}

console = {
        info: function(v) {
            System.out.println(toString(v));
        }
};