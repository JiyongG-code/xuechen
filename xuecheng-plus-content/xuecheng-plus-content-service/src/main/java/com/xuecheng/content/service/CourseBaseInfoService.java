package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.springframework.web.bind.annotation.RequestBody;

//课程管理service
public interface CourseBaseInfoService {

    /**
    * @description 课程查询
    * @param params 分页参数
     * @param queryCourseParamsDto 查询条件
    * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
    * @author J1320
    * @date 2023/2/14 17:10
    */
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto queryCourseParamsDto);


    /***
    * @description TODO
    * @param companyId 培训机构的id
     * @param addCourseDto  新增课程的信息
    * @return 课程信息包括基本信息，营销信息
    * @author J1320
    * @date 2023/2/16 21:38
    */
    public CourseBaseInfoDto  createCourseBase(Long companyId,AddCourseDto addCourseDto);


    /***
    * @description 根据id查询课程消息
    * @param courseId 课程id
    * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
    * @author J1320
    * @date 2023/2/17 17:47
    */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    /**
     * @description 修改课程信息
     * @param companyId  机构id
     * @param dto  课程信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @author Mr.M
     * @date 2022/9/8 21:04
     */
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);
}
