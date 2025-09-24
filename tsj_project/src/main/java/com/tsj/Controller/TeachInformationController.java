package com.tsj.Controller;

import com.tsj.Service.TeachInformationService;
import com.tsj.project.TeachInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学信息控制器
 * 提供教学信息的REST API接口
 */
@RestController
@RequestMapping("/api/teach")
@CrossOrigin(origins = "*")
public class TeachInformationController {

    @Autowired
    private TeachInformationService teachInformationService;

    /**
     * 获取所有教学信息
     * @return 教学信息列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllTeachInformation() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TeachInformation> teachList = teachInformationService.getAllTeachInformation();
            response.put("success", true);
            response.put("data", teachList);
            response.put("message", "获取教学信息列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教学信息列表失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据教师ID获取教学信息
     * @param teacherId 教师ID
     * @return 教学信息
     */
    @GetMapping("/{teacherId}")
    public ResponseEntity<Map<String, Object>> getTeachInformationByTeacherId(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        try {
            TeachInformation teachInformation = teachInformationService.getTeachInformationByTeacherId(teacherId);
            if (teachInformation != null) {
                response.put("success", true);
                response.put("data", teachInformation);
                response.put("message", "获取教学信息成功");
            } else {
                response.put("success", false);
                response.put("message", "教学信息不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教学信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 新增教学信息
     * @param teachInformation 教学信息对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addTeachInformation(@RequestBody TeachInformation teachInformation) {
        Map<String, Object> response = new HashMap<>();
        try {
            teachInformationService.insertTeachInformation(teachInformation);
            response.put("success", true);
            response.put("message", "添加教学信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加教学信息失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 更新教学信息
     * @param teachInformation 教学信息对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateTeachInformation(@RequestBody TeachInformation teachInformation) {
        Map<String, Object> response = new HashMap<>();
        try {
            teachInformationService.updateTeachInformation(teachInformation);
            response.put("success", true);
            response.put("message", "更新教学信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新教学信息失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 删除教学信息
     * @param teacherId 教师ID
     * @return 操作结果
     */
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Map<String, Object>> deleteTeachInformation(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        try {
            teachInformationService.deleteTeachInformation(teacherId);
            response.put("success", true);
            response.put("message", "删除教学信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除教学信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据学科搜索教师
     * @param subject 学科名称
     * @return 教学信息列表
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getTeachersBySubject(@RequestParam String subject) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TeachInformation> teachList = teachInformationService.getTeachersBySubject(subject);
            response.put("success", true);
            response.put("data", teachList);
            response.put("message", "搜索教师信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索教师信息失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 获取教师统计信息
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getTeacherStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            int totalTeachers = teachInformationService.getTeacherCount();
            int activeTeachers = teachInformationService.getActiveTeacherCount();
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalTeachers", totalTeachers);
            statistics.put("activeTeachers", activeTeachers);
            statistics.put("inactiveTeachers", totalTeachers - activeTeachers);
            
            response.put("success", true);
            response.put("data", statistics);
            response.put("message", "获取教师统计信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教师统计信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
