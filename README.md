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
var service = {
    get: function(req, resp) {
        resp.print("Hello World!");
    }
};
```

There is only one rule to follow, every service must have an service object with functions to represent the HTTP methods
that this service will serve.
