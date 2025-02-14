package org.demo.spock

import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration
import org.demo.spock.pages.UploadPage

@Integration
class PageDelegateSpec extends ContainerGebSpec {

    void 'should delegate to page object'() {
        given:
        to UploadPage

        when:
        nop()

        then:
        title == 'Upload Test'
    }

}
