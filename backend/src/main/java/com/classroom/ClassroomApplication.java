package com.classroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 课堂信息管理系统 - 主启动类
 * 
 * @author Classroom Management Team
 */
@SpringBootApplication
@MapperScan("com.classroom.mapper")
public class ClassroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassroomApplication.class, args);
        System.out.println("===========================================");
        System.out.println("   课堂信息管理系统启动成功！");
        System.out.println("   Classroom Management System Started!");
        System.out.println("===========================================");
    }
}
