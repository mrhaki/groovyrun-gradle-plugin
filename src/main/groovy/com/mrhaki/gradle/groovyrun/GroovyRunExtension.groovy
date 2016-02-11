package com.mrhaki.gradle.groovyrun

import groovy.transform.CompileStatic

/**
 * Extension for the Gradle groovyrun plugin.
 */
@CompileStatic
class GroovyRunExtension {

    /**
     * The property {@link #groovyVersion} is used to set the
     * Groovy version that must be used to run
     * Groovy code.
     */
    String groovyVersion

}
