# Spring Boot Chaos Monkey Scripts

The project comes with the Spring Boot Chaos Monkey configured for the following services:

* `customers-service`
* `visits-service`
* `vets-service`

Adding the following dependency:

```
  <!-- Chaos Monkey -->
  <dependency>
      <groupId>de.codecentric</groupId>
      <artifactId>chaos-monkey-spring-boot</artifactId>
  </dependency>
```

And activating the Spring Configuration at global level (`application.yml`):

```
# Chaos Engineering
---
spring:
  config:
    activate:
      on-profile: chaos-monkey
management.endpoint.chaosmonkey.enabled: true
chaos:
  monkey:
    enabled: true
    watcher:
      component: false
      controller: false
      repository: false
      rest-controller: false
      service: false
```

You can read more about the possible configuration options [here](https://codecentric.github.io/chaos-monkey-spring-boot/latest/#_properties).

## Fire the Chaos

In order to active Spring Boot Chaos Monkey's assault options and component instrumentation, you need to call the project's API.

For your convenience we're providing a [script](./scripts/chaos) that turns on various watchers and attacks. To print out the usage description just call the script without any parameters.

```
$ ./scripts/chaos/call_chaos.sh
usage: ./scripts/chaos/call_chaos.sh: <customers|visits|vets> <attacks_enable_exception|attacks_enable_killapplication|attacks_enable_latency|attacks_enable_memory|watcher_enable_component|watcher_enable_controller|watcher_enable_repository|watcher_enable_restcontroller|watcher_enable_service|watcher_disable>
First pick either customers, visits or vets
Then pick what to enable. Order matters!
Example
./scripts/chaos/call_chaos.sh visits attacks_enable_exception watcher_enable_restcontroller
```

The script takes in at minimum 2 parameters.

 * First provides the name of the application for which you want to turn on Chaos Monkey features.
 * The subsequent ones will enable attacks and watchers.

The name of the desired feature maps to a json file that gets updated to `http://localhost:${PORT}/actuator/chaosmonkey/assaults` and `http://localhost:${PORT}/actuator/chaosmonkey/watchers` respectively. Example of enabling exception assault via rest controllers for the visits microservice:

```
$ ./scripts/chaos/call_chaos.sh visits attacks_enable_exception watcher_enable_restcontroller
```

The default assault configuration is set to fail every 5th request. That means that the first four will work as if Chaos Monkey was be disabled.
