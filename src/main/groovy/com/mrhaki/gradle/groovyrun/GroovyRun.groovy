package com.mrhaki.gradle.groovyrun

import org.gradle.api.tasks.JavaExec

class GroovyRun extends JavaExec {
    
    Integer listenerPort
    
    private static final String MAIN_CLASS = 'groovy.ui.GroovyMain'
    
    GroovyRun() {
        super()
        main = MAIN_CLASS
        classpath = project.configurations[GroovyRunPlugin.CONFIGURATION_NAME]
    }
    
    void evaluate(final String script) {
        args("-e").args(script)
    }
    
    void setEvaluate(final String script) {
        evaluate(script)
    }
    
    void listenerPort(final Integer port) {
        this.listenerPort = port
        args("-l").args(port.toString())
    }
    
    void setListenerPort(final Integer port) {
        listenerPort(port)
    }
    
    void file(final File scriptFile) {
        args(scriptFile.absolutePath)
    }

    void setFile(final File scriptFile) {
        file(scriptFile)
    }
}
