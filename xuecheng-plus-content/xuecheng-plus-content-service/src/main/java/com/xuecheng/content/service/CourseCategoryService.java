package com.xuecheng.content.service;

import com.alibaba.fastjson.support.geo.LineString;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

public interface CourseCategoryService {

  public List<CourseCategoryTreeDto> queryTreeNodes(String id);
//  public List<CourseCategoryTreeDto> queryTreeNodes();
}
