package com.tsj.Mapper;

import com.tsj.project.TeachInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教学信息Mapper接口
 */
@Mapper
public interface TeachInformationMapper {
    
    /**
     * 获取所有教学信息
     * @return 教学信息列表
     */
    List<TeachInformation> getAllTeachInformation();
    
    /**
     * 根据教师ID获取教学信息
     * @param teacherId 教师ID
     * @return 教学信息
     */
    TeachInformation getTeachInformationByTeacherId(@Param("teacherId") Integer teacherId);
    
    /**
     * 插入教学信息
     * @param teachInformation 教学信息对象
     */
    void insertTeachInformation(TeachInformation teachInformation);
    
    /**
     * 更新教学信息
     * @param teachInformation 教学信息对象
     */
    void updateTeachInformation(TeachInformation teachInformation);
    
    /**
     * 删除教学信息
     * @param teacherId 教师ID
     */
    void deleteTeachInformation(@Param("teacherId") Integer teacherId);
    
    /**
     * 根据学科搜索教师
     * @param subject 学科名称
     * @return 教学信息列表
     */
    List<TeachInformation> getTeachersBySubject(@Param("subject") String subject);
    
    /**
     * 获取教师数量统计
     * @return 教师总数
     */
    int getTeacherCount();
    
    /**
     * 获取有教学任务的教师数量
     * @return 有教学任务的教师数量
     */
    int getActiveTeacherCount();
}
