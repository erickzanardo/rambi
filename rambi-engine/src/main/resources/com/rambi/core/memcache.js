importPackage(com.google.appengine.api.memcache);

function cache() {
    return new function() {
        var utils = importModule('com/rambi/core/utils.js', 'utils');

        this.put = function(key, value, expirationInMillis) {
            var cacheService = MemcacheServiceFactory.getMemcacheService();
            var v = utils.jsonToJavaType(value);

            if (typeof(expirationInMillis) !== "undefined") {
                var e = Expiration.byDeltaMillis(expirationInMillis);
                cacheService.put(key, v, e);
            } else {
                cacheService.put(key, v);
            }
        };

        this.get = function(key) {
            var cacheService = MemcacheServiceFactory.getMemcacheService();
            var value = cacheService.get(key);
            return utils.javaToJsonType(value);
        };

        this.delete = function(key) {
            var cacheService = MemcacheServiceFactory.getMemcacheService();
            cacheService.delete(key);
        };
    };
}