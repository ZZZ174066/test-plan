package com.tsj.project;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 通知信息实体类
 */
public class NotificationInformation {
    
    private Integer notificationId;          // 通知ID，主键，自增
    private String notificationSubject;      // 通知主题，上限20字
    private String notificationCourse;       // 通知学科，上限20字
    private String notificationContent;      // 通知正文，上限300字
    private LocalDate publishDate;           // 发布时间，年月日
    private LocalDateTime createdAt;         // 创建时间戳
    private LocalDateTime updatedAt;         // 更新时间戳

    // 无参构造函数
    public NotificationInformation() {}

    // 全参构造函数
    public NotificationInformation(Integer notificationId, String notificationSubject, 
                                 String notificationCourse, String notificationContent, 
                                 LocalDate publishDate, LocalDateTime createdAt, 
                                 LocalDateTime updatedAt) {
        this.notificationId = notificationId;
        this.notificationSubject = notificationSubject;
        this.notificationCourse = notificationCourse;
        this.notificationContent = notificationContent;
        this.publishDate = publishDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter和Setter方法
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    public String getNotificationCourse() {
        return notificationCourse;
    }

    public void setNotificationCourse(String notificationCourse) {
        this.notificationCourse = notificationCourse;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "NotificationInformation{" +
                "notificationId=" + notificationId +
                ", notificationSubject='" + notificationSubject + '\'' +
                ", notificationCourse='" + notificationCourse + '\'' +
                ", notificationContent='" + notificationContent + '\'' +
                ", publishDate=" + publishDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
