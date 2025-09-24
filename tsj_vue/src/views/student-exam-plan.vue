<template>
  <div class="dashboard">
    <!-- 筛选按钮区域 -->
    <div class="filters-container">
      <!-- 考试状态筛选 -->
      <div class="filter-card" :class="{ black: currentFilter === 'all', white: currentFilter !== 'all' }" @click="filterExams('all')">
        <div class="filter-icon">
          <el-icon><document /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ examStats.total }}</div>
          <div class="filter-label">全部考试</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'unscheduled', white: currentFilter !== 'unscheduled' }" @click="filterExams('unscheduled')">
        <div class="filter-icon">
          <el-icon><circle-close /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ examStats.unscheduled }}</div>
          <div class="filter-label">未安排</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'scheduled', white: currentFilter !== 'scheduled' }" @click="filterExams('scheduled')">
        <div class="filter-icon">
          <el-icon><clock /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ examStats.scheduled }}</div>
          <div class="filter-label">已安排</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'finished', white: currentFilter !== 'finished' }" @click="filterExams('finished')">
        <div class="filter-icon">
          <el-icon><circle-check /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ examStats.finished }}</div>
          <div class="filter-label">已结束</div>
        </div>
      </div>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-container">
      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索课程代码或课程名称" class="search-input" @keyup.enter="handleSearch"></el-input>
        <button class="action-btn search" @click="handleSearch">搜索</button>
        <button class="action-btn refresh" @click="handleManualRefresh">刷新</button>
      </div>
    </div>
    
    <!-- 考试管理表格 -->
    <div class="exam-table-container">
      <!-- 固定表头 -->
      <div class="table-header-fixed">
        <table class="exam-table header-table">
          <thead>
            <tr>
              <th style="width: 15%;">课程代码</th>
              <th style="width: 30%;">课程名称</th>
              <th style="width: 20%;">考试状态</th>
              <th style="width: 12%;">考试日期</th>
              <th style="width: 12%;">考试时段</th>
              <th style="width: 11%;">考试地点</th>
            </tr>
          </thead>
        </table>
      </div>
      
      <!-- 可滚动表格内容 -->
      <div class="table-body-scrollable">
        <table class="exam-table body-table">
          <tbody>
            <tr v-for="exam in filteredExamList" :key="exam.testId">
              <td style="width: 15%;">{{ exam.courseCode || '-' }}</td>
              <td style="width: 30%;">{{ exam.courseName }}</td>
              <td style="width: 20%;">
                <span class="status-badge" :class="getStatusClass(exam.examStatus)">
                  {{ exam.examStatus }}
                </span>
              </td>
              <td style="width: 12%;">{{ exam.examDate || '-' }}</td>
              <td style="width: 12%;">{{ exam.examSession || '-' }}</td>
              <td style="width: 11%;">{{ exam.examLocation || '-' }}</td>
            </tr>
            <tr v-if="filteredExamList.length === 0">
              <td colspan="6" class="no-data">暂无已报名的考试数据</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { 
  Document, 
  CircleClose, 
  Clock, 
  CircleCheck,
  List
} from '@element-plus/icons-vue'

