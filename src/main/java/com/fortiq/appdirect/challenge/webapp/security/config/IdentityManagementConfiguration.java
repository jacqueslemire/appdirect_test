package com.fortiq.appdirect.challenge.webapp.security.config;

import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;

import javax.enterprise.inject.Produces;

/**
 * We use the default PicketLink identity object model and persist it through JPA. 
 */
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
