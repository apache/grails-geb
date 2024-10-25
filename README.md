# Grails Geb Plugin

[![Maven Central](https://img.shields.io/maven-central/v/org.grails.plugins/geb.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.grails.plugins/geb)
[![Java CI](https://github.com/grails/geb/actions/workflows/gradle.yml/badge.svg?event=push)](https://github.com/grails/geb/actions/workflows/gradle.yml)

## Geb Functional Testing for the Grails® framework

This plugin provides the Geb dependencies and a `create-functional-test` command for generating Geb tests in a Grails app.

It also provides a `ContainerGebSpec` class, which can be used in place of `GebSpec`, that automatically
runs the browser in a container using [Testcontainers](https://java.testcontainers.org/). This requires a
[compatible container runtime](https://java.testcontainers.org/supported_docker_environment/) be installed, such as:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [OrbStack](https://orbstack.dev/) - macOS only
- [Rancher Desktop](https://rancherdesktop.io/)
- [podman desktop](https://podman-desktop.io/)
- [Colima](https://github.com/abiosoft/colima) - macOS and Linux

For further reference please see the [Geb documentation](https://www.gebish.org).

## Examples

If you are looking for examples on how to write Geb tests, check:

[Geb/Grails example project](https://github.com/grails-samples/geb-example-grails) or [Grails functional test suite](https://github.com/grails/grails-functional-tests) where Geb tests are used extensively.

## Additional Drivers

Grails comes with a set of pre-installed browser drivers.
To set up additional drivers you need to add the driver to your `build.gradle` for example:
```groovy
integrationTestRuntimeOnly "org.seleniumhq.selenium:selenium-edge-driver:$seleniumVersion"
```

You also need to add it to the `GebConfig.groovy` file in the `src/integration-test/resources/` directory. For example:
```groovy
/*
    This is the Geb configuration file.

    See: http://www.gebish.org/manual/current/#configuration
*/

/* ... */
import org.openqa.selenium.edge.EdgeDriver

environments {
    
    /* ... */
    
    edge {
        driver = { new EdgeDriver() }
    }
}
```

Now you can run your tests with the new driver by specifying the Geb environment:
```console
./gradlew integrationTest -Dgeb.env=edge
```
