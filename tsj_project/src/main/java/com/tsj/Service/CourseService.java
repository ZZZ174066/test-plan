package com.tsj.Service;

import com.tsj.Mapper.CourseMapper;
import com.tsj.project.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {
    
    @Autowired
    private CourseMapper courseMapper;
    
    // 获取所有课程
    public List<Course> getCourse() {
        return courseMapper.getCourse();
    }
    
    // 插入课程
    public void insertCourse(Course course) {
        courseMapper.insertCourse(course);
    }
    
    // 更新课程
    public void updateCourse(Course course) {
        courseMapper.updateCourse(course);
    }
    
    // 删除课程
    public void deleteCourse(Long courseId) {
        courseMapper.deleteCourse(courseId);
    }
    
    // 根据ID获取课程
    public Course getCourseById(Long courseId) {
        return courseMapper.getCourseById(courseId);
    }
    
    // 根据课程代码获取课程
    public Course getCourseByCourseCode(String courseCode) {
        return courseMapper.getCourseByCourseCode(courseCode);
    }
    
    // 根据课程类型获取课程
    public List<Course> getCoursesByType(String courseType) {
        return courseMapper.getCoursesByType(courseType);
    }
    
    // 根据课程性质获取课程
    public List<Course> getCoursesByNature(String courseNature) {
        return courseMapper.getCoursesByNature(courseNature);
    }
    
    // 根据学期获取课程
    public List<Course> getCoursesBySemester(Integer semester) {
        return courseMapper.getCoursesBySemester(semester);
    }
    
    // 根据学院获取课程
    public List<Course> getCoursesByCollege(String college) {
        return courseMapper.getCoursesByCollege(college);
    }
    
    // 根据学分范围获取课程
    public List<Course> getCoursesByCreditRange(Double minCredit, Double maxCredit) {
        return courseMapper.getCoursesByCreditRange(minCredit, maxCredit);
    }
    
    // 检查课程代码是否已存在
    public boolean isCourseCodeExists(String courseCode) {
        Course course = courseMapper.getCourseByCourseCode(courseCode);
        return course != null;
    }
    
    // 获取指定类型和学期的课程
    public List<Course> getCoursesByTypeAndSemester(String courseType, Integer semester) {
        List<Course> courses = courseMapper.getCoursesByType(courseType);
        return courses.stream()
                .filter(course -> course.getSemester().equals(semester))
                .collect(java.util.stream.Collectors.toList());
    }
}
