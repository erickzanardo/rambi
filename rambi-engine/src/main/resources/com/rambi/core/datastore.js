importPackage(com.google.appengine.api.datastore);

function db() {
	var service = function() {
		return DatastoreServiceFactory.getDatastoreService();
	};

	var createKey = function(kind, id) {
		return KeyFactory.createKey(kind, id);
	}

	var jsonToEntity = function(json, entity) {
		// TODO Lists
		for ( var i in json) {
			entity.setProperty(i, json[i]);
		}
		return entity;
	};

	var entityToJson = function(entity) {
		// TODO Lists
		

		var json = {};
		var map = entity.getProperties();

		var keys = map.keySet().toArray();
		for ( var i in keys) {
			var key = keys[i];
			var value = map.get(key);
			
			// Convert from java type, to a js type
			if (value instanceof String) {
				json[key] = '' + map.get(key);
			}

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
