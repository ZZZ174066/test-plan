package com.tsj.Service;

import com.tsj.Mapper.StudentGradesMapper;
import com.tsj.project.StudentGrades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 学生成绩信息服务层
 */
@Service
public class StudentGradesService {
    
    @Autowired
    private StudentGradesMapper studentGradesMapper;
    
    /**
     * 获取所有学生成绩信息
     * @return 学生成绩信息列表
     */
    public List<StudentGrades> getAllStudentGrades() {
        return studentGradesMapper.getAllStudentGrades();
    }
    
    /**
     * 根据用户ID获取学生成绩信息
     * @param id 用户ID
     * @return 学生成绩信息
     */
    public StudentGrades getStudentGradesById(Integer id) {
        return studentGradesMapper.getStudentGradesById(id);
    }
    
    /**
     * 插入学生成绩信息
     * @param studentGrades 学生成绩信息对象
     */
    public void insertStudentGrades(StudentGrades studentGrades) {
        studentGradesMapper.insertStudentGrades(studentGrades);
    }
    
    /**
     * 更新学生成绩信息
     * @param studentGrades 学生成绩信息对象
     */
    public void updateStudentGrades(StudentGrades studentGrades) {
        // 验证用户ID是否存在
        if (studentGrades.getId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查成绩记录是否存在
        StudentGrades existing = studentGradesMapper.getStudentGradesById(studentGrades.getId());
        if (existing == null) {
            throw new IllegalArgumentException("成绩记录不存在");
        }
        
        studentGradesMapper.updateStudentGrades(studentGrades);
    }
    
    /**
     * 删除学生成绩信息
     * @param id 用户ID
     */
    public void deleteStudentGrades(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        studentGradesMapper.deleteStudentGrades(id);
    }
    
    /**
     * 更新单个课程的成绩
     * @param id 用户ID
     * @param courseName 课程名称
     * @param grade 成绩
     */
    public void updateCourseGrade(Integer id, String courseName, BigDecimal grade) {
        // 验证参数
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        if (grade != null && (grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(new BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException("成绩必须在0-100之间");
        }
        
        // 检查成绩记录是否存在
        StudentGrades existing = studentGradesMapper.getStudentGradesById(id);
        if (existing == null) {
            throw new IllegalArgumentException("成绩记录不存在");
        }
        
        studentGradesMapper.updateCourseGrade(id, courseName, grade);
    }
    
    /**
     * 获取指定用户的指定课程成绩
     * @param id 用户ID
     * @param courseName 课程名称
     * @return 成绩
     */
    public BigDecimal getCourseGrade(Integer id, String courseName) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        
        return studentGradesMapper.getCourseGrade(id, courseName);
    }
    
    /**
     * 批量更新多个课程的成绩
     * @param id 用户ID
     * @param courseGrades 课程成绩映射
     */
    public void batchUpdateCourseGrades(Integer id, Map<String, BigDecimal> courseGrades) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (courseGrades == null || courseGrades.isEmpty()) {
            throw new IllegalArgumentException("课程成绩映射不能为空");
        }
        
        // 验证所有成绩值
        for (Map.Entry<String, BigDecimal> entry : courseGrades.entrySet()) {
            BigDecimal grade = entry.getValue();
            if (grade != null && (grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(new BigDecimal("100")) > 0)) {
                throw new IllegalArgumentException("成绩必须在0-100之间：" + entry.getKey() + " = " + grade);
            }
        }
        
        // 检查成绩记录是否存在
        StudentGrades existing = studentGradesMapper.getStudentGradesById(id);
        if (existing == null) {
            throw new IllegalArgumentException("成绩记录不存在");
        }
        
        // 通过循环调用updateCourseGrade来实现批量更新
        for (Map.Entry<String, BigDecimal> entry : courseGrades.entrySet()) {
            studentGradesMapper.updateCourseGrade(id, entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * 获取用户的成绩统计信息
     * @param id 用户ID
     * @return 统计信息Map，包含已录入成绩数、平均分等
     */
    public Map<String, Object> getGradeStatistics(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查成绩记录是否存在
        StudentGrades existing = studentGradesMapper.getStudentGradesById(id);
        if (existing == null) {
            throw new IllegalArgumentException("成绩记录不存在");
        }
        
        return studentGradesMapper.getGradeStatistics(id);
    }
    
    /**
     * 获取用户的及格课程数量
     * @param id 用户ID
     * @return 及格课程数量
     */
    public int getPassedCoursesCount(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return studentGradesMapper.getPassedCoursesCount(id);
    }
    
    /**
     * 获取用户的不及格课程数量
     * @param id 用户ID
     * @return 不及格课程数量
     */
    public int getFailedCoursesCount(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return studentGradesMapper.getFailedCoursesCount(id);
    }
    
    /**
     * 获取用户的平均成绩
     * @param id 用户ID
     * @return 平均成绩
     */
    public BigDecimal getAverageGrade(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return studentGradesMapper.getAverageGrade(id);
    }
    
    /**
     * 获取用户已录入成绩的课程数量
     * @param id 用户ID
     * @return 已录入成绩的课程数量
     */
    public int getGradedCoursesCount(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return studentGradesMapper.getGradedCoursesCount(id);
    }
    
    /**
     * 获取用户未录入成绩的课程数量
     * @param id 用户ID
     * @return 未录入成绩的课程数量
     */
    public int getUngradedCoursesCount(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return studentGradesMapper.getUngradedCoursesCount(id);
    }
    
    /**
     * 为新学生用户创建成绩记录
     * @param userId 用户ID
     */
    public void createGradeRecordForNewStudent(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 检查成绩记录是否已存在
        StudentGrades existing = studentGradesMapper.getStudentGradesById(userId);
        if (existing != null) {
            throw new IllegalArgumentException("成绩记录已存在");
        }
        
        // 创建新的成绩记录
        StudentGrades gradeInfo = new StudentGrades();
        gradeInfo.setId(userId);
        studentGradesMapper.insertStudentGrades(gradeInfo);
    }
    
    /**
     * 检查成绩记录是否存在
     * @param id 用户ID
     * @return 是否存在
     */
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        return studentGradesMapper.getStudentGradesById(id) != null;
    }

    /**
     * 获取教师教授且已考试的学科列表
     * @param teacherId 教师ID
     * @return 学科列表
     */
    public List<String> getTeacherExaminedSubjects(Integer teacherId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("教师ID不能为空");
        }
        return studentGradesMapper.getTeacherExaminedSubjects(teacherId);
    }

    /**
     * 获取指定学科的学生成绩列表
     * @param subject 学科名称
     * @return 学生成绩列表
     */
    public List<Map<String, Object>> getStudentGradesBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("学科名称不能为空");
        }
        return studentGradesMapper.getStudentGradesBySubject(subject);
    }

    /**
     * 更新学生某科目成绩
     * @param studentId 学生ID
     * @param subject 科目名称
     * @param grade 成绩
     */
    public void updateStudentSubjectGrade(Integer studentId, String subject, BigDecimal grade) {
        if (studentId == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("科目名称不能为空");
        }
        if (grade != null && (grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(new BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException("成绩必须在0-100之间");
        }

        // 检查成绩记录是否存在
        StudentGrades existing = studentGradesMapper.getStudentGradesById(studentId);
        if (existing == null) {
            throw new IllegalArgumentException("学生成绩记录不存在");
        }

        studentGradesMapper.updateCourseGrade(studentId, subject, grade);
    }
}
