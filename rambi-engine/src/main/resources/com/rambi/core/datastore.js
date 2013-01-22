importPackage(com.google.appengine.api.datastore);
importClass(java.util.ArrayList);
importClass(java.lang.Long);
importClass(java.lang.Double);
importClass(com.google.appengine.api.datastore.Query);
importClass(com.google.appengine.api.datastore.FetchOptions);

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
            try {
                var entity = service().get(createKey(kind, identifier));
                return entityToJson(entity);
            } catch (e if  e.javaException instanceof com.google.appengine.api.datastore.EntityNotFoundException) {
                return null;
            }
        };

        this.query = function(query) {
            var q = new Query(query.kind);
            
            var filters = query.filters;

            if (filters) {
                for (var i in filters) {
                    for (var j in filters[i]) {
                        var operator;
                        if (j == "LT") {
                            operator = Query.FilterOperator.LESS_THAN;
                        }
                        // TODO other operators
                        q.addFilter(i, operator, utils.jsonToJavaType(filters[i][j]));
                    }
                }
            }

            // TODO FetchBuilder add limit and offset
            var result = service().prepare(q).asList(FetchOptions.Builder.withDefaults());

            var list = [];
            for (var i = 0; i < result.size(); i++) {
                list.push(entityToJson(result.get(i)));
            }

            return list;
        };
    };
}
