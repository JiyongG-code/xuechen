package com.xuecheng.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;
import java.util.Calendar;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class TokenConfig {


    private String SIGNING_KEY = "mq123";
    @Autowired
    TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Bean
    public TokenStore tokenStore() {

        return new JwtTokenStore(accessTokenConverter());
    }

//    @Bean
//    public TokenStore  rtokenStore(){
//        return  new RedisTokenStore();
//
//        }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        //在 JWT 编码的令牌值和 OAuth 身份验证信息（双向）之间进行转换的帮助程序。在授予令牌时还充当令牌增强器。
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        
        return converter;
    }

    //令牌管理服务
    @Bean(name="authorizationServerTokenServicesCustom")
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setSupportRefreshToken(true);//支持刷新令牌
        service.setTokenStore(tokenStore);//令牌存储策略

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

//    @Bean
//    public TokenStore tokenStore() {
//        //使用内存存储令牌（普通令牌）
//        return new InMemoryTokenStore();
//    }
//
//    @Bean(name="authorizationServerTokenServicesCustom")
//    public AuthorizationServerTokenServices tokenService() {
//        DefaultTokenServices service=new DefaultTokenServices();
//        service.setSupportRefreshToken(true);//支持刷新令牌
//        service.setTokenStore(tokenStore);//令牌存储策略
//        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
//        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
//        return service;
//    }



}
