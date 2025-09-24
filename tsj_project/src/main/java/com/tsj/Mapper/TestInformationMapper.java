package com.tsj.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.tsj.project.TestInformation;
import java.time.LocalDate;
import java.util.List;

/**
 * 考试信息 Mapper 接口
 * 提供考试信息的数据访问操作
 */
@Mapper
public interface TestInformationMapper {
    
    /**
     * 获取所有考试信息
     * @return 考试信息列表
     */
    List<TestInformation> getAllTestInformation();
    
    /**
     * 根据考试ID获取考试信息
     * @param testId 考试ID
     * @return 考试信息
     */
    TestInformation getTestInformationById(Long testId);
    
    /**
     * 根据课程ID获取考试信息
     * @param courseId 课程ID
     * @return 考试信息
     */
    TestInformation getTestInformationByCourseId(Long courseId);
    
    /**
     * 新增考试信息
     * @param testInformation 考试信息对象
     */
    void insertTestInformation(TestInformation testInformation);
    
    /**
     * 更新考试信息
     * @param testInformation 考试信息对象
     */
    void updateTestInformation(TestInformation testInformation);
    
    /**
     * 删除考试信息
     * @param testId 考试ID
     */
    void deleteTestInformation(Long testId);
    
    /**
     * 根据课程ID删除考试信息
     * @param courseId 课程ID
     */
    void deleteTestInformationByCourseId(Long courseId);
    
    /**
     * 根据考试状态获取考试信息列表
     * @param examStatus 考试状态（未安排/已安排/已考试）
     * @return 考试信息列表
     */
    List<TestInformation> getTestInformationByStatus(String examStatus);
    
    /**
     * 根据考试日期获取考试信息列表
     * @param examDate 考试日期
     * @return 考试信息列表
     */
    List<TestInformation> getTestInformationByDate(LocalDate examDate);
    
    /**
     * 根据考试时段获取考试信息列表
     * @param examSession 考试时段（第一场/第二场/第三场）
     * @return 考试信息列表
     */
    List<TestInformation> getTestInformationBySession(String examSession);
    
    /**
     * 根据考试地点获取考试信息列表
     * @param examLocation 考试地点
     * @return 考试信息列表
     */
    List<TestInformation> getTestInformationByLocation(String examLocation);
    
    /**
     * 根据日期范围获取考试信息列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考试信息列表
     */
    List<TestInformation> getTestInformationByDateRange(@Param("startDate") LocalDate startDate, 
                                                        @Param("endDate") LocalDate endDate);
    
    /**
     * 更新考试状态
     * @param testId 考试ID
     * @param examStatus 新的考试状态
     */
    void updateExamStatus(@Param("testId") Long testId, @Param("examStatus") String examStatus);
    
    /**
     * 安排考试（更新考试日期、时段、地点和状态）
     * @param testId 考试ID
     * @param examDate 考试日期
     * @param examSession 考试时段
     * @param examLocation 考试地点
     */
    void scheduleExam(@Param("testId") Long testId, 
                     @Param("examDate") LocalDate examDate,
                     @Param("examSession") String examSession, 
                     @Param("examLocation") String examLocation);
    
    /**
     * 取消考试安排（将状态重置为未安排，清空日期、时段、地点）
     * @param testId 考试ID
     */
    void cancelExamSchedule(Long testId);
    
    /**
     * 统计各状态的考试数量
     * @return 包含状态统计的列表
     */
    List<java.util.Map<String, Object>> getExamStatusStatistics();
    
    /**
     * 根据课程名称模糊查询考试信息
     * @param courseName 课程名称关键字
     * @return 考试信息列表
     */
    List<TestInformation> searchTestInformationByCourseName(String courseName);
    
    /**
     * 根据教师ID获取该教师教授学科的考试信息列表
     * @param teacherId 教师ID
     * @return 考试信息列表
     */
    List<TestInformation> getExamsByTeacherId(Integer teacherId);
    
    /**
     * 根据教师ID统计该教师教授学科的考试状态数量
     * @param teacherId 教师ID
     * @return 状态统计信息
     */
    List<java.util.Map<String, Object>> getExamStatusStatisticsByTeacherId(Integer teacherId);
}
