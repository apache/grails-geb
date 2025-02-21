package org.demo.spock

import grails.plugin.geb.DefaultContainerGebTestDescription

class ContainerGebTestDescriptionImpl extends DefaultContainerGebTestDescription {
    @Override
    String getTestId() {
        return testInfo.feature.spec.displayName
    }
}
