package org.demo.spock

import grails.plugin.geb.ContainerGebConfiguration
import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration

@Integration
@ContainerGebConfiguration(testDescription = ContainerGebTestDescriptionImpl.class)
class ContainerGebTestDescriptionAnnotationSpec extends ContainerGebSpec {

    void 'should not raise a FileSystemException on recording save by loading custom implementation of TODO'(String data_driven_testing_param_1) {
        when:
        go '/'

        then:
        true

        where:
        data_driven_testing_param_1 << ["very long text lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet Liskov Substitution"]
    }
}