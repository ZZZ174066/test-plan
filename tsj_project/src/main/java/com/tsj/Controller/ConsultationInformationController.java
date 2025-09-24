package com.tsj.Controller;

import com.tsj.Service.ConsultationInformationService;
import com.tsj.project.ConsultationInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线咨询信息控制器
 */
@RestController
@RequestMapping("/api/consultation")
@CrossOrigin(origins = "*")
public class ConsultationInformationController {
    
    @Autowired
    private ConsultationInformationService consultationService;
    
    /**
     * 学生提交咨询问题
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuestion(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer studentId = (Integer) request.get("studentId");
            Integer teacherId = (Integer) request.get("teacherId");
            String question = (String) request.get("question");
            
            if (studentId == null || teacherId == null || question == null || question.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "参数不完整");
                return ResponseEntity.badRequest().body(response);
            }
            
            ConsultationInformation consultation = consultationService.submitQuestion(studentId, teacherId, question);
            
            if (consultation != null) {
                response.put("success", true);
                response.put("message", "提问成功");
                response.put("data", consultation);
            } else {
                response.put("success", false);
                response.put("message", "提问失败");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 教师回答咨询问题
     */
    @PostMapping("/answer")
    public ResponseEntity<Map<String, Object>> answerQuestion(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer consultationId = (Integer) request.get("consultationId");
            String answer = (String) request.get("answer");
            
            if (consultationId == null || answer == null || answer.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "参数不完整");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean success = consultationService.answerQuestion(consultationId, answer);
            
            if (success) {
                response.put("success", true);
                response.put("message", "回答成功");
            } else {
                response.put("success", false);
                response.put("message", "回答失败");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取教师的咨询问题列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<Map<String, Object>> getTeacherConsultations(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ConsultationInformation> consultations = consultationService.getTeacherConsultations(teacherId);
            
            response.put("success", true);
            response.put("message", "获取成功");
            response.put("data", consultations);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取学生的咨询记录列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentConsultations(@PathVariable Integer studentId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ConsultationInformation> consultations = consultationService.getStudentConsultations(studentId);
            
            response.put("success", true);
            response.put("message", "获取成功");
            response.put("data", consultations);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取咨询记录详情
     */
    @GetMapping("/{consultationId}")
    public ResponseEntity<Map<String, Object>> getConsultationById(@PathVariable Integer consultationId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            ConsultationInformation consultation = consultationService.getConsultationById(consultationId);
            
            if (consultation != null) {
                response.put("success", true);
                response.put("message", "获取成功");
                response.put("data", consultation);
            } else {
                response.put("success", false);
                response.put("message", "咨询记录不存在");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取教师待回答咨询数量
     */
    @GetMapping("/pending-count/{teacherId}")
    public ResponseEntity<Map<String, Object>> getPendingCount(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int count = consultationService.getPendingConsultationCount(teacherId);
            
            response.put("success", true);
            response.put("message", "获取成功");
            response.put("count", count);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}
