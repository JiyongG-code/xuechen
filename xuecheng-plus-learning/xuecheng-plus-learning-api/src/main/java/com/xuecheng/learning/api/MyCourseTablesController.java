package com.xuecheng.learning.api;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTablesService;
import com.xuecheng.learning.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/6 18:47
 */
@Api(value ="我的课程表接口",tags = "我的课程表接口")
@Slf4j
@RestController
public class MyCourseTablesController {

    @Autowired
    MyCourseTablesService myCourseTablesService;

    @ApiOperation("添加选课")
    @PostMapping("/choosecourse/{courseId}")
    public XcChooseCourseDto addChooseCourse(@PathVariable("courseId")Long courseId){
        //当前登录用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        //用户id
        String id = user.getId();
        //调用service添加选课
        XcChooseCourseDto xcChooseCourseDto = myCourseTablesService.addChooseCourse(id, courseId);
        return xcChooseCourseDto;
    }

    @ApiOperation("查询学习资格")
    @PostMapping("/choosecourse/learnstatus/{courseId}")
    public XcCourseTablesDto getLearnstatus(@PathVariable("courseId") Long courseId) {
        //登录用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if(user == null){
            XueChengPlusException.cast("请登录后继续选课");
        }
        String userId = user.getId();
        return  myCourseTablesService.getLeanringStatus(userId, courseId);

    }

    @ApiOperation("我的课程表")
    @GetMapping("/mycoursetable")
    public PageResult<XcCourseTables> mycoursetable(MyCourseTableParams params){
        //当前登录的用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if (user==null){
            XueChengPlusException.cast("请登录");
        }
        //用户id
        String userId = user.getId();
        params.setUserId(userId);


        PageResult<XcCourseTables> mycoursetables = myCourseTablesService.mycoursetables(params);

        return mycoursetables;
    }

}
