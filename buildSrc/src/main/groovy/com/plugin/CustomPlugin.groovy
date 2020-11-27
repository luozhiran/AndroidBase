package com.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

 class CustomPlugin implements Plugin<Project> {
    @Override
     void apply(Project project) {
        project.task('showCustomPluginInBuildSrc') {
            group 'abc'
            description "这是一个测试"
            doLast {
                println('task in CustomPluginInBuildSrc')
            }
        }
    }
}
