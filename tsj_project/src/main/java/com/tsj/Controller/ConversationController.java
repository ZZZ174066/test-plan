package com.tsj.Controller;

import com.tsj.Service.ConversationService;
import com.tsj.project.Conversation;
import com.tsj.project.ConversationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversation")
@CrossOrigin(origins = "http://localhost:8081")
public class ConversationController {
    
    @Autowired
    private ConversationService conversationService;
    
    /**
     * 发送消息
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer teacherId = (Integer) request.get("teacherId");
            Integer studentId = (Integer) request.get("studentId");
            String content = (String) request.get("content");
            Integer senderType = (Integer) request.get("senderType");
            
            if (teacherId == null || studentId == null || content == null || content.trim().isEmpty() || senderType == null) {
                response.put("success", false);
                response.put("message", "参数不完整");
                return ResponseEntity.badRequest().body(response);
            }
            
            ConversationMessage message = new ConversationMessage(teacherId, studentId, content.trim(), senderType);
            boolean result = conversationService.sendMessage(message);
            
            if (result) {
                response.put("success", true);
                response.put("message", "消息发送成功");
            } else {
                response.put("success", false);
                response.put("message", "消息发送失败");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "服务器错误: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取教师的对话列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<Map<String, Object>> getTeacherConversations(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Conversation> conversations = conversationService.getConversationsByTeacherId(teacherId);
            
            if (conversations != null) {
                response.put("success", true);
                response.put("data", conversations);
                response.put("message", "获取对话列表成功");
            } else {
                response.put("success", false);
                response.put("message", "获取对话列表失败");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "服务器错误: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取学生的对话列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentConversations(@PathVariable Integer studentId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Conversation> conversations = conversationService.getConversationsByStudentId(studentId);
            
            if (conversations != null) {
                response.put("success", true);
                response.put("data", conversations);
                response.put("message", "获取对话列表成功");
            } else {
                response.put("success", false);
                response.put("message", "获取对话列表失败");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "服务器错误: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取师生之间的消息列表
     */
    @GetMapping("/messages/{teacherId}/{studentId}")
    public ResponseEntity<Map<String, Object>> getMessages(
            @PathVariable Integer teacherId, 
            @PathVariable Integer studentId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ConversationMessage> messages = conversationService.getMessagesByTeacherAndStudent(teacherId, studentId);
            
            if (messages != null) {
                response.put("success", true);
                response.put("data", messages);
                response.put("message", "获取消息列表成功");
            } else {
                response.put("success", false);
                response.put("message", "获取消息列表失败");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "服务器错误: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{teacherId}/{studentId}/{readerType}")
    public ResponseEntity<Map<String, Object>> markAsRead(
            @PathVariable Integer teacherId, 
            @PathVariable Integer studentId,
            @PathVariable Integer readerType) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean result = conversationService.markMessagesAsRead(teacherId, studentId, readerType);
            
            if (result) {
                response.put("success", true);
                response.put("message", "标记已读成功");
            } else {
                response.put("success", false);
                response.put("message", "标记已读失败");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "服务器错误: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
