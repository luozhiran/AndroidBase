import org.gradle.api.publish.maven.MavenPublication

apply plugin:'groovy'



repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation gradleApi()
    implementation localGroovy()
    testImplementation 'junit:junit:4.13'
}

/*
apply plugin: 'maven-publish'
publishing {
    publications {
        mavenPub(MavenPublication) {
            // 这一行表示将 jar 包包含在要发布的组件中
            from components.java
            // 描述性信息
            pom {
                name = 'Robot MyPlugin'
                description = 'This is a test project for gradle plugin.'
                url = 'http://www.helloworld.com'
            }
        }
    }

}*/
