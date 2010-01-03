package org.epseelon.grails.apns

import org.springframework.beans.factory.FactoryBean
import com.notnoop.apns.APNS
import com.notnoop.apns.ApnsService

/**
 * @author sarbogast
 * @version 1 janv. 2010, 00:37:38
 */
class ApnsFactoryBean implements FactoryBean {
    public enum Environment{SANDBOX, PRODUCTION}

    String pathToCertificate
    String password
    boolean queued = false
    Environment environment = Environment.SANDBOX
    boolean nonBlocking = false

    Object getObject() {
        def apnsService = APNS.newService().withCert(pathToCertificate, password)
        switch(environment){
            case Environment.PRODUCTION: apnsService = apnsService.withProductionDestination(); break;
            case Environment.SANDBOX:
            default: apnsService = apnsService.withSandboxDestination(); break;
        }
        if(queued){
            apnsService = apnsService.asQueued()
        }
        if(nonBlocking){
            apnsService = apnsService.asNonBlocking()
        }

        return apnsService.build()
    }

    Class getObjectType() { ApnsService }

    boolean isSingleton() { true }
}
