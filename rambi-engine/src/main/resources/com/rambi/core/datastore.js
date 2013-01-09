importPackage(com.google.appengine.api.datastore);
importClass(java.util.ArrayList);
importClass(java.lang.Long);
importClass(java.lang.Double);

function db() {
    var service = function() {
        return DatastoreServiceFactory.getDatastoreService();
    };

    var createKey = function(kind, id) {
        return KeyFactory.createKey(kind, id);
    }

    // JSON data type to JAVA
    var jsonToEntityType = function(v) {
        if (typeof (v) == 'number') {
            if (v % 1 === 0) {
                return new Long(v);
            } else {
                return new Double(v);
            }
        } else if (v instanceof Date) {
        	return new java.util.Date(v.getTime());
        } else if (typeof (v) == 'boolean') {
        	return new java.lang.Boolean(v);
        } else if (Array.isArray(v)) {
            var list = new ArrayList();
            for (var i = 0; i < v.length; i++) {
                list.add(jsonToEntityType(v[i]));
            }
            return list;
        } else {
            return v;
        }
    };

    // JSON to JAVA
    var jsonToEntity = function(json, entity) {
        for ( var i in json) {
            var v = json[i];
            entity.setProperty(i, jsonToEntityType(v));
        }
        return entity;
    };

    // JAVA data type to JSON 
    var entityToJsonType = function(value) {
        if (value instanceof String) {
            return '' + value;
        } else if (value instanceof java.lang.Number) {
            return new Number(value);
        } else if (value instanceof java.lang.Boolean) {
        	return value ? true : false;
        } else if (value instanceof java.util.Date) {
        	return new Date(value.getTime());
        } else if (value instanceof ArrayList) {
            var list = [];
            for (var i = 0; i < value.size(); i++) {
                list.push(entityToJsonType(value.get(i)));
            }
            return list;
        }
    };

    // JAVA to JSON 
    var entityToJson = function(entity) {
        var json = {};
        var map = entity.getProperties();

        var keys = map.keySet().toArray();
        for ( var i in keys) {
            var key = keys[i];
            var value = map.get(key);
            json[key] = entityToJsonType(value);
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

    return this;
}
