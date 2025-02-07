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

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.runner.Description
import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.IterationInfo
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.DefaultRecordingFileFactory
import org.testcontainers.containers.RecordingFileFactory
import org.testcontainers.containers.VncRecordingContainer

/**
 * A test listener that reports the test result to {@link org.testcontainers.containers.BrowserWebDriverContainer} so
 * that recordings may be saved.
 *
 * @see org.testcontainers.containers.BrowserWebDriverContainer#afterTest
 *
 * @author James Daugherty
 * @since 4.1
 */
@Slf4j
@CompileStatic
class GebRecordingTestListener extends AbstractRunListener {

    ErrorInfo errorInfo
    VncRecordingContainer recordingContainer
    WebDriverContainerHolder containerHolder
    RecordingFileFactory recordingFileFactory = new DefaultRecordingFileFactory()

    GebRecordingTestListener(WebDriverContainerHolder containerHolder) {
        this.containerHolder = containerHolder
    }

    @Override
    void beforeIteration(IterationInfo iteration) {
        recordingContainer = new VncRecordingContainer(containerHolder.currentContainer)
                .withVncPassword("secret")
                .withVncPort(5900)
                .withVideoFormat(containerHolder.grailsGebSettings.recordingFormat)

        recordingContainer.start()
    }

    @Override
    void afterIteration(IterationInfo iteration) {
        ContainerGebTestDescription description = new ContainerGebTestDescription(iteration)

        retainRecordingIfNeeded(description.getFilesystemFriendlyName(), errorInfo?.exception == null)
        errorInfo = null

        recordingContainer.stop()
        recordingContainer = null
    }

    /**
     * This method is copied from BrowserWebDriverContainer until upstream allows triggering a recording
     */
    private void retainRecordingIfNeeded(String prefix, boolean succeeded) {
        final boolean shouldRecord
        switch (containerHolder.grailsGebSettings.recordingMode) {
            case BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL:
                shouldRecord = true
                break
            case BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING:
                shouldRecord = !succeeded
                break
            default:
                shouldRecord = false
                break
        }

        if (shouldRecord) {
            File recordingFile = recordingFileFactory.recordingFileForTest(
                    containerHolder.grailsGebSettings.recordingDirectory,
                    prefix,
                    succeeded,
                    recordingContainer.getVideoFormat()
            )
            log.info("Screen recordings for test {} will be stored at: {}", prefix, recordingFile)
            //TODO: There's some type of race condition here, since the file will always exist if you use the debugger, but when running the test through it errors with a message like:
            /*
            Status 404: {"message":"Could not find the file /newScreen.mp4 in container 044005e429dd268d8149b5c8cda5b1c29d815f6b84705545bc62962bbde9851f"}
             */
            recordingContainer.saveRecordingToFile(recordingFile)
        }
    }

    @Override
    void error(ErrorInfo error) {
        errorInfo = error
    }
}