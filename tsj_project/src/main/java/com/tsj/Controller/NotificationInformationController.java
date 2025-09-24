package com.tsj.Controller;

import com.tsj.project.NotificationInformation;
import com.tsj.Service.NotificationInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知信息控制器
 */
@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationInformationController {
    
    @Autowired
    private NotificationInformationService notificationInformationService;
    
    /**
     * 获取所有通知信息
     * @return 通知信息列表
     */
    @GetMapping("/getAllNotifications")
    public ResponseEntity<Map<String, Object>> getAllNotifications() {
        try {
            List<NotificationInformation> notifications = notificationInformationService.getAllNotifications();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取通知信息成功");
            response.put("data", notifications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取通知信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据ID获取通知信息
     * @param id 通知ID
     * @return 通知信息
     */
    @GetMapping("/getNotificationById/{id}")
    public ResponseEntity<Map<String, Object>> getNotificationById(@PathVariable("id") Integer id) {
        try {
            NotificationInformation notification = notificationInformationService.getNotificationById(id);
            Map<String, Object> response = new HashMap<>();
            if (notification != null) {
                response.put("success", true);
                response.put("message", "获取通知信息成功");
                response.put("data", notification);
            } else {
                response.put("success", false);
                response.put("message", "未找到对应的通知信息");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取通知信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据学科获取通知信息
     * @param course 学科名称
     * @return 通知信息列表
     */
    @GetMapping("/getNotificationsByCourse")
    public ResponseEntity<Map<String, Object>> getNotificationsByCourse(@RequestParam("course") String course) {
        try {
            List<NotificationInformation> notifications = notificationInformationService.getNotificationsByCourse(course);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取学科通知信息成功");
            response.put("data", notifications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学科通知信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 创建新通知信息
     * @param notification 通知信息对象
     * @return 创建结果
     */
    @PostMapping("/createNotification")
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody NotificationInformation notification) {
        try {
            boolean success = notificationInformationService.insertNotification(notification);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "通知信息创建成功");
                response.put("data", notification);
            } else {
                response.put("success", false);
                response.put("message", "通知信息创建失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "通知信息创建失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新通知信息
     * @param notification 通知信息对象
     * @return 更新结果
     */
    @PutMapping("/updateNotification")
    public ResponseEntity<Map<String, Object>> updateNotification(@RequestBody NotificationInformation notification) {
        try {
            boolean success = notificationInformationService.updateNotification(notification);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "通知信息更新成功");
                response.put("data", notification);
            } else {
                response.put("success", false);
                response.put("message", "通知信息更新失败，可能是通知不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "通知信息更新失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除通知信息
     * @param id 通知ID
     * @return 删除结果
     */
    @DeleteMapping("/deleteNotification/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable("id") Integer id) {
        try {
            boolean success = notificationInformationService.deleteNotification(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "通知信息删除成功");
            } else {
                response.put("success", false);
                response.put("message", "通知信息删除失败，可能是通知不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "通知信息删除失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取最近的通知信息
     * @param limit 限制数量（可选，默认10条）
     * @return 最近通知信息列表
     */
    @GetMapping("/getRecentNotifications")
    public ResponseEntity<Map<String, Object>> getRecentNotifications(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        try {
            List<NotificationInformation> notifications = notificationInformationService.getRecentNotifications(limit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取最近通知信息成功");
            response.put("data", notifications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取最近通知信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据关键词搜索通知信息
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("/searchNotifications")
    public ResponseEntity<Map<String, Object>> searchNotifications(@RequestParam("keyword") String keyword) {
        try {
            List<NotificationInformation> notifications = notificationInformationService.searchNotifications(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "搜索通知信息成功");
            response.put("data", notifications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索通知信息失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
