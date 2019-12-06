package com.rxv5.sso.granter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.rxv5.sso.vo.SysVerifyVo;

public abstract class AbstractCustomTokenGranter extends AbstractTokenGranter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected AbstractCustomTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		SysVerifyVo customUser = null;
		try {
			Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
			customUser = getCustomUser(parameters);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new InvalidGrantException(e.getMessage(), e);
		}
		if (customUser == null) {
			throw new InvalidGrantException("无法获取用户信息");
		}

		OAuth2Authentication authentication = super.getOAuth2Authentication(client, tokenRequest);
		authentication.setDetails(customUser);
		authentication.setAuthenticated(true);
		return authentication;
	}

	protected abstract SysVerifyVo getCustomUser(Map<String, String> parameters);
}