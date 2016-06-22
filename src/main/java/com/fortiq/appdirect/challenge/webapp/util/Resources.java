package com.fortiq.appdirect.challenge.webapp.util;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.log4j.Logger;
import org.picketlink.annotations.PicketLink;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Resources {

    @PersistenceContext(unitName = "test-ds")
    private EntityManager em;
    
    @Inject
    @ConfigProperty(name = "oauth.consumer.key")
    private String consumerKey;

    @Inject
    @ConfigProperty(name = "oauth.consumer.secret")
    private String consumerSecret;

    @Produces
    @PicketLink
    public EntityManager getPicketLinkEntityManager() {
        return em;
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
    
    @Produces
	public OAuthConsumer getOAuthConsumer() {
		return new DefaultOAuthConsumer(consumerKey, consumerSecret);
	}

}
