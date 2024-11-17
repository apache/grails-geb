package grails.plugin.geb

import geb.spock.SpockGebTestManagerBuilder
import geb.test.GebTestManager

class GebTestManagerProvider {

    private static volatile GebTestManager INSTANCE
    private static volatile GebTestManager REPORTING_INSTANCE

    private GebTestManagerProvider() {}

    static GebTestManager getInstance() {
        if (INSTANCE == null) {
            synchronized (GebTestManagerProvider) {
                if (INSTANCE == null) {
                    INSTANCE = new SpockGebTestManagerBuilder().build()
                }
            }
        }
        return INSTANCE
    }

    static GebTestManager getReportingInstance() {
        if (REPORTING_INSTANCE == null) {
            synchronized (GebTestManagerProvider) {
                if (REPORTING_INSTANCE == null) {
                    REPORTING_INSTANCE = new SpockGebTestManagerBuilder()
                            .withReportingEnabled(true)
                            .build()
                }
            }
        }
        return REPORTING_INSTANCE
    }
}