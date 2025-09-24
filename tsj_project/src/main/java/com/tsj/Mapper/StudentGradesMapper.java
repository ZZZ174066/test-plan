package com.tsj.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.tsj.project.StudentGrades;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 学生成绩信息Mapper接口
 */
@Mapper
public interface StudentGradesMapper {
    
    /**
     * 获取所有学生成绩信息
     * @return 学生成绩信息列表
     */
    List<StudentGrades> getAllStudentGrades();
    
    /**
     * 根据用户ID获取学生成绩信息
     * @param id 用户ID
     * @return 学生成绩信息
     */
    StudentGrades getStudentGradesById(Integer id);
    
    /**
     * 插入学生成绩信息
     * @param studentGrades 学生成绩信息对象
     */
    void insertStudentGrades(StudentGrades studentGrades);
    
    /**
     * 更新学生成绩信息
     * @param studentGrades 学生成绩信息对象
     */
    void updateStudentGrades(StudentGrades studentGrades);
    
    /**
     * 删除学生成绩信息
     * @param id 用户ID
     */
    void deleteStudentGrades(Integer id);
    
    /**
     * 更新单个课程的成绩
     * @param id 用户ID
     * @param courseName 课程名称
     * @param grade 成绩
     */
    void updateCourseGrade(@Param("id") Integer id, @Param("courseName") String courseName, @Param("grade") BigDecimal grade);
    
    /**
     * 获取指定用户的指定课程成绩
     * @param id 用户ID
     * @param courseName 课程名称
     * @return 成绩
     */
    BigDecimal getCourseGrade(@Param("id") Integer id, @Param("courseName") String courseName);
    
    
    /**
     * 获取用户的成绩统计信息
     * @param id 用户ID
     * @return 统计信息Map，包含总科目数、已录入成绩数、平均分等
     */
    Map<String, Object> getGradeStatistics(Integer id);
    
    /**
     * 获取指定成绩范围的学生数量
     * @param minGrade 最低成绩
     * @param maxGrade 最高成绩
     * @return 学生数量
     */
    int countStudentsByGradeRange(@Param("minGrade") BigDecimal minGrade, @Param("maxGrade") BigDecimal maxGrade);
    
    /**
     * 获取用户的及格课程数量
     * @param id 用户ID
     * @return 及格课程数量
     */
    int getPassedCoursesCount(Integer id);
    
    /**
     * 获取用户的不及格课程数量
     * @param id 用户ID
     * @return 不及格课程数量
     */
    int getFailedCoursesCount(Integer id);
    
    /**
     * 获取用户的平均成绩
     * @param id 用户ID
     * @return 平均成绩
     */
    BigDecimal getAverageGrade(Integer id);
    
    /**
     * 获取用户已录入成绩的课程数量
     * @param id 用户ID
     * @return 已录入成绩的课程数量
     */
    int getGradedCoursesCount(Integer id);
    
    /**
     * 获取用户未录入成绩的课程数量
     * @param id 用户ID
     * @return 未录入成绩的课程数量
     */
    int getUngradedCoursesCount(Integer id);

    /**
     * 获取教师教授且已考试的学科列表
     * @param teacherId 教师ID
     * @return 学科列表
     */
    List<String> getTeacherExaminedSubjects(Integer teacherId);

    /**
     * 获取指定学科的学生成绩列表
     * @param subject 学科名称
     * @return 学生成绩列表（包含学生ID、姓名和成绩）
     */
    List<Map<String, Object>> getStudentGradesBySubject(@Param("subject") String subject);
}
