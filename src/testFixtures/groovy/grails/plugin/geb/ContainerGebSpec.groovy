/*
 * Copyright 2024 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.geb

import geb.spock.GebSpec
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.Testcontainers
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.PortForwardingContainer
import spock.lang.Shared

import java.time.Duration

/**
 * A {@link geb.spock.GebSpec GebSpec} that leverages Testcontainers to run the browser inside a container.
 *
 * <p>Prerequisites:
 * <ul>
 *   <li>
 *       The test class must be annotated with {@link grails.testing.mixin.integration.Integration @Integration}.
 *   </li>
 *   <li>
 *       A <a href="https://java.testcontainers.org/supported_docker_environment/">compatible container runtime</a>
 *       (e.g., Docker) must be available for Testcontainers to utilize.
 *   </li>
 * </ul>
 *
 * @author Søren Berg Glasius
 * @author Mattias Reichel
 * @since 5.0.0
 */
@CompileStatic
abstract class ContainerGebSpec extends GebSpec implements ContainerAwareDownloadSupport {

    private static final String DEFAULT_HOSTNAME_FROM_CONTAINER = 'host.testcontainers.internal'
    private static final String DEFAULT_HOSTNAME_FROM_HOST = 'localhost'
    private static final String DEFAULT_PROTOCOL = 'http'

    private String hostNameFromContainer = DEFAULT_HOSTNAME_FROM_CONTAINER

    @Shared
    BrowserWebDriverContainer webDriverContainer

    @PackageScope
    void initialize() {
        webDriverContainer = new BrowserWebDriverContainer()
        Testcontainers.exposeHostPorts(port)
        webDriverContainer.tap {
            addExposedPort(this.port)
            withAccessToHost(true)
            start()
        }
        if (hostNameChanged) {
            webDriverContainer.execInContainer('/bin/sh', '-c', "echo '$hostIp\t$hostName' | sudo tee -a /etc/hosts")
        }
        WebDriver driver = new RemoteWebDriver(webDriverContainer.seleniumAddress, new ChromeOptions())
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30))
        browser.driver = driver
    }

    void setup() {
        if (notInitialized) {
            initialize()
        }
        browser.baseUrl = "$protocol://$hostName:$port"
    }

    def cleanupSpec() {
        webDriverContainer?.stop()
    }

    /**
     * Get access to container running the web-driver, for convenience to execInContainer, copyFileToContainer etc.
     *
     * @see org.testcontainers.containers.ContainerState#execInContainer(java.lang.String ...)
     * @see org.testcontainers.containers.ContainerState#copyFileToContainer(org.testcontainers.utility.MountableFile, java.lang.String)
     * @see org.testcontainers.containers.ContainerState#copyFileFromContainer(java.lang.String, java.lang.String)
     * @see org.testcontainers.containers.ContainerState
     */
    BrowserWebDriverContainer getContainer() {
        return webDriverContainer
    }

    /**
     * Returns the protocol that the browser will use to access the server under test.
     * <p>Defaults to {@code http}.
     *
     * @return the protocol for accessing the server under test
     */
    String getProtocol() {
        return DEFAULT_PROTOCOL
    }

    /**
     * Returns the hostname that the browser will use to access the server under test.
     * <p>Defaults to {@code host.testcontainers.internal}.
     * <p>This is useful when the server under test needs to be accessed with a certain hostname.
     *
     * @return the hostname for accessing the server under test
     */
    String getHostName() {
        return hostNameFromContainer
    }

    void setHostName(String hostName) {
        hostNameFromContainer = hostName
    }

    /**
     * Returns the hostname that the server under test is available on from the host.
     * <p>This is useful when using any of the {@code download*()} methods as they will connect from the host,
     * and not from within the container.
     * <p>Defaults to {@code localhost}. If the value returned by {@code getHostName()}
     * is different from the default, this method will return the same value same as {@code getHostName()}.
     *
     * @return the hostname for accessing the server under test from the host
     */
    @Override
    String getHostNameFromHost() {
        return hostNameChanged ? hostName : DEFAULT_HOSTNAME_FROM_HOST
    }

    int getPort() {
        try {
            return (int) getProperty('serverPort')
        } catch (Exception ignore) {
            throw new IllegalStateException('Test class must be annotated with @Integration for serverPort to be injected')
        }
    }

    @CompileDynamic
    private static String getHostIp() {
        PortForwardingContainer.INSTANCE.network.get().ipAddress
    }

    private boolean isHostNameChanged() {
        return hostNameFromContainer != DEFAULT_HOSTNAME_FROM_CONTAINER
    }

    private boolean isNotInitialized() {
        webDriverContainer == null
    }
}