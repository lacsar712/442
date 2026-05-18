package com.classroom.service.impl;

import com.classroom.common.PageResult;
import com.classroom.dto.ClassInfoDTO;
import com.classroom.entity.ClassInfo;
import com.classroom.exception.BusinessException;
import com.classroom.mapper.ClassInfoMapper;
import com.classroom.service.ClassInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级服务实现
 */
@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ClassInfoServiceImpl.class);

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Override
    public ClassInfo getById(Long id) {
        return classInfoMapper.selectById(id);
    }

    @Override
    public PageResult<ClassInfo> getList(String keyword, Integer status, Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<ClassInfo> list = classInfoMapper.selectList(keyword, status, offset, limit);
        Long total = classInfoMapper.selectCount(keyword, status);
        return PageResult.of(page, limit, total, list);
    }

    @Override
    public List<ClassInfo> getAll() {
        return classInfoMapper.selectAll();
    }

    @Override
    @Transactional
    public void add(ClassInfoDTO classInfoDTO) {
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(classInfoDTO, classInfo);
        if (classInfo.getStatus() == null) {
            classInfo.setStatus(1);
        }
        if (classInfo.getStudentCount() == null) {
            classInfo.setStudentCount(0);
        }
        
        classInfoMapper.insert(classInfo);
        logger.info("新增班级成功: {}", classInfo.getClassName());
    }

    @Override
    @Transactional
    public void update(ClassInfoDTO classInfoDTO) {
        ClassInfo existClass = classInfoMapper.selectById(classInfoDTO.getId());
        if (existClass == null) {
            throw new BusinessException("班级不存在");
        }
        
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(classInfoDTO, classInfo);
        
        classInfoMapper.update(classInfo);
        logger.info("更新班级成功: {}", classInfo.getClassName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ClassInfo classInfo = classInfoMapper.selectById(id);
        if (classInfo == null) {
            throw new BusinessException("班级不存在");
        }
        
        classInfoMapper.deleteById(id);
        logger.info("删除班级成功: {}", classInfo.getClassName());
    }
}
