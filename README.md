Rambi
=====

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
