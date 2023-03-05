package com.xuecheng.learning.service;

import com.xuecheng.base.model.RestResponse;

/***
* @description 在线学习相关的接口
* @param null
* @return
* @author J1320
* @date 2023/3/13 21:41
*/


public interface LearningService {


    /**
    * @description TODO 获取教学视频
    * @param userId  用户id
     * @param courseId  课程id
     * @param teachplanId  课程计划id
     * @param mediaId  视频文件id
    * @return com.xuecheng.base.model.RestResponse<java.lang.String>
    * @author J1320
    * @date 2023/3/13 21:40
    */

    public RestResponse<String> getVideo(String userId,Long courseId,Long teachplanId,String mediaId);

}
