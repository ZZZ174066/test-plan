package com.tsj.project;


/**
 * 教材信息实体类
 */
public class TextbookInformation {
    
    private Integer textbookId;
    private String textbookName;
    private String textbookAuthor;
    private String textbookVersion;
    private String textbookPublisher;
    private String textbookImageUrl;
    private String downloadUrl;
    
    // 无参构造函数
    public TextbookInformation() {}
    
    // 全参构造函数
    public TextbookInformation(Integer textbookId, String textbookName, String textbookAuthor, 
                             String textbookVersion, String textbookPublisher, String textbookImageUrl, 
                             String downloadUrl) {
        this.textbookId = textbookId;
        this.textbookName = textbookName;
        this.textbookAuthor = textbookAuthor;
        this.textbookVersion = textbookVersion;
        this.textbookPublisher = textbookPublisher;
        this.textbookImageUrl = textbookImageUrl;
        this.downloadUrl = downloadUrl;
    }
    
    // Getter和Setter方法
    public Integer getTextbookId() {
        return textbookId;
    }
    
    public void setTextbookId(Integer textbookId) {
        this.textbookId = textbookId;
    }
    
    public String getTextbookName() {
        return textbookName;
    }
    
    public void setTextbookName(String textbookName) {
        this.textbookName = textbookName;
    }
    
    public String getTextbookAuthor() {
        return textbookAuthor;
    }
    
    public void setTextbookAuthor(String textbookAuthor) {
        this.textbookAuthor = textbookAuthor;
    }
    
    public String getTextbookVersion() {
        return textbookVersion;
    }
    
    public void setTextbookVersion(String textbookVersion) {
        this.textbookVersion = textbookVersion;
    }
    
    public String getTextbookPublisher() {
        return textbookPublisher;
    }
    
    public void setTextbookPublisher(String textbookPublisher) {
        this.textbookPublisher = textbookPublisher;
    }
    
    public String getTextbookImageUrl() {
        return textbookImageUrl;
    }
    
    public void setTextbookImageUrl(String textbookImageUrl) {
        this.textbookImageUrl = textbookImageUrl;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    
    @Override
    public String toString() {
        return "TextbookInformation{" +
                "textbookId=" + textbookId +
                ", textbookName='" + textbookName + '\'' +
                ", textbookAuthor='" + textbookAuthor + '\'' +
                ", textbookVersion='" + textbookVersion + '\'' +
                ", textbookPublisher='" + textbookPublisher + '\'' +
                ", textbookImageUrl='" + textbookImageUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
