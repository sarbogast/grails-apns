import org.epseelon.grails.apns.ApnsFactoryBean
import grails.util.Environment

class ApnsGrailsPlugin {
    // the plugin version
    def version = "1.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.8 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Sebastien Arbogast, Arthur Neves"
    def authorEmail = "sebastien.arbogast@gmail.com, arthurnn@gmail.com"
    def title = "Apple Push Notification Service"
    def description = '''\\
This plugin allows you to integrate with Apple Push Notification service to send
push notifications to an iPhone client of your Grails application.
'''

    // URL to the plugin's documentation
    def documentation = "http://www.grails.org/plugin/apns"

    def doWithSpring = {
        def configuredEnvironment
        if(application.config.apns.environment){
            configuredEnvironment = ApnsFactoryBean.Environment.valueOf(application.config.apns.environment.toUpperCase() as String)
        } else {
            switch(Environment.current){
                case Environment.PRODUCTION: configuredEnvironment = ApnsFactoryBean.Environment.PRODUCTION
                break
                default: configuredEnvironment = ApnsFactoryBean.Environment.SANDBOX
            }
        }

        apnsService(ApnsFactoryBean) {
            pathToCertificate = application.config.apns.pathToCertificate ?: null
            certificateResourcePath = application.config.apns.certificateResourcePath ?: null
            password = application.config.apns.password
            environment = configuredEnvironment
        }
    }

    def doWithApplicationContext = {applicationContext ->
    }

    def doWithWebDescriptor = {xml ->
    }

    def doWithDynamicMethods = {ctx ->
    }

    def onChange = {event ->
        // Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = {event ->
        // Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
