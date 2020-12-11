import javafx.concurrent.Task
apply plugin: 'com.android.library'
apply plugin:'groovy'
apply plugin: 'maven-publish'
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









