package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author J1320
 * @version 1.0
 * @description TODO
 * @date 2023/3/1 12:38
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient() {
            @Override
            public String upload(MultipartFile filedata, String folder, String objectName) {
                throwable.printStackTrace();
                //降级方法
                log.debug("调用媒资管理服务上传文件时发生熔断，异常信息:{}");
                throwable.getMessage();
                return null;
            }
        };
    }
}
