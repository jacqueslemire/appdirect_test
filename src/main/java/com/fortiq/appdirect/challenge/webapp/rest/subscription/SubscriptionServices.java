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

import com.fortiq.appdirect.challenge.webapp.rest.subscription.model.ADOrderEvent;

@Stateless
public class SubscriptionServices {

	public static final String OPENID_USER_ATTRIBUTE = "OPENID_USER_ATTRIBUTE";
	
    @Inject
    private IdentityManager identityManager;
    
    @Inject
    private RelationshipManager relationshipManager;

    public User getUser( ADOrderEvent event ) {
    	return BasicModel.getUser(identityManager, event.getCreator().getUuid());
    }
    
	/* Here we would typically also create a company if it doesn't exist, add subscription 
	 * objects and associate those to the user, etc...
	 **/
	public User createUser( ADOrderEvent event ) {
		User user = new User( event.getCreator().getUuid() );
		user.setFirstName( event.getCreator().getFirstName() );
		user.setLastName( event.getCreator().getLastName() );
		user.setEmail( event.getCreator().getEmail() );
        identityManager.add(user);
        Role client = BasicModel.getRole(identityManager, "client");
        grantRole(relationshipManager, user, client);
		user.setAttribute(new Attribute<Boolean>(OPENID_USER_ATTRIBUTE, Boolean.TRUE));
        return user;
	}
	
}
