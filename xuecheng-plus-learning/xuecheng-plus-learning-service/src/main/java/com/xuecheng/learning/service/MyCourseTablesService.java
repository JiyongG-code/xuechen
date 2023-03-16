package com.xuecheng.learning.service;

import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcCourseTables;

/***
* @description 我的课程表service
* @param null
* @return
* @author J1320
* @date 2023/3/7 13:44
*/

public interface MyCourseTablesService {


    /***
    * @description 添加选课
    * @param userId 用户id
     * @param courseId 课程id
    * @return com.xuecheng.learning.model.dto.XcChooseCourseDto
    * @author J1320
    * @date 2023/3/7 13:50
    */

    public XcChooseCourseDto addChooseCourse(String userId,Long courseId);



    /***
    * @description 判断学习资格
    * @param userId
     * @param courseId
    * @return com.xuecheng.learning.model.dto.XcCourseTablesDto
     * 学习资格状态 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
    * @author J1320
    * @date 2023/3/7 17:09
    */
    public XcCourseTablesDto getLeanringStatus(String userId,Long courseId);

    /**
     * 保存选课成功状态
     * @param chooseCourseId
     * @return
     */
    public boolean saveChooseCourseSuccess(String chooseCourseId);


    public PageResult<XcCourseTables> mycoursetables(MyCourseTableParams params);

}
