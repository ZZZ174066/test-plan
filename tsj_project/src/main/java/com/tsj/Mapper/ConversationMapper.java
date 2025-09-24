package com.tsj.Mapper;

import com.tsj.project.Conversation;
import com.tsj.project.ConversationMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConversationMapper {
    
    /**
     * 发送消息
     */
    int insertMessage(ConversationMessage message);
    
    /**
     * 获取教师的对话列表
     */
    List<Conversation> getConversationsByTeacherId(@Param("teacherId") Integer teacherId);
    
    /**
     * 获取师生之间的消息列表
     */
    List<ConversationMessage> getMessagesByTeacherAndStudent(
        @Param("teacherId") Integer teacherId, 
        @Param("studentId") Integer studentId
    );
    
    /**
     * 标记消息为已读
     */
    int markMessagesAsRead(
        @Param("teacherId") Integer teacherId, 
        @Param("studentId") Integer studentId,
        @Param("readerType") Integer readerType
    );
    
    /**
     * 获取学生的对话列表
     */
    List<Conversation> getConversationsByStudentId(@Param("studentId") Integer studentId);
}
