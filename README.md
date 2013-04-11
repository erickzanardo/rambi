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
    <version>0.1.1</version>
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
