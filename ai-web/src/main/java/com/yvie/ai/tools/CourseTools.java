package com.yvie.ai.tools;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.yvie.ai.entity.po.Course;
import com.yvie.ai.entity.po.CourseReservation;
import com.yvie.ai.entity.po.School;
import com.yvie.ai.entity.query.CourseQuery;
import com.yvie.ai.service.ICourseReservationService;
import com.yvie.ai.service.ICourseService;
import com.yvie.ai.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component // 该类是一个工具类，用于提供工具方法，该类会被Spring容器管理
public class CourseTools {

    private final ICourseService courseService;
    private final ISchoolService schoolService;
    private final ICourseReservationService reservationService;

    @Tool(description = "Query courses by conditions")
    public List<Course> queryCourse(@ToolParam(description = "Query conditions") CourseQuery query) { // query来源于前端
        if (query == null) {
            return courseService.list();
        }
        QueryChainWrapper<Course> wrapper = courseService.query()
                .eq(query.getType() != null, "type", query.getType()) // 课程类型 = query.getType()
                .le(query.getEdu() != null, "edu", query.getEdu()); // 学历 <= query.getEdu()
        if (query.getSorts() != null && !query.getSorts().isEmpty()) {
            for (CourseQuery.Sort sort : query.getSorts()) {
                wrapper.orderBy(true, sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    @Tool(description = "Query all campuses")
    public List<School> querySchool() {
        return schoolService.list();
    }

    @Tool(description = "Create reservation and return reservation ID")
    public Integer createCourseReservation(
            @ToolParam(description = "Reserved course") String course,
            @ToolParam(description = "Reserved campus") String school,
            @ToolParam(description = "Student name") String studentName,
            @ToolParam(description = "Contact number") String contactInfo,
            @ToolParam(description = "Remarks", required = false) String remark) {
        CourseReservation reservation = new CourseReservation()
                .setCourse(course)
                .setSchool(school)
                .setStudentName(studentName)
                .setContactInfo(contactInfo)
                .setRemark(remark);
        reservationService.save(reservation);

        return reservation.getId();
    }
}
