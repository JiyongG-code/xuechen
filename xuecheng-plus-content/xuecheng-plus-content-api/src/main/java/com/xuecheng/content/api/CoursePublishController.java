package com.xuecheng.content.api;

import com.alibaba.fastjson.JSON;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO 课程发布相关接口
 * @date 2023/2/26 18:27
 */
@Controller
public class CoursePublishController {

    @Autowired
    CoursePublishService coursePublishService;
    @ResponseBody
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getCoursePublish(@PathVariable("courseId")Long courseId){
        //封装数据
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        //查询课程发布表
//        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
        //先从缓存查询，缓存中有就返回，没有再查询数据库
        CoursePublish coursePublish = coursePublishService.getCoursePublishCache(courseId);
        if (coursePublish==null){
            return coursePreviewDto;
        }
        //开始向coursePreviewDto填充相互据
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(coursePublish,courseBaseInfoDto);

        //课程计划信息
        String teachplan = coursePublish.getTeachplan();
        //转为List<TeachplanDto>
        List<TeachplanDto> teachplanDtos = JSON.parseArray(teachplan, TeachplanDto.class);
        coursePreviewDto.setCourseBase(courseBaseInfoDto);
        coursePreviewDto.setTeachplans(teachplanDtos);
        return coursePreviewDto;


    }



    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId){

        //查询数据
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);

        ModelAndView modelAndView = new ModelAndView();
        ModelAndView model = modelAndView.addObject("model", coursePreviewInfo);
        modelAndView.setViewName("course_template");


        return modelAndView;
    }
    //提交审核
    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId){
        Long companyId=1232141425L;
        coursePublishService.commitAudit(companyId,courseId);

    }

    @ApiOperation("课程发布")
    @ResponseBody
    @PostMapping("/coursepublish/{courseId}")
    public void coursepublish(@PathVariable("courseId")Long courseId){
        Long companyId=1232141425L;
        coursePublishService.publish(companyId,courseId);
    }

    @ApiOperation("查询课程发布消息")
    @ResponseBody
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursepublish(@PathVariable("courseId") Long courseId){
       CoursePublish   coursepublish = coursePublishService.getCoursePublish(courseId);
       if (coursepublish==null){
           return null;
       }
       //课程发布状态
        String status = coursepublish.getStatus();
       if (status.equals("203002")){
           return coursepublish;
       }

       //课程下线返回null
        return null;
    }

}
