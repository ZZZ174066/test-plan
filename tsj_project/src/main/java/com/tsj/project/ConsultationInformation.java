package com.tsj.project;

import java.time.LocalDateTime;

/**
 * 在线咨询信息实体类
 */
public class ConsultationInformation {
    private Integer consultationId;
    private Integer studentId;
    private Integer teacherId;
    private String question;
    private String answer;
    private LocalDateTime questionTime;
    private LocalDateTime answerTime;
    private Integer status; // 0-待回答，1-已回答
    
    // 关联信息
    private String studentName;
    private String teacherName;
    private String studentAvatar;
    private String teacherAvatar;

    public ConsultationInformation() {}

    public ConsultationInformation(Integer studentId, Integer teacherId, String question) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.question = question;
        this.questionTime = LocalDateTime.now();
        this.status = 0;
    }

    // Getters and Setters
    public Integer getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Integer consultationId) {
        this.consultationId = consultationId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(LocalDateTime questionTime) {
        this.questionTime = questionTime;
    }

    public LocalDateTime getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(LocalDateTime answerTime) {
        this.answerTime = answerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    @Override
    public String toString() {
        return "ConsultationInformation{" +
                "consultationId=" + consultationId +
                ", studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", questionTime=" + questionTime +
                ", answerTime=" + answerTime +
                ", status=" + status +
                ", studentName='" + studentName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", studentAvatar='" + studentAvatar + '\'' +
                ", teacherAvatar='" + teacherAvatar + '\'' +
                '}';
    }
}
