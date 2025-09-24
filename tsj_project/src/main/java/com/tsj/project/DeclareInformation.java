package com.tsj.project;

/**
 * 课程报名信息实体类
 * 对应数据库表 declare_information
 */
public class DeclareInformation {
    private Integer id; // 外键，关联sys_user表中的学生用户ID
    
    // 思想政治类课程
    private String 思想道德与法治;
    private String 中国近现代史纲要;
    private String 马克思主义基本原理;
    private String 毛泽东思想和中国特色社会主义理论体系概论;
    private String 习近平新时代中国特色社会主义思想概论;
    private String 形势与政策Ⅰ;
    private String 形势与政策Ⅱ;
    private String 形势与政策Ⅲ;
    private String 形势与政策Ⅳ;
    private String 形势与政策V;
    private String 形势与政策VⅠ;
    private String 形势与政策VⅡ;
    private String 形势与政策VⅢ;
    
    // 核心通识课程
    private String 智能加交通;
    private String 区块链技术及应用;
    private String 现代铁路信息技术;
    
    // 军事类课程
    private String 军事理论;
    private String 军事技能;
    
    // 体育类课程
    private String 体育Ⅰ;
    private String 体育Ⅱ;
    private String 体育Ⅲ;
    private String 体育Ⅳ;
    private String 体育健康课程Ⅰ;
    private String 体育健康课程Ⅱ;
    
    // 外语类课程
    private String 英语Ⅰ;
    private String 英语Ⅱ;
    private String 通用学术英语;
    private String 职场英语;
    private String 交际与文化视听说;
    private String 语言文化与翻译;
    private String 英语公共演讲;
    
    // 新生研讨课
    private String 计算机学科前沿导论;
    private String 软件学科前沿导论;
    
    // 数学与自然科学
    private String 高等数学Ⅰ;
    private String 高等数学Ⅱ;
    private String 线性代数B;
    private String 概率论与数理统计;
    private String 离散数学;
    private String 大学物理BⅠ;
    private String 大学物理BⅡ;
    private String 大学物理实验Ⅰ;
    private String 大学物理实验Ⅱ;
    
    // 专业基础课
    private String 高级语言程序设计;
    private String 面向对象程序设计;
    private String 数字电子技术B;
    private String 数字电子技术实验;
    private String 数据结构A;
    private String 数据结构实验;
    private String 计算机组成原理B;
    private String 计算机组成实验;
    
    // 专业核心课程
    private String 算法分析与设计B;
    private String 算法分析与设计实验;
    private String 数据库原理;
    private String 数据库原理实验;
    private String 操作系统;
    private String 操作系统实验;
    private String 软件工程;
    private String 计算机网络;
    private String 计算机网络工程实验;
    
    // 专业限修课程
    private String 软件系统分析与设计;
    private String 编译原理;
    private String 人机交互技术;
    private String 人机交互技术实验;
    private String 新技术讲座;
    private String 软件项目管理;
    private String 计算机图形学;
    private String 计算机图形学实验;
    private String 软件质量保证与测试;
    private String 软件综合课程设计;
    
    // 跨学科课程
    private String Java开发技术;
    private String Linux内核分析;
    private String 软件设计模式;
    private String 软件设计模式实验;
    private String 数据挖掘;
    private String 人工智能;
    private String 云计算与并行技术;
    private String 幸福心理学;
    private String 信息检索;

