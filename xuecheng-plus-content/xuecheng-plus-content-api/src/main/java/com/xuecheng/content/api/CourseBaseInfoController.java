package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "课程管理相关的接口",tags = "课程管理相关的接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    CourseBaseInfoService  courseBaseInfoService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    @PreAuthorize("hasAuthority('course_find_list')")
    public PageResult<CourseBase> list(PageParams params, @RequestBody QueryCourseParamsDto queryCourseParamsDto){
        //获取用户身份
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        //获取用户所属的机构id
        String companyId = user.getCompanyId();



        //调用service获取数据
        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(params, queryCourseParamsDto);
        return courseBasePageResult;
    }
    @ApiOperation("新增课程")
    @PostMapping("/course")
//    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Inster.class) AddCourseDto addCourseDto){
//     如果指定分组，所有的需要验证的属性都必须添加指定分组才会校验，没有指明分组的属性都属于Default，所以分组接口继承Default就可以解决
      public CourseBaseInfoDto createCourseBase(@RequestBody  AddCourseDto addCourseDto){
        //获取当前用户所属培训机构的id
        Long companyId =1232141425L;
        //调用service
        CourseBaseInfoDto courseBase = courseBaseInfoService.createCourseBase(companyId, addCourseDto);
        return courseBase;
    }

    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId){

        //取出当前用户身份
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //将principal转成对象
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        System.out.println(user);
        return  courseBaseInfoService.getCourseBaseInfo(courseId);
    }

    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody EditCourseDto editCourseDto){
        Long companyId =1232141425L;
        return courseBaseInfoService.updateCourseBase(companyId,editCourseDto);
    }
}
