The problem with Apple Push Notification service is that the API provided by Apple is pretty low-level and the documentation is rather scarce. And it's understandable because they expose a service that should be usable by any server technology, whether it is Java, .Net, PHP, Rails, etc. Fortunately, Java has a huge community and a great Open Source spirit, so it wasn't long before a few projects were created to build an abstraction layer on top of APNs API. The simplest and most documented one I found is java-apns by Mahmood Ali. This plugin wraps up java-apns for Grails. Here is how it works.

Configuration
=============

Once the plugin is installed, add the following code to grails-app/Config.groovy:

    environments {
        development {
            apns {
                pathToCertificate = "/Users/sarbogast/Desktop/APNs_development_certificates.p12"
                password = "password"
                environment = "sandbox"
            }
        }
        test {
            apns {
                pathToCertificate = "/usr/local/myapp/APNs_development_certificates.p12"
                password = "password"
                environment = "sandbox"
            }
        }

        production {
            apns {
                pathToCertificate = "/usr/local/myapp/APNs_production_certificates.p12"
                password = "password"
                environment = "production"
            }
        }
    }

Of course replace certificate paths and passwords with real values for your environments. p12 files have to be exported from your Keychain access using the procedure
described in Apple documentation [here](https://developer.apple.com/library/ios/#documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/ProvisioningDevelopment/ProvisioningDevelopment.html).

Since version 1.0, it is also possible to make this configuration much shorter.

If you omit the environment setting, production environment will point to APNs production gateway, and all other environments will point to APNs sandbox gateway.

If your sandbox certificate is in src/java/apns-dev.p12 and your production certificate is in src/java/apns-prod.p12, you can omit pathToCertificate. In this case, all you need to configure is apns.password.

If you don't have access to your server's file system, you can also replace pathToCertificate by certificateResourcePath and set it to the path of your certificate, relative to src/java. So for example, if your certificate is in src/java/mycert.p12, you can set certificateResourcePath="/mycert.p12".

This new configuration system is way better for Heroku deployment.

You can configure a bean to be notified by sent event via the option "delegateBean". This bean must implement the interface com.notnoop.apns.ApnsDelegate.

       apns {
            pathToCertificate = "/usr/local/myapp/APNs_production_certificates.p12"
            password = "password"
            environment = "production"
            delegateBean = "apnsDelegateService"
       }

Usage
=====

Once you have done that, a new "apnsService" is available in all of your controllers, services and so on, and you can use it to send push notification using java-apns API
described [here](https://github.com/notnoop/java-apns) and [there](http://notnoop.github.com/java-apns/apidocs/index.html). For example, I personnally defined a message service like the following:

    import com.notnoop.apns.APNS
    import com.notnoop.apns.ApnsNotification
    import com.notnoop.apns.ApnsService
    class MessageService {

        boolean transactional = true

        ApnsService apnsService

        def sendMessageToDevices(List<Device> recipients, String messageKey, String … arguments) {
            recipients.each {Device device ->
                def payload = APNS.newPayload()
                    .badge(device.messages.size())
                    .localizedKey(messageKey)
                    .localizedArguments(arguments)
                    .sound("default")

                if (payload.isTooLong()) log.info("Message is too long: " + payload.length())
                try {
                    apnsService.push(new ApnsNotification(
                            device.token,
                            payload.build().getBytes("UTF-8"))
                    )
                } catch (Exception e) {
                    log.error("Could not connect to APNs to send the notification")
                }
            }
        }
    }

Remember that the sandbox environment has its down moments, so sometimes, you send a message, you don't get any exception or error message of any sort, and still your notification doesn't get through. I hope this won't happen too much in production.

There is still an important aspect that I have not covered in this plugin yet: when you send messages to a device and the device is not here to receive it, after several failures, APNs will flag it as unavailable (probably your iPhone application was uninstalled from the target device). And APNs provides an API to get the list of unavailable device tokens so that you can stop trying to send notifications to these. I guess the best approach for this is to set up a Quartz job to call ApnsService.getInactiveDevices() every day and do what's necessary in your database so that you don't send any message to the corresponding tokens.