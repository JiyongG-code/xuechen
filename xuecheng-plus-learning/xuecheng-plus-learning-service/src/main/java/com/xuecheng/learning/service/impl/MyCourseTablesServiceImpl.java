package com.xuecheng.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.mapper.XcCourseTablesMapper;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/7 13:50
 */
@Slf4j
@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService {

    @Autowired
    ContentServiceClient contentServiceClient;

    @Autowired
    MyCourseTablesServiceImpl myCourseTablesServicePoxy;

    @Autowired
    XcChooseCourseMapper chooseCourseMapper;

    @Autowired
    XcCourseTablesMapper courseTablesMapper;

    //添加选课
    @Override
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId) {

        //调用内容管理服务查询课程消息
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        //收费规则
        String charge = coursepublish.getCharge();
        XcChooseCourse xcChooseCourse=null;
        if ("201000".equals(charge)){//免费课程
            //添加免费课程
            xcChooseCourse =  addFreeCoruse(userId,coursepublish);

        }else {
            //收费课程，只能添加到选课记录表
            xcChooseCourse =myCourseTablesServicePoxy.addChargeCoruse(userId, coursepublish);
        }
        //获取用户对该课程的学习资格
        XcCourseTablesDto leanringStatus = getLeanringStatus(userId, courseId);
        //学习资格
        String learnStatus = leanringStatus.getLearnStatus();
        //构造返回对象
        XcChooseCourseDto xcChooseCourseDto = new XcChooseCourseDto();
        BeanUtils.copyProperties(xcChooseCourse,xcChooseCourseDto);
        xcChooseCourseDto.setLearnStatus(learnStatus);
        return xcChooseCourseDto;
    }
    //学习资格状态 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]


    @Override
    public XcCourseTablesDto getLeanringStatus(String userId, Long courseId) {
        //从哪里查询数据进行判断学习资格
        XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
        XcCourseTablesDto xcCourseTablesDto = new XcCourseTablesDto();
        if (xcCourseTables == null){
            xcCourseTablesDto.setLearnStatus("702002");//没有学习资格
            return xcCourseTablesDto;
        }
        //判断课程是否过期
        LocalDateTime validtimeEnd = xcCourseTables.getValidtimeEnd();
        if (LocalDateTime.now().isAfter(validtimeEnd)){//说明课程已过期
            xcCourseTablesDto.setLearnStatus("702003");//已经过去
            return xcCourseTablesDto;

        }
        //正常学习
        xcCourseTablesDto.setLearnStatus("702001");//正常学习
        return xcCourseTablesDto;

    }

    //添加收费课程
    @Transactional
    public XcChooseCourse addChargeCoruse(String userId,CoursePublish coursepublish){
        //课程id
        Long courseId = coursepublish.getId();
        //检验该课程是否添加到了选课记录表，如可已经添加则直接返回。
        LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, courseId)
                .eq(XcChooseCourse::getOrderType, "700002")//收费课程
                .eq(XcChooseCourse::getStatus, "701002");
        List<XcChooseCourse> xcChooseCourses = chooseCourseMapper.selectList(queryWrapper);
        if (xcChooseCourses!=null&& xcChooseCourses.size()>0){
            return  xcChooseCourses.get(0);
        }
        //向选课记录添加记录
        XcChooseCourse chooseCourse = new XcChooseCourse();
        chooseCourse.setCourseId(courseId);
        chooseCourse.setCourseName(coursepublish.getName());
        chooseCourse.setUserId(userId);
        chooseCourse.setCompanyId(coursepublish.getCompanyId());
        chooseCourse.setOrderType("700002");//收费课程
        chooseCourse.setCreateDate(LocalDateTime.now());
        chooseCourse.setCoursePrice(coursepublish.getPrice());
        chooseCourse.setValidDays(coursepublish.getValidDays());//有效期(天)
        chooseCourse.setStatus("701002");//待支付
        chooseCourse.setValidtimeStart(LocalDateTime.now());
        chooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(coursepublish.getValidDays()));//截止日期
        chooseCourseMapper.insert(chooseCourse);


        return chooseCourse;

    }

    @Override
    public boolean saveChooseCourseSuccess(String chooseCourseId) {
        //根据选课id查询选课表
        XcChooseCourse chooseCourse = chooseCourseMapper.selectById(chooseCourseId);
        if (chooseCourse ==null){
            log.debug("接收购买课程的消息，根据选课id从数据库找不到选课记录,选课id:{}",chooseCourseId);
            return false;
        }
        //选课状态
        String status = chooseCourse.getStatus();
        //只有当未支付时才更新未已支付
        if ("701002".equals(status)){
            //更新选课记录的状态未支付成功
            chooseCourse.setStatus("701001");
            int i = chooseCourseMapper.updateById(chooseCourse);
            if (i<=0){
                log.debug("添加选课记录失败：{}",chooseCourse);
                XueChengPlusException.cast("添加选课记录失败");
            }
            //向我的课程表插入记录
            XcCourseTables xcCourseTables = addCourseTabls(chooseCourse);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<XcCourseTables> mycoursetables(MyCourseTableParams params) {
        //用户id
        String userId = params.getUserId();
        //当前页面
        int pageNo = params.getPage();
        //每页记录数
        int size = params.getSize();

        Page<XcCourseTables> courseTablesPage = new Page<>(pageNo, size);
        LambdaQueryWrapper<XcCourseTables> lambdaQueryWrapper = new LambdaQueryWrapper<XcCourseTables>().eq(XcCourseTables::getUserId, userId);


        //查询数据
        Page<XcCourseTables> xcCourseTablesPage = courseTablesMapper.selectPage(courseTablesPage, lambdaQueryWrapper);

        //数据列表
        List<XcCourseTables> records = xcCourseTablesPage.getRecords();

        //总记录数
        long total = xcCourseTablesPage.getTotal();

        //List<T> item,long counts ,long page, long pageSize
        PageResult pageResult = new PageResult(records, total, pageNo, size);

        return pageResult;
    }

    @Transactional
    //添加免费课程，免费课程的加入选课记录表，我的课程表
    public  XcChooseCourse addFreeCoruse(String userId,CoursePublish coursePublish){
        //课程id
        Long courseId = coursePublish.getId();
        //校验该课程是否添加到了选课记录表，如果已添加则直接返回
        LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>().eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, courseId)
                .eq(XcChooseCourse::getOrderType, "700001")//免费课程
                .eq(XcChooseCourse::getStatus, "701001");
        List<XcChooseCourse> xcChooseCourses = chooseCourseMapper.selectList(queryWrapper);

        if (xcChooseCourses!=null && xcChooseCourses.size()>0){
            return xcChooseCourses.get(0);
        }
        XcChooseCourse chooseCourse = new XcChooseCourse();
        chooseCourse.setCourseId(courseId);
        chooseCourse.setCourseName(coursePublish.getName());
        chooseCourse.setUserId(userId);
        chooseCourse.setCompanyId(coursePublish.getCompanyId());
        chooseCourse.setOrderType("700001");//免费课程
        chooseCourse.setCreateDate(LocalDateTime.now());
        chooseCourse.setCoursePrice(0f);//免费课程的价格为0
        chooseCourse.setValidDays(coursePublish.getValidDays());//有效期(天）
        chooseCourse.setStatus("701001");//选课成功
        chooseCourse.setValidtimeStart(LocalDateTime.now());
        chooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(coursePublish.getValidDays()));//截至日期
        chooseCourseMapper.insert(chooseCourse);

        //向我的课程表添加记录
        XcCourseTables xcCourseTables = myCourseTablesServicePoxy.addCourseTabls(chooseCourse);

        return  chooseCourse;

    }

    /****
    * @description TODO添加到我的课程表
    * @param xcChooseCourse
    * @return com.xuecheng.learning.model.po.XcCourseTables
    * @author J1320
    * @date 2023/3/7 15:45
    */
    @Transactional
    public XcCourseTables addCourseTabls(XcChooseCourse xcChooseCourse){
        //校验该课程在我的课程表中是否存在
        //课程id
        Long courseId = xcChooseCourse.getCourseId();
        //用户id
        String userId = xcChooseCourse.getUserId();
        XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
        if (xcCourseTables!=null){
            return xcCourseTables;
        }
        //向我的课题添加记录
        XcCourseTables courseTables  = new XcCourseTables();
        //选课记录id
        Long xcChooseCourseId = xcChooseCourse.getId();
        courseTables.setChooseCourseId(xcChooseCourseId);
        courseTables.setUserId(xcChooseCourse.getUserId());
        courseTables.setCourseId(xcChooseCourse.getCourseId());
        courseTables.setCompanyId(xcChooseCourse.getCompanyId());
        courseTables.setCourseName(xcChooseCourse.getCourseName());
        courseTables.setCreateDate(LocalDateTime.now());
        courseTables.setValidtimeStart(xcChooseCourse.getValidtimeStart());

        courseTables.setValidtimeEnd(xcChooseCourse.getValidtimeEnd());
        courseTables.setCourseType(xcChooseCourse.getOrderType());
        courseTablesMapper.insert(courseTables);
        return  courseTables;
    }


    /***
    * @description 根据课程和用户查询我的课程表中某一门课程
    * @param userId
     * @param courseId
    * @return com.xuecheng.learning.model.po.XcCourseTables
    * @author J1320
    * @date 2023/3/7 15:51
    */
    public XcCourseTables getXcCourseTables(String userId, Long courseId){
        LambdaQueryWrapper<XcCourseTables> queryWrapper = new LambdaQueryWrapper<XcCourseTables>()
                .eq(XcCourseTables::getCourseId, userId);
        XcCourseTables courseTables = courseTablesMapper.selectOne(queryWrapper);
        return courseTables;


    }
}
