package com.tsj.Service;

import com.tsj.Mapper.TeachInformationMapper;
import com.tsj.project.TeachInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 教学信息服务类
 */
@Service
public class TeachInformationService {
    
    @Autowired
    private TeachInformationMapper teachInformationMapper;
    
    /**
     * 获取所有教学信息
     * @return 教学信息列表
     */
    public List<TeachInformation> getAllTeachInformation() {
        return teachInformationMapper.getAllTeachInformation();
    }
    
    /**
     * 根据教师ID获取教学信息
     * @param teacherId 教师ID
     * @return 教学信息
     */
    public TeachInformation getTeachInformationByTeacherId(Integer teacherId) {
        return teachInformationMapper.getTeachInformationByTeacherId(teacherId);
    }
    
    /**
     * 插入教学信息
     * @param teachInformation 教学信息对象
     */
    public void insertTeachInformation(TeachInformation teachInformation) {
        teachInformationMapper.insertTeachInformation(teachInformation);
    }
    
    /**
     * 更新教学信息
     * @param teachInformation 教学信息对象
     */
    public void updateTeachInformation(TeachInformation teachInformation) {
        teachInformationMapper.updateTeachInformation(teachInformation);
    }
    
    /**
     * 删除教学信息
     * @param teacherId 教师ID
     */
    public void deleteTeachInformation(Integer teacherId) {
        teachInformationMapper.deleteTeachInformation(teacherId);
    }
    
    /**
     * 根据学科搜索教师
     * @param subject 学科名称
     * @return 教学信息列表
     */
    public List<TeachInformation> getTeachersBySubject(String subject) {
        return teachInformationMapper.getTeachersBySubject(subject);
    }
    
    /**
     * 获取教师数量统计
     * @return 教师总数
     */
    public int getTeacherCount() {
        return teachInformationMapper.getTeacherCount();
    }
    
    /**
     * 获取有教学任务的教师数量
     * @return 有教学任务的教师数量
     */
    public int getActiveTeacherCount() {
        return teachInformationMapper.getActiveTeacherCount();
    }
}
