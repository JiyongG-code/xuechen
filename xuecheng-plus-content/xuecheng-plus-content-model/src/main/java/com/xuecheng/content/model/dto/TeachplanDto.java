package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/18 12:38
 */
@Data
public class TeachplanDto extends Teachplan {

    //关联的媒咨信息
    TeachplanMedia teachplanMedia;
    //子目录
    List<TeachplanDto> teachPlanTreeNodes;

}
