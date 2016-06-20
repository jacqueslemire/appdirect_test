package com.fortiq.appdirect.challenge.webapp.security;

import javax.enterprise.event.Observes;

import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

public class HttpSecurityConfiguration {

    public void onInit(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();

        builder
            .http()
                .allPaths()
                    .authenticateWith()
                        .form()
                            .loginPage("/login.jsf")
                .forPath("/javax.faces.resource/*")
                    .unprotected()
                .forPath("/openIdLogin.jsf")
                    .unprotected()
                .forPath("/openIdValidate.jsf")
                    .unprotected();
    }
    
}
