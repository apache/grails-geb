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
package grails.plugin.geb

import groovy.transform.CompileStatic

/**
 * ServiceLoader (META-INF) Loader that allows programmatic overwriting.
 *
 * @since 4.2
 */
@CompileStatic
class ContainerGebTestDescriptionServiceLoader {

    private static ContainerGebTestDescription instance

    static ContainerGebTestDescription getInstance() {
        if (instance != null) return instance
        return ServiceLoader.load(ContainerGebTestDescription).findFirst()
                .orElse(new DefaultContainerGebTestDescription())
    }

    static void setInstance(ContainerGebTestDescription instance) {
        this.instance = instance
    }
    /**
     * Class must have a zero-argument constructor (ServiceLoader Requirement).
     */
    static void setInstance(Class<? extends ContainerGebTestDescription> clazz) {
        setInstance(clazz.getDeclaredConstructor().newInstance())
    }
}

