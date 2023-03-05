package com.xuecheng.auth.controller;

import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.impl.WxAuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/4 19:23
 */
@Controller
public class WxLoginController {

    @Autowired
    WxAuthServiceImpl wxAuthService;

    @RequestMapping("/wxLogin")
    public String wxLogin(String code,String state ) throws IOException{

        //拿授权码申请令牌，查询用户
        XcUser xcUser = wxAuthService.wxAuth(code);
        if(xcUser ==null){
            //重定向到一个错误页面
            return "redirect:http://www.xuecheng-plus.com/error.htlm";
        }else {
            String username = xcUser.getUsername();
            return "redirect:http://www.xuecheng-plus.com/sign.html?username="+username+"&authType=wx";

        }

    }


}
