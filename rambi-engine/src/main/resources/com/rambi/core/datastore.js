importPackage(com.google.appengine.api.datastore);
importClass(java.util.ArrayList);
importClass(java.lang.Long);
importClass(java.lang.Double);

var utils = importModule('com/rambi/core/utils.js', 'utils');

function db() {
    return new function() {
        var service = function() {
            return DatastoreServiceFactory.getDatastoreService();
        };

        var createKey = function(kind, id) {
            return KeyFactory.createKey(kind, id);
        }

        // JSON to JAVA
        var jsonToEntity = function(json, entity) {
            for ( var i in json) {
                var v = json[i];
                entity.setProperty(i, utils.jsonToJavaType(v));
            }
            return entity;
        };


        // JAVA to JSON 
        var entityToJson = function(entity) {
            var json = {};
            var map = entity.getProperties();

            var keys = map.keySet().toArray();
            for ( var i in keys) {
                var key = keys[i];
                var value = map.get(key);
                json[key] = utils.javaToJsonType(value);
            }

            return json;
        };

        this.put = function(kind, data, identifier) {
            var entity;

            if (identifier) {
                entity = new Entity(kind, identifier);
            } else {
                entity = new Entity(kind);
            }
            entity = jsonToEntity(data, entity);

            var key = service().put(entity);
            return key.getId();
        };

        this.get = function(kind, identifier) {
            var entity = service().get(createKey(kind, identifier));
            return entityToJson(entity);
        }
    };
}
