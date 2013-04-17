importClass(com.google.appengine.api.users.UserServiceFactory);

var utils = importModule('com/rambi/core/utils.js', 'utils');

function userService() {
    return new function() {
        var User = function(_user) {
            this.authDomain = function() {
                return utils.javaToJsonType(_user.getAuthDomain());
            };

            this.email = function() {
                return utils.javaToJsonType(_user.getEmail());
            };

            this.nickname = function() {
                return utils.javaToJsonType(_user.getNickname());
            };

            this.id = function() {
                return utils.javaToJsonType(_user.getUserId());
            };
        };

        var service = function() {
            return UserServiceFactory.getUserService();
        };

        this.admin = function() {
            return utils.javaToJsonType(service().isUserAdmin());
        };

        this.logged = function() {
            return utils.javaToJsonType(service().isUserLoggedIn());
        };

        this.currentUser = function() {
            var current = service().getCurrentUser();
            if (current) {
                return new User(current);
            }
            return null;
        };

        this.createLoginUrl = function(url, auth) {
            var ret = null;
            
            if (auth) {
                ret = service().createLoginURL(utils.jsonToJavaType(url), utils.jsonToJavaType(auth));
            } else {
                ret = service().createLoginURL(utils.jsonToJavaType(url));
            }
            
            return utils.javaToJsonType(ret);
        };

        this.createLogoutUrl = function(url, auth) {
            var ret = null;
            
            if (auth) {
                ret = service().createLogoutURL(utils.jsonToJavaType(url), utils.jsonToJavaType(auth));
            } else {
                ret = service().createLogoutURL(utils.jsonToJavaType(url));
            }
            
            return utils.javaToJsonType(ret);
        };

    };
}
