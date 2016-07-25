# httpserver

Simple web server based on [com.sun.net.httpserver](http://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html). This is an exercise of a web server implementation using only JAVA SE 8 classes. Only tests have external dependencies which are [JUnit](http://junit.org/junit4/) and [Apache HttpClient](https://hc.apache.org/httpcomponents-client-4.5.x/index.html). 

## Description

The server has three private pages page1.html, page2.html and page3.html and a login page, login.html. One administrator (admin:admin) and three users have been created for testing purposes. User (user1) has access to page1, (user2) to page2 and (user3) to page3. The static resources are:

* /login.html
* /page1.html
* /page2.html
* /page3.html

Once log in, users are granted a session token which flow from browser to server using cookies. Sessions expire after 5 minutes of inactivity.

There is also a REST API at endpoint /api/user. The administrator user can create, update and delete users. It must be authenticated through basic authentication The operations are as follows:

* **POST** /api/user/user4?password=user4&role=PAGE_1,PAGE_2
* **PUT** /api/user/user4?role=PAGE_3
* **DELETE** /api/user/user4 

All the authenticated users can also read the REST API:

* **GET** /api/user/user4

## Installation

You need [Maven](https://maven.apache.org/download.cgi) to launch tests, compile and build the jar. And obviously the JVM. The application targets Java 1.8. You can download JAVA 8 SE [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

To pass the tests, compile and build the jar, once in the project directory, type:
$ mvn test
$ mvn compile
$ mvn jar:jar

You will find the jar located at "target" directory, named "test-web-application.jar". You can launch the server with an argument for the port to bind like this:
$ java -jar target/test-web-application.jar 8001

## Design considerations

Simple web server is a week-end training exercise where to apply the common design patterns for a web application. It is also a work in progress with some "TODO" left for future sessions.

The core pattern for the application is a [Model-View-Controller Pattern](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) with the views as static resources. I used TDD [Red-Green-Refactor cycle](http://blog.cleancoder.com/uncle-bob/2014/12/17/TheCyclesOfTDD.html) to implement the solution, mainly integration more than unit testing due to the nature of classes and methods. All the classes dependencies are handled manually by a [Dependency Injection Container](http://martinfowler.com/articles/injection.html) implemented in the Context, RunContext and TestContext classes, which is a must if you want to drive your development with TDD and inject mocks and different configurations for testing purposes. See the usage of IClock interface both in Test and Run configuration to investigate an example of that. 

The main controller (Front Controller) ServerHandler is an [HttpHandler](http://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpHandler.html) and implements a [Mediator Pattern](https://en.wikipedia.org/wiki/Mediator_pattern). There is also a ServerAction for each controller action. ServerAction follows [Strategy Pattern](https://en.wikipedia.org/wiki/Strategy_pattern) and its creation is left to a ServerActionFactory. This factory uses a router class, ServerRouter, which maps all the endpoints to the appropriate ServerAction and stores access rights for each endpoint. These are mainly the most important patterns used.

We manage authorization through an [Authenticator](), which is a proprietary class of com.sun.net.httpserver package. We implement a class named ServerAuthenticator which extends Authenticator and manage all the roles and access rights for the web server using also ServerRouter. We manage session cookies for the static web pages and Basic Authentication for the REST API.

A lot of refactor is already needed to get rid of [Arrow code anti-pattern](http://c2.com/cgi/wiki?ArrowAntiPattern) scattered through the action classes and also to honor Single Responsibility Principle for some classes like ServerRouter, which is managing both the endpoint mapping to actions and the access rights. Also a best use of Java 8 Optional type, which is difficult to use if you have to deal with side effects, when only we have "ifPresent" implemented and not "ifNotPresent" which will arrive in Java 9. The lifecycle of [HttpExchange](http://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpExchange.html) (which is a mutable class encapsulating both the request and the response) across the application leave to few room for immutability and functional programming, but I have tried to use Optional and lambdas when possible, where they make a cleaner code. Maybe sometimes I achieve just the opposite. 

