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

import geb.Browser
import geb.download.DownloadException
import geb.download.DownloadSupport
import groovy.transform.CompileStatic
import org.openqa.selenium.Cookie

/**
 * A {@link geb.download.DownloadSupport} implementation that allows for using the {@code download*()} methods.
 *
 * @author Mattias Reichel
 * @since 5.0.0
 */
@CompileStatic
trait ContainerAwareDownloader implements DownloadSupport {

    // HTTP 1.1 states that this charset is the default if none was specified
    static final private DEFAULT_CHARSET = 'ISO-8859-1'

    abstract Browser getBrowser()
    abstract String getHostNameFromHost()
    abstract String getProtocol()
    abstract int getPort()

    /**
     * Creates a http url connection to a url, that has the same cookies as the browser.
     * <p>
     * Valid options are:
     *
     * <ul>
     * <li>{@code uri} - <em>optional</em> - the uri to resolve relative to the base option (current browser page used if {@code null})
     * <li>{@code base} - <em>optional</em> - what to resolve the uri against (current browser page used if {@code null})
     * </ul>
     */
    HttpURLConnection download(Map options = [:]) {
        URL url = resolveUrl(options)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection()
        applyCookies(connection, browser)
        browser.config.downloadConfig?.call(connection)
        connection
    }

    HttpURLConnection download(String uri) {
        download(uri: uri)
    }

    /**
     * Opens a url connection via {@link #download(Map)} and returns the response input stream.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     */
    InputStream downloadStream(Map options = [:], Closure connectionConfig = null) {
        (InputStream) wrapInDownloadException(downloadWithConfig(options, connectionConfig)) {
            ((HttpURLConnection) it).inputStream
        }
    }

    /**
     * Opens a url connection via {@link #download(String)} and returns the response input stream.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     */
    InputStream downloadStream(String uri, Closure connectionConfig = null) {
        downloadStream(uri: uri, connectionConfig)
    }

    InputStream downloadStream(Closure connectionConfig) {
        downloadStream([:], connectionConfig)
    }

    /**
     * Opens a url connection via {@link #download(Map)} and returns the response text, if the content type was textual.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     */
    String downloadText(Map options = [:], Closure connectionConfig = null) {
        HttpURLConnection connection = downloadWithConfig(options, connectionConfig)
        connection.connect()
        String contentType = connection.contentType

        if (isTextContentType(contentType)) {
            String charset = determineCharset(contentType)
            wrapInDownloadException(connection) { ((HttpURLConnection) it).inputStream.getText(charset) }
        } else {
            throw new DownloadException(connection, "cannot extract text from connection as content type is non text (is: $contentType)")
        }
    }

    /**
     * Opens a url connection via {@link #download(String)} and returns the response text, if the content type was textual.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     */
    String downloadText(String uri, Closure connectionConfig = null) {
        downloadText(uri: uri, connectionConfig)
    }

    String downloadText(Closure connectionConfig) {
        downloadText([:], connectionConfig)
    }

    /**
     * Opens a url connection via {@link #download(Map)} and returns the raw bytes.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     */
    byte[] downloadBytes(Map options = [:], Closure connectionConfig = null) {
        downloadStream(options, connectionConfig).bytes
    }

    byte[] downloadBytes(Closure connectionConfig) {
        downloadStream(connectionConfig).bytes
    }

    /**
     * Opens a url connection via {@link #download(String)} and returns the raw bytes.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     */
    byte[] downloadBytes(String uri, Closure connectionConfig = null) {
        downloadBytes(uri: uri, connectionConfig)
    }

    /**
     * Opens a url connection via {@link #download(Map)} and returns the content object.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     *
     * @see URLConnection#getContent()
     */
    Object downloadContent(Map options = [:], Closure connectionConfig = null) {
        wrapInDownloadException(downloadWithConfig(options, connectionConfig)) { ((HttpURLConnection) it).content }
    }

    /**
     * Opens a url connection via {@link #download(String)} and returns the content object.
     * <p>
     * If connectionConfig is given, it is called with the {@link HttpURLConnection} before the request is made.
     *
     * @see URLConnection#getContent()
     */
    Object downloadContent(String uri, Closure connectionConfig = null) {
        downloadContent(uri: uri, connectionConfig)
    }

    Object downloadContent(Closure connectionConfig) {
        downloadContent([:], connectionConfig)
    }

    private HttpURLConnection downloadWithConfig(Map options, Closure config) {
        def connection = download(options)
        config?.call(connection)
        connection
    }

    /**
     * Returns a URL for what is to be downloaded.
     * <p>
     * If uri is non {@code null}, it is resolved against the browser's current page url. If it is {@code null},
     * the browser's current page url will be returned.
     */
    private URL resolveUrl(Map options) {
        String uri = options.uri
        String base = options.base ?: browser.driver.currentUrl.replaceAll(/(https?:\/\/)([^\/:]+)(:\d+\/.*)/) { match, protocol, host, rest ->
            "${protocol}${hostNameFromHost}${rest}"
        }
        uri ? new URI(base).resolve(uri).toURL() : new URL(base)
    }

    /**
     * Copies the browser's current cookies to the given connection via the "Cookie" header.
     */
    private applyCookies(HttpURLConnection connection, Browser browser) {
        applyCookies(connection, browser.driver.manage().cookies)
    }

    /**
     * Copies the given cookies to the given connection via the "Cookie" header.
     */
    private applyCookies(HttpURLConnection connection, Collection<Cookie> cookies) {
        def cookieHeader = cookies.collect { "${it.name}=${it.value}" }.join('; ')
        connection.setRequestProperty('Cookie', cookieHeader)
    }

    private boolean isTextContentType(String contentType) {
        contentType?.startsWith('text/')
    }

    private determineCharset(String contentType) {
        if (contentType) {
            def parts = contentType.split(';')*.trim()
            def charsetPart = parts.find { it.startsWith('charset=') }
            if (charsetPart) {
                charsetPart.split('=', 2)[1]
            } else {
                DEFAULT_CHARSET
            }
        } else {
            DEFAULT_CHARSET
        }
    }

    private wrapInDownloadException(HttpURLConnection connection, Closure operation) {
        try {
            operation(connection)
        } catch (Throwable e) {
            throw new DownloadException(connection, 'An error occurred during the download operation', e)
        }
    }
}