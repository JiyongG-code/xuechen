package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/18 16:19
 */
public interface TeachplanService {
    public List<TeachplanDto> findTeachplayTree(Long courseId);

    public void saveTeachplan(SaveTeachplanDto dto);

    public void deleteTeachplan(Long courseId);

    public void orderByTeachplan(String moveType, Long teachplanId);
}
