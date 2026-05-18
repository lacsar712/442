# 构建阶段
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
# 拷贝加速配置
COPY settings.xml /usr/share/maven/ref/settings.xml
COPY pom.xml .
# 预下载依赖
RUN mvn -s /usr/share/maven/ref/settings.xml dependency:go-offline -B
COPY src ./src
# 打包
RUN mvn -s /usr/share/maven/ref/settings.xml package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:17-jre
WORKDIR /app
# 创建上传目录
RUN mkdir -p /app/uploads
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "-Dfile.encoding=UTF-8", "-Dsun.jnu.encoding=UTF-8", "app.jar"]
