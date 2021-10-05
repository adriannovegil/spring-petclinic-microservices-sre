# Distributed version of the Spring PetClinic Sample Application built with Spring Cloud

*Based on the original [Spring Petclinic microservices](https://github.com/spring-petclinic/spring-petclinic-microservices) repository.*

## Starting services locally without Docker

Every microservice is a Spring Boot application and can be started locally using IDE or `./mvnw spring-boot:run` command.

Please note that supporting services (__Config and Discovery Server__) must be started before any other application (Customers, Vets, Visits and API).

Startup of __Admin server__ is optional.

## Starting services locally with docker-compose

In order to start entire infrastructure using Docker, you have to build images by executing

```
$ ./mvnw clean install -PbuildDocker
```

from a project root.

Once images are ready, you can start them with a single command

```
$ docker-compose up
```

After starting services it takes a while for `API Gateway` to be in sync with service registry, so don't be scared of initial Zuul timeouts.

*NOTE: Under MacOSX or Windows, make sure that the Docker VM has enough memory to run the microservices. The default settings
are usually not enough and make the `docker-compose up` painfully slow.*

## Database configuration

In its default configuration, Petclinic uses an in-memory database (HSQLDB) which gets populated at startup with data.

A similar setup is provided for MySql in case a persistent database configuration is needed.

Dependency for Connector/J, the MySQL JDBC driver is already included in the `pom.xml` files.

### Start a MySql database

You may start a MySql database with docker:

```
docker run -e MYSQL_ROOT_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7.8
```

or download and install the MySQL database (e.g., MySQL Community Server 5.7 GA), which can be found here: https://dev.mysql.com/downloads/

### Use the Spring 'mysql' profile

To use a MySQL database, you have to start 3 microservices (`visits-service`, `customers-service` and `vets-services`) with the `mysql` Spring profile. Add the `--spring.profiles.active=mysql` as programm argument.

By default, at startup, database schema will be created and data will be populated.
You may also manualy create the PetClinic database and data by executing the `"db/mysql/{schema,data}.sql"` scripts of each 3 microservices.
In the `application.yml` of the [Configuration repository], set the `initialization-mode` to `never`.

If you are running the microservices with Docker, you have to add the `mysql` profile into the (Dockerfile)[docker/Dockerfile]:

```
ENV SPRING_PROFILES_ACTIVE docker,mysql
```

In the `mysql section` of the `application.yml` from the [Configuration repository], you have to change
the host and port of your MySQL JDBC connection string.

## Services

If everything goes well, you can access the following services at given location:

__Business services__

  * AngularJS frontend (API Gateway) - http://localhost:8080
  * Customers, Vets and Visits Services - random port, check Eureka Dashboard

__Infrastructure__

 * Discovery Server - http://localhost:8761
 * Config Server - http://localhost:8888
 * Admin Server (Spring Boot Admin) - http://localhost:9090

__Load__

 * [Artillery](https://artillery.io/) load generator for the Pet-Clinic. (To ensure that all services are up and running properly.)

## References

 * https://github.com/spring-petclinic/spring-petclinic-microservices
 * https://github.com/spring-petclinic/spring-petclinic-rest
 * https://github.com/gantsign/spring-petclinic-openapi/tree/master/src/main/java/org/springframework/samples/petclinic/web/api


## Contributing

For a complete guide to contributing to the project, see the [Contribution Guide](CONTRIBUTING.md).

We welcome contributions of any kind including documentation, organization, tutorials, blog posts, bug reports, issues, feature requests, feature implementations, pull requests, answering questions on the forum, helping to manage issues, etc.

The project community and maintainers are very active and helpful, and the project benefits greatly from this activity.

### Reporting Issues

If you believe you have found a defect in the project or its documentation, use the repository issue tracker to report the problem to the project maintainers.

If you're not sure if it's a bug or not, start by asking in the discussion forum. When reporting the issue, please provide the version.

### Submitting Patches

The project welcomes all contributors and contributions regardless of skill or experience level.

If you are interested in helping with the project, we will help you with your contribution.

We want to create the best possible tool for our development teams and the best contribution experience for our developers, we have a set of guidelines which ensure that all contributions are acceptable.

The guidelines are not intended as a filter or barrier to participation. If you are unfamiliar with the contribution process, the team will help you and teach you how to bring your contribution in accordance with the guidelines.

For a complete guide to contributing, see the [Contribution Guide](CONTRIBUTING.md).

## Code of Conduct

See the [code-of-conduct.md](./code-of-conduct.md) file

## License

See the [LICENSE](./LICENSE) file
