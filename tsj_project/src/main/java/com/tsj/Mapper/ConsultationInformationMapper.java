package com.tsj.Mapper;

import com.tsj.project.ConsultationInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 在线咨询信息Mapper接口
 */
@Mapper
public interface ConsultationInformationMapper {
    
    /**
     * 添加新的咨询问题
     * @param consultation 咨询信息
     * @return 插入的记录数
     */
    int insertConsultation(ConsultationInformation consultation);
    
    /**
     * 根据教师ID获取所有咨询问题（包括学生信息）
     * @param teacherId 教师ID
     * @return 咨询问题列表
     */
    List<ConsultationInformation> getConsultationsByTeacherId(Integer teacherId);
    
    /**
     * 根据学生ID获取所有咨询记录（包括教师信息）
     * @param studentId 学生ID
     * @return 咨询记录列表
     */
    List<ConsultationInformation> getConsultationsByStudentId(Integer studentId);
    
    /**
     * 根据咨询ID更新回答
     * @param consultationId 咨询ID
     * @param answer 回答内容
     * @return 更新的记录数
     */
    int updateAnswer(Integer consultationId, String answer);
    
    /**
     * 根据咨询ID获取单条咨询记录
     * @param consultationId 咨询ID
     * @return 咨询记录
     */
    ConsultationInformation getConsultationById(Integer consultationId);
    
    /**
     * 获取教师的待回答咨询数量
     * @param teacherId 教师ID
     * @return 待回答数量
     */
    int getPendingConsultationCount(Integer teacherId);
}
