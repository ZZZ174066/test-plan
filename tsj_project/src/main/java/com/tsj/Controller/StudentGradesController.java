package com.tsj.Controller;

import com.tsj.project.StudentGrades;
import com.tsj.Service.StudentGradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生成绩信息控制器
 */
@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class StudentGradesController {
    
    @Autowired
    private StudentGradesService studentGradesService;
    
    /**
     * 获取所有学生成绩信息
     * @return 学生成绩信息列表
     */
    @GetMapping("/getAllStudentGrades")
    public ResponseEntity<Map<String, Object>> getAllStudentGrades() {
        try {
            List<StudentGrades> gradesList = studentGradesService.getAllStudentGrades();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取学生成绩信息成功");
            response.put("data", gradesList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生成绩信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据用户ID获取学生成绩信息
     * @param id 用户ID
     * @return 学生成绩信息
     */
    @GetMapping("/getStudentGradesById/{id}")
    public ResponseEntity<Map<String, Object>> getStudentGradesById(@PathVariable Integer id) {
        try {
            StudentGrades gradesInfo = studentGradesService.getStudentGradesById(id);
            Map<String, Object> response = new HashMap<>();
            if (gradesInfo != null) {
                response.put("success", true);
                response.put("message", "获取学生成绩信息成功");
                response.put("data", gradesInfo);
            } else {
                response.put("success", false);
                response.put("message", "成绩记录不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生成绩信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 插入学生成绩信息
     * @param studentGrades 学生成绩信息对象
     * @return 操作结果
     */
    @PostMapping("/insertStudentGrades")
    public ResponseEntity<Map<String, Object>> insertStudentGrades(@RequestBody StudentGrades studentGrades) {
        try {
            studentGradesService.insertStudentGrades(studentGrades);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "成绩记录创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建成绩记录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新学生成绩信息
     * @param studentGrades 学生成绩信息对象
     * @return 操作结果
     */
    @PutMapping("/updateStudentGrades")
    public ResponseEntity<Map<String, Object>> updateStudentGrades(@RequestBody StudentGrades studentGrades) {
        try {
            studentGradesService.updateStudentGrades(studentGrades);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "学生成绩信息更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新学生成绩信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除学生成绩信息
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/deleteStudentGrades/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudentGrades(@PathVariable Integer id) {
        try {
            studentGradesService.deleteStudentGrades(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "成绩记录删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除成绩记录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取教师教授且已考试的学科列表
     * @param teacherId 教师ID
     * @return 学科列表
     */
    @GetMapping("/getTeacherExaminedSubjects/{teacherId}")
    public ResponseEntity<Map<String, Object>> getTeacherExaminedSubjects(@PathVariable Integer teacherId) {
        try {
            List<String> subjects = studentGradesService.getTeacherExaminedSubjects(teacherId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取学科列表成功");
            response.put("data", subjects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学科列表失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取指定学科的学生成绩列表
     * @param subject 学科名称
     * @return 学生成绩列表
     */
    @GetMapping("/getStudentGradesBySubject")
    public ResponseEntity<Map<String, Object>> getStudentGradesBySubject(@RequestParam String subject) {
        try {
            List<Map<String, Object>> gradesList = studentGradesService.getStudentGradesBySubject(subject);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取学科成绩成功");
            response.put("data", gradesList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学科成绩失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 更新学生某科目成绩
     * @param request 包含学生ID、科目名和成绩的请求体
     * @return 操作结果
     */
    @PutMapping("/updateStudentSubjectGrade")
    public ResponseEntity<Map<String, Object>> updateStudentSubjectGrade(@RequestBody Map<String, Object> request) {
        try {
            Integer studentId = (Integer) request.get("studentId");
            String subject = (String) request.get("subject");
            BigDecimal grade = new BigDecimal(request.get("grade").toString());
            
            studentGradesService.updateStudentSubjectGrade(studentId, subject, grade);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "成绩更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "成绩更新失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新单个课程的成绩
     * @param id 用户ID
     * @param courseName 课程名称
     * @param grade 成绩
     * @return 操作结果
     */
    @PutMapping("/updateCourseGrade")
    public ResponseEntity<Map<String, Object>> updateCourseGrade(
            @RequestParam Integer id, 
            @RequestParam String courseName, 
            @RequestParam BigDecimal grade) {
        try {
            studentGradesService.updateCourseGrade(id, courseName, grade);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程成绩更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新课程成绩失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取指定用户的指定课程成绩
     * @param id 用户ID
     * @param courseName 课程名称
     * @return 成绩
     */
    @GetMapping("/getCourseGrade")
    public ResponseEntity<Map<String, Object>> getCourseGrade(
            @RequestParam Integer id, 
            @RequestParam String courseName) {
        try {
            BigDecimal grade = studentGradesService.getCourseGrade(id, courseName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取课程成绩成功");
            response.put("data", grade);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取课程成绩失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 批量更新多个课程的成绩
     * @param requestData 请求数据，包含用户ID和课程成绩映射
     * @return 操作结果
     */
    @PutMapping("/batchUpdateCourseGrades")
    public ResponseEntity<Map<String, Object>> batchUpdateCourseGrades(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> courseGrades = (Map<String, BigDecimal>) requestData.get("courseGrades");
            
            studentGradesService.batchUpdateCourseGrades(id, courseGrades);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量更新课程成绩成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量更新课程成绩失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户的成绩统计信息
     * @param id 用户ID
     * @return 统计信息
     */
    @GetMapping("/getGradeStatistics/{id}")
    public ResponseEntity<Map<String, Object>> getGradeStatistics(@PathVariable Integer id) {
        try {
            Map<String, Object> statistics = studentGradesService.getGradeStatistics(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取成绩统计信息成功");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取成绩统计信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户的及格课程数量
     * @param id 用户ID
     * @return 及格课程数量
     */
    @GetMapping("/getPassedCoursesCount/{id}")
    public ResponseEntity<Map<String, Object>> getPassedCoursesCount(@PathVariable Integer id) {
        try {
            int count = studentGradesService.getPassedCoursesCount(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取及格课程数量成功");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取及格课程数量失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户的不及格课程数量
     * @param id 用户ID
     * @return 不及格课程数量
     */
    @GetMapping("/getFailedCoursesCount/{id}")
    public ResponseEntity<Map<String, Object>> getFailedCoursesCount(@PathVariable Integer id) {
        try {
            int count = studentGradesService.getFailedCoursesCount(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取不及格课程数量成功");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取不及格课程数量失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户的平均成绩
     * @param id 用户ID
     * @return 平均成绩
     */
    @GetMapping("/getAverageGrade/{id}")
    public ResponseEntity<Map<String, Object>> getAverageGrade(@PathVariable Integer id) {
        try {
            BigDecimal averageGrade = studentGradesService.getAverageGrade(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取平均成绩成功");
            response.put("data", averageGrade);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取平均成绩失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户已录入成绩的课程数量
     * @param id 用户ID
     * @return 已录入成绩的课程数量
     */
    @GetMapping("/getGradedCoursesCount/{id}")
    public ResponseEntity<Map<String, Object>> getGradedCoursesCount(@PathVariable Integer id) {
        try {
            int count = studentGradesService.getGradedCoursesCount(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取已录入成绩课程数量成功");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取已录入成绩课程数量失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 为新学生用户创建成绩记录
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/createGradeRecordForNewStudent")
    public ResponseEntity<Map<String, Object>> createGradeRecordForNewStudent(@RequestParam Integer userId) {
        try {
            studentGradesService.createGradeRecordForNewStudent(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "为新学生用户创建成绩记录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建成绩记录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 检查成绩记录是否存在
     * @param id 用户ID
     * @return 是否存在
     */
    @GetMapping("/existsById/{id}")
    public ResponseEntity<Map<String, Object>> existsById(@PathVariable Integer id) {
        try {
            boolean exists = studentGradesService.existsById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "检查成绩记录存在性成功");
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "检查成绩记录存在性失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 录入课程成绩（教师录入学生成绩）
     * @param requestData 录入数据，包含用户ID、课程名称和成绩
     * @return 操作结果
     */
    @PostMapping("/inputGrade")
    public ResponseEntity<Map<String, Object>> inputGrade(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            String courseName = (String) requestData.get("courseName");
            BigDecimal grade = new BigDecimal(requestData.get("grade").toString());
            
            studentGradesService.updateCourseGrade(id, courseName, grade);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "成绩录入成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "成绩录入失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 批量录入成绩
     * @param requestData 批量录入数据，包含用户ID和多个课程成绩
     * @return 操作结果
     */
    @PostMapping("/batchInputGrades")
    public ResponseEntity<Map<String, Object>> batchInputGrades(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> courseGrades = (Map<String, BigDecimal>) requestData.get("courseGrades");
            
            studentGradesService.batchUpdateCourseGrades(id, courseGrades);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量录入成绩成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量录入成绩失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
