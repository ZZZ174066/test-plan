package com.tsj.Controller;

import com.tsj.Service.TextbookInformationService;
import com.tsj.project.TextbookInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教材信息控制器
 */
@RestController
@RequestMapping("/api/textbook")
@CrossOrigin
public class TextbookInformationController {
    
    @Autowired
    private TextbookInformationService textbookService;
    
    /**
     * 创建教材信息
     * @param textbook 教材信息对象
     * @return 响应结果
     */
    @PostMapping("/createTextbook")
    public ResponseEntity<Map<String, Object>> createTextbook(@RequestBody TextbookInformation textbook) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = textbookService.createTextbook(textbook);
            if (success) {
                response.put("success", true);
                response.put("message", "教材创建成功");
            } else {
                response.put("success", false);
                response.put("message", "教材创建失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据ID获取教材信息
     * @param textbookId 教材ID
     * @return 教材信息
     */
    @GetMapping("/{textbookId}")
    public ResponseEntity<Map<String, Object>> getTextbookById(@PathVariable Integer textbookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            TextbookInformation textbook = textbookService.getTextbookById(textbookId);
            if (textbook != null) {
                response.put("success", true);
                response.put("data", textbook);
            } else {
                response.put("success", false);
                response.put("message", "教材不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取所有教材信息
     * @return 教材信息列表
     */
    @GetMapping("/getAllTextbooks")
    public ResponseEntity<Map<String, Object>> getAllTextbooks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TextbookInformation> textbooks = textbookService.getAllTextbooks();
            response.put("success", true);
            response.put("data", textbooks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新教材信息
     * @param textbook 教材信息对象
     * @return 响应结果
     */
    @PostMapping("/updateTextbook")
    public ResponseEntity<Map<String, Object>> updateTextbook(@RequestBody TextbookInformation textbook) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = textbookService.updateTextbook(textbook);
            if (success) {
                response.put("success", true);
                response.put("message", "教材更新成功");
            } else {
                response.put("success", false);
                response.put("message", "教材更新失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除教材信息
     * @param textbookId 教材ID
     * @return 响应结果
     */
    @DeleteMapping("/{textbookId}")
    public ResponseEntity<Map<String, Object>> deleteTextbook(@PathVariable Integer textbookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = textbookService.deleteTextbook(textbookId);
            if (success) {
                response.put("success", true);
                response.put("message", "教材删除成功");
            } else {
                response.put("success", false);
                response.put("message", "教材删除失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据教材名搜索教材
     * @param textbookName 教材名
     * @return 教材信息列表
     */
    @GetMapping("/searchByName")
    public ResponseEntity<Map<String, Object>> searchTextbooksByName(@RequestParam String textbookName) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TextbookInformation> textbooks = textbookService.searchTextbooksByName(textbookName);
            response.put("success", true);
            response.put("data", textbooks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据作者搜索教材
     * @param author 作者名
     * @return 教材信息列表
     */
    @GetMapping("/searchByAuthor")
    public ResponseEntity<Map<String, Object>> searchTextbooksByAuthor(@RequestParam String author) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TextbookInformation> textbooks = textbookService.searchTextbooksByAuthor(author);
            response.put("success", true);
            response.put("data", textbooks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据出版社搜索教材
     * @param publisher 出版社名
     * @return 教材信息列表
     */
    @GetMapping("/searchByPublisher")
    public ResponseEntity<Map<String, Object>> searchTextbooksByPublisher(@RequestParam String publisher) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TextbookInformation> textbooks = textbookService.searchTextbooksByPublisher(publisher);
            response.put("success", true);
            response.put("data", textbooks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取教材总数
     * @return 教材总数
     */
    @GetMapping("/getTextbookCount")
    public ResponseEntity<Map<String, Object>> getTextbookCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            int count = textbookService.getTextbookCount();
            response.put("success", true);
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 分页获取教材信息
     * @param page 页码
     * @param size 每页大小
     * @return 教材信息列表
     */
    @GetMapping("/getTextbooksWithPagination")
    public ResponseEntity<Map<String, Object>> getTextbooksWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TextbookInformation> textbooks = textbookService.getTextbooksWithPagination(page, size);
            int totalCount = textbookService.getTextbookCount();
            
            response.put("success", true);
            response.put("data", textbooks);
            response.put("totalCount", totalCount);
            response.put("currentPage", page);
            response.put("pageSize", size);
            response.put("totalPages", (int) Math.ceil((double) totalCount / size));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
