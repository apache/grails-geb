package org.demo.spock

import grails.plugin.geb.ContainerGebConfiguration
import grails.plugin.geb.IContainerGebConfiguration
import grails.plugin.geb.ContainerGebSpec
import grails.testing.mixin.integration.Integration

/**
 * Adaptation of {@link ServerNameControllerSpec}
 */
@Integration
class SuperSpec extends ContainerGebSpec implements IContainerGebConfiguration {
    @Override
    String hostName() {
        return 'super.example.com'
    }
}

@Integration
@ContainerGebConfiguration(hostName = 'not.example.com')
class NotSuperSpec extends ContainerGebSpec {}

@Integration
class InheritedConfigSpec extends SuperSpec {
    void 'should show the right server name when visiting /serverName'() {
        when: 'visiting the server name controller'
        go '/serverName'

        then: 'the emitted hostname is correct'
        $('p').text() == 'Server name: super.example.com'
    }
}

@Integration
class NotInheritedConfigSpec extends NotSuperSpec {
    void 'should show the right server name when visiting /serverName'() {
        when: 'visiting the server name controller'
        go '/serverName'

        then: 'the emitted hostname is correct'
        $('p').text() != 'Server name: not.example.com'
    }
}

@Integration
class ChildPreferenceInheritedConfigSpec extends SuperSpec {
    @Override
    String hostName() {
        return 'child.example.com'
    }
    
    void 'should show the right server name when visiting /serverName'() {
        when: 'visiting the server name controller'
        go '/serverName'

        then: 'the emitted hostname is correct'
        $('p').text() == 'Server name: child.example.com'

        when:
        report('whatever')

        then:
        // geb.test.GebTestManager: "Reporting has not been enabled on this GebTestManager yet report() was called"
        Throwable t = thrown(Exception)
        t.message.contains("not been enabled")
    }
}

// No sane person would do this, but lets test anyway
@Integration
class SuperSuperInheritedConfigSpec extends SuperSpec {
    @Override
    boolean reporting() {
        return true
    }
}

@Integration
class MultipleInheritanceSpec extends SuperSuperInheritedConfigSpec {
    void 'should show the right server name when visiting /serverName'() {
        when: 'visiting the server name controller'
        go '/serverName'

        then: 'the emitted hostname is correct'
        $('p').text() == 'Server name: super.example.com'
        report('multi inheritance report')
    }
}
