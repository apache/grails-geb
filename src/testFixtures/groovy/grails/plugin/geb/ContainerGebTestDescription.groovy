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
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.IterationInfo
import org.springframework.lang.Nullable
import org.testcontainers.lifecycle.TestDescription

/**
 * java.util.ServiceLoader Compatible Interface ("must have a zero-argument constructor")
 *
 * @see GebRecordingTestListener
 */
@CompileStatic
interface ContainerGebTestDescription extends TestDescription {

    IterationInfo getIterationInfo()
    /**
     * Will be set to value of
     * {@link org.spockframework.runtime.IRunListener#afterIteration(org.spockframework.runtime.model.IterationInfo)}
     */
    void setIterationInfo(IterationInfo iterationInfo)

    /**
     * Will be used by
     * {@link org.testcontainers.containers.BrowserWebDriverContainer#afterTest(org.testcontainers.lifecycle.TestDescription, java.util.Optional)}
     *
     * You could overwrite {@link ErrorInfo#getException()} to mimic that an Exception occurred, resulting in a recording file
     * if {@link org.testcontainers.containers.BrowserWebDriverContainer#recordingMode} is RECORD_FAILING.
     */
    @Nullable
    ErrorInfo getErrorInfo()
    /**
     * Will be set to value of
     * {@link org.spockframework.runtime.IRunListener#error(org.spockframework.runtime.model.ErrorInfo)}
     */
    void setErrorInfo(ErrorInfo errorInfo)
}
