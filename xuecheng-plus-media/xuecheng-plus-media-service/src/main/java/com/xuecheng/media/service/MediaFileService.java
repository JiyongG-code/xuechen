package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.RestResponse;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

 /**
  * @description 媒资文件查询方法
  * @param pageParams 分页参数
  * @param queryMediaParamsDto 查询条件
  * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
  * @author Mr.M
  * @date 2022/9/10 8:57
 */
 public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

/***
* @description TODO
* @param companyId jigouID
 * @param uploadFileParamsDto 文件信息
 * @param bytes 文件字节数组
 * @param folder  桶下面的子目录
 * @param objectName  对象名
* @return com.xuecheng.media.model.dto.UploadFileResultDto
* @author J1320
* @date 2023/2/20 19:39
*/
 //上传文件的通用接口
 public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);

 //上传到数据库
 public MediaFiles addMediaFilesToDb(Long companyId,String fileId,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName);


/**
* @description 检查文件是否存在
* @param fileMd5  文件的md5
* @return com.xuecheng.media.model.dto.RestResponse<java.lang.Boolean>
* @author J1320
* @date 2023/2/22 18:40
*/
 public RestResponse<Boolean> checkFile(String fileMd5);

 /**
 * @description 检查分块是否存在
 * @param filedMd5 文件的md5
  * @param chunkIndex 分块序号
 * @return com.xuecheng.media.model.dto.RestResponse<java.lang.Boolean>
 * @author J1320
 * @date 2023/2/23 11:54
 */
 public RestResponse<Boolean> checkChunk(String filedMd5,int chunkIndex);



 /***
 * @description 上传分块
 * @param fileMd5  文件md5
  * @param chunk  分块序号
  * @param bytes  文件字节
 * @return com.xuecheng.media.model.dto.RestResponse
 * @author J1320
 * @date 2023/2/23 13:14
 */
 public RestResponse uploadChunk(String fileMd5,int chunk,byte[] bytes);


 /***
 * @description TODO
 * @param companyId
  * @param fileMd5
  * @param chunkTotal
  * @param uploadFileParamsDto
 * @return com.xuecheng.media.model.dto.RestResponse
 * @author J1320
 * @date 2023/2/23 13:36
 */

 public RestResponse mergechunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);

 /***
 * @description 根据id查询文件信息
 * @param id 文件id
 * @return com.xuecheng.media.model.po.MediaFiles 文件信息
 * @author J1320
 * @date 2023/2/23 21:14
 */
 public MediaFiles getFileById(String id);
}

