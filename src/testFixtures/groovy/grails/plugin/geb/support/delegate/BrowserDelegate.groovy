/*
 * Copyright 2025 original author or authors
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
package grails.plugin.geb.support.delegate

import geb.Browser
import geb.Page
import geb.js.JavascriptInterface
import geb.url.UrlFragment
import geb.webstorage.WebStorage
import grails.plugin.geb.ContainerGebSpec
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.SelfType
import org.openqa.selenium.WebDriver

import java.time.Duration

import static groovy.lang.Closure.DELEGATE_FIRST

/**
 * Handles delegation to the browser instance so that the Geb API can be used directly in the test.
 * <p>
 * As method parameter names are not available in the Geb artifacts we are delegating manually,
 * instead of using @Delegate AST transform, to get the best possible end user IDE support and user experience.
 *
 * @author Mattias Reichel
 * @since 4.2
 */
@CompileStatic
@SelfType(ContainerGebSpec)
trait BrowserDelegate {

    Browser getBrowser() {
        testManager.browser
    }

    WebDriver getDriver() {
        browser.driver
    }

    <T extends Page> T at(Class<T> pageType) {
        browser.at(pageType)
    }

    <T extends Page, R> R at(Class<T> pageType, @DelegatesTo(strategy = DELEGATE_FIRST, genericTypeIndex = 0) Closure<R> assertions) {
        browser.at(at(pageType), assertions)
    }

    <T extends Page> T at(T page) {
        browser.at(page)
    }

    boolean isAt(Class<? extends Page> pageType, boolean honourGlobalAtCheckWaiting = true) {
        browser.isAt(pageType, honourGlobalAtCheckWaiting)
    }

    boolean isAt(Page page, boolean honourGlobalAtCheckWaiting = true) {
        browser.isAt(page, honourGlobalAtCheckWaiting)
    }

    void checkIfAtAnUnexpectedPage(Class<? extends Page>[] expectedPages) {
        browser.checkIfAtAnUnexpectedPage(expectedPages)
    }

    void checkIfAtAnUnexpectedPage(Page[] expectedPages) {
        browser.checkIfAtAnUnexpectedPage(expectedPages)
    }

    void go(String url) {
        browser.go(url)
    }

    void go(String url, UrlFragment fragment) {
        browser.go(url, fragment)
    }

    void go(Map params = [:], UrlFragment fragment) {
        browser.go(params, fragment)
    }

    void go(Map params = [:], String url = null, UrlFragment fragment = null) {
        browser.go(params, url, fragment)
    }

    <T extends Page> T to(Map params = [:], Class<T> pageType, Object[] args) {
        browser.to(params, pageType, args)
    }

    <T extends Page> T to(Map params = [:], Class<T> pageType, UrlFragment fragment, Object[] args) {
        browser.to(params, pageType, fragment, args)
    }

    <T extends Page> T to(Map params = [:], T page, Object[] args) {
        browser.to(params, page, args)
    }

    <T extends Page> T to(Map params = [:], T page, UrlFragment fragment, Object[] args) {
        browser.to(params, page, fragment, args)
    }

    <T extends Page> T via(Map params = [:], Class<T> pageType, Object[] args) {
        browser.via(params, pageType, args)
    }

    <T extends Page> T via(Map params = [:], Class<T> pageType, UrlFragment fragment, Object[] args) {
        browser.via(params, pageType, fragment, args)
    }

    <T extends Page> T via(Map params = [:], T page, Object[] args) {
        browser.via(params, page, args)
    }

    <T extends Page> T via(Map params = [:], T page, UrlFragment fragment, Object[] args) {
        browser.via(params, page, fragment, args)
    }

    void clearCookies(String... additionalUrls) {
        browser.clearCookies(additionalUrls)
    }

    void clearCookies() {
        browser.clearCookies()
    }

    void clearCookiesQuietly() {
        browser.clearCookiesQuietly()
    }

    void clearWebStorage() {
        browser.clearWebStorage()
    }

    void clearWebStorageQuietly() {
        browser.clearWebStorageQuietly()
    }

    void quit() {
        browser.quit()
    }

    void close() {
        browser.close()
    }

    Set<String> getAvailableWindows() {
        browser.getAvailableWindows()
    }

    <T> T withWindow(String window, @DelegatesTo(value = Browser, strategy = DELEGATE_FIRST) Closure<T> block) {
        browser.withWindow(window, block)
    }

    <T> List<T> withWindow(Closure specification, @DelegatesTo(value = Browser, strategy = DELEGATE_FIRST) Closure<T> block) {
        browser.withWindow(specification, block)
    }

    <T> List<T> withWindow(Map options, Closure specification, @DelegatesTo(value = Browser, strategy = DELEGATE_FIRST) Closure<T> block) {
        browser.withWindow(options, specification, block)
    }

    <T> T withWindow(Map options, String window, @DelegatesTo(value = Browser, strategy = DELEGATE_FIRST) Closure<T> block) {
        browser.withWindow(options, window, block)
    }

    <T> T withNewWindow(Map options, Closure windowOpeningBlock, @DelegatesTo(value = Browser, strategy = DELEGATE_FIRST) Closure<T> block) {
        browser.withNewWindow(options, windowOpeningBlock, block)
    }

    <T> T withNewWindow(Closure windowOpeningBlock, @DelegatesTo(value = Browser, strategy = DELEGATE_FIRST) Closure<T> block) {
        browser.withNewWindow(windowOpeningBlock, block)
    }

    <T extends Page> T createPage(Class<T> pageType) {
        browser.createPage(pageType)
    }

    JavascriptInterface getJs() {
        browser.js
    }

    File getReportGroupDir() {
        browser.reportGroupDir
    }

    void reportGroup(String path) {
        browser.reportGroup(path)
    }

    void reportGroup(Class clazz) {
        browser.reportGroup(clazz)
    }

    void cleanReportGroupDir() {
        browser.cleanReportGroupDir()
    }

    void pause() {
        browser.pause()
    }

    WebStorage getLocalStorage() {
        browser.localStorage
    }

    WebStorage getSessionStorage() {
        browser.sessionStorage
    }

    void verifyAtImplicitly(Class<? extends Page> targetPage) {
        browser.verifyAtImplicitly(targetPage)
    }

    void verifyAtImplicitly(Page targetPage) {
        browser.verifyAtImplicitly(targetPage)
    }

    void setNetworkLatency(Duration duration) {
        browser.setNetworkLatency(duration)
    }

    void resetNetworkLatency() {
        browser.resetNetworkLatency()
    }

    <T> Optional<T> driverAs(Class<T> castType) {
        browser.driverAs(castType)
    }

    @CompileDynamic
    def methodMissing(String name, args) {
        browser.page."$name"(*args)
    }

    @CompileDynamic
    def propertyMissing(String name) {
        browser.page."$name"
    }

    @CompileDynamic
    def propertyMissing(String name, value) {
        browser.page."$name" = value
    }
}