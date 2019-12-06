package com.rxv5.sso.granter;

import java.util.Map;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.rxv5.sso.vo.SysVerifyVo;

public class WeChatTokenGranter extends AbstractCustomTokenGranter {

	public final static String GRANT_TYPE = "we_chat";//微信认证

	//private UserServiceApi userService;

	//private WxUserService wxUserService;

	/*
	 * public WeChatTokenGranter(AuthorizationServerTokenServices tokenServices,
	 * ClientDetailsService clientDetailsService, OAuth2RequestFactory
	 * requestFactory, UserServiceApi userService, WxUserService wxUserService) {
	 * super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	 * this.userService = userService; this.wxUserService = wxUserService; }
	 */
	
	public WeChatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	@Override
	protected SysVerifyVo getCustomUser(Map<String, String> parameters) {
		SysVerifyVo user = null;
		String code = parameters.get("we_chat_code");
		//CusBaseVo baseUser = wxUserService.code2Session(code);//调用微信登录
		//String openid = baseUser.getOpenid();
		//user = userService.findUserByOpenId(openid);
		//if (user == null) {
		//	user = userService.saveWxInfo(baseUser);
		//}
		//user.setCusId(baseUser.getCusId());
		return user;
	}

}
