package com.tsj.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.tsj.project.DeclareInformation;
import java.util.List;

/**
 * 课程报名信息Mapper接口
 */
@Mapper
public interface DeclareInformationMapper {
    
    /**
     * 获取所有报名信息
     * @return 报名信息列表
     */
    List<DeclareInformation> getAllDeclareInformation();
    
    /**
     * 根据用户ID获取报名信息
     * @param id 用户ID
     * @return 报名信息
     */
    DeclareInformation getDeclareInformationById(Integer id);
    
    /**
     * 插入报名信息
     * @param declareInformation 报名信息对象
     */
    void insertDeclareInformation(DeclareInformation declareInformation);
    
    /**
     * 更新报名信息
     * @param declareInformation 报名信息对象
     */
    void updateDeclareInformation(DeclareInformation declareInformation);
    
    /**
     * 删除报名信息
     * @param id 用户ID
     */
    void deleteDeclareInformation(Integer id);
    
    /**
     * 更新单个课程的报名状态
     * @param id 用户ID
     * @param courseName 课程名称
     * @param status 报名状态
     */
    void updateCourseStatus(@Param("id") Integer id, @Param("courseName") String courseName, @Param("status") String status);
    
    /**
     * 获取指定用户的指定课程报名状态
     * @param id 用户ID
     * @param courseName 课程名称
     * @return 报名状态
     */
    String getCourseStatus(@Param("id") Integer id, @Param("courseName") String courseName);
    
    /**
     * 批量更新多个课程的报名状态
     * @param id 用户ID
     * @param courseStatuses 课程状态映射
     */
    void batchUpdateCourseStatus(@Param("id") Integer id, @Param("courseStatuses") java.util.Map<String, String> courseStatuses);
    
    /**
     * 获取指定状态的报名记录数量
     * @param status 报名状态
     * @return 记录数量
     */
    int countByStatus(String status);
    
    /**
     * 获取用户的报名统计信息
     * @param id 用户ID
     * @return 统计信息Map，包含未报名、已报名、已通过的数量
     */
    java.util.Map<String, Integer> getDeclareStatistics(Integer id);
}
