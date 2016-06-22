package com.fortiq.appdirect.challenge.webapp.security;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;

@Stateless
public class IdentityServices {

	public static final String OPENID_USER_ATTRIBUTE = "OPENID";

	@Inject
	private IdentityManager identityManager;

	public void setUserOpenId(User user, String openId) {
		user.setAttribute(new Attribute<String>(OPENID_USER_ATTRIBUTE, openId));
	}

	public User getUserForOpenId(String openId) {
		IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
		IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class)
				.where(queryBuilder.equal(IdentityType.QUERY_ATTRIBUTE.byName(OPENID_USER_ATTRIBUTE), openId));
		List<User> result = query.getResultList();		
		return result.size() > 0 ? result.get(0) : null;
	}
}
