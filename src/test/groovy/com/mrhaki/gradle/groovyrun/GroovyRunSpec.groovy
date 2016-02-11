package com.mrhaki.gradle.groovyrun

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class GroovyRunSpec extends AbstractIntegrationSpec {

    def "evaluate Groovy script expressed as String value"() {
        given:
        buildFile << """
            plugins { id 'com.mrhaki.groovyrun' }
            
            runGroovyScript {
               evaluate "println 'Hello Gradle!'"
            }   
        """

        when:
        final def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath(pluginClasspath)
                .withArguments('runGroovyScript')
                .build()

        then:
        result.output.contains('Hello Gradle!')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }

    def "evaluate Groovy script file"() {
        given:
        final File scriptFile = testProjectDir.newFile('print.groovy')
        scriptFile << "println 'Hello Gradle!'"
        
        and:
        buildFile << """
            plugins { id 'com.mrhaki.groovyrun' }
            
            runGroovyScript {
               file file('print.groovy')
            }   
        """

        when:
        final def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath(pluginClasspath)
                .withArguments('runGroovyScript')
                .build()

        then:
        result.output.contains('Hello Gradle!')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }
}
