package com.rxv5.sso.exception;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) {
		OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
		logger.error("CustomWebResponseExceptionTranslator.status:{},oAuth2ErrorCode:{},message:{}",
				oAuth2Exception.getHttpErrorCode(), oAuth2Exception.getOAuth2ErrorCode(), oAuth2Exception.getMessage());
		return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(oAuth2Exception);
	}
}
