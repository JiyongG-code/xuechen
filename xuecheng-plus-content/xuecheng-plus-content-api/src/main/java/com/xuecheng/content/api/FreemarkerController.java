package com.xuecheng.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/26 16:37
 */
@Controller
public class FreemarkerController {

    @GetMapping("/testfreemarker")
    public ModelAndView test(){

        ModelAndView modelAndView = new ModelAndView();

        //准备模型数据
        modelAndView.addObject("name","小米");

        //设置视图的名称，就是模板文件的名称（去掉扩展名)
        modelAndView.setViewName("test");

        return modelAndView;
    }

}
