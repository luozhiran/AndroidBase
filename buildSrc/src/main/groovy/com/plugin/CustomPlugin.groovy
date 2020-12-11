package com.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenArtifact
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Upload
import org.gradle.util.ConfigureUtil


class HelloConfigExtension {
    String msg =  'Hello gradle plugin'
    String version = '1.0.1'
}

class CustomPlugin implements Plugin<Project> {
    @Override
     void apply(Project project) {
        def extension =project.extensions.create('hello',HelloConfigExtension)
        project.task('hello') {
            group 'abc'
            description "这是一个测试"
            doLast {
                println extension.msg
                println extension.version
                upload()
            }
        }
    }


}



