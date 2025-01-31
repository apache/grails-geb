package org.demo.spock

import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration
import org.demo.spock.pages.UploadPage
import spock.lang.IgnoreIf
import spock.lang.Requires

@Integration
class UploadSpec extends ContainerGebSpec {

    @Requires({ os.windows })
    void 'should be able to upload files on a Windows host'() {
        given:
        def uploadPage = to UploadPage

        when:
        uploadPage.fileInput.file = createFileInputSource(
                'src/integration-test/resources/assets/upload-test.txt',
                '/tmp/upload-test.txt'
        )

        and:
        uploadPage.submitBtn.click()

        then:
        title == 'File Uploaded'
        pageSource.contains('File uploaded successfully')
    }

    @IgnoreIf({ os.windows })
    void 'should be able to upload files on a non-Windows host'() {
        given:
        def uploadPage = to UploadPage

        when:
        uploadPage.fileInput.file = createFileInputSource(
                'src/integration-test/resources/assets/upload-test.txt',
                '/tmp/upload-test.txt'
        )

        and:
        uploadPage.submitBtn.click()

        then:
        title == 'File Uploaded'
        pageSource.contains('File uploaded successfully')
    }
}