package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/16 19:44
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory {

    List childrenTreeNodes;
}
