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
    
    def "task must set listener port arguments before evaluate arguments if evaluate is set first"() {
        given:
        task.evaluate "println 'Hello Gradle'"
        task.listenerPort 9000
        
        expect:
        task.args == ['-l', '9000', '-e', "println 'Hello Gradle'"]
    }

    def "task must set listener port arguments before evaluate arguments if listener port is set first"() {
        given:
        task.listenerPort 9000
        task.evaluate "println 'Hello Gradle'"

        expect:
        task.args == ['-l', '9000', '-e', "println 'Hello Gradle'"]
    }
    
    def "task must set evaluate argument if evaluate is set as property"() {
        given:
        task.evaluate = "println 'test'"

        expect:
        task.args == ['-e', "println 'test'"]

    }

    def "task must set evaluate argument if evaluate is set as method"() {
        given:
        task.evaluate "println 'test'"

        expect:
        task.args == ['-e', "println 'test'"]
    }
    
    def "task must set -n argument if byLine property is set"() {
        given:
        task.byLine = true
        
        expect:
        task.args == ['-n']
    }

    def "task must set -p argument if byLine method is used"() {
        given:
        task.byLine true

        expect:
        task.args == ['-n']
    }

    def "task must set -p argument if byLineAndPrint property is set"() {
        given:
        task.byLineAndPrint = true

        expect:
        task.args == ['-p']
    }

    def "task must not set -p argument if byLineAndPrint property is false"() {
        given:
        task.byLineAndPrint = false

        expect:
        task.args == []
    }

    def "task must set -n argument if byLineAndPrint method is used"() {
        given:
        task.byLineAndPrint true

        expect:
        task.args == ['-p']
    }

    def "task must set -d argument if showStackTrace property is set"() {
        given:
        task.showStackTrace = true

        expect:
        task.args == ['-d']
    }

    def "task must not set -d argument if showStackTrace property is false"() {
        given:
        task.showStackTrace = false

        expect:
        task.args == []
    }

    def "task must set -d argument if showStackTrace method is used"() {
        given:
        task.showStackTrace true

        expect:
        task.args == ['-d']
    }

    def "task must set -i argument with extension if backupExtension property is set"() {
        given:
        task.backupExtension = '.bak'

        expect:
        task.args == ['-i', '.bak']
    }

    def "task must set -i argument with extension if backupExtension method is used"() {
        given:
        task.backupExtension '.bak'

        expect:
        task.args == ['-i', '.bak']
    }

}
