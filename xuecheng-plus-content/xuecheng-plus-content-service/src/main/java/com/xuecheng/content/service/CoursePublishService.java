package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import org.springframework.stereotype.Service;

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
}
