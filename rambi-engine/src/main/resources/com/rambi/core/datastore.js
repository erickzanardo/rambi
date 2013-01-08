importPackage(com.google.appengine.api.datastore);
importClass(java.lang.Long);
importClass(java.lang.Double);

function db() {
    var service = function() {
        return DatastoreServiceFactory.getDatastoreService();
    };

    var createKey = function(kind, id) {
        return KeyFactory.createKey(kind, id);
    }

    var jsonToEntityType = function(v) {
        if (typeof (v) == 'number') {
            if (v % 1 === 0) {
                return new Long(v);
            } else {
                return new Double(v);
            }
        } else {
            return v;
        }
    };

    var jsonToEntity = function(json, entity) {
        // TODO Lists
        for ( var i in json) {
            var v = json[i];
            entity.setProperty(i, jsonToEntityType(v));
        }
        return entity;
    };

    var entityToJsonType = function(value) {
        if (value instanceof String) {
            return '' + value;
        } else if (value instanceof java.lang.Number) {
            return new Number(value);
        }
    };

    var entityToJson = function(entity) {
        // TODO Lists

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
