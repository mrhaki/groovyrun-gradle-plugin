package com.mrhaki.gradle.groovyrun

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

class GroovyRunPlugin implements Plugin<Project> {
    
    public static final String CONFIGURATION_NAME = 'groovyRun'
    
    private static final String TASK_NAME = 'runGroovyScript'
    
    private static final String EXTENSION_NAME = 'groovyRun'
    
    private Project project
    
    void apply(final Project project) {
        this.project = project
        
        createExtension()
        configureConfiguration()
        configureTask()
    }
    
    private void createExtension() {
        project.extensions.create EXTENSION_NAME, GroovyRunExtension
    }
    
    private void configureConfiguration() {
        final Configuration configuration = 
                project.configurations.create(CONFIGURATION_NAME) {
                    description = 'Classpath for running Groovy scripts/code'
                    visible = false
                    transitive = true
                }
        
        configuration.incoming.beforeResolve {
            final GroovyRunExtension extension = project.extensions.findByType(GroovyRunExtension)
            if (extension.groovyVersion) {
                project.dependencies.add(
                        CONFIGURATION_NAME,
                        project.dependencies.create(
                                "org.codehaus.groovy:groovy-all:$extension.groovyVersion"))
            } else {
                project.dependencies.add(
                        CONFIGURATION_NAME, 
                        project.dependencies.localGroovy())
            }
        }
    }
    
    private void configureTask() {
        project.tasks.create TASK_NAME, GroovyRun
    }
    
}
