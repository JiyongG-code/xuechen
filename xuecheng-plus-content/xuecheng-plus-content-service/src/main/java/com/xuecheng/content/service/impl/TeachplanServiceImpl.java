package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/18 16:22
 */
@Service
@Slf4j
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplayTree(Long courseId) {
      return   teachplanMapper.selectTreeNodes(courseId);
    }

    //新增，修改
    @Override
    public void saveTeachplan(SaveTeachplanDto dto) {

        Long id = dto.getId();
        Teachplan teachplan = teachplanMapper.selectById(id);
        if (teachplan ==null){
            teachplan = new Teachplan();
            BeanUtils.copyProperties(dto,teachplan);
            //找到同级课程计划的数量
            int count = getTeacheplanCount(dto.getCourseId(), dto.getParentid());
           //新课程ID
            teachplan.setOrderby(count+1);


            teachplanMapper.insert(teachplan);
        }else {

            BeanUtils.copyProperties(dto,teachplan);
            //更新id
            teachplanMapper.updateById(teachplan);
        }
    }
    //计算新课程计划的orderby找到同级课程计划的数量
    private int getTeacheplanCount(Long courseId,Long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count.intValue();

    }

    @Override
    public void deleteTeachplan(Long courseId) {
        if (courseId==0||courseId==null)
            XueChengPlusException.cast("课程计划id不能为0或空");
        Teachplan teachplan = teachplanMapper.selectById(courseId);
        if (teachplan.getGrade()==1){
            LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teachplanLambdaQueryWrapper.eq(Teachplan::getParentid,courseId);
            Integer count = teachplanMapper.selectCount(teachplanLambdaQueryWrapper);
            if (count>0){
                XueChengPlusException.cast("120409","课程计划信息还有子级信息，无法操作");
            }else {
                teachplanMapper.deleteById(courseId);
            }

        }else {
            teachplanMapper.deleteById(courseId);
            LambdaQueryWrapper<TeachplanMedia> teachplanMediaLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teachplanMediaLambdaQueryWrapper.eq(TeachplanMedia::getTeachplanId,courseId);
            teachplanMediaMapper.deleteById(teachplanMediaLambdaQueryWrapper);

        }

    }

    @Override
    public void orderByTeachplan(String moveType, Long teachplanId) {
        //
        if (StringUtils.isBlank(moveType)||teachplanId<0){
            XueChengPlusException.cast("请输入合法参数");
        }
        // 获取层级和当前orderby，章节移动和小节移动的处理方式不同
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        Integer grade = teachplan.getGrade(); //1表示章节，2表示小节
        Integer orderby = teachplan.getOrderby();
        // 章节移动是比较同一课程id下的orderby
        Long courseId = teachplan.getCourseId();
        // 小节移动是比较同一章节id下的orderby
        Long parentid = teachplan.getParentid();
        if ("moveup".equals(moveType)){
            if (grade==1) {
                // 章节上移，找到上一个章节的orderby，然后与其交换orderby
                // SELECT * FROM teachplan WHERE courseId = ? AND grade = 1  AND orderby < 1 ORDER BY orderby DESC LIMIT 1
                LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
                teachplanLambdaQueryWrapper.eq(Teachplan::getGrade, 1)
                        .eq(Teachplan::getCourseId, courseId)
                        .lt(Teachplan::getOrderby, orderby)
                        .orderByDesc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(teachplanLambdaQueryWrapper);
                exchangeOrderby(teachplan, tmp);
            }else if (grade==2){
                // 小节上移
                // SELECT * FROM teachplan WHERE parentId = 268 AND orderby < 5 ORDER BY orderby DESC LIMIT 1
                LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
                teachplanLambdaQueryWrapper.eq(Teachplan::getParentid,parentid)
                        .lt(Teachplan::getOrderby,orderby)
                        .orderByDesc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(teachplanLambdaQueryWrapper);
                exchangeOrderby(teachplan,tmp);

            }
        } else if ("movedown".equals(moveType)) {
            if (grade==1){
                // 章节下移
                // SELECT * FROM teachplan WHERE courseId = 117 AND grade = 1 AND orderby > 1 ORDER BY orderby ASC LIMIT 1
                LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
                teachplanLambdaQueryWrapper.eq(Teachplan::getGrade,1)
                        .eq(Teachplan::getCourseId,courseId)
                        .gt(Teachplan::getOrderby,orderby)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(teachplanLambdaQueryWrapper);
                exchangeOrderby(teachplan,tmp);
            } else if (grade==2) {
                LambdaQueryWrapper<Teachplan> teachplanLambdaQueryWrapper = new LambdaQueryWrapper<>();
                teachplanLambdaQueryWrapper.eq(Teachplan::getParentid,parentid)
                        .gt(Teachplan::getOrderby,orderby)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(teachplanLambdaQueryWrapper);
                exchangeOrderby(teachplan,tmp);

            }

        }

    }
    private void exchangeOrderby(Teachplan teachplan,Teachplan tmp){
        if (tmp==null){
            XueChengPlusException.cast("已经到头了，不能在移动了");
        }else {
            Integer orderby = teachplan.getOrderby();
            Integer orderby1 = tmp.getOrderby();
            teachplan.setOrderby(orderby1);
            tmp.setOrderby(orderby);
            teachplanMapper.updateById(tmp);
            teachplanMapper.updateById(teachplan);
        }
    }
}
