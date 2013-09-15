grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()

        mavenCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        //mavenRepo "https://oss.sonatype.org/content/repositories/snapshots/"
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
        grailsRepo "http://grails.org/plugins"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        /*
         * We cannot use any dependency from maven, beause apns uses slf4j 1.6 which
         * has a conflict with grails 1.3.x ! so better including the jars!
         */
        //		runtime 'com.notnoop.apns:apns:0.1.6'
        //      runtime 'org.slf4j:slf4j-api:1.5.8'
        //		runtime 'org.apache.mina:mina-core:2.0.0-RC1'
        //      compile 'org.codehaus.jackson:jackson-mapper-asl:1.4.0'

        compile 'com.notnoop.apns:apns:0.2.3'
    }

    plugins {
        build(':release:2.2.1', ':rest-client-builder:1.0.3') {
            export = false
        }
    }

}
