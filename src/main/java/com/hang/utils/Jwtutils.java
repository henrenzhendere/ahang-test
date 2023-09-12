package com.hang.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

import java.util.Calendar;

public class Jwtutils {
	
	public static final String sale="123456";
	
	//把需要放入的数据放入到token
	public static String jwtToken(String username) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, 600);
		///设置超时时间

		Builder builder = JWT.create();

		String token = builder// head
				.withClaim("username",username)
				.withExpiresAt(instance.getTime())
				.sign(Algorithm.HMAC256(sale));// 签名
			//获取加密后的token
		return token;
	}
	
	//解密token获取加密前字符串
	public static String getTokenName(String token) {
		Verification verification = JWT.require(Algorithm.HMAC256(sale));// 签名
		JWTVerifier jwtVerifier = verification.build();
		DecodedJWT verify;
		try {
			verify = jwtVerifier.verify(token);
			Claim username = verify.getClaim("username");
			return username.asString();
			} catch (JWTVerificationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	//校验token
	public static boolean verifyToken(String token) {
		// 创建验证对象
		Verification verification = JWT.require(Algorithm.HMAC256(sale));// 签名
		JWTVerifier jwtVerifier = verification.build();

		
		try {
			jwtVerifier.verify(token);
			//解密token  如果 传入的token无法解析   或者 超时   就报错
			return true;
		} catch (JWTVerificationException e) {
			return false;
		}
	}

}
