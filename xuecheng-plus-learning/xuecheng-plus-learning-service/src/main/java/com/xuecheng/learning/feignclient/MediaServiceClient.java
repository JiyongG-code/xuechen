package com.xuecheng.learning.feignclient;


import com.xuecheng.base.model.RestResponse;
import com.xuecheng.content.model.po.CoursePublish;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/***
* @description TODO
* @param null
* @return
* @author J1320
* @date 2023/3/13 21:47
*/
@FeignClient(value = "media-api",fallbackFactory = MediaServiceClientFallbackFactory.class)
public interface MediaServiceClient {

    @ResponseBody
    @GetMapping("/content/r/coursepublish/{courseId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable("mediaId") String mediaId);

}
