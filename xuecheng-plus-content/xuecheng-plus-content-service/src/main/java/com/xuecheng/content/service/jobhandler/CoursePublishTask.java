package com.xuecheng.content.service.jobhandler;

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/2/28 20:40
 */

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

    //课程发布任务执行入口，由xxl-job调度
    @XxlJob("CoursePublishJobHandler")
    public void coursePublishJobHandler() throws Exception{
        //分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        //参数:分片序号、分片总数、消息类型、一次最多取到的任务数量、一次任务调度执行的超时时间
        process(shardIndex,shardTotal,"course_publish",5,60);
    }
    //课程发布执行逻辑
    @Override
    public boolean execute(MqMessage mqMessage) {
        log.debug("开始执行课程发布任务，课程id：{}",mqMessage.getBusinessKey1());

        //将课程消息进行静态化....

        //将静态页面上传到minIo
        return false;
    }
}
