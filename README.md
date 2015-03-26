fluenthttp-swagger
==============
[![Build Status](https://api.travis-ci.org/livingobjects/fluenthttp-swagger.png)](https://travis-ci.org/livingobjects/fluenthttp-swagger)

A swagger extension for fluenthttp which provides Swagger REST API documentation automatically (<a href="https://github.com/swagger-api/swagger-spec/blob/master/versions/2.0.md">Swagger spec v2.0</a>).
For information about supported format see parser : https://github.com/livingobjects/myrddin/blob/develop/README.md

## Why fluenthttp-swagger ?

This module is done to be deployed to your fluenthttp.
- an HTML page which display the REST API documentation as a styled UI.
- and the raw swagger file (in YAML)

See details below for the routes created by fluenthttp-swagger.

## Usage

```java
new WebServer().configure(routes -> SwaggerEntryPoint.addApiDocRoutes(routes, swaggerFiles))
```

We are waiting for https://github.com/CodeStory/fluent-http/pull/102 to be merged

## Build

Run maven

```shell
mvn clean install
```

To deploy to your local repository use "altDeploymentRepository" option like this :

```shell
mvn deploy -DaltDeploymentRepository=nexus::default::http://xxx.xx.xx.xx:8081/nexus/content/repositories/snapshots
```

Or to deploy a release:

```shell
mvn deploy -DaltDeploymentRepository=nexus::default::http://xxx.xx.xx.xx:8081/nexus/content/repositories/releases
```

## How to use

#### Add fluenthttp-swagger dependency

You have to deploy the fluenthttp-swagger module and its dependencies : myrddin, snakeyaml into your application.
 
#### Create your swagger documentation

Into your own application, add a Swagger YAML documentation of your REST api, example:
```
src/main/resources/swagger/api.yaml
```

See example on Swagger 2.0 format on http://editor.swagger.io/

#### Declare your Swagger doc to fluenthttp-swagger

Fluenthttp-swagger will server the documentation.

It will read all the base uris routes defined into your Swagger documentation and serve a page for these uris.

Example:

```yaml
#Swagger file
paths:
  /route1/test
  [...]
  /route2/test
  [...]
```

The following urls will be served:

 http://localhost:9000/api-doc/route1

 http://localhost:9000/api-doc/route2

 http://localhost:9000/api-doc/route1/raw

 http://localhost:9000/api-doc/route2/raw


