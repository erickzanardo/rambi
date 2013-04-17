var userService = importModule('com/rambi/core/userservice.js', 'userService');

var service = {
    get : function(req, resp) {
    	var s = userService();
    	var user = s.currentUser();

    	var ret = {
    			logged: s.logged(),
    			admin: s.admin(),
    			authDomain: user.authDomain(),
    			email: user.email()
    	};
    	resp.printJson(ret);
  }
};