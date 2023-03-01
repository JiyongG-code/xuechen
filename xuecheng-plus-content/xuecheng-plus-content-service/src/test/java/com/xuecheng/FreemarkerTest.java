package com.xuecheng;

import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CoursePublishService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/28 22:12
 */
@SpringBootTest
public class FreemarkerTest {

    @Autowired
    CoursePublishService coursePublishService;

    //测试页面静态化
    @Test
    public void testGenerateHtmlByTemplate() throws IOException, TemplateException{
        //配置freemarker
        Configuration configuration = new Configuration(Configuration.getVersion());
        //加载模板
        //选指定模板路径，classpath下templates下
        //得到classpath路径
        String classpath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(
                classpath+"/templates"
        ));
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");
        //指定模板文件名称
        Template template = configuration.getTemplate("course_template.ftl");

        //准备数据
       CoursePreviewDto coursePreviewInfo  = coursePublishService.getCoursePreviewInfo(18L);

        HashMap<String, Object> map = new HashMap<>();
        map.put("model",coursePreviewInfo);

        //静态化
        //参数1:模板，参数2:数据模型
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(content);
        //将静态化内容输出到文件中
        InputStream inputStream = IOUtils. toInputStream(content, (Charset) null);
        //输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\develop\\test.html");
        IOUtils.copy(inputStream,fileOutputStream);

    }

}
