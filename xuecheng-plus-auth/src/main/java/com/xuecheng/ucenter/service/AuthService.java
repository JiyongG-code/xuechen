package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;

public interface AuthService {

    /***
    * @description TODO 认证方法
    * @param authParamsDto  认证参数
    * @return com.xuecheng.ucenter.model.dto.XcUserExt
    * @author J1320
    * @date 2023/3/4 13:19
    */

    XcUserExt execute(AuthParamsDto authParamsDto);
}
