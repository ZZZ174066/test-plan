package com.tsj.project;

import java.time.LocalDate;

/**
 * 考试信息实体类
 * 对应数据库表：test_information
 */
public class TestInformation {
    
    private Long testId;           // 考试ID，主键
    private Long courseId;         // 课程ID，外键
    private String courseCode;     // 课程代码
    private String courseName;     // 课程名称
    private String examStatus;     // 考试状态：未安排/已安排/已考试
    private LocalDate examDate;    // 考试日期
    private String examSession;    // 考试时段：第一场/第二场/第三场
    private String examLocation;   // 考试地点

    // 默认构造函数
    public TestInformation() {}

    // 全参构造函数
    public TestInformation(Long testId, Long courseId, String courseCode, String courseName, String examStatus, 
                          LocalDate examDate, String examSession, String examLocation) {
        this.testId = testId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.examStatus = examStatus;
        this.examDate = examDate;
        this.examSession = examSession;
        this.examLocation = examLocation;
    }

    // Getter 和 Setter 方法
    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getExamSession() {
        return examSession;
    }

    public void setExamSession(String examSession) {
        this.examSession = examSession;
    }

    public String getExamLocation() {
        return examLocation;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }

    @Override
    public String toString() {
        return "TestInformation{" +
                "testId=" + testId +
                ", courseId=" + courseId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", examStatus='" + examStatus + '\'' +
                ", examDate=" + examDate +
                ", examSession='" + examSession + '\'' +
                ", examLocation='" + examLocation + '\'' +
                '}';
    }
}
