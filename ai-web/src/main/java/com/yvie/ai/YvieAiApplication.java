package com.yvie.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yvie.ai.mapper") // 扫描mapper接口，实现数据库的增删改查
@SpringBootApplication
public class YvieAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YvieAiApplication.class, args);
    }

}
