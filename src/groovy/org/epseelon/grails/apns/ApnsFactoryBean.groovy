package org.epseelon.grails.apns

import groovy.transform.CompileStatic

import org.springframework.beans.factory.FactoryBean

import com.notnoop.apns.APNS
import com.notnoop.apns.ApnsService
import com.notnoop.apns.ApnsServiceBuilder

/**
 * @author sarbogast
 * @version 1 janv. 2010, 00:37:38
 */
@CompileStatic
class ApnsFactoryBean implements FactoryBean {

    static enum Environment { SANDBOX, PRODUCTION }

    String pathToCertificate
    String certificateResourcePath
    String password
    boolean queued = false
    Environment environment = Environment.SANDBOX
    boolean nonBlocking = false

    def getObject() {
        ApnsServiceBuilder apnsService = APNS.newService()
        if (pathToCertificate) {
            apnsService.withCert(pathToCertificate, password)
        }
        else {
            if (!certificateResourcePath) {
                switch (environment) {
                    case Environment.PRODUCTION: certificateResourcePath = "/apns-prod.p12"; break
                    case Environment.SANDBOX:    certificateResourcePath = "/apns-dev.p12"
                }
            }
            apnsService.withCert(getClass().getResourceAsStream(certificateResourcePath), password)
        }

        switch (environment) {
            case Environment.PRODUCTION: apnsService.withProductionDestination(); break
            case Environment.SANDBOX:    apnsService.withSandboxDestination()
        }

        /*
         * Apns, nowadays, is just supporting Queued service.
         * Was talking to the developer of the API, and he told me that furtermore
         * even the method asNonBlocking might be removed!
         */

        return apnsService.asQueued().build()
    }

    Class getObjectType() { ApnsService }

    boolean isSingleton() { true }
}
