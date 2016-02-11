package com.mrhaki.gradle.groovyrun

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

class GroovyRunStructureSpec extends Specification {
    
    private final Project project = ProjectBuilder.builder().build()
    
    @Subject
    private GroovyRun task

    def setup() {
        project.pluginManager.apply 'com.mrhaki.groovyrun'
        task = project.tasks.runGroovyScript
    }

    def "task must have the main property set to Groovy command line class"() {
        expect:
        task.main == 'groovy.ui.GroovyMain'
    }
    
    def "task must have classpath property set to 'groovyRun' configuration"() {
        expect:
        task.classpath.name == 'groovyRun'
    }
    
    def "task must set listener command line option when listenerPort method is used"() {
        given:
        task.listenerPort(8000)
        
        expect:
        task.args == ['-l', '8000']
    }

    def "task must set listener command line option when listenerPort property is used"() {
        given:
        task.listenerPort = 8100

        expect:
        task.args == ['-l', '8100']
    }
}
