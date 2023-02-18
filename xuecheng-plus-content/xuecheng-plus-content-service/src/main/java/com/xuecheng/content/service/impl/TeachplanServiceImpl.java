package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
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
}
