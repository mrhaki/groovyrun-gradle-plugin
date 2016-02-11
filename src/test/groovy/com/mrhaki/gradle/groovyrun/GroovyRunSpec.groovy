package com.mrhaki.gradle.groovyrun

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class GroovyRunSpec extends AbstractIntegrationSpec {
    
    def setup() {
        buildFile << "plugins { id 'com.mrhaki.groovyrun' }"
    }

    def "evaluate Groovy script expressed as String value"() {
        given:
        buildFile << """
            runGroovyScript {
               evaluate "println 'Hello Gradle!'"
            }   
        """

        when:
        final def result = runGradle('runGroovyScript')

        then:
        result.output.contains('Hello Gradle!')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }
    
    def "evaluate Groovy script with arguments"() {
        given:
        buildFile << """
            runGroovyScript {
               evaluate "println args[0]"
               args "Gradle"
            }   
        """

        when:
        final def result = runGradle('runGroovyScript')

        then:
        result.output.contains('Gradle')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }

    def "process given file by line if byLine property is true"() {
        given:
        final File scriptFile = testProjectDir.newFile('sample.txt')
        scriptFile << """\
one
two"""

        and:
        buildFile << """
            runGroovyScript {
               evaluate "println line.toUpperCase()"
               byLine = true
               args '${scriptFile.absolutePath}'
            }   
        """

        when:
        final def result = runGradle('runGroovyScript')

        then:
        result.output.contains('ONE')
        result.output.contains('TWO')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }

    def "process given file by line and print if byLineAndPrint property is true"() {
        given:
        final File scriptFile = testProjectDir.newFile('sample.txt')
        scriptFile << """\
one
two"""

        and:
        buildFile << """
            runGroovyScript {
               evaluate "line.toUpperCase()"
               byLineAndPrint = true
               args '${scriptFile.absolutePath}'
            }   
        """

        when:
        final def result = runGradle('runGroovyScript')

        then:
        result.output.contains('ONE')
        result.output.contains('TWO')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }
    
    def "evaluate Groovy script file"() {
        given:
        final File scriptFile = testProjectDir.newFile('print.groovy')
        scriptFile << "println 'Hello Gradle!'"
        
        and:
        buildFile << """
            runGroovyScript {
               file file('print.groovy')
            }   
        """

        when:
        final def result = runGradle('runGroovyScript')

        then:
        result.output.contains('Hello Gradle!')
        result.task(":runGroovyScript").outcome == TaskOutcome.SUCCESS
    }
    
    private def runGradle(final String... arguments) {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath(pluginClasspath)
                .withArguments(arguments)
                .build()
    }
}
