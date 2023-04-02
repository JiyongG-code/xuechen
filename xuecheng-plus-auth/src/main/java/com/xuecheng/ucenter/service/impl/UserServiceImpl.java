package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcMenu;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/3 16:43
 */
@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {



    @Autowired
    XcMenuMapper xcMenuMapper;

    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    ApplicationContext applicationContext;



//    @Autowired
//    AuthService authService;

    /***
    * @description  查询用户消息组成用户身份信息
    * @param s AuthParamsDto类型得json数据
    * @return org.springframework.security.core.userdetails.UserDetails
    * @author J1320
    * @date 2023/3/4 13:21
    */


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        AuthParamsDto authParamsDto=null;
        try {
            //将认证参数转未AuthParamsDto类型
             authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        }catch (Exception e){
            log.info("认证请求不符合项目要求:{}",s);
        }

        //开始认证
        String authType = authParamsDto.getAuthType();
        AuthService authService = applicationContext.getBean(authType + "_authservice", AuthService.class);
        XcUserExt user = authService.execute(authParamsDto);

       return getUserPrincipal(user);

    }

    /***
    * @description 查询用户信息
    * @param user 用户id,主键
    * @return org.springframework.security.core.userdetails.UserDetails z
    * @author J1320
    * @date 2023/3/4 14:30
    */
    public UserDetails getUserPrincipal(XcUserExt user){
        //调用mapper查询数据库得到用户的权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(user.getId());
        String[] authorities ={"p1"};//默认权限
        ArrayList<String> list = new ArrayList<>();
        xcMenus.forEach(xcMenu -> {
            list.add(xcMenu.getCode());
        });
        if (list.size()>0){
            authorities = list.toArray(new String[0]);//这里的String[0]只是一个模板，不是长度。
        }
        String password = user.getPassword();
        //为了安全在令牌中不放密码
        user.setPassword(null);
        //将user对象转json
        String userString = JSON.toJSONString(user);
        //创建UserDetails对象
        UserDetails userDetails = User.withUsername(userString).password(password).authorities(authorities).build();
        return userDetails;
    }
}
