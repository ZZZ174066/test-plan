package com.tsj.Controller;

import com.tsj.project.Course;
import com.tsj.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    // 获取所有课程
    @GetMapping("/getCourse")
    public List<Course> getCourse() {
        return courseService.getCourse();
    }
    
    // 插入课程
    @PostMapping("/insertCourse")
    public ResponseEntity<Map<String, Object>> insertCourse(@RequestBody Course course) {
        try {
            // 检查课程代码是否已存在
            if (courseService.isCourseCodeExists(course.getCourseCode())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "课程代码已存在");
                return ResponseEntity.ok(response);
            }
            
            courseService.insertCourse(course);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "课程创建失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 更新课程
    @PutMapping("/updateCourse")
    public ResponseEntity<Map<String, Object>> updateCourse(@RequestBody Course course) {
        try {
            // 检查课程是否存在
            Course existingCourse = courseService.getCourseById(course.getCourseId());
            if (existingCourse == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "课程不存在");
                return ResponseEntity.ok(response);
            }
            
            // 如果课程代码发生变化，检查新代码是否已存在
            if (!existingCourse.getCourseCode().equals(course.getCourseCode())) {
                if (courseService.isCourseCodeExists(course.getCourseCode())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "课程代码已存在");
                    return ResponseEntity.ok(response);
                }
            }
            
            courseService.updateCourse(course);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "课程更新失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 删除课程
    @DeleteMapping("/deleteCourse/{courseId}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long courseId) {
        try {
            // 检查课程是否存在
            Course existingCourse = courseService.getCourseById(courseId);
            if (existingCourse == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "课程不存在");
                return ResponseEntity.ok(response);
            }
            
            courseService.deleteCourse(courseId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "课程删除失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 根据ID获取课程
    @GetMapping("/getCourseById/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable Long courseId) {
        try {
            Course course = courseService.getCourseById(courseId);
            Map<String, Object> response = new HashMap<>();
            if (course != null) {
                response.put("success", true);
                response.put("data", course);
            } else {
                response.put("success", false);
                response.put("message", "课程不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取课程失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 根据课程代码获取课程
    @GetMapping("/getCourseByCourseCode/{courseCode}")
    public ResponseEntity<Map<String, Object>> getCourseByCourseCode(@PathVariable String courseCode) {
        try {
            Course course = courseService.getCourseByCourseCode(courseCode);
            Map<String, Object> response = new HashMap<>();
            if (course != null) {
                response.put("success", true);
                response.put("data", course);
            } else {
                response.put("success", false);
                response.put("message", "课程不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取课程失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    // 根据课程类型获取课程
    @GetMapping("/getCoursesByType/{courseType}")
    public List<Course> getCoursesByType(@PathVariable String courseType) {
        return courseService.getCoursesByType(courseType);
    }
    
    // 根据课程性质获取课程
    @GetMapping("/getCoursesByNature/{courseNature}")
    public List<Course> getCoursesByNature(@PathVariable String courseNature) {
        return courseService.getCoursesByNature(courseNature);
    }
    
    // 根据学期获取课程
    @GetMapping("/getCoursesBySemester/{semester}")
    public List<Course> getCoursesBySemester(@PathVariable Integer semester) {
        return courseService.getCoursesBySemester(semester);
    }
    
    // 根据学院获取课程
    @GetMapping("/getCoursesByCollege/{college}")
    public List<Course> getCoursesByCollege(@PathVariable String college) {
        return courseService.getCoursesByCollege(college);
    }
    
    // 根据学分范围获取课程
    @GetMapping("/getCoursesByCreditRange")
    public List<Course> getCoursesByCreditRange(
            @RequestParam(defaultValue = "0.0") Double minCredit,
            @RequestParam(defaultValue = "5.0") Double maxCredit) {
        return courseService.getCoursesByCreditRange(minCredit, maxCredit);
    }
    
    // 获取课程统计信息
    @GetMapping("/getCourseStats")
    public ResponseEntity<Map<String, Object>> getCourseStats() {
        try {
            List<Course> allCourses = courseService.getCourse();
            Map<String, Object> stats = new HashMap<>();
            
            // 总课程数
            stats.put("totalCourses", allCourses.size());
            
            // 按类型统计
            Map<String, Long> typeStats = new HashMap<>();
            allCourses.stream().forEach(course -> {
                typeStats.merge(course.getCourseType(), 1L, Long::sum);
            });
            stats.put("typeStats", typeStats);
            
            // 按性质统计
            Map<String, Long> natureStats = new HashMap<>();
            allCourses.stream().forEach(course -> {
                natureStats.merge(course.getCourseNature(), 1L, Long::sum);
            });
            stats.put("natureStats", natureStats);
            
            // 按学期统计
            Map<Integer, Long> semesterStats = new HashMap<>();
            allCourses.stream().forEach(course -> {
                semesterStats.merge(course.getSemester(), 1L, Long::sum);
            });
            stats.put("semesterStats", semesterStats);
            
            // 学分统计
            double totalCredits = allCourses.stream().mapToDouble(Course::getCredit).sum();
            double avgCredits = allCourses.isEmpty() ? 0 : totalCredits / allCourses.size();
            stats.put("totalCredits", totalCredits);
            stats.put("avgCredits", Math.round(avgCredits * 10.0) / 10.0);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取课程统计失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
