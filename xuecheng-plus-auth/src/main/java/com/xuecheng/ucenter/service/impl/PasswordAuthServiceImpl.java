package com.xuecheng.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.feignclient.CheckCodeClient;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/4 13:24
 */
@Service("password_authservice")
public class  PasswordAuthServiceImpl implements AuthService {

    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CheckCodeClient checkCodeClient;

    //实现账号和密码认证
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {

        //得到验证码
        String checkcode = authParamsDto.getCheckcode();
        String checkcodekey = authParamsDto.getCheckcodekey();
        if (StringUtils.isBlank(checkcodekey)||StringUtils.isBlank(checkcode)){
            throw new RuntimeException("验证码为空");
        }
        //校验验证码，请求验证码服务进行校验
        Boolean result = checkCodeClient.verify(checkcodekey, checkcode);
        if (result==null||!result){
            throw new RuntimeException("验证码错误");
        }


        //账号
        String username = authParamsDto.getUsername();
        XcUser user = xcUserMapper.selectOne(
                new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username)
        );
        if (user==null){
            //放回空表示用户不存在
            throw  new RuntimeException("账号不存在");
        }
        //对比密码
        String passwordDB = user.getPassword();//正确得密码（加密后)
        String passwordInput = authParamsDto.getPassword();//输入的密码
        boolean matches = passwordEncoder.matches(passwordInput, passwordDB);
        if (!matches){
            throw new RuntimeException("行号或密码错误");
        }

        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(user,xcUserExt);
        //校验密码
        //取出数据库存储得正确密码
        return xcUserExt;
    }
}
