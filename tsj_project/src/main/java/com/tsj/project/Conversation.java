package com.tsj.project;

import java.util.Date;

/**
 * 对话实体类
 */
public class Conversation {
    private Integer teacherId;
    private Integer studentId;
    private String studentName;
    private String studentAvatar;
    private Integer messageCount;
    private Date lastMessageTime;
    private String lastMessageContent;
    private Integer hasUnread; // 0-无未读, 1-有未读
    
    public Conversation() {}

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public Integer getHasUnread() {
        return hasUnread;
    }

    public void setHasUnread(Integer hasUnread) {
        this.hasUnread = hasUnread;
    }
}
