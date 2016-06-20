package com.fortiq.appdirect.challenge.webapp.security;

import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;

import javax.enterprise.inject.Produces;

public class IdentityManagementConfiguration {

    @Produces IdentityConfiguration produceIdentityManagementConfiguration() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
            .named("default")
            .stores()
            .jpa()
                // Specify that this identity store configuration supports all features
            .supportAllFeatures();

        return builder.build();
    }

}
