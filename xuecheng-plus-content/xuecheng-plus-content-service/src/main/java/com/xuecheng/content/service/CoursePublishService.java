package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/27 15:14
 */

public interface CoursePublishService {


    /***
    * @description 获取课程预览信息
    * @param courseId  课程id
    * @return com.xuecheng.content.model.dto.CoursePreviewDto
    * @author J1320
    * @date 2023/2/27 15:16
    */

    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /***
    * @description TODO 提交审核
    * @param companyId  课程id
     * @param courseId
    * @return   void
    * @author J1320
    * @date 2023/2/27 18:33
    */
    public void commitAudit(Long companyId,Long courseId);


    /***
    * @description TODO 课程发布接口
    * @param companyId 机构id
     * @param courseId 课程id
    * @return void
    * @author J1320
    * @date 2023/2/28 17:49
    */
    public void publish(Long companyId,Long courseId);

    /***
    * @description TODO 课程静态化
    * @param courseId  课程id
    * @return java.io.File 静态化文件
    * @author J1320
    * @date 2023/3/1 13:11
    */
    public File generateCourseHtml(Long courseId);

    /***
    * @description TODO 上传课程静态化页面
    * @param file 静态化文件
    * @return
    * @author J1320
    * @date 2023/3/1 13:11
    */
    public void uploadCourseHtml(Long courseId, File file);


    //创建索引
    public Boolean saveCourseIndex(Long courseId) ;

    public CoursePublish getCoursePublish(Long courseId);
    public CoursePublish getCoursePublishCache(Long courseId);
}
