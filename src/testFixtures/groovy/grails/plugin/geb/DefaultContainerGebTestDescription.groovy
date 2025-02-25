/*
 * Copyright 2024-2025 original author or authors
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
import org.spockframework.runtime.model.IterationInfo

/**
 * Implements {@link org.testcontainers.lifecycle.TestDescription} to customize recording names.
 *
 * @author James Daugherty
 * @since 4.1
 */
@CompileStatic
class DefaultContainerGebTestDescription implements ContainerGebTestDescription {

    IterationInfo iterationInfo

    @Override
    String getTestId() {
        return [
                iterationInfo.feature.spec.displayName,
                iterationInfo.feature.displayName,
                iterationInfo.displayName != iterationInfo.feature.displayName ? iterationInfo.displayName : null,
                iterationInfo.displayName != iterationInfo.feature.displayName ? iterationInfo.iterationIndex : null
        ].findAll(/* Remove nulls */).join(' ')
    }

    @Override
    String getFilesystemFriendlyName() {
        String safeName = getTestId().replaceAll('\\W+', '_')
        return safeName
    }
}

/**
 * Used by {@link ContainerGebConfiguration#testDescription()} interface to represent a null value.
 */
class NullContainerGebTestDescription extends DefaultContainerGebTestDescription {

}