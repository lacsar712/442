package com.classroom.controller;

import com.classroom.common.PageResult;
import com.classroom.common.Result;
import com.classroom.dto.CourseDTO;
import com.classroom.entity.Course;
import com.classroom.service.CourseService;
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
 * 课程管理控制器
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取课程列表
     */
    @GetMapping
    public Result<PageResult<Course>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        PageResult<Course> result = courseService.getList(keyword, status, page, limit);
        return Result.success(result);
    }

    /**
     * 获取所有课程
     */
    @GetMapping("/all")
    public Result<List<Course>> getAll() {
        List<Course> list = courseService.getAll();
        return Result.success(list);
    }

    /**
     * 获取课程详情
     */
    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Long id) {
        Course course = courseService.getById(id);
        return Result.success(course);
    }

    /**
     * 新增课程
     */
    @PostMapping
    public Result<Void> add(@RequestBody @Validated CourseDTO courseDTO) {
        courseService.add(courseDTO);
        return Result.success("新增成功", null);
    }

    /**
     * 更新课程
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(id);
        courseService.update(courseDTO);
        return Result.success("更新成功", null);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 导出课程数据
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<Course> list = courseService.getAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("课程列表");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"课程名称", "课程代码", "学分", "学时", "授课教师", "描述", "状态", "创建时间"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        
        // 填充数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowNum = 1;
        for (Course course : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(course.getCourseName());
            row.createCell(1).setCellValue(course.getCourseCode());
            row.createCell(2).setCellValue(course.getCredit() != null ? course.getCredit() : 0);
            row.createCell(3).setCellValue(course.getHours() != null ? course.getHours() : 0);
            row.createCell(4).setCellValue(course.getTeacherName() != null ? course.getTeacherName() : "");
            row.createCell(5).setCellValue(course.getDescription() != null ? course.getDescription() : "");
            row.createCell(6).setCellValue(course.getStatus() == 1 ? "正常" : "禁用");
            row.createCell(7).setCellValue(course.getCreateTime() != null ? 
                course.getCreateTime().format(formatter) : "");
        }
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + 
            URLEncoder.encode("课程列表.xlsx", StandardCharsets.UTF_8));
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
