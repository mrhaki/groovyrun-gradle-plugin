package com.mrhaki.gradle.groovyrun

import groovy.transform.PackageScope
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * Gradle plugin to run Groovy code like with
 * Groovy's command line.
 */
class GroovyRunPlugin implements Plugin<Project> {

    /**
     * Name for dependency configuration.
     */
    @PackageScope static final String CONFIGURATION_NAME = 'groovyRun'

    /**
     * Task name for running Groovy code.
     */
    private static final String TASK_NAME = 'runGroovyScript'

    /**
     * Task name for running simple HTTP server.
     */
    private static final String TASK_HTTP_SERVER_NAME = 'runHttpServer'

    /**
     * Name for registering extension in the project.
     */
    private static final String EXTENSION_NAME = 'groovyRun'
    
    private Project project
    
    void apply(final Project project) {
        this.project = project
        
        createExtension()
        configureConfiguration()
        configureTask()
    }

    /**
     * Create extension with name {@link #EXTENSION_NAME} and 
     * add to the project.
     */
    private void createExtension() {
        project.extensions.create EXTENSION_NAME, GroovyRunExtension
    }

    /**
     * Create dependency configuration. If the <code>groovyVersion</code>
     * property of the extension is set then a dependency on
     * <code>org.codehaus.groovy:groovy-all</code> with the given version
     * is set. If the <code>groovyVersion</code> is not set then 
     * the Groovy version that comes with Gradle is used with the dependency
     * <code>localGroovy()</code>.
     */
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

    /**
     * Add tasks to run Groovy code and a simple HTTP server.
     */
    private void configureTask() {
        project.tasks.create TASK_NAME, GroovyRun
        project.tasks.create TASK_HTTP_SERVER_NAME, SimpleHttpServer
    }
    
}
