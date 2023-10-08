package com.panda.sport.admin.utils;

import com.panda.sport.admin.security.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

/**
 * @auth: YK
 * @Description:jwt的生成器和验证器
 * @Date:2020/5/10 17:04
 */
@Component
@Slf4j
public class JwtTokenUtil {
    private static final String SECRET = "9a96349e12345385785e804e0f4254dee";

    private static String ISSUER = "panda_user";
    private Clock clock = DefaultClock.INSTANCE;


    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    public String getUsernameFromToken(String token) {
        Map<String, String> result = verifyToken(token);
        return result.get("userName");
        // return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    /**
     * 外部直接调用此方法生产token方法
     *
     * @param userDetails
     * @return
     */
    public String generateToken(JwtUser userDetails, Date date) {
        Map<String, String> claims = new HashMap<>();
        claims.put("userName", userDetails.getUsername());
        claims.put("merchantName", userDetails.getMerchantName());
        claims.put("merchantCode", userDetails.getMerchantCode());
        claims.put("merchantId", userDetails.getMerchantId());
        claims.put("agentLevel", userDetails.getAgentLevel() + "");
        claims.put("openVrSport", userDetails.getOpenVrSport() + "");
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create().
                    withIssuer(ISSUER). //发行人
                    withExpiresAt(date); //过期时间点
            claims.forEach((key, value) -> {
                builder.withClaim(key, value);
            });
            //签名加密
            return builder.sign(algorithm);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        //return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * 生成token
     *
     * @param claims
     * @param expireDatePoint 过期时间点
     * @return
     */
    public static String genToken(Map<String, String> claims, Date expireDatePoint) {
        try {
            //使用HMAC256进行加密
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //创建jwt
            JWTCreator.Builder builder = JWT.create().
                    withIssuer(ISSUER). //发行人
                    withExpiresAt(expireDatePoint); //过期时间点
            //传入参数
            claims.forEach((key, value) -> {
                builder.withClaim(key, value);
            });
            //签名加密
            return builder.sign(algorithm);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生产token
     *
     * @param claims
     * @param subject
     * @return
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate, "refresh");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    /**
     * 刷新token的验证
     * status 为refresh就是刷新token,如果不是就是让token一秒钟失效
     *
     * @param token
     * @return
     */
    public String refreshToken(String token, String status) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate, status);
        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


    /**
     * 验证token是不是有效性
     *
     * @param token
     * @param
     * @return
     */
    public Boolean validateToken(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        try {
            verifyToken(token);
            return true;
        } catch (Exception e) {
            log.error("解析JWT 异常!", e);
            return false;
        }


    }

    /**
     * 解密jwt
     *
     * @param token
     * @return
     * @throws RuntimeException
     */
    public static Map<String, String> verifyToken(String token) throws RuntimeException {
        Algorithm algorithm = null;
        try {
            //使用HMAC256进行加密
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //解密
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        Claim claim = map.get("exp");
        Integer expDate = claim.asInt();
        Map<String, String> resultMap = new HashMap<>();
        map.forEach((k, v) -> resultMap.put(k, v.asString()));
        return resultMap;
    }

    /**
     * status 如果状态为fresh的话会重新刷新token值，如果不是则1秒秒后失效
     *
     * @param createdDate
     * @param status
     * @return
     */
    private Date calculateExpirationDate(Date createdDate, String status) {
        return "refresh".equals(status) ? new Date(createdDate.getTime() + expiration) : new Date(createdDate.getTime() + 3);
    }
}
