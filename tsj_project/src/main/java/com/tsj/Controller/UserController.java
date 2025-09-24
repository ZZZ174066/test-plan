package com.tsj.Controller;
import com.tsj.project.User;
import com.tsj.project.TeachInformation;
import com.tsj.Service.UserService;
import com.tsj.Service.TeachInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TeachInformationService teachInformationService;
    private static boolean maintenanceMode = false;
    private static List<Map<String, Object>> userActivities = new ArrayList<>();
    @GetMapping("/getUser")
    public List<User> getUser(){
        return userService.getUser();
    }
    
    @PostMapping("/insertUser")
    public ResponseEntity<Map<String, Object>> insertUser(@RequestBody User user){
        try {
            userService.insertUser(user);
            
            // 记录用户注册活动
            String displayName = user.getRealName() != null && !user.getRealName().isEmpty() 
                ? user.getRealName() : user.getUsername();
            addUserActivity("新用户 " + displayName + " 注册成功");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户创建失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @GetMapping("/deleteUser/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id){
        try {
            // 先获取用户信息用于记录
            User user = userService.getUser2(id);
            
            userService.deleteUser(id);
            
            // 记录用户删除活动
            if (user != null) {
                String displayName = user.getRealName() != null && !user.getRealName().isEmpty() 
                    ? user.getRealName() : user.getUsername();
                addUserActivity("用户 " + displayName + " 被删除");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户删除失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping("/updateUser")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody User user){
        try {
            userService.updateUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户信息更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户信息更新失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @PutMapping("/updateUser")
    public ResponseEntity<Map<String, Object>> updateUserPut(@RequestBody User user){
        try {
            userService.updateUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户信息更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户信息更新失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @GetMapping("/getUser2")
    public User getUser2(@RequestParam Long id){
        return userService.getUser2(id);
    }
    
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id){
        try {
            User user = userService.getUser2(id);
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                response.put("success", true);
                response.put("data", user);
                response.put("message", "获取用户信息成功");
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取用户信息失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @GetMapping("/getUserByUsername")
    public User getUserByUsername(@RequestParam String username){
        return userService.getUserByUsername(username);
    }
    
    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsernameApi(@PathVariable String username){
        try {
            User user = userService.getUserByUsername(username);
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                response.put("success", true);
                response.put("data", user);
                response.put("message", "获取用户信息成功");
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取用户信息失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping("/updateOnlineStatus")
    public void updateOnlineStatus(@RequestParam Long id, @RequestParam Integer status){
        userService.updateOnlineStatus(id, status);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginInfo){
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");
        
        Map<String, Object> response = new HashMap<>();
        
        User user = userService.login(username, password);
        
        if (user != null) {
            // 检查维护模式（管理员可以在维护模式下登录）
            if (maintenanceMode && (user.getUserType() == null || user.getUserType() != 1)) {
                response.put("success", false);
                response.put("message", "系统正在维护！");
                response.put("maintenance", true);
                return ResponseEntity.ok(response);
            }
            
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("user", user);
            response.put("maintenance", false);
            
            // 记录用户登录活动（排除管理员）
            if (user.getUserType() == null || user.getUserType() != 1) {
                String displayName = user.getRealName() != null && !user.getRealName().isEmpty() 
                    ? user.getRealName() : username;
                addUserActivity(displayName + "登录系统");
            }
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            response.put("maintenance", false);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestParam Long userId){
        // 获取用户信息用于记录活动
        User user = userService.getUser2(userId);
        
        userService.logout(userId);
        
        // 记录用户退出活动（排除管理员）
        if (user != null && (user.getUserType() == null || user.getUserType() != 1)) {
            addUserActivity((user.getRealName() != null ? user.getRealName() : user.getUsername()) + "退出登录");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "已成功登出");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/getMaintenanceMode")
    public ResponseEntity<Map<String, Object>> getMaintenanceMode(){
        Map<String, Object> response = new HashMap<>();
        response.put("maintenanceMode", maintenanceMode);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/setMaintenanceMode")
    public ResponseEntity<Map<String, Object>> setMaintenanceMode(@RequestBody Map<String, Object> request){
        Boolean mode = (Boolean) request.get("maintenanceMode");
        if (mode != null) {
            boolean oldMode = maintenanceMode;
            maintenanceMode = mode;
            
            // 记录维护模式变更活动
            if (oldMode != mode) {
                addUserActivity("管理员" + (mode ? "开启" : "关闭") + "了维护模式");
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "维护模式设置已保存");
        response.put("maintenanceMode", maintenanceMode);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/getUserActivities")
    public ResponseEntity<List<Map<String, Object>>> getUserActivities(){
        // 返回最近20条活动记录
        List<Map<String, Object>> recentActivities = new ArrayList<>();
        int size = userActivities.size();
        int start = Math.max(0, size - 20);
        
        for (int i = size - 1; i >= start; i--) {
            recentActivities.add(userActivities.get(i));
        }
        
        return ResponseEntity.ok(recentActivities);
    }
    
    @PostMapping("/clearSystemLogs")
    public ResponseEntity<Map<String, Object>> clearSystemLogs(){
        try {
            userActivities.clear();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "系统日志已清除");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "清除日志失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 教师注册专用接口
     * 同时处理用户信息和教学信息的创建
     */
    @PostMapping("/registerTeacher")
    public ResponseEntity<Map<String, Object>> registerTeacher(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取用户数据
            @SuppressWarnings("unchecked")
            Map<String, Object> userData = (Map<String, Object>) request.get("userData");
            @SuppressWarnings("unchecked")
            Map<String, Object> teachingData = (Map<String, Object>) request.get("teachingData");
            
            if (userData == null) {
                response.put("success", false);
                response.put("message", "用户数据不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 创建用户对象
            User user = new User();
            user.setUsername((String) userData.get("username"));
            user.setPassword((String) userData.get("password"));
            user.setRealName((String) userData.get("realName"));
            user.setPhone((String) userData.get("phone"));
            user.setEmail((String) userData.get("email"));
            user.setUserType((Integer) userData.get("userType"));
            user.setAvatar((String) userData.get("avatar"));
            user.setOnlineStatus((Integer) userData.get("onlineStatus"));
            
            // 先创建用户
            userService.insertUser(user);
            
            // 如果是教师用户且提供了教学数据，创建教学信息
            if (user.getUserType() == 2 && teachingData != null) {
                // 获取刚创建用户的ID
                User createdUser = userService.getUserByUsername(user.getUsername());
                if (createdUser != null) {
                    TeachInformation teachInfo = new TeachInformation();
                    teachInfo.setTeacherId(Math.toIntExact(createdUser.getId()));
                    teachInfo.setSubject1((String) teachingData.get("subject1"));
                    teachInfo.setSubject2((String) teachingData.get("subject2"));
                    teachInfo.setSubject3((String) teachingData.get("subject3"));
                    teachInfo.setSubject4((String) teachingData.get("subject4"));
                    teachInfo.setSubject5((String) teachingData.get("subject5"));
                    teachInfo.setSubject6((String) teachingData.get("subject6"));
                    teachInfo.setSubject7((String) teachingData.get("subject7"));
                    teachInfo.setSubject8((String) teachingData.get("subject8"));
                    teachInfo.setSubject9((String) teachingData.get("subject9"));
                    teachInfo.setSubject10((String) teachingData.get("subject10"));
                    
                    // 插入或更新教学信息
                    TeachInformation existingTeachInfo = teachInformationService.getTeachInformationByTeacherId(teachInfo.getTeacherId());
                    if (existingTeachInfo != null) {
                        // 更新现有记录
                        teachInformationService.updateTeachInformation(teachInfo);
                    } else {
                        // 插入新记录
                        teachInformationService.insertTeachInformation(teachInfo);
                    }
                }
            }
            
            // 记录用户注册活动
            String displayName = user.getRealName() != null && !user.getRealName().isEmpty() 
                ? user.getRealName() : user.getUsername();
            String userTypeStr = user.getUserType() == 2 ? "教师" : "用户";
            addUserActivity("新" + userTypeStr + " " + displayName + " 注册成功");
            
            response.put("success", true);
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // 添加用户活动记录的辅助方法
    private void addUserActivity(String description) {
        Map<String, Object> activity = new HashMap<>();
        activity.put("id", System.currentTimeMillis());
        activity.put("description", description);
        activity.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        userActivities.add(activity);
        
        // 保持最多100条记录
        if (userActivities.size() > 100) {
            userActivities.remove(0);
        }
    }
}