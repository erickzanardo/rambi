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
            delete json._id;
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

            if (entity.getKey() != null) {
                json._id = entity.getKey().getId();
            }

            return json;
        };

        this.put = function(kind, data, identifier) {
            if (typeof(kind) !== "string") {
                throw "Kind must be String!";
            }
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

        this.prepareQuery = function(kind) {
            return new function() {
                var query = {
                        kind: kind,
                        filters: [],
                        sort: []
                };

                var addFilter = function(field, value, operator) {
                    var f = {};
                    f[field] = {};
                    f[field][operator] = value;
                    query.filters.push(f);
                };

                var addSort = function(field, direction) {
                    var s = {};
                    s[field] = direction;
                    query.sort.push(s);
                };

                this.result = function() {
                    return db().query(query);
                }

                this.limit = function(l) {
                    query.limit = l;
                    return this;
                };

                this.offset = function(o) {
                    query.offset = o;
                    return this;
                };

                this.desc = function(field) {
                    addSort(field, "DESC");
                    return this;
                };

                this.asc = function(field) {
                    addSort(field, "ASC");
                    return this;
                };

                this.lt = function(field, value) {
                    addFilter(field, value, "LT");
                    return this;
                };

                this.ltEq = function(field, value) {
                    addFilter(field, value, "LT_EQ");
                    return this;
                };

                this.gt = function(field, value) {
                    addFilter(field, value, "GT");
                    return this;
                };

                this.gtEq = function(field, value) {
                    addFilter(field, value, "GT_EQ");
                    return this;
                };

                this.gtEq = function(field, value) {
                    addFilter(field, value, "GT_EQ");
                    return this;
                };

                this.eq = function(field, value) {
                    addFilter(field, value, "EQ");
                    return this;
                };

                this.notEq = function(field, value) {
                    addFilter(field, value, "NOT_EQ");
                    return this;
                };

                this.in = function(field, value) {
                    addFilter(field, value, "IN");
                    return this;
                };
            }
        };

        this.query = function(query) {
            var q = new Query(query.kind);
            
            var filters = query.filters;
            var filterList = new ArrayList();

            if (filters) {
                for (var i = 0; i < filters.length; i++) {
                    for (var o in filters[i]) {
                        for (var j in filters[i][o]) {
                            var operator;
                            if (j == "LT") {
                                operator = Query.FilterOperator.LESS_THAN;
                            } else if (j == "LT_EQ") {
                                operator = Query.FilterOperator.LESS_THAN_OR_EQUAL;
                            } else if (j == "GT") {
                                operator = Query.FilterOperator.GREATER_THAN;
                            } else if (j == "GT_EQ") {
                                operator = Query.FilterOperator.GREATER_THAN_OR_EQUAL;
                            } else if (j == "EQ") {
                                operator = Query.FilterOperator.EQUAL;
                            } else if (j == "NOT_EQ") {
                                operator = Query.FilterOperator.NOT_EQUAL;
                            } else if (j == "IN") {
                                operator = Query.FilterOperator.IN;
                            }
                            filterList.add(new Query.FilterPredicate(o, operator, utils.jsonToJavaType(filters[i][o][j])));
                        }
                    }
                }
            }

            var sort = query.sort;
            if (sort) {
                for (var i = 0; i < sort.length; i++) {
                    for (var j in sort[i]) {
                        var dir;
                        var value = sort[i][j];

                        if (value == "ASC") {
                            dir = Query.SortDirection.ASCENDING;
                        } else if (value == "DESC") {
                            dir = Query.SortDirection.DESCENDING;
                        }

                        if (dir) {
                            q.addSort(j, dir);
                        }
                    }
                }
            }

            if (filterList.size() > 1) {
                q.setFilter(new Query.CompositeFilter(Query.CompositeFilterOperator.AND, filterList));
            } else if (filterList.size() > 0) {
                q.setFilter(filterList.get(0));
            }

            var fetchOptions = FetchOptions.Builder.withDefaults();

            if (query.hasOwnProperty("offset")) {
                fetchOptions.offset(query.offset);
            }

            if (query.hasOwnProperty("limit")) {
                fetchOptions.limit(query.limit);
            }

            var result = service().prepare(q).asList(fetchOptions);

            var list = [];
            for (var i = 0; i < result.size(); i++) {
                list.push(entityToJson(result.get(i)));
            }

            return list;
        };

        this.delete = function(kind, id) {
            if (typeof (kind) === "undefined" || typeof(id) == "undefined") {
                throw "kind or id is undefined";
            }

            var key = createKey(kind, id);
            service().delete(key);
        }
    };
}
