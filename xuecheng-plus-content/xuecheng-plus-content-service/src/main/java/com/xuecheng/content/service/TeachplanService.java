package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/18 16:19
 */
public interface TeachplanService {
    public List<TeachplanDto> findTeachplanTree(Long courseId);

    public void saveTeachplan(SaveTeachplanDto dto);

    public void deleteTeachplan(Long courseId);

    public void orderByTeachplan(String moveType, Long teachplanId);

    /***
    * @description 教学计划绑定媒资
    * @param bindTeachplanMediaDto
    * @return com.xuecheng.content.model.po.TeachplanMedia
    * @author J1320
    * @date 2023/2/26 15:13
    */
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);
}
