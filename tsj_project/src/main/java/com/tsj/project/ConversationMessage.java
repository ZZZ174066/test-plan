package com.tsj.project;

import java.util.Date;

/**
 * 对话消息实体类
 */
public class ConversationMessage {
    private Integer messageId;
    private Integer teacherId;
    private Integer studentId;
    private String content;
    private Integer senderType; // 1-学生发送, 2-教师发送
    private Date sendTime;
    private Integer isRead; // 0-未读, 1-已读
    
    // 关联信息字段（用于显示）
    private String senderName;
    private String senderAvatar;
    
    public ConversationMessage() {}
    
    public ConversationMessage(Integer teacherId, Integer studentId, String content, Integer senderType) {
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.content = content;
        this.senderType = senderType;
        this.sendTime = new Date();
        this.isRead = 0;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSenderType() {
        return senderType;
    }

    public void setSenderType(Integer senderType) {
        this.senderType = senderType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }
}
