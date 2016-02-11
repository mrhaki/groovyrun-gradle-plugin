package com.mrhaki.gradle.groovyrun

import org.gradle.testkit.runner.GradleRunner

class ConfigurationSpec extends AbstractIntegrationSpec {
    
    def "explicit Groovy version must be set as dependency when groovyVersion property is set via the extension"() {
        given:
        buildFile << """
            plugins { id 'com.mrhaki.groovyrun' }
            
            repositories {
                jcenter()
            }
            
            groovyRun {
                groovyVersion = '2.4.3'
            }
        """

        when:
        final def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath(pluginClasspath)
                .withArguments('dependencies')
                .build()

        then:
        result.output.contains('org.codehaus.groovy:groovy-all:2.4.3')
//        result.task(":helloWorld").outcome == TaskOutcome.SUCCESS
    }
    
}
