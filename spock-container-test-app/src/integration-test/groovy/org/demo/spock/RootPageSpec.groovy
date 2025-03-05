package org.demo.spock

import geb.report.CompositeReporter
import geb.report.PageSourceReporter
import geb.report.Reporter
import grails.plugin.geb.ContainerGebConfiguration
import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration

/**
 * See https://docs.grails.org/latest/guide/testing.html#functionalTesting and https://www.gebish.org/manual/current/
 * for more instructions on how to write functional tests with Grails and Geb.
 */
@Integration
@ContainerGebConfiguration(reporting = true)
class RootPageSpec extends ContainerGebSpec {

    @Override
    Reporter createReporter() {
        // Override the default reporter to demonstrate how this can be customized
        new CompositeReporter(new PageSourceReporter())
    }

    void 'should display the correct title on the home page'() {
        when: 'visiting the home page'
        go '/'

        then: 'the page title is correct'
        report('root page report')
        title == 'Welcome to Grails'
    }
}
