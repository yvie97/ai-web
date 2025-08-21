package com.yvie.ai.entity.query;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

@Data // 该类用于封装查询条件
public class CourseQuery {
    @ToolParam(required = false, description = "Course type: Programming, Design, Social Media, Others")
    private String type;
    @ToolParam(required = false, description = "Education requirement: 0-None, 1-Junior high, 2-High school, 3-College, 4-Bachelor or above")
    private Integer edu;
    @ToolParam(required = false, description = "Sort order")
    private List<Sort> sorts;

    @Data
    public static class Sort {
        @ToolParam(required = false, description = "Sort field: price or duration")
        private String field;
        @ToolParam(required = false, description = "Is ascending order: true/false")
        private Boolean asc;
    }
}