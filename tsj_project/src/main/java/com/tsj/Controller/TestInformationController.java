package com.tsj.Controller;

import com.tsj.Service.TestInformationService;
import com.tsj.project.TestInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试信息控制器
 * 提供考试信息的REST API接口
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestInformationController {

    @Autowired
    private TestInformationService testInformationService;

    /**
     * 获取所有考试信息
     * @return 考试信息列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllTestInformation() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TestInformation> testList = testInformationService.getAllTestInformation();
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据考试ID获取考试信息
     * @param testId 考试ID
     * @return 考试信息
     */
    @GetMapping("/{testId}")
    public ResponseEntity<Map<String, Object>> getTestInformationById(@PathVariable Long testId) {
        Map<String, Object> response = new HashMap<>();
        try {
            TestInformation testInformation = testInformationService.getTestInformationById(testId);
            if (testInformation != null) {
                response.put("success", true);
                response.put("data", testInformation);
                response.put("message", "获取考试信息成功");
            } else {
                response.put("success", false);
                response.put("message", "考试信息不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据课程ID获取考试信息
     * @param courseId 课程ID
     * @return 考试信息
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getTestInformationByCourseId(@PathVariable Long courseId) {
        Map<String, Object> response = new HashMap<>();
        try {
            TestInformation testInformation = testInformationService.getTestInformationByCourseId(courseId);
            if (testInformation != null) {
                response.put("success", true);
                response.put("data", testInformation);
                response.put("message", "获取考试信息成功");
            } else {
                response.put("success", false);
                response.put("message", "该课程暂无考试信息");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 新增考试信息
     * @param testInformation 考试信息对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addTestInformation(@RequestBody TestInformation testInformation) {
        Map<String, Object> response = new HashMap<>();
        try {
            testInformationService.addTestInformation(testInformation);
            response.put("success", true);
            response.put("message", "添加考试信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加考试信息失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 更新考试信息
     * @param testInformation 考试信息对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateTestInformation(@RequestBody TestInformation testInformation) {
        Map<String, Object> response = new HashMap<>();
        try {
            testInformationService.updateTestInformation(testInformation);
            response.put("success", true);
            response.put("message", "更新考试信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新考试信息失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 删除考试信息
     * @param testId 考试ID
     * @return 操作结果
     */
    @DeleteMapping("/{testId}")
    public ResponseEntity<Map<String, Object>> deleteTestInformation(@PathVariable Long testId) {
        Map<String, Object> response = new HashMap<>();
        try {
            testInformationService.deleteTestInformation(testId);
            response.put("success", true);
            response.put("message", "删除考试信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除考试信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据考试状态获取考试信息列表
     * @param status 考试状态
     * @return 考试信息列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getTestInformationByStatus(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TestInformation> testList = testInformationService.getTestInformationByStatus(status);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 根据考试日期获取考试信息列表
     * @param date 考试日期 (格式: yyyy-MM-dd)
     * @return 考试信息列表
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<Map<String, Object>> getTestInformationByDate(@PathVariable String date) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate examDate = LocalDate.parse(date);
            List<TestInformation> testList = testInformationService.getTestInformationByDate(examDate);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            response.put("success", false);
            response.put("message", "日期格式错误，请使用 yyyy-MM-dd 格式");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据考试时段获取考试信息列表
     * @param session 考试时段
     * @return 考试信息列表
     */
    @GetMapping("/session/{session}")
    public ResponseEntity<Map<String, Object>> getTestInformationBySession(@PathVariable String session) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TestInformation> testList = testInformationService.getTestInformationBySession(session);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 根据考试地点获取考试信息列表
     * @param location 考试地点
     * @return 考试信息列表
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<Map<String, Object>> getTestInformationByLocation(@PathVariable String location) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TestInformation> testList = testInformationService.getTestInformationByLocation(location);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 安排考试
     * @param payload 包含testId, examDate, examSession, examLocation的JSON对象
     * @return 操作结果
     */
    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> scheduleExam(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long testId = Long.valueOf(payload.get("testId").toString());
            LocalDate examDate = LocalDate.parse(payload.get("examDate").toString());
            String examSession = payload.get("examSession").toString();
            String examLocation = payload.get("examLocation").toString();
            
            testInformationService.scheduleExam(testId, examDate, examSession, examLocation);
            response.put("success", true);
            response.put("message", "安排考试成功");
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            response.put("success", false);
            response.put("message", "日期格式错误，请使用 yyyy-MM-dd 格式");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "安排考试失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 取消考试安排
     * @param testId 考试ID
     * @return 操作结果
     */
    @PostMapping("/cancel/{testId}")
    public ResponseEntity<Map<String, Object>> cancelExamSchedule(@PathVariable Long testId) {
        Map<String, Object> response = new HashMap<>();
        try {
            testInformationService.cancelExamSchedule(testId);
            response.put("success", true);
            response.put("message", "取消考试安排成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "取消考试安排失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 更新考试状态
     * @param payload 包含testId和examStatus的JSON对象
     * @return 操作结果
     */
    @PostMapping("/updateStatus")
    public ResponseEntity<Map<String, Object>> updateExamStatus(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long testId = Long.valueOf(payload.get("testId").toString());
            String examStatus = payload.get("examStatus").toString();
            
            testInformationService.updateExamStatus(testId, examStatus);
            response.put("success", true);
            response.put("message", "更新考试状态成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新考试状态失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 统计各状态的考试数量
     * @return 状态统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getExamStatusStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> statistics = testInformationService.getExamStatusStatistics();
            response.put("success", true);
            response.put("data", statistics);
            response.put("message", "获取考试状态统计成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试状态统计失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据教师ID获取该教师教授学科的考试信息列表
     * @param teacherId 教师ID
     * @return 考试信息列表
     */
    @GetMapping("/teacher-exams/{teacherId}")
    public ResponseEntity<Map<String, Object>> getExamsByTeacherId(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TestInformation> testList = testInformationService.getExamsByTeacherId(teacherId);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取教师考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教师考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据教师ID统计该教师教授学科的考试状态数量
     * @param teacherId 教师ID
     * @return 状态统计信息
     */
    @GetMapping("/teacher-statistics/{teacherId}")
    public ResponseEntity<Map<String, Object>> getExamStatusStatisticsByTeacherId(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> statistics = testInformationService.getExamStatusStatisticsByTeacherId(teacherId);
            response.put("success", true);
            response.put("data", statistics);
            response.put("message", "获取教师考试状态统计成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教师考试状态统计失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据课程名称搜索考试信息
     * @param courseName 课程名称关键字
     * @return 考试信息列表
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchTestInformationByCourseName(@RequestParam String courseName) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TestInformation> testList = testInformationService.searchTestInformationByCourseName(courseName);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "搜索考试信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "搜索考试信息失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * 根据日期范围获取考试信息列表
     * @param startDate 开始日期 (格式: yyyy-MM-dd)
     * @param endDate 结束日期 (格式: yyyy-MM-dd)
     * @return 考试信息列表
     */
    @GetMapping("/dateRange")
    public ResponseEntity<Map<String, Object>> getTestInformationByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<TestInformation> testList = testInformationService.getTestInformationByDateRange(start, end);
            response.put("success", true);
            response.put("data", testList);
            response.put("message", "获取考试信息列表成功");
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            response.put("success", false);
            response.put("message", "日期格式错误，请使用 yyyy-MM-dd 格式");
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取考试信息列表失败：" + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
}
