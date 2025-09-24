package com.tsj.project;

import java.math.BigDecimal;

/**
 * 学生成绩信息实体类
 * 对应数据库表 student_grades
 */
public class StudentGrades {
    private Integer id; // 外键，关联sys_user表中的学生用户ID
    
    // 思想政治类课程成绩
    private BigDecimal 思想道德与法治;
    private BigDecimal 中国近现代史纲要;
    private BigDecimal 马克思主义基本原理;
    private BigDecimal 毛泽东思想和中国特色社会主义理论体系概论;
    private BigDecimal 习近平新时代中国特色社会主义思想概论;
    private BigDecimal 形势与政策Ⅰ;
    private BigDecimal 形势与政策Ⅱ;
    private BigDecimal 形势与政策Ⅲ;
    private BigDecimal 形势与政策Ⅳ;
    private BigDecimal 形势与政策V;
    private BigDecimal 形势与政策VⅠ;
    private BigDecimal 形势与政策VⅡ;
    private BigDecimal 形势与政策VⅢ;
    
    // 核心通识课程成绩
    private BigDecimal 智能加交通;
    private BigDecimal 区块链技术及应用;
    private BigDecimal 现代铁路信息技术;
    
    // 军事类课程成绩
    private BigDecimal 军事理论;
    private BigDecimal 军事技能;
    
    // 体育类课程成绩
    private BigDecimal 体育Ⅰ;
    private BigDecimal 体育Ⅱ;
    private BigDecimal 体育Ⅲ;
    private BigDecimal 体育Ⅳ;
    private BigDecimal 体育健康课程Ⅰ;
    private BigDecimal 体育健康课程Ⅱ;
    
    // 外语类课程成绩
    private BigDecimal 英语Ⅰ;
    private BigDecimal 英语Ⅱ;
    private BigDecimal 通用学术英语;
    private BigDecimal 职场英语;
    private BigDecimal 交际与文化视听说;
    private BigDecimal 语言文化与翻译;
    private BigDecimal 英语公共演讲;
    
    // 新生研讨课成绩
    private BigDecimal 计算机学科前沿导论;
    private BigDecimal 软件学科前沿导论;
    
    // 数学与自然科学成绩
    private BigDecimal 高等数学Ⅰ;
    private BigDecimal 高等数学Ⅱ;
    private BigDecimal 线性代数B;
    private BigDecimal 概率论与数理统计;
    private BigDecimal 离散数学;
    private BigDecimal 大学物理BⅠ;
    private BigDecimal 大学物理BⅡ;
    private BigDecimal 大学物理实验Ⅰ;
    private BigDecimal 大学物理实验Ⅱ;
    
    // 专业基础课成绩
    private BigDecimal 高级语言程序设计;
    private BigDecimal 面向对象程序设计;
    private BigDecimal 数字电子技术B;
    private BigDecimal 数字电子技术实验;
    private BigDecimal 数据结构A;
    private BigDecimal 数据结构实验;
    private BigDecimal 计算机组成原理B;
    private BigDecimal 计算机组成实验;
    
    // 专业核心课程成绩
    private BigDecimal 算法分析与设计B;
    private BigDecimal 算法分析与设计实验;
    private BigDecimal 数据库原理;
    private BigDecimal 数据库原理实验;
    private BigDecimal 操作系统;
    private BigDecimal 操作系统实验;
    private BigDecimal 软件工程;
    private BigDecimal 计算机网络;
    private BigDecimal 计算机网络工程实验;
    
    // 专业限修课程成绩
    private BigDecimal 软件系统分析与设计;
    private BigDecimal 编译原理;
    private BigDecimal 人机交互技术;
    private BigDecimal 人机交互技术实验;
    private BigDecimal 新技术讲座;
    private BigDecimal 软件项目管理;
    private BigDecimal 计算机图形学;
    private BigDecimal 计算机图形学实验;
    private BigDecimal 软件质量保证与测试;
    private BigDecimal 软件综合课程设计;
    
    // 跨学科课程成绩
    private BigDecimal Java开发技术;
    private BigDecimal Linux内核分析;
    private BigDecimal 软件设计模式;
    private BigDecimal 软件设计模式实验;
    private BigDecimal 数据挖掘;
    private BigDecimal 人工智能;
    private BigDecimal 云计算与并行技术;
    private BigDecimal 幸福心理学;
    private BigDecimal 信息检索;

