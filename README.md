# Distributed version of the Spring PetClinic Sample Application built with Spring Cloud

*Based on the original [Spring Petclinic Microservices](https://github.com/spring-petclinic/spring-petclinic-microservices) repository and the [Spring Petclinic Microservices CLoud Version](https://github.com/spring-petclinic/spring-petclinic-cloud).*

This microservices branch was initially derived from [AngularJS version](https://github.com/spring-petclinic/spring-petclinic-angular1) to demonstrate how to split sample Spring application into [microservices](http://www.martinfowler.com/articles/microservices.html). To achieve that goal, we use Spring Cloud Gateway, Spring Cloud Circuit Breaker, Spring Cloud Config, Spring Cloud Sleuth, Resilience4j, Micrometer and the Eureka Service Discovery from the [Spring Cloud Netflix](https://github.com/spring-cloud/spring-cloud-netflix) technology stack.

## Starting services locally

Every microservice is a Spring Boot application and can be started locally.

Please note that supporting services (__Config and Discovery Server__) must be started before any other application (Customers, Vets, Visits and API).

Startup of __Admin server__ is optional.

### Starting Services Locally without Docker

Check the [local.md](./doc/local.md) file in the [doc](./doc) folder

### Starting Services Locally with Docker

Check the [local-docker.md](./doc/local-docker.md) file in the [doc](./doc) folder

### Starting Services Locally with Docker & Docker Compose

Check the [local-compose.md](./doc/local-compose.md) file in the [doc](./doc) folder

### Starting Services Locally with Kubernetes (Minikube)

Check the [local-k8s.md](./doc/local-k8s.md) file in the [doc](./doc) folder

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
