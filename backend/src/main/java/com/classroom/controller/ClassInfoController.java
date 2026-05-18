package com.classroom.controller;

import com.classroom.common.PageResult;
import com.classroom.common.Result;
import com.classroom.dto.ClassInfoDTO;
import com.classroom.entity.ClassInfo;
import com.classroom.service.ClassInfoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 班级管理控制器
 */
@RestController
@RequestMapping("/api/classes")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    /**
     * 获取班级列表
     */
    @GetMapping
    public Result<PageResult<ClassInfo>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        PageResult<ClassInfo> result = classInfoService.getList(keyword, status, page, limit);
        return Result.success(result);
    }

    /**
     * 获取所有班级
     */
    @GetMapping("/all")
    public Result<List<ClassInfo>> getAll() {
        List<ClassInfo> list = classInfoService.getAll();
        return Result.success(list);
    }

    /**
     * 获取班级详情
     */
    @GetMapping("/{id}")
    public Result<ClassInfo> getById(@PathVariable Long id) {
        ClassInfo classInfo = classInfoService.getById(id);
        return Result.success(classInfo);
    }

    /**
     * 新增班级
     */
    @PostMapping
    public Result<Void> add(@RequestBody @Validated ClassInfoDTO classInfoDTO) {
        classInfoService.add(classInfoDTO);
        return Result.success("新增成功", null);
    }

    /**
     * 更新班级
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ClassInfoDTO classInfoDTO) {
        classInfoDTO.setId(id);
        classInfoService.update(classInfoDTO);
        return Result.success("更新成功", null);
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        classInfoService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 导出班级数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<ClassInfo> list = classInfoService.getAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("班级列表");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"班级名称", "年级", "专业", "学生人数", "班主任", "状态", "创建时间"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        
        // 填充数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowNum = 1;
        for (ClassInfo classInfo : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(classInfo.getClassName());
            row.createCell(1).setCellValue(classInfo.getGrade());
            row.createCell(2).setCellValue(classInfo.getMajor() != null ? classInfo.getMajor() : "");
            row.createCell(3).setCellValue(classInfo.getStudentCount() != null ? classInfo.getStudentCount() : 0);
            row.createCell(4).setCellValue(classInfo.getTeacherName() != null ? classInfo.getTeacherName() : "");
            row.createCell(5).setCellValue(classInfo.getStatus() == 1 ? "正常" : "禁用");
            row.createCell(6).setCellValue(classInfo.getCreateTime() != null ? 
                classInfo.getCreateTime().format(formatter) : "");
        }
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + 
            URLEncoder.encode("班级列表.xlsx", StandardCharsets.UTF_8));
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
