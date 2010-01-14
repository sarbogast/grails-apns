import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.epseelon.grails.apns.ApnsFactoryBean

class ApnsGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Sebastien Arbogast"
    def authorEmail = "sebastien.arbogast@gmail.com"
    def title = "Apple Push Notification Service"
    def description = '''\\
This plugin allows you to integrate with Apple Push Notification service to send
push notifications to an iPhone client of your Grails application.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Apns+Plugin"

    def doWithSpring = {
        apnsService(org.epseelon.grails.apns.ApnsFactoryBean) {
            pathToCertificate = ConfigurationHolder.config.apns.pathToCertificate
            password = ConfigurationHolder.config.apns.password
            queued = ConfigurationHolder.config.apns.queued
            environment = ConfigurationHolder.config.apns.environment ? ApnsFactoryBean.Environment.valueOf(ConfigurationHolder.config.apns.environment.toUpperCase()) : ApnsFactoryBean.Environment.SANDBOX
            nonBlocking = !ConfigurationHolder.config.apns.blocking
        }
    }

    def doWithApplicationContext = {applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def doWithWebDescriptor = {xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def doWithDynamicMethods = {ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = {event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = {event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
