package com.classroom.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * 文件服务接口
 */
public interface FileService {
    
    /**
     * 上传文件
     */
    Map<String, String> upload(MultipartFile file);
    
    /**
     * 获取文件流
     */
    InputStream getFileStream(String filePath);
    
    /**
     * 删除文件
     */
    void delete(String filePath);
}
