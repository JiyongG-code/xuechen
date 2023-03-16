package com.xuecheng.learning;

import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.model.dto.MyCourseTableItemDto;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/10/2 10:32
 * @version 1.0
 */
 @SpringBootTest
public class Test1 {
  @Autowired
 ContentServiceClient contentServiceClient;

  @Test
 public void test(){
   CoursePublish coursepublish = contentServiceClient.getCoursepublish(2L);
   System.out.println(coursepublish);
  }

}
