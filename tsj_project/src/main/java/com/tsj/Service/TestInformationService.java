package com.tsj.Service;

import com.tsj.Mapper.TestInformationMapper;
import com.tsj.project.TestInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 考试信息服务类
 * 提供考试信息的业务逻辑处理
 */
@Service
@Transactional
public class TestInformationService {

    @Autowired
    private TestInformationMapper testInformationMapper;

    /**
     * 获取所有考试信息
     * @return 考试信息列表
     */
    public List<TestInformation> getAllTestInformation() {
        return testInformationMapper.getAllTestInformation();
    }

    /**
     * 根据考试ID获取考试信息
     * @param testId 考试ID
     * @return 考试信息
     */
    public TestInformation getTestInformationById(Long testId) {
        if (testId == null) {
            throw new IllegalArgumentException("考试ID不能为空");
        }
        return testInformationMapper.getTestInformationById(testId);
    }

    /**
     * 根据课程ID获取考试信息
     * @param courseId 课程ID
     * @return 考试信息
     */
    public TestInformation getTestInformationByCourseId(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }
        return testInformationMapper.getTestInformationByCourseId(courseId);
    }

    /**
     * 新增考试信息
     * @param testInformation 考试信息对象
     */
    public void addTestInformation(TestInformation testInformation) {
        validateTestInformation(testInformation);
        testInformationMapper.insertTestInformation(testInformation);
    }

    /**
     * 更新考试信息
     * @param testInformation 考试信息对象
     */
    public void updateTestInformation(TestInformation testInformation) {
        if (testInformation.getTestId() == null) {
            throw new IllegalArgumentException("考试ID不能为空");
        }
        validateTestInformation(testInformation);
        testInformationMapper.updateTestInformation(testInformation);
    }

    /**
     * 删除考试信息
     * @param testId 考试ID
     */
    public void deleteTestInformation(Long testId) {
        if (testId == null) {
            throw new IllegalArgumentException("考试ID不能为空");
        }
        testInformationMapper.deleteTestInformation(testId);
    }

    /**
     * 根据课程ID删除考试信息
     * @param courseId 课程ID
     */
    public void deleteTestInformationByCourseId(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }
        testInformationMapper.deleteTestInformationByCourseId(courseId);
    }

    /**
     * 根据考试状态获取考试信息列表
     * @param examStatus 考试状态
     * @return 考试信息列表
     */
    public List<TestInformation> getTestInformationByStatus(String examStatus) {
        if (examStatus == null || examStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("考试状态不能为空");
        }
        return testInformationMapper.getTestInformationByStatus(examStatus);
    }

    /**
     * 根据考试日期获取考试信息列表
     * @param examDate 考试日期
     * @return 考试信息列表
     */
    public List<TestInformation> getTestInformationByDate(LocalDate examDate) {
        if (examDate == null) {
            throw new IllegalArgumentException("考试日期不能为空");
        }
        return testInformationMapper.getTestInformationByDate(examDate);
    }

    /**
     * 根据考试时段获取考试信息列表
     * @param examSession 考试时段
     * @return 考试信息列表
     */
    public List<TestInformation> getTestInformationBySession(String examSession) {
        if (examSession == null || examSession.trim().isEmpty()) {
            throw new IllegalArgumentException("考试时段不能为空");
        }
        return testInformationMapper.getTestInformationBySession(examSession);
    }

    /**
     * 根据考试地点获取考试信息列表
     * @param examLocation 考试地点
     * @return 考试信息列表
     */
    public List<TestInformation> getTestInformationByLocation(String examLocation) {
        if (examLocation == null || examLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("考试地点不能为空");
        }
        return testInformationMapper.getTestInformationByLocation(examLocation);
    }

    /**
     * 根据日期范围获取考试信息列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考试信息列表
     */
    public List<TestInformation> getTestInformationByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("开始日期和结束日期不能为空");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        return testInformationMapper.getTestInformationByDateRange(startDate, endDate);
    }

    /**
     * 更新考试状态
     * @param testId 考试ID
     * @param examStatus 新的考试状态
     */
    public void updateExamStatus(Long testId, String examStatus) {
        if (testId == null) {
            throw new IllegalArgumentException("考试ID不能为空");
        }
        if (examStatus == null || examStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("考试状态不能为空");
        }
        if (!isValidExamStatus(examStatus)) {
            throw new IllegalArgumentException("无效的考试状态：" + examStatus);
        }
        testInformationMapper.updateExamStatus(testId, examStatus);
    }

    /**
     * 安排考试
     * @param testId 考试ID
     * @param examDate 考试日期
     * @param examSession 考试时段
     * @param examLocation 考试地点
     */
    public void scheduleExam(Long testId, LocalDate examDate, String examSession, String examLocation) {
        if (testId == null) {
            throw new IllegalArgumentException("考试ID不能为空");
        }
        if (examDate == null) {
            throw new IllegalArgumentException("考试日期不能为空");
        }
        if (examSession == null || examSession.trim().isEmpty()) {
            throw new IllegalArgumentException("考试时段不能为空");
        }
        if (examLocation == null || examLocation.trim().isEmpty()) {
            throw new IllegalArgumentException("考试地点不能为空");
        }
        if (!isValidExamSession(examSession)) {
            throw new IllegalArgumentException("无效的考试时段：" + examSession);
        }
        if (examDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("考试日期不能早于当前日期");
        }
        
        testInformationMapper.scheduleExam(testId, examDate, examSession, examLocation);
    }

    /**
     * 取消考试安排
     * @param testId 考试ID
     */
    public void cancelExamSchedule(Long testId) {
        if (testId == null) {
            throw new IllegalArgumentException("考试ID不能为空");
        }
        testInformationMapper.cancelExamSchedule(testId);
    }

    /**
     * 统计各状态的考试数量
     * @return 包含状态统计的列表
     */
    public List<Map<String, Object>> getExamStatusStatistics() {
        return testInformationMapper.getExamStatusStatistics();
    }

    /**
     * 根据教师ID获取该教师教授学科的考试信息列表
     * @param teacherId 教师ID
     * @return 考试信息列表
     */
    public List<TestInformation> getExamsByTeacherId(Integer teacherId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("教师ID不能为空");
        }
        return testInformationMapper.getExamsByTeacherId(teacherId);
    }

    /**
     * 根据教师ID统计该教师教授学科的考试状态数量
     * @param teacherId 教师ID
     * @return 状态统计信息
     */
    public List<Map<String, Object>> getExamStatusStatisticsByTeacherId(Integer teacherId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("教师ID不能为空");
        }
        return testInformationMapper.getExamStatusStatisticsByTeacherId(teacherId);
    }

    /**
     * 根据课程名称模糊查询考试信息
     * @param courseName 课程名称关键字
     * @return 考试信息列表
     */
    public List<TestInformation> searchTestInformationByCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        return testInformationMapper.searchTestInformationByCourseName(courseName.trim());
    }

    /**
     * 验证考试信息
     * @param testInformation 考试信息对象
     */
    private void validateTestInformation(TestInformation testInformation) {
        if (testInformation == null) {
            throw new IllegalArgumentException("考试信息不能为空");
        }
        if (testInformation.getCourseId() == null) {
            throw new IllegalArgumentException("课程ID不能为空");
        }
        if (testInformation.getCourseName() == null || testInformation.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }
        if (testInformation.getExamStatus() == null || testInformation.getExamStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("考试状态不能为空");
        }
        if (!isValidExamStatus(testInformation.getExamStatus())) {
            throw new IllegalArgumentException("无效的考试状态：" + testInformation.getExamStatus());
        }

        // 验证业务逻辑：当状态为"未安排"时，日期、时段、地点必须为空
        if ("未安排".equals(testInformation.getExamStatus())) {
            if (testInformation.getExamDate() != null || 
                testInformation.getExamSession() != null || 
                testInformation.getExamLocation() != null) {
                throw new IllegalArgumentException("考试状态为\"未安排\"时，考试日期、时段、地点必须为空");
            }
        }
        
        // 验证业务逻辑：当状态为"已安排"或"已考试"时，日期、时段、地点不能为空
        if ("已安排".equals(testInformation.getExamStatus()) || "已考试".equals(testInformation.getExamStatus())) {
            if (testInformation.getExamDate() == null || 
                testInformation.getExamSession() == null || testInformation.getExamSession().trim().isEmpty() ||
                testInformation.getExamLocation() == null || testInformation.getExamLocation().trim().isEmpty()) {
                throw new IllegalArgumentException("考试状态为\"已安排\"或\"已考试\"时，考试日期、时段、地点不能为空");
            }
            if (!isValidExamSession(testInformation.getExamSession())) {
                throw new IllegalArgumentException("无效的考试时段：" + testInformation.getExamSession());
            }
        }
    }

    /**
     * 验证考试状态是否有效
     * @param examStatus 考试状态
     * @return 是否有效
     */
    private boolean isValidExamStatus(String examStatus) {
        return "未安排".equals(examStatus) || "已安排".equals(examStatus) || "已考试".equals(examStatus);
    }

    /**
     * 验证考试时段是否有效
     * @param examSession 考试时段
     * @return 是否有效
     */
    private boolean isValidExamSession(String examSession) {
        return "第一场".equals(examSession) || "第二场".equals(examSession) || "第三场".equals(examSession);
    }
}
