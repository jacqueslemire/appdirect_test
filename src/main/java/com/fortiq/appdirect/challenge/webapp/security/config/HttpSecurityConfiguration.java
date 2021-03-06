package com.fortiq.appdirect.challenge.webapp.security.config;

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
                            .loginPage("/login.xhtml")
                .forPath("/javax.faces.resource/*")
                    .unprotected()
                .forPath("/rest/*")
                    .unprotected()
                .forPath("/openid/*")
                    .unprotected();
    }
    
}
