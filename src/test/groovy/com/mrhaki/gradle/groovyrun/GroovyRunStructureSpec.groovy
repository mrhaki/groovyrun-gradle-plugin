package com.mrhaki.gradle.groovyrun

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class GroovyRunStructureSpec extends Specification {

    private final Project project = ProjectBuilder.builder().build()

    def setup() {
        project.pluginManager.apply 'com.mrhaki.groovyrun'
    }

    def "groovyRun task must have the main property set to Groovy command line class"() {
        expect:
        project.tasks.runGroovyScript.main == 'groovy.ui.GroovyMain'
    }
    
    def "groovyRun task must have classpath property set to 'groovyRun' configuration"() {
        expect:
        project.tasks.runGroovyScript.classpath.name == 'groovyRun'
    }
}