    // Getter and Setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal get思想道德与法治() {
        return 思想道德与法治;
    }

    public void set思想道德与法治(BigDecimal 思想道德与法治) {
        this.思想道德与法治 = 思想道德与法治;
    }

    public BigDecimal get中国近现代史纲要() {
        return 中国近现代史纲要;
    }

    public void set中国近现代史纲要(BigDecimal 中国近现代史纲要) {
        this.中国近现代史纲要 = 中国近现代史纲要;
    }

    public BigDecimal get马克思主义基本原理() {
        return 马克思主义基本原理;
    }

    public void set马克思主义基本原理(BigDecimal 马克思主义基本原理) {
        this.马克思主义基本原理 = 马克思主义基本原理;
    }

    public BigDecimal get毛泽东思想和中国特色社会主义理论体系概论() {
        return 毛泽东思想和中国特色社会主义理论体系概论;
    }

    public void set毛泽东思想和中国特色社会主义理论体系概论(BigDecimal 毛泽东思想和中国特色社会主义理论体系概论) {
        this.毛泽东思想和中国特色社会主义理论体系概论 = 毛泽东思想和中国特色社会主义理论体系概论;
    }

    public BigDecimal get习近平新时代中国特色社会主义思想概论() {
        return 习近平新时代中国特色社会主义思想概论;
    }

    public void set习近平新时代中国特色社会主义思想概论(BigDecimal 习近平新时代中国特色社会主义思想概论) {
        this.习近平新时代中国特色社会主义思想概论 = 习近平新时代中国特色社会主义思想概论;
    }

    public BigDecimal get形势与政策Ⅰ() {
        return 形势与政策Ⅰ;
    }

    public void set形势与政策Ⅰ(BigDecimal 形势与政策Ⅰ) {
        this.形势与政策Ⅰ = 形势与政策Ⅰ;
    }

    public BigDecimal get形势与政策Ⅱ() {
        return 形势与政策Ⅱ;
    }

    public void set形势与政策Ⅱ(BigDecimal 形势与政策Ⅱ) {
        this.形势与政策Ⅱ = 形势与政策Ⅱ;
    }

    public BigDecimal get形势与政策Ⅲ() {
        return 形势与政策Ⅲ;
    }

    public void set形势与政策Ⅲ(BigDecimal 形势与政策Ⅲ) {
        this.形势与政策Ⅲ = 形势与政策Ⅲ;
    }

    public BigDecimal get形势与政策Ⅳ() {
        return 形势与政策Ⅳ;
    }

    public void set形势与政策Ⅳ(BigDecimal 形势与政策Ⅳ) {
        this.形势与政策Ⅳ = 形势与政策Ⅳ;
    }

    public BigDecimal get形势与政策V() {
        return 形势与政策V;
    }

    public void set形势与政策V(BigDecimal 形势与政策V) {
        this.形势与政策V = 形势与政策V;
    }

    public BigDecimal get形势与政策VⅠ() {
        return 形势与政策VⅠ;
    }

    public void set形势与政策VⅠ(BigDecimal 形势与政策VⅠ) {
        this.形势与政策VⅠ = 形势与政策VⅠ;
    }

    public BigDecimal get形势与政策VⅡ() {
        return 形势与政策VⅡ;
    }

    public void set形势与政策VⅡ(BigDecimal 形势与政策VⅡ) {
        this.形势与政策VⅡ = 形势与政策VⅡ;
    }

    public BigDecimal get形势与政策VⅢ() {
        return 形势与政策VⅢ;
    }

    public void set形势与政策VⅢ(BigDecimal 形势与政策VⅢ) {
        this.形势与政策VⅢ = 形势与政策VⅢ;
    }

    public BigDecimal get智能加交通() {
        return 智能加交通;
    }

    public void set智能加交通(BigDecimal 智能加交通) {
        this.智能加交通 = 智能加交通;
    }

    public BigDecimal get区块链技术及应用() {
        return 区块链技术及应用;
    }

    public void set区块链技术及应用(BigDecimal 区块链技术及应用) {
        this.区块链技术及应用 = 区块链技术及应用;
    }

    public BigDecimal get现代铁路信息技术() {
        return 现代铁路信息技术;
    }

    public void set现代铁路信息技术(BigDecimal 现代铁路信息技术) {
        this.现代铁路信息技术 = 现代铁路信息技术;
    }

    public BigDecimal get军事理论() {
        return 军事理论;
    }

    public void set军事理论(BigDecimal 军事理论) {
        this.军事理论 = 军事理论;
    }

    public BigDecimal get军事技能() {
        return 军事技能;
    }

    public void set军事技能(BigDecimal 军事技能) {
        this.军事技能 = 军事技能;
    }

    public BigDecimal get体育Ⅰ() {
        return 体育Ⅰ;
    }

    public void set体育Ⅰ(BigDecimal 体育Ⅰ) {
        this.体育Ⅰ = 体育Ⅰ;
    }

    public BigDecimal get体育Ⅱ() {
        return 体育Ⅱ;
    }

    public void set体育Ⅱ(BigDecimal 体育Ⅱ) {
        this.体育Ⅱ = 体育Ⅱ;
    }

    public BigDecimal get体育Ⅲ() {
        return 体育Ⅲ;
    }

    public void set体育Ⅲ(BigDecimal 体育Ⅲ) {
        this.体育Ⅲ = 体育Ⅲ;
    }

    public BigDecimal get体育Ⅳ() {
        return 体育Ⅳ;
    }

    public void set体育Ⅳ(BigDecimal 体育Ⅳ) {
        this.体育Ⅳ = 体育Ⅳ;
    }

    public BigDecimal get体育健康课程Ⅰ() {
        return 体育健康课程Ⅰ;
    }

    public void set体育健康课程Ⅰ(BigDecimal 体育健康课程Ⅰ) {
        this.体育健康课程Ⅰ = 体育健康课程Ⅰ;
    }

    public BigDecimal get体育健康课程Ⅱ() {
        return 体育健康课程Ⅱ;
    }

    public void set体育健康课程Ⅱ(BigDecimal 体育健康课程Ⅱ) {
        this.体育健康课程Ⅱ = 体育健康课程Ⅱ;
    }

    public BigDecimal get英语Ⅰ() {
        return 英语Ⅰ;
    }

    public void set英语Ⅰ(BigDecimal 英语Ⅰ) {
        this.英语Ⅰ = 英语Ⅰ;
    }

    public BigDecimal get英语Ⅱ() {
        return 英语Ⅱ;
    }

    public void set英语Ⅱ(BigDecimal 英语Ⅱ) {
        this.英语Ⅱ = 英语Ⅱ;
    }

    public BigDecimal get通用学术英语() {
        return 通用学术英语;
    }

    public void set通用学术英语(BigDecimal 通用学术英语) {
        this.通用学术英语 = 通用学术英语;
    }

    public BigDecimal get职场英语() {
        return 职场英语;
    }

    public void set职场英语(BigDecimal 职场英语) {
        this.职场英语 = 职场英语;
    }

    public BigDecimal get交际与文化视听说() {
        return 交际与文化视听说;
    }

    public void set交际与文化视听说(BigDecimal 交际与文化视听说) {
        this.交际与文化视听说 = 交际与文化视听说;
    }

    public BigDecimal get语言文化与翻译() {
        return 语言文化与翻译;
    }

    public void set语言文化与翻译(BigDecimal 语言文化与翻译) {
        this.语言文化与翻译 = 语言文化与翻译;
    }

    public BigDecimal get英语公共演讲() {
        return 英语公共演讲;
    }

    public void set英语公共演讲(BigDecimal 英语公共演讲) {
        this.英语公共演讲 = 英语公共演讲;
    }

    public BigDecimal get计算机学科前沿导论() {
        return 计算机学科前沿导论;
    }

    public void set计算机学科前沿导论(BigDecimal 计算机学科前沿导论) {
        this.计算机学科前沿导论 = 计算机学科前沿导论;
    }

    public BigDecimal get软件学科前沿导论() {
        return 软件学科前沿导论;
    }

    public void set软件学科前沿导论(BigDecimal 软件学科前沿导论) {
        this.软件学科前沿导论 = 软件学科前沿导论;
    }

    public BigDecimal get高等数学Ⅰ() {
        return 高等数学Ⅰ;
    }

    public void set高等数学Ⅰ(BigDecimal 高等数学Ⅰ) {
        this.高等数学Ⅰ = 高等数学Ⅰ;
    }

    public BigDecimal get高等数学Ⅱ() {
        return 高等数学Ⅱ;
    }

    public void set高等数学Ⅱ(BigDecimal 高等数学Ⅱ) {
        this.高等数学Ⅱ = 高等数学Ⅱ;
    }

    public BigDecimal get线性代数B() {
        return 线性代数B;
    }

    public void set线性代数B(BigDecimal 线性代数B) {
        this.线性代数B = 线性代数B;
    }

    public BigDecimal get概率论与数理统计() {
        return 概率论与数理统计;
    }

    public void set概率论与数理统计(BigDecimal 概率论与数理统计) {
        this.概率论与数理统计 = 概率论与数理统计;
    }

    public BigDecimal get离散数学() {
        return 离散数学;
    }

    public void set离散数学(BigDecimal 离散数学) {
        this.离散数学 = 离散数学;
    }

    public BigDecimal get大学物理BⅠ() {
        return 大学物理BⅠ;
    }

    public void set大学物理BⅠ(BigDecimal 大学物理BⅠ) {
        this.大学物理BⅠ = 大学物理BⅠ;
    }

    public BigDecimal get大学物理BⅡ() {
        return 大学物理BⅡ;
    }

    public void set大学物理BⅡ(BigDecimal 大学物理BⅡ) {
        this.大学物理BⅡ = 大学物理BⅡ;
    }

    public BigDecimal get大学物理实验Ⅰ() {
        return 大学物理实验Ⅰ;
    }

    public void set大学物理实验Ⅰ(BigDecimal 大学物理实验Ⅰ) {
        this.大学物理实验Ⅰ = 大学物理实验Ⅰ;
    }

    public BigDecimal get大学物理实验Ⅱ() {
        return 大学物理实验Ⅱ;
    }

    public void set大学物理实验Ⅱ(BigDecimal 大学物理实验Ⅱ) {
        this.大学物理实验Ⅱ = 大学物理实验Ⅱ;
    }

    public BigDecimal get高级语言程序设计() {
        return 高级语言程序设计;
    }

    public void set高级语言程序设计(BigDecimal 高级语言程序设计) {
        this.高级语言程序设计 = 高级语言程序设计;
    }

    public BigDecimal get面向对象程序设计() {
        return 面向对象程序设计;
    }

    public void set面向对象程序设计(BigDecimal 面向对象程序设计) {
        this.面向对象程序设计 = 面向对象程序设计;
    }

    public BigDecimal get数字电子技术B() {
        return 数字电子技术B;
    }

    public void set数字电子技术B(BigDecimal 数字电子技术B) {
        this.数字电子技术B = 数字电子技术B;
    }

    public BigDecimal get数字电子技术实验() {
        return 数字电子技术实验;
    }

    public void set数字电子技术实验(BigDecimal 数字电子技术实验) {
        this.数字电子技术实验 = 数字电子技术实验;
    }

    public BigDecimal get数据结构A() {
        return 数据结构A;
    }

    public void set数据结构A(BigDecimal 数据结构A) {
        this.数据结构A = 数据结构A;
    }

    public BigDecimal get数据结构实验() {
        return 数据结构实验;
    }

    public void set数据结构实验(BigDecimal 数据结构实验) {
        this.数据结构实验 = 数据结构实验;
    }

    public BigDecimal get计算机组成原理B() {
        return 计算机组成原理B;
    }

    public void set计算机组成原理B(BigDecimal 计算机组成原理B) {
        this.计算机组成原理B = 计算机组成原理B;
    }

    public BigDecimal get计算机组成实验() {
        return 计算机组成实验;
    }

    public void set计算机组成实验(BigDecimal 计算机组成实验) {
        this.计算机组成实验 = 计算机组成实验;
    }

    public BigDecimal get算法分析与设计B() {
        return 算法分析与设计B;
    }

    public void set算法分析与设计B(BigDecimal 算法分析与设计B) {
        this.算法分析与设计B = 算法分析与设计B;
    }

    public BigDecimal get算法分析与设计实验() {
        return 算法分析与设计实验;
    }

    public void set算法分析与设计实验(BigDecimal 算法分析与设计实验) {
        this.算法分析与设计实验 = 算法分析与设计实验;
    }

    public BigDecimal get数据库原理() {
        return 数据库原理;
    }

    public void set数据库原理(BigDecimal 数据库原理) {
        this.数据库原理 = 数据库原理;
    }

    public BigDecimal get数据库原理实验() {
        return 数据库原理实验;
    }

    public void set数据库原理实验(BigDecimal 数据库原理实验) {
        this.数据库原理实验 = 数据库原理实验;
    }

    public BigDecimal get操作系统() {
        return 操作系统;
    }

    public void set操作系统(BigDecimal 操作系统) {
        this.操作系统 = 操作系统;
    }

    public BigDecimal get操作系统实验() {
        return 操作系统实验;
    }

    public void set操作系统实验(BigDecimal 操作系统实验) {
        this.操作系统实验 = 操作系统实验;
    }

    public BigDecimal get软件工程() {
        return 软件工程;
    }

    public void set软件工程(BigDecimal 软件工程) {
        this.软件工程 = 软件工程;
    }

    public BigDecimal get计算机网络() {
        return 计算机网络;
    }

    public void set计算机网络(BigDecimal 计算机网络) {
        this.计算机网络 = 计算机网络;
    }

    public BigDecimal get计算机网络工程实验() {
        return 计算机网络工程实验;
    }

    public void set计算机网络工程实验(BigDecimal 计算机网络工程实验) {
        this.计算机网络工程实验 = 计算机网络工程实验;
    }

    public BigDecimal get软件系统分析与设计() {
        return 软件系统分析与设计;
    }

    public void set软件系统分析与设计(BigDecimal 软件系统分析与设计) {
        this.软件系统分析与设计 = 软件系统分析与设计;
    }

    public BigDecimal get编译原理() {
        return 编译原理;
    }

    public void set编译原理(BigDecimal 编译原理) {
        this.编译原理 = 编译原理;
    }

    public BigDecimal get人机交互技术() {
        return 人机交互技术;
    }

    public void set人机交互技术(BigDecimal 人机交互技术) {
        this.人机交互技术 = 人机交互技术;
    }

    public BigDecimal get人机交互技术实验() {
        return 人机交互技术实验;
    }

    public void set人机交互技术实验(BigDecimal 人机交互技术实验) {
        this.人机交互技术实验 = 人机交互技术实验;
    }

    public BigDecimal get新技术讲座() {
        return 新技术讲座;
    }

    public void set新技术讲座(BigDecimal 新技术讲座) {
        this.新技术讲座 = 新技术讲座;
    }

    public BigDecimal get软件项目管理() {
        return 软件项目管理;
    }

    public void set软件项目管理(BigDecimal 软件项目管理) {
        this.软件项目管理 = 软件项目管理;
    }

    public BigDecimal get计算机图形学() {
        return 计算机图形学;
    }

    public void set计算机图形学(BigDecimal 计算机图形学) {
        this.计算机图形学 = 计算机图形学;
    }

    public BigDecimal get计算机图形学实验() {
        return 计算机图形学实验;
    }

    public void set计算机图形学实验(BigDecimal 计算机图形学实验) {
        this.计算机图形学实验 = 计算机图形学实验;
    }

    public BigDecimal get软件质量保证与测试() {
        return 软件质量保证与测试;
    }

    public void set软件质量保证与测试(BigDecimal 软件质量保证与测试) {
        this.软件质量保证与测试 = 软件质量保证与测试;
    }

    public BigDecimal get软件综合课程设计() {
        return 软件综合课程设计;
    }

    public void set软件综合课程设计(BigDecimal 软件综合课程设计) {
        this.软件综合课程设计 = 软件综合课程设计;
    }

    public BigDecimal getJava开发技术() {
        return Java开发技术;
    }

    public void setJava开发技术(BigDecimal java开发技术) {
        Java开发技术 = java开发技术;
    }

    public BigDecimal getLinux内核分析() {
        return Linux内核分析;
    }

    public void setLinux内核分析(BigDecimal linux内核分析) {
        Linux内核分析 = linux内核分析;
    }

    public BigDecimal get软件设计模式() {
        return 软件设计模式;
    }

    public void set软件设计模式(BigDecimal 软件设计模式) {
        this.软件设计模式 = 软件设计模式;
    }

    public BigDecimal get软件设计模式实验() {
        return 软件设计模式实验;
    }

    public void set软件设计模式实验(BigDecimal 软件设计模式实验) {
        this.软件设计模式实验 = 软件设计模式实验;
    }

    public BigDecimal get数据挖掘() {
        return 数据挖掘;
    }

    public void set数据挖掘(BigDecimal 数据挖掘) {
        this.数据挖掘 = 数据挖掘;
    }

    public BigDecimal get人工智能() {
        return 人工智能;
    }

    public void set人工智能(BigDecimal 人工智能) {
        this.人工智能 = 人工智能;
    }

    public BigDecimal get云计算与并行技术() {
        return 云计算与并行技术;
    }

    public void set云计算与并行技术(BigDecimal 云计算与并行技术) {
        this.云计算与并行技术 = 云计算与并行技术;
    }

    public BigDecimal get幸福心理学() {
        return 幸福心理学;
    }

    public void set幸福心理学(BigDecimal 幸福心理学) {
        this.幸福心理学 = 幸福心理学;
    }

    public BigDecimal get信息检索() {
        return 信息检索;
    }

    public void set信息检索(BigDecimal 信息检索) {
        this.信息检索 = 信息检索;
    }
}
