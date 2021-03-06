package com.mrhaki.gradle.groovyrun

import groovy.transform.CompileStatic
import org.gradle.api.tasks.JavaExec

/**
 * Gradle task to run Groovy code using
 * Groovy's command line interpreter.
 */
@CompileStatic
class GroovyRun extends JavaExec {

    /**
     * Port Groovy should listen on for scripts.
     */
    protected Integer listenerPort

    /**
     * Class name for Groovy's command line.
     */
    private static final String MAIN_CLASS = 'groovy.ui.GroovyMain'
    
    GroovyRun() {
        super()
        main = MAIN_CLASS
        classpath = project.configurations[GroovyRunPlugin.CONFIGURATION_NAME]
    }

    void evaluate(final String script) {
        setEvaluate(script)
    }
    
    void setEvaluate(final String script) {
        args("-e").args(script)
    }
    
    void listenerPort(final Integer port) {
        setListenerPort(port)
    }
    
    void setListenerPort(final Integer port) {
        this.listenerPort = port
        
        // Listener argument must be first.
        final List<String> oldArgs = args
        args = []
        args("-l").args(port.toString())
        oldArgs?.each { arg ->
            args(arg)
        }
    }
    
    void file(final File scriptFile) {
        setFile(scriptFile)
    }

    void setFile(final File scriptFile) {
        args(scriptFile.absolutePath)
    }
}
