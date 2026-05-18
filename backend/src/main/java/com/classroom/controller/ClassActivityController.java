package com.classroom.controller;

import com.classroom.common.PageResult;
import com.classroom.common.Result;
import com.classroom.dto.ClassActivityDTO;
import com.classroom.entity.ClassActivity;
import com.classroom.service.ClassActivityService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 课堂活动控制器
 */
@RestController
@RequestMapping("/api/activities")
public class ClassActivityController {

    @Autowired
    private ClassActivityService activityService;

    /**
     * 获取活动列表
     */
    @GetMapping
    public Result<PageResult<ClassActivity>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        PageResult<ClassActivity> result = activityService.getList(keyword, classId, courseId, 
                                                                    activityType, status, page, limit);
        return Result.success(result);
    }

    /**
     * 获取最近活动
     */
    @GetMapping("/recent")
    public Result<List<ClassActivity>> getRecent(@RequestParam(defaultValue = "5") Integer limit) {
        List<ClassActivity> list = activityService.getRecent(limit);
        return Result.success(list);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = activityService.getStatistics();
        return Result.success(stats);
    }

    /**
     * 获取活动详情
     */
    @GetMapping("/{id}")
    public Result<ClassActivity> getById(@PathVariable Long id) {
        ClassActivity activity = activityService.getById(id);
        return Result.success(activity);
    }

    /**
     * 新增活动
     */
    @PostMapping
    public Result<Void> add(@RequestBody @Validated ClassActivityDTO activityDTO, 
                            HttpServletRequest request) {
        Long creatorId = (Long) request.getAttribute("userId");
        activityService.add(activityDTO, creatorId);
        return Result.success("新增成功", null);
    }

    /**
     * 更新活动
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ClassActivityDTO activityDTO) {
        activityDTO.setId(id);
        activityService.update(activityDTO);
        return Result.success("更新成功", null);
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 导出活动数据
     */
    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long courseId,
            HttpServletResponse response) throws IOException {
        
        PageResult<ClassActivity> pageResult = activityService.getList(null, classId, courseId, 
                                                                        null, null, 1, 10000);
        List<ClassActivity> list = pageResult.getList();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("课堂活动列表");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"活动标题", "班级", "课程", "活动类型", "活动日期", "创建者", "状态", "创建时间"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        
        // 填充数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int rowNum = 1;
        for (ClassActivity activity : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(activity.getTitle());
            row.createCell(1).setCellValue(activity.getClassName() != null ? activity.getClassName() : "");
            row.createCell(2).setCellValue(activity.getCourseName() != null ? activity.getCourseName() : "");
            row.createCell(3).setCellValue(getActivityTypeName(activity.getActivityType()));
            row.createCell(4).setCellValue(activity.getActivityDate() != null ? 
                activity.getActivityDate().format(dateFormatter) : "");
            row.createCell(5).setCellValue(activity.getCreatorName() != null ? activity.getCreatorName() : "");
            row.createCell(6).setCellValue(activity.getStatus() == 1 ? "正常" : "禁用");
            row.createCell(7).setCellValue(activity.getCreateTime() != null ? 
                activity.getCreateTime().format(formatter) : "");
        }
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + 
            URLEncoder.encode("课堂活动列表.xlsx", StandardCharsets.UTF_8));
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private String getActivityTypeName(String type) {
        if (type == null) return "";
        switch (type) {
            case "lecture": return "讲座";
            case "experiment": return "实验";
            case "discussion": return "讨论";
            case "exam": return "考试";
            default: return "其他";
        }
    }
}
