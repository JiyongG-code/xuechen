package com.xuecheng.media;

import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

/**
 * @author J1320
 * @version 1.0
 * @description TODO minIO上传文件，删除文件，查询文件
 * @date 2023/2/20 15:17
 */
public class MinIoTest {
    static MinioClient minioClient=
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin","minioadmin")
                    .build();

    @Test
    public  void upload(){
        try {
            UploadObjectArgs uploadObjectArgs= UploadObjectArgs.builder()
                    .bucket("testbucket1")
                    .object("1.mp4")
                    .filename("C:\\Users\\J1320\\Downloads\\1.mp4")
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功");
        }catch (Exception e){
            System.out.println("上传失败");
        }
    }
    //子目录
    @Test
    public  void upload2(){
        try {
            UploadObjectArgs uploadObjectArgs= UploadObjectArgs.builder()
                    .bucket("testbucket1")
                    .object("test/1.mp4")
                    .filename("C:\\Users\\J1320\\Downloads\\1.mp4")
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功");
        }catch (Exception e){
            System.out.println("上传失败");
        }
    }
    @Test
    public  void delete(){
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket("testbucket1").object("1.mp4").build();

           minioClient.removeObject(removeObjectArgs);
            System.out.println("上传成功");
        }catch (Exception e){
            System.out.println("上传失败");
        }
    }
    @Test
    public  void getFile(){
        GetObjectArgs testbucket1 = GetObjectArgs.builder().bucket("testbucket1").object("1.mp4").build();
        try(FilterInputStream inputStream = minioClient.getObject(testbucket1);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\J1320\\Downloads\\1-1.mp4"));) {
            if (inputStream!=null){
                IOUtils.copy(inputStream,fileOutputStream);
            }
            System.out.println("上传成功");
        }catch (Exception e){
            System.out.println("上传失败");
        }
    }

}
