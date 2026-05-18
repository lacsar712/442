package com.classroom.controller;

import com.classroom.common.Result;
import com.classroom.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 文件管理控制器
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = fileService.upload(file);
        return Result.success("上传成功", result);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(
            @RequestParam String filePath,
            @RequestParam(required = false) String fileName) {
        
        InputStream inputStream = fileService.getFileStream(filePath);
        
        String downloadName = fileName != null ? fileName : filePath.substring(filePath.lastIndexOf('/') + 1);
        String encodedName = URLEncoder.encode(downloadName, StandardCharsets.UTF_8);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedName + "\"");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    /**
     * 删除文件
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam String filePath) {
        fileService.delete(filePath);
        return Result.success("删除成功", null);
    }
}
