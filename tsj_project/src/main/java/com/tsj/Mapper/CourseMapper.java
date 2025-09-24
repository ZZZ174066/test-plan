package com.tsj.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.tsj.project.Course;
import java.util.List;

@Mapper
public interface CourseMapper {
    
    // 获取所有课程
    List<Course> getCourse();
    
    // 插入课程
    void insertCourse(Course course);
    
    // 更新课程
    void updateCourse(Course course);
    
    // 删除课程
    void deleteCourse(Long courseId);
    
    // 根据ID获取课程
    Course getCourseById(Long courseId);
    
    // 根据课程代码获取课程
    Course getCourseByCourseCode(String courseCode);
    
    // 根据课程类型获取课程
    List<Course> getCoursesByType(String courseType);
    
    // 根据课程性质获取课程
    List<Course> getCoursesByNature(String courseNature);
    
    // 根据学期获取课程
    List<Course> getCoursesBySemester(Integer semester);
    
    // 根据学院获取课程
    List<Course> getCoursesByCollege(String college);
    
    // 根据学分范围获取课程
    List<Course> getCoursesByCreditRange(@Param("minCredit") Double minCredit, @Param("maxCredit") Double maxCredit);
}
