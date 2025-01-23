package grails.plugin.geb.support

import geb.report.CompositeReporter
import geb.report.PageSourceReporter
import geb.report.Reporter
import grails.plugin.geb.ContainerGebSpec
import groovy.transform.CompileStatic
import groovy.transform.SelfType

@CompileStatic
@SelfType(ContainerGebSpec)
trait ReportingSupport {

    void report(String message) {
        testManager.report(message)
    }

    /**
     * The reporter that Geb should use when reporting is enabled.
     */
    Reporter createReporter() {
        new CompositeReporter(new PageSourceReporter())
    }
}