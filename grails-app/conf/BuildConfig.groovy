grails.project.work.dir = 'target'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'

    repositories {
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        compile 'com.notnoop.apns:apns:1.0.0.Beta6'
        runtime 'org.apache.mina:mina-core:2.0.0-RC1'
        compile 'org.codehaus.jackson:jackson-mapper-asl:1.4.0'
    }

    plugins {
        build(':release:3.1.2', ':rest-client-builder:2.1.1') {
            export = false
        }
    }
}
