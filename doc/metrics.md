# Metrics Configuration

You can use this repo in combination with the [Observability Sandbox Lite](https://github.com/adriannovegil/observability-sandbox-lite)

The project is launched with [Micrometer](https://micrometer.io/) and the [Micrometer Prometheus Registry](https://micrometer.io/docs/registry/prometheus) activated in the following services:

 * `customers-service`
 * `visits-service`
 * `vets-service`
 * `api-gateway`

## Maven Dependencies

To collect the metrics and expose it using Prometheus add this dependencies to the project

```
<!-- Micrometer -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.7.5</version>
</dependency>
<dependency>
    <groupId>io.github.mweirauch</groupId>
    <artifactId>micrometer-jvm-extras</artifactId>
    <version>0.2.2</version>
</dependency>
```

[`micrometer-jvm-extras`](https://github.com/mweirauch/micrometer-jvm-extras) is a set of additional JVM process metrics for [Micrometer](https://micrometer.io/).

## Spring Configuration

Add the following configuration to your projects to activate and configure the metrics extraction:

### Global configuration

Add this lines to your project or in the global configuration file. In our case we added this lines in the `application.yml` file

```
# Metrics
management:
  endpoint:
    metrics:
      enabled: true
    prometheus:
      enabled: true
  endpoints:
    web:
      exposure:
        include: '*'
  metrics:
    export:
      prometheus:
        enabled: true
```

### Project Configuration

Add this lines on each project you want to activate the metrics recollection

```
spring:
  application:
    name: customer-service
```

The application name is used to tag the metrics

```
management:
  metrics:
    tags:
      application: ${spring.application.name}
    distribution:
      percentiles-histogram:
        http.server:
          requests: true
      slo:
        http:
          server:
            requests: 50ms, 100ms, 200ms, 400ms
      percentiles:
        http:
          server:
            requests: 0.5, 0.9, 0.95, 0.99, 0.999
```

We activate the metrics and the percentile calculation for the http layer

## Prometheus Target Configuration

To get the metrics just add this code to the Prometheus targets configuration:

```
  # Spring petclinic services monitoring
  # You can use Eureka's application instance metadata.
  # If you are using SpringBoot, you can add metadata using eureka.instance.metadataMap like this:
  # application.yaml (spring-boot)
  # eureka:
  #  instance:
  #    metadataMap:
  #      "prometheus.scrape": "true"
  #      "prometheus.path": "/actuator/prometheus"
  - job_name: eureka-discovery
    metrics_path: /actuator/prometheus
    scrape_interval: 15s
    scrape_timeout: 15s
    eureka_sd_configs:
    - server: http://discovery-server:8761/eureka
      refresh_interval: 30s
    relabel_configs:
    # Relabel to customize metric path based on application
    # "prometheus.path = <metric path>" annotation.
    - source_labels: [__address__, __meta_eureka_app_instance_metadata_prometheus_port]
      action: replace
      regex: ([^:]+)(?::\d+)?;(\d+)
      replacement: $1:$2
      target_label: __address__
    # Relabel to scrape only application that have
    # "prometheus.scrape = true" metadata.
    - source_labels: [__meta_eureka_app_instance_metadata_prometheus_scrape]
      action: keep
      regex: true
```

In the case we discover the targets using the Eureka server of the project.

The Docker Compose file have been configured with the observability network, so you don't need to do nothing to connect all the containers together:

```
networks:
  observabilitysandbox:
    name: observabilitysandbox
    external: true
```

## Custom metrics

Spring Boot registers a lot number of core metrics: JVM, CPU, Tomcat, Logback... The Spring Boot auto-configuration enables the instrumentation of requests handled by Spring MVC.

### Class Level Metrics with the `@Timed` Annotation

All those three REST controllers `OwnerResource`, `PetResource` and `VisitResource` have been instrumented by the `@Timed` Micrometer annotation at class level.

 * `customers-service` application has the following custom metrics enabled:
  * `@Timed`: `petclinic.owner`
  * `@Timed`: `petclinic.pet`
 * visits-service application has the following custom metrics enabled:
  * `@Timed`: `petclinic.visit`

### Code Leve Custom Metrics

In the OwnerResource class we define a totalMethodTimer to measure the execution time of the methods at code level.

```
...
    private final Timer totalMethodTimer = Timer.builder("petclinic.owner.request.total")
        .publishPercentiles(0.5, 0.9, 0.99)
        .percentilePrecision(0)
        .distributionStatisticExpiry(Duration.ofMinutes(1))
        .distributionStatisticBufferLength(32767)
        .publishPercentileHistogram()
        .register(Metrics.globalRegistry);
...

    /**
     * Create Owner
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Owner createOwner(@Valid @RequestBody Owner owner) {        
        Timer.Sample methodTimer = Timer.start(Metrics.globalRegistry);                
        Owner localOwner = ownerRepository.save(owner);
        // Export the metric
        methodTimer.stop(totalMethodTimer);
        return localOwner;
    }
...
```
