-- 课堂信息管理系统数据库初始化脚本
-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS classroom_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE classroom_db;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像路径',
    role TINYINT DEFAULT 2 COMMENT '角色: 0-管理员, 1-教师, 2-学生',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 班级表
CREATE TABLE IF NOT EXISTS class_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    class_name VARCHAR(100) NOT NULL COMMENT '班级名称',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    major VARCHAR(100) COMMENT '专业',
    student_count INT DEFAULT 0 COMMENT '学生人数',
    teacher_id BIGINT COMMENT '班主任ID',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_grade (grade),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级表';

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(50) NOT NULL UNIQUE COMMENT '课程代码',
    credit DECIMAL(3,1) COMMENT '学分',
    hours INT COMMENT '学时',
    teacher_id BIGINT COMMENT '授课教师ID',
    description TEXT COMMENT '课程描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_course_code (course_code),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- 课堂活动表
CREATE TABLE IF NOT EXISTS class_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    title VARCHAR(200) NOT NULL COMMENT '活动标题',
    content TEXT COMMENT '活动内容',
    class_id BIGINT COMMENT '班级ID',
    course_id BIGINT COMMENT '课程ID',
    creator_id BIGINT COMMENT '创建者ID',
    activity_type VARCHAR(20) DEFAULT 'other' COMMENT '活动类型: lecture/experiment/discussion/exam/other',
    activity_date DATETIME COMMENT '活动日期',
    attachment VARCHAR(255) COMMENT '附件路径',
    attachment_name VARCHAR(100) COMMENT '附件名称',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_class_id (class_id),
    INDEX idx_course_id (course_id),
    INDEX idx_creator_id (creator_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_activity_date (activity_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课堂活动表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value VARCHAR(500) COMMENT '配置值',
    description VARCHAR(200) COMMENT '配置说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    module VARCHAR(50) COMMENT '模块',
    operation VARCHAR(50) COMMENT '操作',
    method VARCHAR(200) COMMENT '方法',
    params TEXT COMMENT '参数',
    ip VARCHAR(50) COMMENT 'IP地址',
    duration BIGINT COMMENT '耗时(ms)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
    error_msg VARCHAR(500) COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_module (module),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 初始化管理员账户 (密码: 123456, BCrypt加密)
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 0, 1),
('teacher', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张老师', 1, 1),
('user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李明', 2, 1);

-- 初始化班级数据
INSERT INTO class_info (class_name, grade, major, student_count, teacher_id, description, status) VALUES
('软件工程1班', '2024级', '软件工程', 45, 2, '2024级软件工程专业1班', 1),
('软件工程2班', '2024级', '软件工程', 42, 2, '2024级软件工程专业2班', 1),
('计算机科学1班', '2024级', '计算机科学与技术', 48, 2, '2024级计算机科学与技术专业1班', 1),
('人工智能1班', '2023级', '人工智能', 38, 2, '2023级人工智能专业1班', 1);

-- 初始化课程数据
INSERT INTO course (course_name, course_code, credit, hours, teacher_id, description, status) VALUES
('Java程序设计', 'CS101', 4.0, 64, 2, 'Java语言基础与面向对象编程', 1),
('数据结构与算法', 'CS102', 4.0, 64, 2, '常用数据结构和算法设计', 1),
('数据库原理', 'CS201', 3.5, 56, 2, '关系型数据库原理与SQL', 1),
('Web开发技术', 'CS301', 3.0, 48, 2, '前后端开发技术栈', 1),
('人工智能导论', 'AI101', 3.0, 48, 2, '人工智能基础概念与应用', 1);

-- 初始化课堂活动数据
INSERT INTO class_activity (title, content, class_id, course_id, creator_id, activity_type, activity_date, status) VALUES
('Java期中考试', 'Java程序设计期中测验，涵盖第1-6章内容', 1, 1, 2, 'exam', DATE_ADD(NOW(), INTERVAL 7 DAY), 1),
('数据结构实验1', '链表的实现与操作实验', 1, 2, 2, 'experiment', DATE_ADD(NOW(), INTERVAL 3 DAY), 1),
('数据库设计讨论', '小组讨论数据库设计规范化问题', 2, 3, 2, 'discussion', DATE_ADD(NOW(), INTERVAL 5 DAY), 1),
('AI技术前沿讲座', '邀请业界专家分享AI最新发展趋势', 4, 5, 2, 'lecture', DATE_ADD(NOW(), INTERVAL 10 DAY), 1),
('Web项目答辩', 'Web开发课程期末项目答辩', 3, 4, 2, 'other', DATE_ADD(NOW(), INTERVAL 14 DAY), 1);

-- 初始化系统配置
INSERT INTO system_config (config_key, config_value, description) VALUES
('system_name', '课堂信息管理系统', '系统名称'),
('system_version', '1.0.0', '系统版本'),
('max_upload_size', '52428800', '最大上传文件大小(字节)'),
('log_retention_days', '90', '日志保留天数'),
('allow_register', 'true', '是否允许用户注册');
