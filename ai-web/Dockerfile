FROM openjdk:17-jre-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制生成的jar文件
COPY --from=builder /app/target/yvie-ai-0.0.1-SNAPSHOT.jar app.jar

# 暴露默认端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
