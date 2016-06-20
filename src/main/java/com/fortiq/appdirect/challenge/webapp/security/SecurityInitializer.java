package com.fortiq.appdirect.challenge.webapp.security;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static org.picketlink.idm.model.basic.BasicModel.grantRole;

/**
 * Sets up the base roles and credentials on application startup
 */
@Singleton
@Startup
public class SecurityInitializer {

    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {

        User jlemire = new User("jlemire");
        jlemire.setEmail("jlemire@fortiq.com");
        jlemire.setFirstName("Jacques");
        jlemire.setLastName("Lemire");

        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        identityManager.add(jlemire);
        identityManager.updateCredential(jlemire, new Password("jlemire"));

        Role admin = new Role("admin");
        identityManager.add(admin);

        Role client = new Role("client");
        identityManager.add(client);

        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

        grantRole(relationshipManager, jlemire, admin);
    }
}
