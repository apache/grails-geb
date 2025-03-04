package org.demo.spock

import grails.plugin.geb.ContainerFileDetector
import grails.plugin.geb.ContainerGebConfiguration
import grails.plugin.geb.ContainerGebSpec
import grails.plugin.geb.UselessContainerFileDetector
import grails.plugin.geb.serviceloader.ServiceRegistry
import grails.testing.mixin.integration.Integration
import org.demo.spock.pages.UploadPage
import org.openqa.selenium.WebDriverException
import spock.lang.IgnoreIf
import spock.lang.Requires

/**
 * Altered copy of {@link ContainerFileDetectorAnnotationSpec}
 */
@Integration
class ContainerFileDetectorSpockSpec extends ContainerGebSpec {

    def setupSpec(){
        ServiceRegistry.setInstance(ContainerFileDetector, new UselessContainerFileDetector())
    }

    def cleanupSpec(){
        ServiceRegistry.setInstance(ContainerFileDetector, null)
    }
    
    @Requires({ os.windows })
    void 'should be able to find and upload files on a Windows host'() {
        given:
        def uploadPage = to UploadPage

        when:
        uploadPage.fileInput.file = new File('src/integration-test/resources/assets/upload-test.txt')

        and:
        uploadPage.submitBtn.click()

        then:
        def e = thrown(WebDriverException)
        e.message.contains('File not found')
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
        def e = thrown(WebDriverException)
        e.message.contains('File not found')
    }
}