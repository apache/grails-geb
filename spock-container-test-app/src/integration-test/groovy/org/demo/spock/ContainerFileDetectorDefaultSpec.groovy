package org.demo.spock

import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration
import org.demo.spock.pages.UploadPage
import spock.lang.IgnoreIf
import spock.lang.Requires

/**
 * Altered copy of {@link UploadSpec} that depends on default of
 * {@link grails.plugin.geb.ContainerGebConfiguration#fileDetector} to be
 * {@link org.openqa.selenium.remote.LocalFileDetector}
 */
@Integration
class ContainerFileDetectorDefaultSpec extends ContainerGebSpec {

    @Requires({ os.windows })
    void 'should be able to find and upload files on a Windows host'() {
        given:
        def uploadPage = to UploadPage

        when:
        uploadPage.fileInput.file = new File('src/integration-test/resources/assets/upload-test.txt')

        and:
        uploadPage.submitBtn.click()

        then:
        title == 'File Uploaded'
        pageSource.contains('File uploaded successfully')
    }

    @IgnoreIf({ os.windows })
    void 'should be able to find and upload files on a non-Windows host'() {
        given:
        def uploadPage = to UploadPage

        when:
        uploadPage.fileInput.file = new File('src/integration-test/resources/assets/upload-test.txt')

        and:
        uploadPage.submitBtn.click()

        then:
        title == 'File Uploaded'
        pageSource.contains('File uploaded successfully')
    }
}