package com.classroom.service.impl;

import com.classroom.common.PageResult;
import com.classroom.dto.CourseDTO;
import com.classroom.entity.Course;
import com.classroom.exception.BusinessException;
import com.classroom.mapper.CourseMapper;
import com.classroom.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务实现
 */
@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Course getById(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    public PageResult<Course> getList(String keyword, Integer status, Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<Course> list = courseMapper.selectList(keyword, status, offset, limit);
        Long total = courseMapper.selectCount(keyword, status);
        return PageResult.of(page, limit, total, list);
    }

    @Override
    public List<Course> getAll() {
        return courseMapper.selectAll();
    }

    @Override
    @Transactional
    public void add(CourseDTO courseDTO) {
        // 检查课程代码是否重复
        Course existCourse = courseMapper.selectByCode(courseDTO.getCourseCode());
        if (existCourse != null) {
            throw new BusinessException("课程代码已存在");
        }
        
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        if (course.getStatus() == null) {
            course.setStatus(1);
        }
        
        courseMapper.insert(course);
        logger.info("新增课程成功: {}", course.getCourseName());
    }

    @Override
    @Transactional
    public void update(CourseDTO courseDTO) {
        Course existCourse = courseMapper.selectById(courseDTO.getId());
        if (existCourse == null) {
            throw new BusinessException("课程不存在");
        }
        
        // 检查课程代码是否重复
        Course courseByCode = courseMapper.selectByCode(courseDTO.getCourseCode());
        if (courseByCode != null && !courseByCode.getId().equals(courseDTO.getId())) {
            throw new BusinessException("课程代码已存在");
        }
        
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        
        courseMapper.update(course);
        logger.info("更新课程成功: {}", course.getCourseName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        
        courseMapper.deleteById(id);
        logger.info("删除课程成功: {}", course.getCourseName());
    }
}
