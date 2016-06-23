package com.fortiq.appdirect.challenge.webapp.rest.subscription;

import static org.picketlink.idm.model.basic.BasicModel.grantRole;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADCreateEvent;
import com.fortiq.appdirect.challenge.webapp.security.IdentityServices;

/**
 * Services that manage the local subscription objects for api calls. 
 */
@Stateless
public class SubscriptionServices {

	public static final String APPDIRECT_USER_ATTRIBUTE = "APPDIRECT_USER_ATTRIBUTE";
	
    @Inject
    private IdentityManager identityManager;
    
    @Inject
    private RelationshipManager relationshipManager;
    
    @Inject
    private IdentityServices identityServices;

    public User getUser( String loginName ) {
    	return BasicModel.getUser(identityManager, loginName);
    }
    
	/* Here we would typically also create a company if it doesn't exist, add subscription 
	 * objects and associate those to the user, etc...
	 **/
	public User createUser( ADCreateEvent event ) {
		
		// Our product can only be bought and used by the subscription creator  
		User user = new User( event.getCreator().getUuid() );
		user.setFirstName( event.getCreator().getFirstName() );
		user.setLastName( event.getCreator().getLastName() );
		user.setEmail( event.getCreator().getEmail() );
        identityManager.add(user);
        
        Role client = BasicModel.getRole(identityManager, "client");
        grantRole(relationshipManager, user, client);
        
        identityServices.setUserOpenId(user, event.getCreator().getOpenId() );
		user.setAttribute(new Attribute<Boolean>(APPDIRECT_USER_ATTRIBUTE, Boolean.TRUE));
        return user;
	}

	public void deleteUser(User user) {
		identityManager.remove(user);
	}
	
}
