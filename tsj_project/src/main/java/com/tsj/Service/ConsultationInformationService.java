package com.tsj.Service;

import com.tsj.Mapper.ConsultationInformationMapper;
import com.tsj.project.ConsultationInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线咨询信息服务类
 */
@Service
public class ConsultationInformationService {
    
    @Autowired
    private ConsultationInformationMapper consultationMapper;
    
    /**
     * 学生提交咨询问题
     * @param studentId 学生ID
     * @param teacherId 教师ID
     * @param question 问题内容
     * @return 咨询记录
     */
    public ConsultationInformation submitQuestion(Integer studentId, Integer teacherId, String question) {
        ConsultationInformation consultation = new ConsultationInformation();
        consultation.setStudentId(studentId);
        consultation.setTeacherId(teacherId);
        consultation.setQuestion(question);
        consultation.setQuestionTime(LocalDateTime.now());
        consultation.setStatus(0); // 待回答
        
        int result = consultationMapper.insertConsultation(consultation);
        if (result > 0) {
            return consultation;
        }
        return null;
    }
    
    /**
     * 教师回答咨询问题
     * @param consultationId 咨询ID
     * @param answer 回答内容
     * @return 是否成功
     */
    public boolean answerQuestion(Integer consultationId, String answer) {
        int result = consultationMapper.updateAnswer(consultationId, answer);
        return result > 0;
    }
    
    /**
     * 获取教师的所有咨询问题
     * @param teacherId 教师ID
     * @return 咨询列表
     */
    public List<ConsultationInformation> getTeacherConsultations(Integer teacherId) {
        return consultationMapper.getConsultationsByTeacherId(teacherId);
    }
    
    /**
     * 获取学生的所有咨询记录
     * @param studentId 学生ID
     * @return 咨询记录列表
     */
    public List<ConsultationInformation> getStudentConsultations(Integer studentId) {
        return consultationMapper.getConsultationsByStudentId(studentId);
    }
    
    /**
     * 根据ID获取咨询记录
     * @param consultationId 咨询ID
     * @return 咨询记录
     */
    public ConsultationInformation getConsultationById(Integer consultationId) {
        return consultationMapper.getConsultationById(consultationId);
    }
    
    /**
     * 获取教师待回答咨询数量
     * @param teacherId 教师ID
     * @return 待回答数量
     */
    public int getPendingConsultationCount(Integer teacherId) {
        return consultationMapper.getPendingConsultationCount(teacherId);
    }
}
