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

import geb.spock.SpockGebTestManagerBuilder
import geb.test.GebTestManager
import groovy.transform.CompileStatic

/**
 * A provider class for managing instances of {@link GebTestManager}.
 * This class uses the Initialization-on-Demand Holder Idiom to ensure thread-safe,
 * lazy initialization of {@link GebTestManager} instances for use in Geb tests.
 *
 * <p>The class provides two static instances:</p>
 * <ul>
 *   <li>{@code instance}: A standard {@link GebTestManager} instance.</li>
 *   <li>{@code reportingInstance}: A {@link GebTestManager} instance with reporting enabled.</li>
 * </ul>
 *
 * <p>This class cannot be instantiated, as it is designed to be used as a
 * utility provider.</p>
 *
 * @see GebTestManager
 * @see SpockGebTestManagerBuilder
 *
 * @author Mattias Reichel
 * @author Søren Berg Glasius
 * @since 5.0
 */
@CompileStatic
class GebTestManagerProvider {

    /**
     * A lazily and thread-safe-initialized instance of {@link GebTestManager}.
     * Built using {@link SpockGebTestManagerBuilder}.
     */
    @Lazy
    static volatile GebTestManager instance = {
        new SpockGebTestManagerBuilder().build()
    }()

    /**
     * A lazily and thread-safe-initialized instance of {@link GebTestManager}
     * with reporting enabled. Built using {@link SpockGebTestManagerBuilder}.
     */
    @Lazy
    static volatile GebTestManager reportingInstance = {
        new SpockGebTestManagerBuilder()
                .withReportingEnabled(true)
                .build()
    }()

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GebTestManagerProvider() {}

}