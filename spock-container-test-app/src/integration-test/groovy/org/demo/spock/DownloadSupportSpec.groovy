package org.demo.spock

import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DownloadSupportSpec extends ContainerGebSpec {

    void 'should be able to use download methods'() {
        when:
        go '/'

        then:
        downloadText().contains('Welcome to Grails')
    }

    void 'should display the correct title on the home page'() {
        when: 'visiting the home page'
        go '/'

        then: 'the page title is correct'
        title == 'Welcome to Grails'
    }
}