package com.tsj.project;

/**
 * 教学信息实体类
 * 对应数据库表 teach_information
 */
public class TeachInformation {
    private Integer teacherId;      // 教师ID，外键，关联sys_user表中的教师用户ID
    private String subject1;        // 教授学科1
    private String subject2;        // 教授学科2
    private String subject3;        // 教授学科3
    private String subject4;        // 教授学科4
    private String subject5;        // 教授学科5
    private String subject6;        // 教授学科6
    private String subject7;        // 教授学科7
    private String subject8;        // 教授学科8
    private String subject9;        // 教授学科9
    private String subject10;       // 教授学科10
    
    // 教师基本信息（从sys_user表关联查询）
    private String teacherName;     // 教师姓名
    private String phoneNumber;     // 手机号
    private String email;          // 邮箱
    private Integer onlineStatus;   // 在线状态
    private String avatar;         // 头像

    // 默认构造函数
    public TeachInformation() {}

    // 全参构造函数
    public TeachInformation(Integer teacherId, String subject1, String subject2, 
                           String subject3, String subject4, String subject5,
                           String subject6, String subject7, String subject8,
                           String subject9, String subject10) {
        this.teacherId = teacherId;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
        this.subject6 = subject6;
        this.subject7 = subject7;
        this.subject8 = subject8;
        this.subject9 = subject9;
        this.subject10 = subject10;
    }

    // Getter 和 Setter 方法
    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public String getSubject3() {
        return subject3;
    }

    public void setSubject3(String subject3) {
        this.subject3 = subject3;
    }

    public String getSubject4() {
        return subject4;
    }

    public void setSubject4(String subject4) {
        this.subject4 = subject4;
    }

    public String getSubject5() {
        return subject5;
    }

    public void setSubject5(String subject5) {
        this.subject5 = subject5;
    }

    public String getSubject6() {
        return subject6;
    }

    public void setSubject6(String subject6) {
        this.subject6 = subject6;
    }

    public String getSubject7() {
        return subject7;
    }

    public void setSubject7(String subject7) {
        this.subject7 = subject7;
    }

    public String getSubject8() {
        return subject8;
    }

    public void setSubject8(String subject8) {
        this.subject8 = subject8;
    }

    public String getSubject9() {
        return subject9;
    }

    public void setSubject9(String subject9) {
        this.subject9 = subject9;
    }

    public String getSubject10() {
        return subject10;
    }

    public void setSubject10(String subject10) {
        this.subject10 = subject10;
    }


    // 教师基本信息的getter和setter方法
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "TeachInformation{" +
                "teacherId=" + teacherId +
                ", subject1='" + subject1 + '\'' +
                ", subject2='" + subject2 + '\'' +
                ", subject3='" + subject3 + '\'' +
                ", subject4='" + subject4 + '\'' +
                ", subject5='" + subject5 + '\'' +
                ", subject6='" + subject6 + '\'' +
                ", subject7='" + subject7 + '\'' +
                ", subject8='" + subject8 + '\'' +
                ", subject9='" + subject9 + '\'' +
                ", subject10='" + subject10 + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
