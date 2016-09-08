import org.epseelon.grails.apns.ApnsFactoryBean
import grails.util.Environment

class ApnsGrailsPlugin {
    def version = "1.2"
    def grailsVersion = "2.3 > *"
    def title = "Apple Push Notification Service Plugin"
    def author = "Sebastien Arbogast"
    def authorEmail = "sebastien.arbogast@gmail.com"
    def description = 'Integrates with Apple Push Notification service to send push notifications to an iPhone client of your application'
    def documentation = "http://grails.org/plugin/apns"
    def license = 'GPL3'
    def developers = [
        [name: 'Arthur Neves', email: 'arthurnn@gmail.com']
    ]
    def issueManagement = [url: 'https://github.com/sarbogast/grails-apns/issues']
    def scm = [url: 'https://github.com/sarbogast/grails-apns']

    def doWithSpring = {
        def conf = application.config.apns

        ApnsFactoryBean.Environment apnsEnvironment
        if (conf.environment) {
            apnsEnvironment = ApnsFactoryBean.Environment.valueOf(conf.environment.toString().toUpperCase())
        }
        else {
            if (Environment.current == Environment.PRODUCTION) {
                apnsEnvironment = ApnsFactoryBean.Environment.PRODUCTION
            }
            else {
                apnsEnvironment = ApnsFactoryBean.Environment.SANDBOX
            }
        }

        apnsService(ApnsFactoryBean) {
            pathToCertificate = conf.pathToCertificate ?: null
            certificateResourcePath = conf.certificateResourcePath ?: null
            password = conf.password
            environment = apnsEnvironment
        }
    }
}
