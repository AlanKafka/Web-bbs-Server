package com.kafka.webbbs.model.untils;





import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kafka.webbbs.common.ErrorCode;
import com.kafka.webbbs.exception.BusinessException;
import com.kafka.webbbs.model.entity.User;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
@Configuration
public class TokeConfig {
    final static String signature = "kafka";
    Algorithm algorithm = Algorithm.HMAC256("kafka");//创建算法签名
    /**
     * 生成token
     * @param userAccount
     * @param userPassword
     * @return
     */
    public String getTokes(Long uid,String userAccount,String userPassword){
        HashMap<String, Object> map = new HashMap<>();
        map.put("signature",signature);
        map.put("arith","HS256");
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND,20000000); //设置jwt有效时间
        String toke  = JWT.create()
                .withHeader(map) //header
                .withClaim("uid",uid)
                .withClaim("userAccount", userAccount)  //payLoad
                .withClaim("userPassword", userPassword)
                .withExpiresAt(instance.getTime()) //指定令牌过期时间
                .sign(algorithm);//签名
        return toke;
    }

    /***
     * 验证token有效期
     * @param token
     * @return
     */
    public boolean checkToke(String token){
        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT verify = jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    /***
     * 解析数据
     * @param token
     * @return
     */
    public User getTokeMessage(String token){
        if(!checkToke(token)){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"token异常");
        }
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        Long uid = verify.getClaim("uid").asLong();
        String userAccount = verify.getClaim("userAccount").asString();
        String userPassword = verify.getClaim("userPassword").asString();
        User user = new User();
        user.setUid(uid);
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);
        return user;
    }
}
