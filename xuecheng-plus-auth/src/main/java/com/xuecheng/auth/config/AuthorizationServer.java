package com.xuecheng.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.annotation.Resource;

/**
 * @description 授权服务器配置
 * @author Mr.M
 * @date 2022/9/26 22:25
 * @version 1.0
 */
 @Configuration
 @EnableAuthorizationServer
 public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

  //自己实现的Token配置，令牌管理服务
  @Resource(name="authorizationServerTokenServicesCustom")
  private AuthorizationServerTokenServices authorizationServerTokenServices;


  //spring security默认的身份认证管理器
 @Autowired
 private AuthenticationManager authenticationManager;

  //客户端详情服务
  @Override
  public void configure(ClientDetailsServiceConfigurer clients)
          throws Exception {
   //因为不需要动态修改，直接在内存中配置了。
        clients.inMemory()// 使用in-memory存储
                .withClient("XcWebApp")// client_id
//                .secret("XcWebApp")//客户端密钥
                .secret(new BCryptPasswordEncoder().encode("XcWebApp"))//客户端密钥
                .resourceIds("xuecheng-plus")//资源列表
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")// 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
                .scopes("all")// 允许的授权范围
                .autoApprove(false)//false跳转到授权页面，它可以控制客户端在登录后是否绕过批准询问 (/oauth/confirm_access)。如果设置为true，就不会弹出授权页面，直接跳转到客户端的回调地址.
                //客户端接收授权码的重定向地址
                .redirectUris("http://www.xuecheng-plus.com")
   ;
  }


  //令牌端点的访问配置
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
   endpoints
           .authenticationManager(authenticationManager)//认证管理器
           .tokenServices(authorizationServerTokenServices)//令牌管理服务
           .allowedTokenEndpointRequestMethods(HttpMethod.POST);
  }

  //令牌端点的安全配置
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security){
   security
           .tokenKeyAccess("permitAll()")                    //oauth/token_key是公开
           .checkTokenAccess("permitAll()")                  //oauth/check_token公开
           .allowFormAuthenticationForClients()				//表单认证（申请令牌）
   ;
  }

 }
