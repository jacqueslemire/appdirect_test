package com.fortiq.appdirect.challenge.webapp.util;

import org.apache.log4j.Logger;
import org.picketlink.annotations.PicketLink;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the {@link FacesContext}, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private FacesContext facesContext;
 * </pre>
 */
public class Resources {

    @PersistenceContext(unitName = "test-ds")
    private EntityManager em;

    /*
     * Since we are using JPAIdentityStore to store identity-related data, we must provide it with an EntityManager via a
     * producer method or field annotated with the @PicketLink qualifier.
     */
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
	private OAuthConsumer getOAuthConsumer() {
		// should be configurable, obviously
		return new DefaultOAuthConsumer("testapplication-122531", "5jIzxHVmYTPPNZDi");
	}

}
