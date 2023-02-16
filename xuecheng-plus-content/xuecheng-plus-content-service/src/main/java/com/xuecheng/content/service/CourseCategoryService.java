package com.xuecheng.content.service;

import com.alibaba.fastjson.support.geo.LineString;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {
  List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
