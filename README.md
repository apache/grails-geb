# Grails Geb Plugin

[![Maven Central](https://img.shields.io/maven-central/v/org.grails.plugins/geb.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.grails.plugins/geb)
[![Java CI](https://github.com/grails/geb/actions/workflows/gradle.yml/badge.svg?event=push)](https://github.com/grails/geb/actions/workflows/gradle.yml)

## Geb Functional Testing for the Grails® framework

This plugin integrates [Geb](https://www.gebish.org) with [Grails](https://www.grails.org) to make it easy to write functional tests for your applications.

## Examples

If you are looking for examples on how to write Geb tests, check:

[Geb/Grails example project](https://github.com/grails-samples/geb-example-grails) or [Grails functional test suite](https://github.com/grails/grails-functional-tests) where Geb tests are used extensively.
For further reference please see the [Geb documentation](https://www.gebish.org).

## Usage

To use the plugin, add the following dependencies to your `build.gradle`:
```groovy
dependencies {
    implementation 'org.grails.plugins:geb' // This is only needed to use the create-functional-test command (see below)
    integrationTestImplemntation testFixtures('org.grails.plugins:geb') // This is needed to compile and run the tests
}
```

To get started, you can use the `create-functional-test` command to generate a new Geb test:

```console
./grailsw create-functional-test com.example.MyFunctionalSpec
```

This will create a new Geb test in the `src/integration-test/groovy/com/example` directory.

There are two ways to use this plugin, either by extending your test classes with the `ContainerGebSpec` class or with the `GebSpec` class.

### ContainerGebSpec (recommended)

By extending your test classes with the `ContainerGebSpec` class, you can run your tests in a containerized browser using [Testcontainers](https://java.testcontainers.org/).
This requires a [compatible container runtime](https://java.testcontainers.org/supported_docker_environment/) to be installed, such as:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [OrbStack](https://orbstack.dev/) - macOS only
- [Rancher Desktop](https://rancherdesktop.io/)
- [podman desktop](https://podman-desktop.io/)
- [Colima](https://github.com/abiosoft/colima) - macOS and Linux

If you choose to use the `ContainerGebSpec` class, as long as you have a compatible container runtime installed, you don't need to do anything else.
Just run `./gradlew integrationTest` and a container will be started and configured to start a browser that can access your application under test.

### GebSpec

If you choose to use the `GebSpec` class, you will need to have a browser driver installed that matches a browser you have installed on your system.
This plugin comes with the `selenium-chrome-driver` pre-installed, but you can also set up additional drivers.

To set up additional drivers, you need to add the driver to your `build.gradle` for example:
```groovy
integrationTestRuntimeOnly "org.seleniumhq.selenium:selenium-firefox-driver"
integrationTestRuntimeOnly "org.seleniumhq.selenium:selenium-edge-driver"
```

You also need to add a `GebConfig.groovy` file in the `src/integration-test/resources/` directory. For example:
```groovy
/*
    This is the Geb configuration file.

    See: http://www.gebish.org/manual/current/#configuration
*/

/* ... */
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver

environments {
    
    /* ... */
    edge {
        driver = { new EdgeDriver() }
    }
    firefox {
        driver = { new FirefoxDriver() }
    }
}
```

and pass on the `geb.env` system property when running your tests via Gradle

```groovy
// build.gradle
tasks.withType(Test) {
    useJUnitPlatform()
    systemProperty 'geb.env', System.getProperty('geb.env')
}
```

Now you can run your tests with the browsers installed on your system by specifying the Geb environment you have set up in your `GebConfig.groovy` file. For example:
```console
./gradlew integrationTest -Dgeb.env=edge
```