export default {
  name: 'StudentExamPlan',
  components: {
    Document,
    CircleClose,
    Clock,
    CircleCheck,
    List
  },
  data() {
    return {
      examList: [],
      filteredExamList: [],
      currentFilter: 'all',
      searchKeyword: '',
      studentId: null,
      declareInformation: null,
      examStats: {
        total: 0,
        unscheduled: 0,
        scheduled: 0,
        finished: 0
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    async initData() {
      // 获取学生ID
      this.studentId = this.$route.query.userId
      
      // 先加载考试统计和列表
      await this.loadExamStats()
      await this.loadExamList()
      
      // 然后加载报名信息并更新状态
      await this.loadDeclareInformation()
    },
    
    async loadExamStats() {
      // 考试统计现在基于已报名的学科，在 calculateExamStats 方法中计算
      this.examStats = {
        total: 0,
        unscheduled: 0,
        scheduled: 0,
        finished: 0
      }
    },
    
    async loadExamList() {
      try {
        console.log('正在加载考试列表...')
        const res = await axios.get('http://localhost:8080/api/test/list')
        console.log('考试列表API响应:', res.data)
        
        if (res.data.success) {
          this.examList = res.data.data.sort((a, b) => b.testId - a.testId).map(exam => ({
            ...exam,
            declareStatus: '未报名' // 设置默认报名状态
          }))
          console.log('加载到考试数据:', this.examList.length, '条')
          console.log('考试数据示例:', this.examList[0])
          this.applyFilter()
        } else {
          console.error('考试列表API返回失败:', res.data.message)
          ElMessage.error('加载考试列表失败：' + res.data.message)
        }
      } catch (error) {
        console.error('加载考试列表失败:', error)
        ElMessage.error('加载考试列表失败，请稍后重试')
      }
    },
    
    async loadDeclareInformation() {
      if (!this.studentId) return
      
      try {
        const res = await axios.get(`http://localhost:8080/api/declare/getDeclareInformationById/${this.studentId}`)
        
        if (res.data.success) {
          this.declareInformation = res.data.data
          this.updateExamStatuses() // 这里已经会调用 calculateExamStats()
          this.applyFilter()
        } else {
          this.setDefaultDeclareStatuses()
        }
      } catch (error) {
        // 如果是404错误，说明该学生没有报名记录，尝试创建
        if (error.response?.status === 404) {
          await this.createDeclareRecord()
        } else {
          this.setDefaultDeclareStatuses()
        }
      }
    },
    
    async createDeclareRecord() {
      try {
        const res = await axios.post(`http://localhost:8080/api/declare/createDeclareRecordForNewStudent?userId=${this.studentId}`)
        
        if (res.data.success) {
          // 创建成功后重新加载报名信息
          await this.loadDeclareInformation()
        } else {
          this.setDefaultDeclareStatuses()
        }
      } catch (error) {
        this.setDefaultDeclareStatuses()
      }
    },
    
    setDefaultDeclareStatuses() {
      this.examList = this.examList.map(exam => ({
        ...exam,
        declareStatus: '未报名'
      }))
      this.calculateExamStats()
    },
    
    updateExamStatuses() {
      if (!this.declareInformation || !this.examList.length) return
      
      this.examList = this.examList.map(exam => {
        const declareStatus = this.getCourseStatus(exam.courseName)
        return {
          ...exam,
          declareStatus
        }
      })
      
      // 更新状态后重新计算统计
      this.calculateExamStats()
    },
    
    getCourseStatus(courseName) {
      if (!this.declareInformation || !courseName) return '未报名'
      
      // 根据课程名称获取报名状态
      const status = this.declareInformation[courseName]
      
      // 如果报名信息中没有该课程，尝试去除空格匹配
      if (!status) {
        const normalizedCourseName = courseName.trim()
        const matchingKey = Object.keys(this.declareInformation).find(key => 
          key.trim() === normalizedCourseName
        )
        if (matchingKey) {
          return this.declareInformation[matchingKey] || '未报名'
        }
      }
      
      return status || '未报名'
    },
    
    calculateExamStats() {
      if (!this.examList.length) return
      
      // 只统计已报名的学科考试
      const declaredExams = this.examList.filter(exam => exam.declareStatus === '已报名')
      
      this.examStats = {
        total: declaredExams.length,
        unscheduled: 0,
        scheduled: 0,
        finished: 0
      }
      
      declaredExams.forEach(exam => {
        if (exam.examStatus === '未安排') {
          this.examStats.unscheduled++
        } else if (exam.examStatus === '已安排') {
          this.examStats.scheduled++
        } else if (exam.examStatus === '已考试') {
          this.examStats.finished++
        }
      })
    },
    
    filterExams(filter) {
      this.currentFilter = filter
      this.applyFilter()
    },
    
    applyFilter() {
      // 只显示已报名的学科考试
      let filtered = this.examList.filter(exam => exam.declareStatus === '已报名')
      console.log('应用过滤器，原始考试数据:', this.examList.length, '条')
      console.log('已报名的考试数据:', filtered.length, '条')
      
      // 按考试状态筛选
      if (this.currentFilter !== 'all') {
        if (this.currentFilter === 'unscheduled') {
          filtered = filtered.filter(exam => exam.examStatus === '未安排')
        } else if (this.currentFilter === 'scheduled') {
          filtered = filtered.filter(exam => exam.examStatus === '已安排')
        } else if (this.currentFilter === 'finished') {
          filtered = filtered.filter(exam => exam.examStatus === '已考试')
        }
        console.log('按状态筛选后:', filtered.length, '条，筛选条件:', this.currentFilter)
      }
      
      // 按搜索关键字筛选
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        filtered = filtered.filter(exam => 
          exam.courseName.toLowerCase().includes(keyword) ||
          (exam.courseCode && exam.courseCode.toLowerCase().includes(keyword))
        )
        console.log('按关键字筛选后:', filtered.length, '条，关键字:', keyword)
      }
      
      this.filteredExamList = filtered
      console.log('最终显示的考试数据:', this.filteredExamList.length, '条')
    },
    
    handleSearch() {
      this.applyFilter()
    },
    
    handleManualRefresh() {
      this.refreshAllData()
      ElMessage.success('数据已刷新')
    },
    
    async refreshAllData() {
      await this.loadExamStats()
      await this.loadExamList()
      await this.loadDeclareInformation()
    },
    
    getStatusClass(status) {
      switch (status) {
        case '未安排':
          return 'unscheduled'
        case '已安排':
          return 'scheduled'
        case '已考试':
          return 'finished'
        default:
          return 'unknown'
      }
    }
  }
}
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 统一筛选器容器 */
.filters-container {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  flex-shrink: 0;
  align-items: center;
}

/* 统一筛选卡片 */
.filter-card {
  min-width: 180px;
  height: 100px;
  border: 2px solid #000000;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  padding: 15px;
  flex: 1;
}

.filter-card.black {
  background-color: #000000;
  color: #ffffff;
}

.filter-card.white {
  background-color: #ffffff;
  color: #000000;
}

.filter-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

.filter-icon {
  font-size: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.filter-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px;
}

.filter-number {
  font-size: 26px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 4px;
}

.filter-label {
  font-size: 14px;
  font-weight: bold;
}

/* 分隔线 */
.filter-divider {
  width: 2px;
  height: 80px;
  background: linear-gradient(to bottom, transparent, #000000, transparent);
  margin: 0 6px;
  opacity: 0.6;
}

/* 搜索区域 */
.search-container {
  margin-bottom: 20px;
  flex-shrink: 0;
}

.search-box {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 15px;
}

.search-input {
  width: 300px;
}

:deep(.search-input .el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 4px;
  background: #ffffff;
  height: 40px;
  box-sizing: border-box;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

:deep(.search-input .el-input__inner) {
  color: #000000;
  font-size: 16px;
  font-weight: bold;
  height: 36px;
  line-height: 36px;
}

/* 考试表格容器 */
.exam-table-container {
  margin-top: 0px;
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  border: 2px solid #000000;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

/* 固定表头 */
.table-header-fixed {
  flex-shrink: 0;
  border: 2px solid #000000;
  border-bottom: none;
  border-radius: 12px 12px 0 0;
  overflow: hidden;
  background: #000000;
}

.table-body-scrollable {
  flex: 1;
  overflow-y: auto;
  border: 2px solid #000000;
  border-top: none;
  border-radius: 0 0 12px 12px;
  background: #ffffff;
}

.table-body-scrollable::-webkit-scrollbar {
  display: none;
}

.header-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border: none;
  table-layout: fixed;
}

.body-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border: none;
  table-layout: fixed;
}

.exam-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.exam-table th,
.exam-table td {
  padding: 6px 15px;
  text-align: center;
  vertical-align: middle;
  font-size: 14px;
  font-weight: bold;
  color: #000000;
  line-height: 1.1;
}

.exam-table th {
  background-color: #000000;
  color: #ffffff;
  font-weight: bold;
  height: 50px;
  border-right: 1px solid #ffffff;
}

.exam-table th:last-child {
  border-right: none;
}

.exam-table td {
  border-bottom: 1px solid #000000;
  border-right: 1px solid #000000;
}

.exam-table td:last-child {
  border-right: none;
}

.exam-table tbody tr {
  height: 50px;
}

.exam-table tbody tr:nth-child(even) {
  background-color: #f8f8f8;
}

.exam-table tbody tr:hover {
  background-color: #e8e8e8;
}

.no-data {
  color: #666666 !important;
  font-style: italic;
}

/* 状态标签样式 */
.status-badge {
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  min-width: 80px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  transition: all 0.3s;
}

.status-badge.unscheduled {
  background-color: #ffffff;
  color: #000000;
  border: 2px solid #000000;
}

.status-badge.scheduled {
  background-color: #ffffff;
  color: #000000;
  border: 2px solid #ff0000;
}

.status-badge.finished {
  background-color: #000000;
  color: #ffffff;
}

.status-badge.unknown {
  background-color: #a0aec0;
  color: #ffffff;
}

/* 操作按钮 */
.action-btn {
  padding: 8px 16px;
  border: 2px solid #000000;
  background: #ffffff;
  color: #000000;
  cursor: pointer;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.3s;
  min-width: 80px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.action-btn:hover {
  background: #000000 !important;
  color: #ffffff !important;
}

.action-btn:active {
  background: #000000 !important;
  color: #ffffff !important;
}

.action-btn:disabled {
  background: #cccccc !important;
  color: #666666 !important;
  cursor: not-allowed !important;
  border-color: #cccccc !important;
}

.action-btn.search {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

.action-btn.refresh {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

.action-btn.view {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

</style>