package com.rxv5.sso.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.rxv5.sso.exception.CustomWebResponseExceptionTranslator;
import com.rxv5.sso.oauth2.CustomTokenEnhancer;
import com.rxv5.sso.oauth2.MyRedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthenticationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    RedisConnectionFactory redisConnectionFactory;

	/**
	 * jwt 对称加密密钥
	 */
	@Value("${spring.security.oauth2.jwt.signingKey}")
	private String signingKey ;

	@Value("${spring.security.oauth2.jwt.expiration}")
	private Integer expiration ;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		// 支持将client参数放在header或body中
		oauthServer.allowFormAuthenticationForClients();
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()// 配置内存中，也可以是数据库
				.withClient("rxv5-client")// clientid
				.secret(passwordEncoder.encode("rxv5-secret"))
				.accessTokenValiditySeconds(expiration)// token有效时间
																				// 秒
				// .authorizedGrantTypes("refresh_token", "password",
				// "authorization_code")//token模式
				.authorizedGrantTypes("password","refresh_token")// token模式
				.scopes("all");// 限制允许的权限配置
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// 配置token的数据源、自定义的tokenServices等信息,配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
		endpoints.tokenStore(tokenStore());
		endpoints.exceptionTranslator(customExceptionTranslator());
		endpoints.authenticationManager(authenticationManager);
		endpoints.userDetailsService(userDetailsService);
		endpoints.accessTokenConverter(accessTokenConverter());
		endpoints.tokenEnhancer(tokenEnhancerChain());
		//endpoints.tokenServices(authorizationServerTokenServices());

	}

	/**
	 * 自定义OAuth2异常处理
	 *
	 * @return CustomWebResponseExceptionTranslator
	 */
	@Bean
	public WebResponseExceptionTranslator<OAuth2Exception> customExceptionTranslator() {
		return new CustomWebResponseExceptionTranslator();
	}

	@Bean
	public AuthorizationServerTokenServices authorizationServerTokenServices() {
		DefaultTokenServices dts = new DefaultTokenServices();
		dts.setTokenStore(tokenStore());
		dts.setSupportRefreshToken(true);
		dts.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30));
		dts.setRefreshTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30));
		return dts;

	}

	/**
	 * token的持久化
	 *
	 * @return JwtTokenStore
	 */
	@Bean
	public TokenStore tokenStore() {
		//return new JwtTokenStore(accessTokenConverter());
		//return new RedisTokenStore(redisConnectionFactory);//报错
		return new MyRedisTokenStore(redisConnectionFactory);
	}

	/**
	 * 自定义token
	 *
	 * @return tokenEnhancerChain
	 */
	@Bean
	public TokenEnhancerChain tokenEnhancerChain() {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), accessTokenConverter()));
		return tokenEnhancerChain;
	}

	/**
	 * jwt token的生成配置
	 *
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);
		return converter;
	}

}