package org.demo.spock

import grails.plugin.geb.DefaultContainerGebTestDescription
import groovy.transform.CompileStatic

@CompileStatic
class ContainerGebTestDescriptionImpl extends DefaultContainerGebTestDescription {
    @Override
    String getTestId() {
        return "Custom_${getIterationInfo().feature.spec.displayName}"
    }
}
