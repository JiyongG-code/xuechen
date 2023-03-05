package com.xuecheng.learning.api;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.LearningService;
import com.xuecheng.learning.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/13 21:38
 */

@Api(value = "学习过程管理接口",tags = "学习过程管理接口")
@Slf4j
@RestController
public class MyLearningController {

    @Autowired
    LearningService learningService;

   @ApiOperation("获取视频")
   @GetMapping("/open/learn/getvideo/{courseId}/{teachplanId}/{mediaId}")
    public RestResponse<String> getvideo(@PathVariable("courseId") Long courseId,@PathVariable("teachplanId") Long teachplanId, @PathVariable("mediaId")String mediaId){
       SecurityUtil.XcUser user = SecurityUtil.getUser();
       String id = user.getId();
       //获取视频
       RestResponse<String> video = learningService.getVideo(id, courseId, teachplanId, mediaId);
       return video;
   }



}
