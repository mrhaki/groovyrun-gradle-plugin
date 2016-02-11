package com.mrhaki.gradle.groovyrun

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class PluginStructureSpec extends Specification {
    
    private final Project project = ProjectBuilder.builder().build()
    
    def setup() {
        project.pluginManager.apply 'com.mrhaki.groovyrun'
    }
    
    def "plugin must add 'runGroovyScript' task"() {
        expect:
        project.tasks.runGroovyScript instanceof GroovyRun
    }

    def "plugin must add 'runHttpServer' task"() {
        expect:
        project.tasks.runHttpServer instanceof SimpleHttpServer
    }
    
    def "plugin must add 'groovyRun' extension"() {
        expect:
        project.extensions.groovyRun instanceof GroovyRunExtension
    }
    
    def "plugin must add 'groovyRun' dependency configuration"() {
        expect:
        project.configurations.groovyRun
    }

}
