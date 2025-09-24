package com.tsj.Service;

import com.tsj.Mapper.DeclareInformationMapper;
import com.tsj.project.DeclareInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 课程报名信息服务层
 */
@Service
public class DeclareInformationService {
    
    @Autowired
    private DeclareInformationMapper declareInformationMapper;
    
    /**
     * 获取所有报名信息
     * @return 报名信息列表
     */
    public List<DeclareInformation> getAllDeclareInformation() {
        return declareInformationMapper.getAllDeclareInformation();
    }
    
    /**
     * 根据用户ID获取报名信息
     * @param id 用户ID
     * @return 报名信息
     */
    public DeclareInformation getDeclareInformationById(Integer id) {
        return declareInformationMapper.getDeclareInformationById(id);
    }
    
    /**
     * 插入报名信息
     * @param declareInformation 报名信息对象
     */
    public void insertDeclareInformation(DeclareInformation declareInformation) {
        declareInformationMapper.insertDeclareInformation(declareInformation);
    }
    
    /**
     * 更新报名信息
     * @param declareInformation 报名信息对象
     */
    public void updateDeclareInformation(DeclareInformation declareInformation) {
        // 验证用户ID是否存在
        if (declareInformation.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查报名记录是否存在
        DeclareInformation existing = declareInformationMapper.getDeclareInformationById(declareInformation.getId());
        if (existing == null) {
            throw new IllegalArgumentException("报名记录不存在");
        }
        
        declareInformationMapper.updateDeclareInformation(declareInformation);
    }
    
    /**
     * 删除报名信息
     * @param id 用户ID
     */
    public void deleteDeclareInformation(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        declareInformationMapper.deleteDeclareInformation(id);
    }
    
    /**
     * 更新单个课程的报名状态
     * @param id 用户ID
     * @param courseName 课程名称
     * @param status 报名状态
     */
    public void updateCourseStatus(Integer id, String courseName, String status) {
        // 验证参数
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("报名状态不能为空");
        }
        
        // 验证状态值
        if (!"未报名".equals(status) && !"已报名".equals(status) && !"已通过".equals(status)) {
            throw new IllegalArgumentException("报名状态值无效，只能是：未报名、已报名、已通过");
        }
        
        // 检查报名记录是否存在
        DeclareInformation existing = declareInformationMapper.getDeclareInformationById(id);
        if (existing == null) {
            throw new IllegalArgumentException("报名记录不存在");
        }
        
        declareInformationMapper.updateCourseStatus(id, courseName, status);
    }
    
    /**
     * 获取指定用户的指定课程报名状态
     * @param id 用户ID
     * @param courseName 课程名称
     * @return 报名状态
     */
    public String getCourseStatus(Integer id, String courseName) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        
        return declareInformationMapper.getCourseStatus(id, courseName);
    }
    
    /**
     * 批量更新多个课程的报名状态
     * @param id 用户ID
     * @param courseStatuses 课程状态映射
     */
    public void batchUpdateCourseStatus(Integer id, Map<String, String> courseStatuses) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (courseStatuses == null || courseStatuses.isEmpty()) {
            throw new IllegalArgumentException("课程状态映射不能为空");
        }
        
        // 验证所有状态值
        for (Map.Entry<String, String> entry : courseStatuses.entrySet()) {
            String status = entry.getValue();
            if (!"未报名".equals(status) && !"已报名".equals(status) && !"已通过".equals(status)) {
                throw new IllegalArgumentException("报名状态值无效：" + status + "，只能是：未报名、已报名、已通过");
            }
        }
        
        // 检查报名记录是否存在
        DeclareInformation existing = declareInformationMapper.getDeclareInformationById(id);
        if (existing == null) {
            throw new IllegalArgumentException("报名记录不存在");
        }
        
        declareInformationMapper.batchUpdateCourseStatus(id, courseStatuses);
    }
    
    /**
     * 获取指定状态的报名记录数量
     * @param status 报名状态
     * @return 记录数量
     */
    public int countByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("报名状态不能为空");
        }
        
        // 验证状态值
        if (!"未报名".equals(status) && !"已报名".equals(status) && !"已通过".equals(status)) {
            throw new IllegalArgumentException("报名状态值无效，只能是：未报名、已报名、已通过");
        }
        
        return declareInformationMapper.countByStatus(status);
    }
    
    /**
     * 获取用户的报名统计信息
     * @param id 用户ID
     * @return 统计信息Map，包含未报名、已报名、已通过的数量
     */
    public Map<String, Integer> getDeclareStatistics(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查报名记录是否存在
        DeclareInformation existing = declareInformationMapper.getDeclareInformationById(id);
        if (existing == null) {
            throw new IllegalArgumentException("报名记录不存在");
        }
        
        return declareInformationMapper.getDeclareStatistics(id);
    }
    
    /**
     * 为新学生用户创建报名记录
     * @param userId 用户ID
     */
    public void createDeclareRecordForNewStudent(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查报名记录是否已存在
        DeclareInformation existing = declareInformationMapper.getDeclareInformationById(userId);
        if (existing != null) {
            throw new IllegalArgumentException("报名记录已存在");
        }
        
        // 创建新的报名记录
        DeclareInformation declareInfo = new DeclareInformation();
        declareInfo.setId(userId);
        declareInformationMapper.insertDeclareInformation(declareInfo);
    }
    
    /**
     * 检查报名记录是否存在
     * @param id 用户ID
     * @return 是否存在
     */
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        return declareInformationMapper.getDeclareInformationById(id) != null;
    }
}
