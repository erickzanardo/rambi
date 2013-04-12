Rambi
=====

Contents
<table>
  <tr>
    <td><a href="#rambi">Rambi introduction</a></td>
  </tr>
  <tr>
    <td><a href="#request-and-response">Request and response</a></td>
  </tr>
  <tr>
    <td><a href="#modules">Modules</a></td>
  </tr>
  <tr>
    <td><a href="#datastore">Datastore</a></td>
  </tr>
  <tr>
    <td><a href="#memcache">Memcache</a></td>
  </tr>
  <tr>
    <td><a href="#urlfetch">URLFetch</a></td>
  </tr>
</table>

Development: [![Build Status](https://travis-ci.org/erickzanardo/rambi.png?branch=master)](https://travis-ci.org/erickzanardo/rambi)

Rambi is a simple framework to let you develop on Google App Engine using JavaScript. To use it in your maven project
just add the following repository and dependency to you pom.xml.

```xml
  <repository>
    <id>erickzanardo-releases</id>
    <url>http://erickzanardo.github.com/maven/releases/</url>
  </repository>

  <dependency>
    <groupId>com.rambi</groupId>
    <artifactId>rambi-engine</artifactId>
    <version>1.1.3</version>
  </dependency>
```

You must configure a filter to enable rambi handle the requests to your services, for example:
```xml
  <filter>
    <filter-name>RambiFilter</filter-name>
    <filter-class>com.rambi.core.RambiFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>RambiFilter</filter-name>
    <url-pattern>/services/*</url-pattern>
  </filter-mapping>
```

Google App Engine does cache automatically of static resources (like our js files), making impossible for rambi to filter the service files, so we must tell GAE not to do this cache with our files, to do so just add the following lines in you appengine-web.xml

```xml
  <static-files>
		<exclude path="/services/**.js" />
	</static-files>
```

This means that every js file under the folder services are only avaible through rambi. To create a new service just add
a new js file into your mapped folder, below I'll show a simple hello world service

```javascript
//webapp/services/hello.js
var service = {
    get: function(req, resp) {
        resp.print("Hello World!");
    }
};
```

Acessing now http://localhost:8080/services/hello.js will print Hello World on your browser!

There is only one rule to follow, every service must have a ``` service ``` object with functions to represent the HTTP methods
that this service will serve.


Request and response
=====
Every function that represents a http method in a service receives as parameter a request and a response, the following code shows
functions avaiable on those objects

```javascript
// Response
resp.characterEncoding("UTF-8");
resp.contentType("application/json");
resp.contentLength(10000);
resp.dateHeader("Date-Header", 0);
resp.intHeader("Int-Header", 1);
resp.header("Test-Header", "Test");
resp.characterEncoding();
resp.contentType();
resp.containsHeader("Test-Header");
resp.addCookie("My-Cookie", "CookieTestValue");
resp.print("");
resp.println("");
resp.printJson({ret}); // shortcut for resp.print(JSON.stringify({}))
resp.status(200);

// Request
req.param("paramname")
req.paramAsJson("json"); // shortcut for JSON.parse(req.param("paramname"))
req.localAddr();
req.localName();
req.remoteAddr();
req.remoteHost();
req.protocol();
req.method();
req.requestURI();
req.queryString();
req.contextPath();
req.paramMap();
req.paramNames();
req.paramValues("param");
req.cookies();
req.header("headerTest");
```

Modules
=====

In Rambi you can import others js files to reuse code. There are two types of import functions: importModule and importWebModule
, the differences are that the importModule search the files in the class loader and importWebModule search in the servlet context.
Those functions accepts two parameters, the first is the path to the file and the second is the name of the object to be
imported

Example:

```javascript
//webapp/js/module.js
function webModule() {
  return "I'm a web module";
}

//src/main/resources/com.app/module.js
function module() {
  return "I'm a module";
}

//webapp/services/modulesExamples.js
var webModule = importWebModule("/js/module.js", "webModule");
var module = importModule("com/app/module.js", "module");

var service = {
    get: function(req, resp) {
        resp.println(webModule());
        resp.println(module());
    }
};
```
Acessing http://localhost:8080/services/modulesExamples.js in your browser, should print:
I'm a web module
I'm a module

Datastore
=====

Rambi provides a simple interface to deal with data persistence on datastore. To use it you must import the datastore.js module
using the following code

```javascript
var db = importModule('com/rambi/core/datastore.js', 'db');
```

PUT

The put function is used to persist data, it returns the id for the created entity and receives three parameters:
<table>
  <tr>
    <th>Name</th><th>Type</th><th>Optional</th><th>Description</th>
  </tr>
  <tr>
    <td>Kind</td><td>String</td><td>No</td><td>Represents the kind for the datastore entity</td>
  </tr>
  <tr>
    <td>Data</td><td>JSON</td><td>No</td><td>The json to be persisted</td>
  </tr>
  <tr>
    <td>Identifier</td><td>Number</td><td>Yes</td><td>The identifier to be used to create the entity, when not provided datastore automatically generate one</td>
  </tr>
</table>

Example
```javascript
var id = db().put("MyKind", {language: "JavaScript"});
```

GET

This function is used to recover data from datastore, it requires two parameters
<table>
  <tr>
    <th>Name</th><th>Type</th><th>Optional</th><th>Description</th>
  </tr>
  <tr>
    <td>Kind</td><td>String</td><td>No</td><td>kind on the datastore entity to look</td>
  </tr>
  <tr>
    <td>Identifier</td><td>Number</td><td>No</td><td>The identifier that represents this entity</td>
  </tr>
</table>

Example
```javascript
var entity = db().get("MyKind", 1);
```

DELETE

This function is used to delete entities from the datastore and it requires the same parameters that the get function require.

```javascript
db().delete("MyKind", 1);
```
QUERIES

It's possible to perform queries on your persisted data using the function prepareQuery, it's receive the kind as parameter and return an object based on the design pattern builder, that allow you to add filters, sorts, limit, offset and fecth the results, the following table list the avaiable functions on this object.

<table>
  <tr>
    <th>Function</th><th>Usage</th><th>Description</th>
  </tr>
  <tr>
    <td>in</td>
    <td>db().prepareQuery("Kind").in("Field", [0,1])</td>
    <td>Filter operator IN</td>
  </tr>
  <tr>
    <td>notEq</td>
    <td>db().prepareQuery("Kind").notEq("Field", 1)</td>
    <td>Filter operator NOT EQUALS</td>
  </tr>
  <tr>
    <td>eq</td>
    <td>db().prepareQuery("Kind").eq("Field", 1)</td>
    <td>Filter operator EQUALS</td>
  </tr>
  <tr>
    <td>gtEq</td>
    <td>db().prepareQuery("Kind").gtEq("Field", 1)</td>
    <td>Filter operator GREATER OR EQUALS THAN</td>
  </tr>
  <tr>
    <td>gt</td>
    <td>db().prepareQuery("Kind").gt("Field", 1)</td>
    <td>Filter operator GREATER THAN</td>
  </tr>
  <tr>
    <td>ltEq</td>
    <td>db().prepareQuery("Kind").ltEq("Field", 1)</td>
    <td>Filter operator LESS OR EQUALS THAN</td>
  </tr>
  <tr>
    <td>lt</td>
    <td>db().prepareQuery("Kind").lt("Field", 1)</td>
    <td>Filter operator LESS THAN</td>
  </tr>
  <tr>
    <td>asc</td>
    <td>db().prepareQuery("Kind").asc("Field")</td>
    <td>Sort operator ASCENDING</td>
  </tr>
  <tr>
    <td>desc</td>
    <td>db().prepareQuery("Kind").desc("Field")</td>
    <td>Sort operator DESCENDING</td>
  </tr>
  <tr>
    <td>offset</td>
    <td>db().prepareQuery("Kind").offset(10)</td>
    <td>Set the offset for this query</td>
  </tr>
  <tr>
    <td>limit</td>
    <td>db().prepareQuery("Kind").limit(10)</td>
    <td>Set the limit for this query</td>
  </tr>
  <tr>
    <td>result</td>
    <td>db().prepareQuery("Kind").result()</td>
    <td>Returns an array of the resulting entities</td>
  </tr>
</table>

Another way to perfom queries, is declaring a literal object that represents de query, and calling the query function passing that object as parameter, for example:


```javascript
var query = {
    kind : "KIND",
    filters : [{ field1 : { EQ : 1 } },
               { field2 : { EQ : 2 } }],
    limit: 1,
    offset: 2,
    sort: [{field1: "ASC"}]
};
result = db().query(query);
```

Memcache
=====

Rambi provides a simple interface to access Google App Engine memcache, it contains functions to put, delete and get information. Bellow there is a simple service as an example

```javascript
var cache = importModule('com/rambi/core/memcache.js', 'cache');

var service = {
    get : function(req, resp) {
        var value = cache().get("value");
        resp.print(value);
    },
    put : function(req, resp) {
        if (req.param("expirationInMillis")) {
            var e = parseInt(req.param("expirationInMillis"));
            cache().put("value", req.param("value"), e);
        } else {
            cache().put("value", req.param("value"));
        }
    },
    delete : function(req, resp) {
        cache().delete("value");
    }
};
```

URLFetch
=====

It's possible to fetch urls from your service using the urlfetcher.js module, the following code shows how to read a simple json from an url and write it on the response

```javascript
var fetcher = importModule('com/rambi/core/urlfetcher.js', 'urlfetcher');

var service = {
    get : function(req, resp) {
      var str = fetcher().get("http://localhost:8080/check.json");
      var ret = JSON.parse(str);
      resp.print(JSON.stringify(ret));
    }
};
```