    // Getter and Setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String get思想道德与法治() {
        return 思想道德与法治;
    }

    public void set思想道德与法治(String 思想道德与法治) {
        this.思想道德与法治 = 思想道德与法治;
    }

    public String get中国近现代史纲要() {
        return 中国近现代史纲要;
    }

    public void set中国近现代史纲要(String 中国近现代史纲要) {
        this.中国近现代史纲要 = 中国近现代史纲要;
    }

    public String get马克思主义基本原理() {
        return 马克思主义基本原理;
    }

    public void set马克思主义基本原理(String 马克思主义基本原理) {
        this.马克思主义基本原理 = 马克思主义基本原理;
    }

    public String get毛泽东思想和中国特色社会主义理论体系概论() {
        return 毛泽东思想和中国特色社会主义理论体系概论;
    }

    public void set毛泽东思想和中国特色社会主义理论体系概论(String 毛泽东思想和中国特色社会主义理论体系概论) {
        this.毛泽东思想和中国特色社会主义理论体系概论 = 毛泽东思想和中国特色社会主义理论体系概论;
    }

    public String get习近平新时代中国特色社会主义思想概论() {
        return 习近平新时代中国特色社会主义思想概论;
    }

    public void set习近平新时代中国特色社会主义思想概论(String 习近平新时代中国特色社会主义思想概论) {
        this.习近平新时代中国特色社会主义思想概论 = 习近平新时代中国特色社会主义思想概论;
    }

    public String get形势与政策Ⅰ() {
        return 形势与政策Ⅰ;
    }

    public void set形势与政策Ⅰ(String 形势与政策Ⅰ) {
        this.形势与政策Ⅰ = 形势与政策Ⅰ;
    }

    public String get形势与政策Ⅱ() {
        return 形势与政策Ⅱ;
    }

    public void set形势与政策Ⅱ(String 形势与政策Ⅱ) {
        this.形势与政策Ⅱ = 形势与政策Ⅱ;
    }

    public String get形势与政策Ⅲ() {
        return 形势与政策Ⅲ;
    }

    public void set形势与政策Ⅲ(String 形势与政策Ⅲ) {
        this.形势与政策Ⅲ = 形势与政策Ⅲ;
    }

    public String get形势与政策Ⅳ() {
        return 形势与政策Ⅳ;
    }

    public void set形势与政策Ⅳ(String 形势与政策Ⅳ) {
        this.形势与政策Ⅳ = 形势与政策Ⅳ;
    }

    public String get形势与政策V() {
        return 形势与政策V;
    }

    public void set形势与政策V(String 形势与政策V) {
        this.形势与政策V = 形势与政策V;
    }

    public String get形势与政策VⅠ() {
        return 形势与政策VⅠ;
    }

    public void set形势与政策VⅠ(String 形势与政策VⅠ) {
        this.形势与政策VⅠ = 形势与政策VⅠ;
    }

    public String get形势与政策VⅡ() {
        return 形势与政策VⅡ;
    }

    public void set形势与政策VⅡ(String 形势与政策VⅡ) {
        this.形势与政策VⅡ = 形势与政策VⅡ;
    }

    public String get形势与政策VⅢ() {
        return 形势与政策VⅢ;
    }

    public void set形势与政策VⅢ(String 形势与政策VⅢ) {
        this.形势与政策VⅢ = 形势与政策VⅢ;
    }

    public String get智能加交通() {
        return 智能加交通;
    }

    public void set智能加交通(String 智能加交通) {
        this.智能加交通 = 智能加交通;
    }

    public String get区块链技术及应用() {
        return 区块链技术及应用;
    }

    public void set区块链技术及应用(String 区块链技术及应用) {
        this.区块链技术及应用 = 区块链技术及应用;
    }

    public String get现代铁路信息技术() {
        return 现代铁路信息技术;
    }

    public void set现代铁路信息技术(String 现代铁路信息技术) {
        this.现代铁路信息技术 = 现代铁路信息技术;
    }

    public String get军事理论() {
        return 军事理论;
    }

    public void set军事理论(String 军事理论) {
        this.军事理论 = 军事理论;
    }

    public String get军事技能() {
        return 军事技能;
    }

    public void set军事技能(String 军事技能) {
        this.军事技能 = 军事技能;
    }

    public String get体育Ⅰ() {
        return 体育Ⅰ;
    }

    public void set体育Ⅰ(String 体育Ⅰ) {
        this.体育Ⅰ = 体育Ⅰ;
    }

    public String get体育Ⅱ() {
        return 体育Ⅱ;
    }

    public void set体育Ⅱ(String 体育Ⅱ) {
        this.体育Ⅱ = 体育Ⅱ;
    }

    public String get体育Ⅲ() {
        return 体育Ⅲ;
    }

    public void set体育Ⅲ(String 体育Ⅲ) {
        this.体育Ⅲ = 体育Ⅲ;
    }

    public String get体育Ⅳ() {
        return 体育Ⅳ;
    }

    public void set体育Ⅳ(String 体育Ⅳ) {
        this.体育Ⅳ = 体育Ⅳ;
    }

    public String get体育健康课程Ⅰ() {
        return 体育健康课程Ⅰ;
    }

    public void set体育健康课程Ⅰ(String 体育健康课程Ⅰ) {
        this.体育健康课程Ⅰ = 体育健康课程Ⅰ;
    }

    public String get体育健康课程Ⅱ() {
        return 体育健康课程Ⅱ;
    }

    public void set体育健康课程Ⅱ(String 体育健康课程Ⅱ) {
        this.体育健康课程Ⅱ = 体育健康课程Ⅱ;
    }

    public String get英语Ⅰ() {
        return 英语Ⅰ;
    }

    public void set英语Ⅰ(String 英语Ⅰ) {
        this.英语Ⅰ = 英语Ⅰ;
    }

    public String get英语Ⅱ() {
        return 英语Ⅱ;
    }

    public void set英语Ⅱ(String 英语Ⅱ) {
        this.英语Ⅱ = 英语Ⅱ;
    }

    public String get通用学术英语() {
        return 通用学术英语;
    }

    public void set通用学术英语(String 通用学术英语) {
        this.通用学术英语 = 通用学术英语;
    }

    public String get职场英语() {
        return 职场英语;
    }

    public void set职场英语(String 职场英语) {
        this.职场英语 = 职场英语;
    }

    public String get交际与文化视听说() {
        return 交际与文化视听说;
    }

    public void set交际与文化视听说(String 交际与文化视听说) {
        this.交际与文化视听说 = 交际与文化视听说;
    }

    public String get语言文化与翻译() {
        return 语言文化与翻译;
    }

    public void set语言文化与翻译(String 语言文化与翻译) {
        this.语言文化与翻译 = 语言文化与翻译;
    }

    public String get英语公共演讲() {
        return 英语公共演讲;
    }

    public void set英语公共演讲(String 英语公共演讲) {
        this.英语公共演讲 = 英语公共演讲;
    }

    public String get计算机学科前沿导论() {
        return 计算机学科前沿导论;
    }

    public void set计算机学科前沿导论(String 计算机学科前沿导论) {
        this.计算机学科前沿导论 = 计算机学科前沿导论;
    }

    public String get软件学科前沿导论() {
        return 软件学科前沿导论;
    }

    public void set软件学科前沿导论(String 软件学科前沿导论) {
        this.软件学科前沿导论 = 软件学科前沿导论;
    }

    public String get高等数学Ⅰ() {
        return 高等数学Ⅰ;
    }

    public void set高等数学Ⅰ(String 高等数学Ⅰ) {
        this.高等数学Ⅰ = 高等数学Ⅰ;
    }

    public String get高等数学Ⅱ() {
        return 高等数学Ⅱ;
    }

    public void set高等数学Ⅱ(String 高等数学Ⅱ) {
        this.高等数学Ⅱ = 高等数学Ⅱ;
    }

    public String get线性代数B() {
        return 线性代数B;
    }

    public void set线性代数B(String 线性代数B) {
        this.线性代数B = 线性代数B;
    }

    public String get概率论与数理统计() {
        return 概率论与数理统计;
    }

    public void set概率论与数理统计(String 概率论与数理统计) {
        this.概率论与数理统计 = 概率论与数理统计;
    }

    public String get离散数学() {
        return 离散数学;
    }

    public void set离散数学(String 离散数学) {
        this.离散数学 = 离散数学;
    }

    public String get大学物理BⅠ() {
        return 大学物理BⅠ;
    }

    public void set大学物理BⅠ(String 大学物理BⅠ) {
        this.大学物理BⅠ = 大学物理BⅠ;
    }

    public String get大学物理BⅡ() {
        return 大学物理BⅡ;
    }

    public void set大学物理BⅡ(String 大学物理BⅡ) {
        this.大学物理BⅡ = 大学物理BⅡ;
    }

    public String get大学物理实验Ⅰ() {
        return 大学物理实验Ⅰ;
    }

    public void set大学物理实验Ⅰ(String 大学物理实验Ⅰ) {
        this.大学物理实验Ⅰ = 大学物理实验Ⅰ;
    }

    public String get大学物理实验Ⅱ() {
        return 大学物理实验Ⅱ;
    }

    public void set大学物理实验Ⅱ(String 大学物理实验Ⅱ) {
        this.大学物理实验Ⅱ = 大学物理实验Ⅱ;
    }

    public String get高级语言程序设计() {
        return 高级语言程序设计;
    }

    public void set高级语言程序设计(String 高级语言程序设计) {
        this.高级语言程序设计 = 高级语言程序设计;
    }

    public String get面向对象程序设计() {
        return 面向对象程序设计;
    }

    public void set面向对象程序设计(String 面向对象程序设计) {
        this.面向对象程序设计 = 面向对象程序设计;
    }

    public String get数字电子技术B() {
        return 数字电子技术B;
    }

    public void set数字电子技术B(String 数字电子技术B) {
        this.数字电子技术B = 数字电子技术B;
    }

    public String get数字电子技术实验() {
        return 数字电子技术实验;
    }

    public void set数字电子技术实验(String 数字电子技术实验) {
        this.数字电子技术实验 = 数字电子技术实验;
    }

    public String get数据结构A() {
        return 数据结构A;
    }

    public void set数据结构A(String 数据结构A) {
        this.数据结构A = 数据结构A;
    }

    public String get数据结构实验() {
        return 数据结构实验;
    }

    public void set数据结构实验(String 数据结构实验) {
        this.数据结构实验 = 数据结构实验;
    }

    public String get计算机组成原理B() {
        return 计算机组成原理B;
    }

    public void set计算机组成原理B(String 计算机组成原理B) {
        this.计算机组成原理B = 计算机组成原理B;
    }

    public String get计算机组成实验() {
        return 计算机组成实验;
    }

    public void set计算机组成实验(String 计算机组成实验) {
        this.计算机组成实验 = 计算机组成实验;
    }

    public String get算法分析与设计B() {
        return 算法分析与设计B;
    }

    public void set算法分析与设计B(String 算法分析与设计B) {
        this.算法分析与设计B = 算法分析与设计B;
    }

    public String get算法分析与设计实验() {
        return 算法分析与设计实验;
    }

    public void set算法分析与设计实验(String 算法分析与设计实验) {
        this.算法分析与设计实验 = 算法分析与设计实验;
    }

    public String get数据库原理() {
        return 数据库原理;
    }

    public void set数据库原理(String 数据库原理) {
        this.数据库原理 = 数据库原理;
    }

    public String get数据库原理实验() {
        return 数据库原理实验;
    }

    public void set数据库原理实验(String 数据库原理实验) {
        this.数据库原理实验 = 数据库原理实验;
    }

    public String get操作系统() {
        return 操作系统;
    }

    public void set操作系统(String 操作系统) {
        this.操作系统 = 操作系统;
    }

    public String get操作系统实验() {
        return 操作系统实验;
    }

    public void set操作系统实验(String 操作系统实验) {
        this.操作系统实验 = 操作系统实验;
    }

    public String get软件工程() {
        return 软件工程;
    }

    public void set软件工程(String 软件工程) {
        this.软件工程 = 软件工程;
    }

    public String get计算机网络() {
        return 计算机网络;
    }

    public void set计算机网络(String 计算机网络) {
        this.计算机网络 = 计算机网络;
    }

    public String get计算机网络工程实验() {
        return 计算机网络工程实验;
    }

    public void set计算机网络工程实验(String 计算机网络工程实验) {
        this.计算机网络工程实验 = 计算机网络工程实验;
    }

    public String get软件系统分析与设计() {
        return 软件系统分析与设计;
    }

    public void set软件系统分析与设计(String 软件系统分析与设计) {
        this.软件系统分析与设计 = 软件系统分析与设计;
    }

    public String get编译原理() {
        return 编译原理;
    }

    public void set编译原理(String 编译原理) {
        this.编译原理 = 编译原理;
    }

    public String get人机交互技术() {
        return 人机交互技术;
    }

    public void set人机交互技术(String 人机交互技术) {
        this.人机交互技术 = 人机交互技术;
    }

    public String get人机交互技术实验() {
        return 人机交互技术实验;
    }

    public void set人机交互技术实验(String 人机交互技术实验) {
        this.人机交互技术实验 = 人机交互技术实验;
    }

    public String get新技术讲座() {
        return 新技术讲座;
    }

    public void set新技术讲座(String 新技术讲座) {
        this.新技术讲座 = 新技术讲座;
    }

    public String get软件项目管理() {
        return 软件项目管理;
    }

    public void set软件项目管理(String 软件项目管理) {
        this.软件项目管理 = 软件项目管理;
    }

    public String get计算机图形学() {
        return 计算机图形学;
    }

    public void set计算机图形学(String 计算机图形学) {
        this.计算机图形学 = 计算机图形学;
    }

    public String get计算机图形学实验() {
        return 计算机图形学实验;
    }

    public void set计算机图形学实验(String 计算机图形学实验) {
        this.计算机图形学实验 = 计算机图形学实验;
    }

    public String get软件质量保证与测试() {
        return 软件质量保证与测试;
    }

    public void set软件质量保证与测试(String 软件质量保证与测试) {
        this.软件质量保证与测试 = 软件质量保证与测试;
    }

    public String get软件综合课程设计() {
        return 软件综合课程设计;
    }

    public void set软件综合课程设计(String 软件综合课程设计) {
        this.软件综合课程设计 = 软件综合课程设计;
    }

    public String getJava开发技术() {
        return Java开发技术;
    }

    public void setJava开发技术(String java开发技术) {
        Java开发技术 = java开发技术;
    }

    public String getLinux内核分析() {
        return Linux内核分析;
    }

    public void setLinux内核分析(String linux内核分析) {
        Linux内核分析 = linux内核分析;
    }

    public String get软件设计模式() {
        return 软件设计模式;
    }

    public void set软件设计模式(String 软件设计模式) {
        this.软件设计模式 = 软件设计模式;
    }

    public String get软件设计模式实验() {
        return 软件设计模式实验;
    }

    public void set软件设计模式实验(String 软件设计模式实验) {
        this.软件设计模式实验 = 软件设计模式实验;
    }

    public String get数据挖掘() {
        return 数据挖掘;
    }

    public void set数据挖掘(String 数据挖掘) {
        this.数据挖掘 = 数据挖掘;
    }

    public String get人工智能() {
        return 人工智能;
    }

    public void set人工智能(String 人工智能) {
        this.人工智能 = 人工智能;
    }

    public String get云计算与并行技术() {
        return 云计算与并行技术;
    }

    public void set云计算与并行技术(String 云计算与并行技术) {
        this.云计算与并行技术 = 云计算与并行技术;
    }

    public String get幸福心理学() {
        return 幸福心理学;
    }

    public void set幸福心理学(String 幸福心理学) {
        this.幸福心理学 = 幸福心理学;
    }

    public String get信息检索() {
        return 信息检索;
    }

    public void set信息检索(String 信息检索) {
        this.信息检索 = 信息检索;
    }
}
