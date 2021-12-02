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
make up
```

or, if you prefer

```
docker-compose up
```

After starting services it takes a while for `API Gateway` to be in sync with service registry, so don't be scared of initial Zuul timeouts.

*NOTE: Under MacOSX or Windows, make sure that the Docker VM has enough memory to run the microservices. The default settings
are usually not enough and make the `docker-compose up` painfully slow.*

## Services

The following services will be started. Some of them are accessible via web:

| Component                                  | Description                                                 | Port                               |
| ---------------------------------------    | --------------------------------------------------------    | -------------------------------    |
| `config-server`                            | Spring config server                                        | [`8888`](http://localhost:8888/)   |
| `discovery-server`                         | Spring discovery server                                     | [`8761`](http://localhost:8761/)   |
| `customers-service`                        | Customers service                                           | [`8081`](http://localhost:8081/)   |
| `visits-service`                           | Visits service                                              | [`8082`](http://localhost:8082/)   |
| `vets-service`                             | Vets service                                                | [`8083`](http://localhost:8083/)   |
| `api-gateway`                              | Api Gateway                                                 | [`8080`](http://localhost:8080/)   |
| `admin-server`                             | Admin server                                                | [`9091`](http://localhost:9091/)   |
| `load-server`                              | [Load generator based on Artillery](https://artillery.io/)  | N/A                                |

## Database configuration

Check the [database.md](./doc/database.md) file in the [doc](./doc) folder

## Load Testing

A JMeter load testing script is available to stress the application and generate metrics: [petclinic_test_plan.jmx](./jmeter/petclinic_test_plan.jmx)

## Metrics

Check the [metrics.md](./doc/metrics.md) file in the [doc](./doc) folder

## Tracing

Check the [tracing.md](./doc/tracing.md) file in the [doc](./doc) folder

## Logs

Check the [logs.md](./doc/logs.md) file in the [doc](./doc) folder

## Chaos

Check the [chaos.md](./doc/chaos.md) file in the [doc](./doc) folder

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
