package com.classroom.service.impl;

import com.classroom.common.PageResult;
import com.classroom.dto.ClassActivityDTO;
import com.classroom.entity.ClassActivity;
import com.classroom.exception.BusinessException;
import com.classroom.mapper.ClassActivityMapper;
import com.classroom.mapper.ClassInfoMapper;
import com.classroom.mapper.CourseMapper;
import com.classroom.mapper.UserMapper;
import com.classroom.service.ClassActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课堂活动服务实现
 */
@Service
public class ClassActivityServiceImpl implements ClassActivityService {

    private static final Logger logger = LoggerFactory.getLogger(ClassActivityServiceImpl.class);

    @Autowired
    private ClassActivityMapper activityMapper;
    
    @Autowired
    private ClassInfoMapper classInfoMapper;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public ClassActivity getById(Long id) {
        return activityMapper.selectById(id);
    }

    @Override
    public PageResult<ClassActivity> getList(String keyword, Long classId, Long courseId, 
                                              String activityType, Integer status, 
                                              Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<ClassActivity> list = activityMapper.selectList(keyword, classId, courseId, 
                                                              activityType, status, offset, limit);
        Long total = activityMapper.selectCount(keyword, classId, courseId, activityType, status);
        return PageResult.of(page, limit, total, list);
    }

    @Override
    public List<ClassActivity> getRecent(Integer limit) {
        return activityMapper.selectRecent(limit);
    }

    @Override
    @Transactional
    public void add(ClassActivityDTO activityDTO, Long creatorId) {
        ClassActivity activity = new ClassActivity();
        BeanUtils.copyProperties(activityDTO, activity);
        activity.setCreatorId(creatorId);
        if (activity.getStatus() == null) {
            activity.setStatus(1);
        }
        
        activityMapper.insert(activity);
        logger.info("新增课堂活动成功: {}", activity.getTitle());
    }

    @Override
    @Transactional
    public void update(ClassActivityDTO activityDTO) {
        ClassActivity existActivity = activityMapper.selectById(activityDTO.getId());
        if (existActivity == null) {
            throw new BusinessException("课堂活动不存在");
        }
        
        ClassActivity activity = new ClassActivity();
        BeanUtils.copyProperties(activityDTO, activity);
        
        activityMapper.update(activity);
        logger.info("更新课堂活动成功: {}", activity.getTitle());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ClassActivity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("课堂活动不存在");
        }
        
        activityMapper.deleteById(id);
        logger.info("删除课堂活动成功: {}", activity.getTitle());
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 用户统计
        Long totalUsers = userMapper.selectCount(null, null, null);
        Long teacherCount = userMapper.selectCount(null, null, 1);
        Long studentCount = userMapper.selectCount(null, null, 2);
        
        // 班级统计
        Long classCount = classInfoMapper.selectCount(null, null);
        
        // 课程统计
        Long courseCount = courseMapper.selectCount(null, null);
        
        // 活动统计
        Long activityCount = activityMapper.selectCount(null, null, null, null, null);
        
        stats.put("totalUsers", totalUsers);
        stats.put("teacherCount", teacherCount);
        stats.put("studentCount", studentCount);
        stats.put("classCount", classCount);
        stats.put("courseCount", courseCount);
        stats.put("activityCount", activityCount);
        
        return stats;
    }
}
