package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.mapper.XcUserRoleMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.model.po.XcUserRole;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/4 19:37
 */
@Slf4j
@Service("wx_authservice")
public class WxAuthServiceImpl implements AuthService {

   @Value("${weixin.appid}")
   String appid;

   @Value("${weixin.secret}")
   String secret;

    @Autowired
    XcUserMapper userMapper;
   @Autowired
    RestTemplate restTemplate;

   @Autowired
    XcUserRoleMapper userRoleMapper;
   @Autowired
   WxAuthServiceImpl currentProxy;

    @Autowired
    XcUserMapper xcUserMapper;

    //拿授权码申请令牌，查询用户
    public XcUser wxAuth(String code){
        //拿授权码获取获取access_token
        Map<String, String> access_token_map = getAccess_token(code);
        System.out.println(access_token_map);
        //得到令牌
        String access_token = access_token_map.get("access_token");
        //得到openid
        String openid = access_token_map.get("openid");

        //拿令牌获取用户信息
        Map<String, String> userinfo = getUserinfo(access_token, openid);

        //添加用户到数据库
        System.out.println(userinfo);
        XcUser xcUser = currentProxy.addWxUser(userinfo);


        return xcUser;
    }

    @Transactional
    public XcUser addWxUser(Map<String,String> userInfo_map){
        String unionid = userInfo_map.get("unionid");
        String nickname = userInfo_map.get("nickname");
        //根据unionid查询用户信息
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getWxUnionid, unionid));
        if (xcUser!=null){
            return  xcUser;
        }
        //向数据库新增记录
        xcUser=new XcUser();
        String userId = UUID.randomUUID().toString();
        xcUser.setId(userId);//主键
        xcUser.setUsername(unionid);
        xcUser.setPassword(unionid);
        xcUser.setWxUnionid(unionid);
        xcUser.setNickname(nickname);
        xcUser.setUtype("101001");
        xcUser.setStatus("1");
        xcUser.setCreateTime(LocalDateTime.now());
        //插入
        int insert = xcUserMapper.insert(xcUser);

        //向用户角色关系表新增记录
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(UUID.randomUUID().toString());
        xcUserRole.setUserId(userId);
        xcUserRole.setRoleId("17");//学生角色
        xcUserRole.setCreateTime(LocalDateTime.now());
        userRoleMapper.insert(xcUserRole);
        return xcUser;


    }



    //微信认证方法
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        //获取账号
        String username = authParamsDto.getUsername();
        XcUser xcUser = userMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername,username));
        if(xcUser==null){
            throw new RuntimeException("用户不存在");
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        return xcUserExt;
    }

    //请求微信获取令牌

    /**
     * 微信接口响应结果
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     */
    private Map<String,String> getAccess_token(String code){
        String url_template="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        String url = String.format(url_template, appid, secret, code);
        //请求微信获取令牌
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        System.out.println(response);
        //得到响应串
        String responseString = response.getBody();
        Map map = JSON.parseObject(responseString, Map.class);
        return map;
    }
    //携带令牌查询用户信息
    //http请求方式: GET
    //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
    /**
     {
     "openid":"OPENID",
     "nickname":"NICKNAME",
     "sex":1,
     "province":"PROVINCE",
     "city":"CITY",
     "country":"COUNTRY",
     "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     "privilege":[
     "PRIVILEGE1",
     "PRIVILEGE2"
     ],
     "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

     }
     */

    private Map<String,String> getUserinfo(String access_token,String openid){
        //请求微信查询用户信息
        String url_template="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String url = String.format(url_template, access_token, openid);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String body = response.getBody();

        //将结果转为map
        Map map = JSON.parseObject(body, Map.class);
        return map;
    }
}
