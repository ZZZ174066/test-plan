package com.tsj.Service;

import com.tsj.Mapper.ConversationMapper;
import com.tsj.project.Conversation;
import com.tsj.project.ConversationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {
    
    @Autowired
    private ConversationMapper conversationMapper;
    
    /**
     * 发送消息
     */
    public boolean sendMessage(ConversationMessage message) {
        try {
            int result = conversationMapper.insertMessage(message);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取教师的对话列表
     */
    public List<Conversation> getConversationsByTeacherId(Integer teacherId) {
        try {
            return conversationMapper.getConversationsByTeacherId(teacherId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取学生的对话列表
     */
    public List<Conversation> getConversationsByStudentId(Integer studentId) {
        try {
            return conversationMapper.getConversationsByStudentId(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取师生之间的消息列表
     */
    public List<ConversationMessage> getMessagesByTeacherAndStudent(Integer teacherId, Integer studentId) {
        try {
            return conversationMapper.getMessagesByTeacherAndStudent(teacherId, studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 标记消息为已读
     */
    public boolean markMessagesAsRead(Integer teacherId, Integer studentId, Integer readerType) {
        try {
            int result = conversationMapper.markMessagesAsRead(teacherId, studentId, readerType);
            return result >= 0; // 即使没有未读消息，也返回true
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
