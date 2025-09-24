package com.tsj.Service;

import com.tsj.project.NotificationInformation;
import com.tsj.Mapper.NotificationInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 通知信息服务类
 */
@Service
public class NotificationInformationService {
    
    @Autowired
    private NotificationInformationMapper notificationInformationMapper;
    
    /**
     * 获取所有通知信息
     * @return 通知信息列表
     */
    public List<NotificationInformation> getAllNotifications() {
        try {
            return notificationInformationMapper.getAllNotifications();
        } catch (Exception e) {
            throw new RuntimeException("获取通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据ID获取通知信息
     * @param notificationId 通知ID
     * @return 通知信息
     */
    public NotificationInformation getNotificationById(Integer notificationId) {
        try {
            if (notificationId == null) {
                throw new IllegalArgumentException("通知ID不能为空");
            }
            return notificationInformationMapper.getNotificationById(notificationId);
        } catch (Exception e) {
            throw new RuntimeException("获取通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据学科获取通知信息
     * @param course 学科名称
     * @return 通知信息列表
     */
    public List<NotificationInformation> getNotificationsByCourse(String course) {
        try {
            if (course == null || course.trim().isEmpty()) {
                throw new IllegalArgumentException("学科名称不能为空");
            }
            return notificationInformationMapper.getNotificationsByCourse(course);
        } catch (Exception e) {
            throw new RuntimeException("获取学科通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 插入新通知信息
     * @param notification 通知信息对象
     * @return 是否成功
     */
    public boolean insertNotification(NotificationInformation notification) {
        try {
            validateNotification(notification);
            int result = notificationInformationMapper.insertNotification(notification);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("创建通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新通知信息
     * @param notification 通知信息对象
     * @return 是否成功
     */
    public boolean updateNotification(NotificationInformation notification) {
        try {
            if (notification.getNotificationId() == null) {
                throw new IllegalArgumentException("通知ID不能为空");
            }
            validateNotification(notification);
            int result = notificationInformationMapper.updateNotification(notification);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("更新通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除通知信息
     * @param notificationId 通知ID
     * @return 是否成功
     */
    public boolean deleteNotification(Integer notificationId) {
        try {
            if (notificationId == null) {
                throw new IllegalArgumentException("通知ID不能为空");
            }
            int result = notificationInformationMapper.deleteNotification(notificationId);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取最近的通知信息
     * @param limit 限制数量
     * @return 通知信息列表
     */
    public List<NotificationInformation> getRecentNotifications(Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10; // 默认返回10条
            }
            return notificationInformationMapper.getRecentNotifications(limit);
        } catch (Exception e) {
            throw new RuntimeException("获取最近通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据关键词搜索通知信息
     * @param keyword 关键词
     * @return 通知信息列表
     */
    public List<NotificationInformation> searchNotifications(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new IllegalArgumentException("搜索关键词不能为空");
            }
            return notificationInformationMapper.searchNotifications(keyword.trim());
        } catch (Exception e) {
            throw new RuntimeException("搜索通知信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 验证通知信息的有效性
     * @param notification 通知信息对象
     */
    private void validateNotification(NotificationInformation notification) {
        if (notification == null) {
            throw new IllegalArgumentException("通知信息不能为空");
        }
        
        if (notification.getNotificationSubject() == null || notification.getNotificationSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("通知主题不能为空");
        }
        
        if (notification.getNotificationSubject().length() > 20) {
            throw new IllegalArgumentException("通知主题不能超过20个字符");
        }
        
        if (notification.getNotificationCourse() == null || notification.getNotificationCourse().trim().isEmpty()) {
            throw new IllegalArgumentException("通知学科不能为空");
        }
        
        if (notification.getNotificationCourse().length() > 20) {
            throw new IllegalArgumentException("通知学科不能超过20个字符");
        }
        
        if (notification.getNotificationContent() == null || notification.getNotificationContent().trim().isEmpty()) {
            throw new IllegalArgumentException("通知正文不能为空");
        }
        
        if (notification.getNotificationContent().length() > 300) {
            throw new IllegalArgumentException("通知正文不能超过300个字符");
        }
        
        if (notification.getPublishDate() == null) {
            throw new IllegalArgumentException("发布时间不能为空");
        }
    }
}
