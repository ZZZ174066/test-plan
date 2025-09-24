package com.tsj.Controller;

import com.tsj.project.DeclareInformation;
import com.tsj.Service.DeclareInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程报名信息控制器
 */
@RestController
@RequestMapping("/api/declare")
@CrossOrigin(origins = "*")
public class DeclareInformationController {
    
    @Autowired
    private DeclareInformationService declareInformationService;
    
    /**
     * 获取所有报名信息
     * @return 报名信息列表
     */
    @GetMapping("/getAllDeclareInformation")
    public ResponseEntity<Map<String, Object>> getAllDeclareInformation() {
        try {
            List<DeclareInformation> declareList = declareInformationService.getAllDeclareInformation();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取报名信息成功");
            response.put("data", declareList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取报名信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据用户ID获取报名信息
     * @param id 用户ID
     * @return 报名信息
     */
    @GetMapping("/getDeclareInformationById/{id}")
    public ResponseEntity<Map<String, Object>> getDeclareInformationById(@PathVariable Integer id) {
        try {
            DeclareInformation declareInfo = declareInformationService.getDeclareInformationById(id);
            Map<String, Object> response = new HashMap<>();
            if (declareInfo != null) {
                response.put("success", true);
                response.put("message", "获取报名信息成功");
                response.put("data", declareInfo);
            } else {
                response.put("success", false);
                response.put("message", "报名记录不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取报名信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 插入报名信息
     * @param declareInformation 报名信息对象
     * @return 操作结果
     */
    @PostMapping("/insertDeclareInformation")
    public ResponseEntity<Map<String, Object>> insertDeclareInformation(@RequestBody DeclareInformation declareInformation) {
        try {
            declareInformationService.insertDeclareInformation(declareInformation);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "报名记录创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建报名记录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新报名信息
     * @param declareInformation 报名信息对象
     * @return 操作结果
     */
    @PutMapping("/updateDeclareInformation")
    public ResponseEntity<Map<String, Object>> updateDeclareInformation(@RequestBody DeclareInformation declareInformation) {
        try {
            declareInformationService.updateDeclareInformation(declareInformation);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "报名信息更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新报名信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除报名信息
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/deleteDeclareInformation/{id}")
    public ResponseEntity<Map<String, Object>> deleteDeclareInformation(@PathVariable Integer id) {
        try {
            declareInformationService.deleteDeclareInformation(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "报名记录删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除报名记录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新单个课程的报名状态
     * @param id 用户ID
     * @param courseName 课程名称
     * @param status 报名状态
     * @return 操作结果
     */
    @PutMapping("/updateCourseStatus")
    public ResponseEntity<Map<String, Object>> updateCourseStatus(
            @RequestParam Integer id, 
            @RequestParam String courseName, 
            @RequestParam String status) {
        try {
            declareInformationService.updateCourseStatus(id, courseName, status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程报名状态更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新课程报名状态失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取指定用户的指定课程报名状态
     * @param id 用户ID
     * @param courseName 课程名称
     * @return 报名状态
     */
    @GetMapping("/getCourseStatus")
    public ResponseEntity<Map<String, Object>> getCourseStatus(
            @RequestParam Integer id, 
            @RequestParam String courseName) {
        try {
            String status = declareInformationService.getCourseStatus(id, courseName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取课程报名状态成功");
            response.put("data", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取课程报名状态失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 批量更新多个课程的报名状态
     * @param requestData 请求数据，包含用户ID和课程状态映射
     * @return 操作结果
     */
    @PutMapping("/batchUpdateCourseStatus")
    public ResponseEntity<Map<String, Object>> batchUpdateCourseStatus(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            @SuppressWarnings("unchecked")
            Map<String, String> courseStatuses = (Map<String, String>) requestData.get("courseStatuses");
            
            declareInformationService.batchUpdateCourseStatus(id, courseStatuses);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量更新课程报名状态成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量更新课程报名状态失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取指定状态的报名记录数量
     * @param status 报名状态
     * @return 记录数量
     */
    @GetMapping("/countByStatus")
    public ResponseEntity<Map<String, Object>> countByStatus(@RequestParam String status) {
        try {
            int count = declareInformationService.countByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取报名记录数量成功");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取报名记录数量失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户的报名统计信息
     * @param id 用户ID
     * @return 统计信息
     */
    @GetMapping("/getDeclareStatistics/{id}")
    public ResponseEntity<Map<String, Object>> getDeclareStatistics(@PathVariable Integer id) {
        try {
            Map<String, Integer> statistics = declareInformationService.getDeclareStatistics(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取报名统计信息成功");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取报名统计信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 为新学生用户创建报名记录
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/createDeclareRecordForNewStudent")
    public ResponseEntity<Map<String, Object>> createDeclareRecordForNewStudent(@RequestParam Integer userId) {
        try {
            declareInformationService.createDeclareRecordForNewStudent(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "为新学生用户创建报名记录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建报名记录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 检查报名记录是否存在
     * @param id 用户ID
     * @return 是否存在
     */
    @GetMapping("/existsById/{id}")
    public ResponseEntity<Map<String, Object>> existsById(@PathVariable Integer id) {
        try {
            boolean exists = declareInformationService.existsById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "检查报名记录存在性成功");
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "检查报名记录存在性失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 报名课程（学生报名课程）
     * @param requestData 报名数据，包含用户ID和课程名称
     * @return 操作结果
     */
    @PostMapping("/declareCourse")
    public ResponseEntity<Map<String, Object>> declareCourse(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            String courseName = (String) requestData.get("courseName");
            
            // 将课程状态更新为"已报名"
            declareInformationService.updateCourseStatus(id, courseName, "已报名");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "课程报名成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "课程报名失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 撤销报名（学生撤销课程报名）
     * @param requestData 撤销数据，包含用户ID和课程名称
     * @return 操作结果
     */
    @PostMapping("/undeclareCourse")
    public ResponseEntity<Map<String, Object>> undeclareCourse(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            String courseName = (String) requestData.get("courseName");
            
            // 将课程状态更新为"未报名"
            declareInformationService.updateCourseStatus(id, courseName, "未报名");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "撤销报名成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "撤销报名失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 审批通过（管理员审批学生报名）
     * @param requestData 审批数据，包含用户ID和课程名称
     * @return 操作结果
     */
    @PostMapping("/approveCourse")
    public ResponseEntity<Map<String, Object>> approveCourse(@RequestBody Map<String, Object> requestData) {
        try {
            Integer id = (Integer) requestData.get("id");
            String courseName = (String) requestData.get("courseName");
            
            // 将课程状态更新为"已通过"
            declareInformationService.updateCourseStatus(id, courseName, "已通过");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "审批通过成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "审批通过失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
