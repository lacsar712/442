# 课堂信息管理系统

基于 Spring Boot + Vue3 + LayUI + MySQL 的课堂信息管理系统，支持 Docker 一键部署。

## 🚀 功能特性

### 用户模块
- **用户管理**: 管理员/教师/学生角色管理，支持禁用/启用、删除操作
- **登录注册**: 支持用户登录、注册（可通过系统配置禁用注册功能）
- **一键填写管理员账号**: 登录页面支持快速填写管理员测试账号
- **个人中心**: 个人信息修改、密码修改

### 业务模块
- **班级管理**: 班级的增删改查、班主任分配、Excel导出
- **课程管理**: 课程信息维护、授课教师分配、课程代码唯一性校验、Excel导出
- **课堂活动**: 活动记录管理、**优化的搜索栏布局**、多条件筛选（班级/课程/类型）、**附件上传下载**、Excel导出

### 系统模块
- **系统配置**: 系统名称、版本、日志保留天数、是否允许注册（是/否）
- **操作日志**: 用户操作记录、按模块筛选、日志清理

### 仪表盘
- 统计数据概览：用户数、班级数、课程数、活动数
- 最近活动列表
- 活动类型分布图表

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0 + MyBatis
- **安全**: JWT 认证
- **工具**: Hutool、Apache POI、Lombok

### 前端
- **框架**: Vue 3 + Vue Router
- **UI**: LayUI 2.9.6 + 自定义CSS
- **HTTP**: Axios
- **构建**: Vite 5

### 部署
- Docker + Docker Compose
- Nginx 反向代理

## 📦 快速开始

### Docker 部署（推荐）

```bash
# 1. 进入项目目录

# 2. 一键启动
docker-compose up --build

# 3. 访问系统
http://localhost:3000

# 停止并清理
docker-compose down -v
```

### 本地开发

#### 后端
```bash
cd backend
mvn spring-boot:run
```

#### 前端
```bash
cd frontend
npm install
npm run dev
```

## 🔐 默认账户

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 教师 | teacher | 123456 |
| 学生 | user | 123456 |

## 📁 项目结构

```
├── backend/                 # 后端项目
│   ├── src/main/java/      # Java源码
│   │   └── com/classroom/
│   │       ├── controller/ # 控制器
│   │       ├── service/    # 服务层
│   │       ├── mapper/     # 数据访问层
│   │       ├── entity/     # 实体类
│   │       ├── dto/        # 数据传输对象
│   │       ├── common/     # 通用类
│   │       ├── config/     # 配置类
│   │       ├── filter/     # 过滤器
│   │       ├── aspect/     # 切面
│   │       ├── exception/  # 异常处理
│   │       └── util/       # 工具类
│   ├── src/main/resources/
│   │   ├── mapper/         # MyBatis XML
│   │   └── application.yml # 配置文件
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── api/            # API 接口
│   │   ├── router/         # 路由
│   │   ├── views/          # 页面组件
│   │   └── assets/         # 静态资源
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── db/
│   └── init.sql            # 数据库初始化
├── docker-compose.yml
└── README.md
```

## 🌐 API 接口

| 模块 | 接口 | 说明 |
|------|------|------|
| 认证 | POST /api/auth/login | 用户登录 |
| 认证 | POST /api/auth/register | 用户注册（受系统配置控制） |
| 用户 | GET /api/users | 用户列表 |
| 用户 | GET /api/users/profile | 个人信息 |
| 用户 | GET /api/users/teachers | 教师列表 |
| 班级 | GET /api/classes | 班级列表 |
| 班级 | GET /api/classes/export | 导出Excel |
| 课程 | GET /api/courses | 课程列表 |
| 课程 | GET /api/courses/export | 导出Excel |
| 活动 | GET /api/activities | 活动列表 |
| 活动 | GET /api/activities/statistics | 统计数据 |
| 活动 | GET /api/activities/export | 导出Excel |
| 系统 | GET /api/system/configs | 系统配置 |
| 系统 | GET /api/system/logs | 操作日志 |
| 文件 | POST /api/files/upload | 文件上传 |
| 文件 | GET /api/files/download | 文件下载 |

## ⚙️ 系统配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| system_name | 系统名称 | 课堂信息管理系统 |
| system_version | 系统版本 | 1.0.0 |
| log_retention_days | 日志保留天数 | 30 |
| allow_register | 是否允许用户注册 | true |

## 📝 开发说明

- 使用 BCrypt 加密用户密码
- JWT Token 有效期 24 小时
- 文件上传限制 50MB
- 支持图片、文档、压缩包等格式
- 个人信息修改后头部用户名实时同步

## 🎨 UI 特性

- 现代化渐变设计风格
- 响应式布局，支持移动端
- 自定义下拉选择框样式
- 操作按钮带 tooltip 提示
- LayUI 弹窗消息提示

## 📄 License

MIT License
