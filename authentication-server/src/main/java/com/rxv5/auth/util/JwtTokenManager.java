package com.rxv5.auth.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rxv5.auth.vo.SysVerifyVo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author congrixu
 *
 */
@Component
public class JwtTokenManager {

	@Value("${jwt.expiration}")
	public long expiration;//token超时时间

	private final String SECRET_KEY = "qswe$r^tIy*u(ihf){zxsSDE}DWA?HKNVC<>}{)(*&%@&nalipie;";
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

	/**
	 * 解析token
	 * @param jsonWebToken
	 * @return
	 */
	public Claims parseToken(String jsonWebToken) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jsonWebToken).getBody();
		return claims;
	}

	/**
	 * 新建token
	 * @param user
	 * @return
	 */
	public String createToken(SysVerifyVo user) {

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("user", user);

		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(user.getLoginId()).setClaims(claims)
				.signWith(signatureAlgorithm, SECRET_KEY);

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		// 添加Token签发时间
		builder.setIssuedAt(now);
		// 添加Token过期时间
		if (expiration >= 0) {
			long expMillis = nowMillis + expiration;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}

	/**
	 * 刷新令牌
	 *
	 * @param claims
	 * @return
	 */
	public String refreshToken(Claims claims) {
		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(claims.getSubject())
				.setClaims(claims).signWith(signatureAlgorithm, SECRET_KEY);

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		// 添加Token签发时间
		builder.setIssuedAt(now);
		// 添加Token过期时间
		if (expiration >= 0) {
			long expMillis = nowMillis + expiration;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}

	public String refreshToken(String token) {
		Claims cl = parseToken(token);
		return refreshToken(cl);
	}

	public String getLoginidFromToken(String token) {
		Claims cl = parseToken(token);
		String loginid = cl.getSubject();
		return loginid;
	}

	public SysVerifyVo getUserFromToken(String token) {
		Claims cl = parseToken(token);
		return (SysVerifyVo) cl.get("user");
	}

}
