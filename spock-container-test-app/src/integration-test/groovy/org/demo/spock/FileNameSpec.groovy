package org.demo.spock

import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration

@Integration
class FileNameSpec extends ContainerGebSpec {

    /**
     * "The filename, directory name, or volume label syntax is incorrect" (too long)
     */
    void 'should not raise a FileSystemException on recording save by loading custom implementation of TODO'(String data_driven_testing_param_1, String param_2, int param_3) {
        when:
        go '/'

        then:
        true

        where:
        data_driven_testing_param_1 << ["very long text lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet Liskov Substitution"]
        param_2 << ["If an implementation is hard to explain, it is an established design pattern."]
        param_3 << [Integer.MAX_VALUE]
    }
}