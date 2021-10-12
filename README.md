# sample-app
* Sample app that prints env variables
 
* How to build the jar

./gradlew build -x test
 
* Supported api's
    1. /hola --> Greets with welcome message.
    2. /actuator/health --> checks the connectivity to database.
    3. /actuator/info --> app version information.
    4. /actuator/env --> exposed environment variables. Sensitive info is redacted.
    5. /actuator/metrics --> jvm metrics.
    
>NOTE: This is a sample app for deploying in arcus prod cluster. All the api's require a login.
The credentials for the app are stored as kubernetes secret sampleapp-ap-secret and mounted as env variables
APP_USERNAME and APP_PASSWORD

>NOTE: Sensitive information from actuator endpoints are sanitized in the response
management.endpoint.env.keys-to-sanitize=password,secret,user,username,url,host

* Swagger Spec
    1. /swagger
    2. /swagger-ui.html

* Example
    
    ##### Curl /hola
    ```
    curl -v --user username:password localhost:8080/hola
    *   Trying ::1...
    * TCP_NODELAY set
    * Connected to localhost (::1) port 8080 (#0)
    * Server auth using Basic with user '**********'
    > GET /hola HTTP/1.1
    > Host: localhost:8080
    > Authorization: Basic *************
    > User-Agent: curl/7.54.0
    > Accept: */*
    >
    < HTTP/1.1 200
    < Set-Cookie: JSESSIONID=ABB2A5FA5D52B67A31922588CC9D7772; Path=/; HttpOnly
    < X-Content-Type-Options: nosniff
    < X-XSS-Protection: 1; mode=block
    < Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    < Pragma: no-cache
    < Expires: 0
    < X-Frame-Options: DENY
    < Content-Type: text/plain;charset=ISO-8859-1
    < Content-Length: 27
    < Date: Tue, 23 Oct 2018 16:29:37 GMT
    <
    * Connection #0 to host localhost left intact
    Welcome to ARCUS sample app
    ```
    
    ##### Curl /actuator/health
    ``` 
    curl -v --user username:password localhost:8080/actuator/health
    *   Trying ::1...
    * TCP_NODELAY set
    * Connected to localhost (::1) port 8080 (#0)
    * Server auth using Basic with user '**********'
    > GET /actuator/health HTTP/1.1
    > Host: localhost:8080
    > Authorization: Basic **************
    > User-Agent: curl/7.54.0
    > Accept: */*
    >
    < HTTP/1.1 200
    < Set-Cookie: JSESSIONID=326C139A16FD89AC3A196A2617F7FC9A; Path=/; HttpOnly
    < X-Content-Type-Options: nosniff
    < X-XSS-Protection: 1; mode=block
    < Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    < Pragma: no-cache
    < Expires: 0
    < X-Frame-Options: DENY
    < Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8
    < Transfer-Encoding: chunked
    < Date: Tue, 23 Oct 2018 16:30:01 GMT
    <
    * Connection #0 to host localhost left intact
    {
      "status":"UP"
    }
    ```
    
    ##### Curl /swagger
    ``` 
    curl -v --user username:password localhost:8080/swagger
    *   Trying ::1...
    * TCP_NODELAY set
    * Connected to localhost (::1) port 8080 (#0)
    * Server auth using Basic with user '********'
    > GET /swagger HTTP/1.1
    > Host: localhost:8080
    > Authorization: Basic **************
    > User-Agent: curl/7.54.0
    > Accept: */*
    >
    < HTTP/1.1 200
    < Set-Cookie: JSESSIONID=21697D2361635811AC62E0BF39D404CA; Path=/; HttpOnly
    < X-Content-Type-Options: nosniff
    < X-XSS-Protection: 1; mode=block
    < Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    < Pragma: no-cache
    < Expires: 0
    < X-Frame-Options: DENY
    < Content-Type: application/json;charset=UTF-8
    < Transfer-Encoding: chunked
    < Date: Tue, 23 Oct 2018 16:44:22 GMT
    <
    * Connection #0 to host localhost left intact
    {
      "swagger": "2.0",
      "info": {
        "description": "API to access sample app.",
        "version": "1.0.0",
        "title": "Sample App API",
        "termsOfService": "Terms of service: This is a temporary mock service for internal use only",
        "contact": {
          "name": "www.uday.com"
        },
        "license": {
          "name": "Internal use only",
          "url": "www.uday.com"
        }
      },
      "host": "localhost:8080",
      "basePath": "/",
      "tags": [
        {
          "name": "hola-controller",
          "description": "Hola Controller"
        }
      ],
      "paths": {
        "/hola": {
          "get": {
            "tags": [
              "hola-controller"
            ],
            "summary": "hola",
            "operationId": "holaUsingGET",
            "consumes": [
              "application/json"
            ],
            "produces": [
              "*/*"
            ],
            "responses": {
              "200": {
                "description": "OK",
                "schema": {
                  "type": "string"
                }
              },
              "401": {
                "description": "Unauthorized"
              },
              "403": {
                "description": "Forbidden"
              },
              "404": {
                "description": "Not Found"
              }
            }
          }
        }
      }
    }
    ```
    
* Install
    ```
    Pre-Requisite:
    1. Kubernetes secret sampleapp-ap-secret and mysql-apcustomer-secret
    2. Kubernetes configmap external-service-config.yaml
    ```