<template>
  <div class="dashboard">
    <!-- 筛选按钮区域 -->
    <div class="filters-container">
      <!-- 成绩状态筛选 -->
      <div class="filter-card" :class="{ black: currentFilter === 'all', white: currentFilter !== 'all' }" @click="filterResults('all')">
        <div class="filter-icon">
          <el-icon><list /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ resultStats.total }}</div>
          <div class="filter-label">全部状态</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'passed', white: currentFilter !== 'passed' }" @click="filterResults('passed')">
        <div class="filter-icon">
          <el-icon><circle-check /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ resultStats.passed }}</div>
          <div class="filter-label">已通过</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'failed', white: currentFilter !== 'failed' }" @click="filterResults('failed')">
        <div class="filter-icon">
          <el-icon><circle-close /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ resultStats.failed }}</div>
          <div class="filter-label">未通过</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'not_uploaded', white: currentFilter !== 'not_uploaded' }" @click="filterResults('not_uploaded')">
        <div class="filter-icon">
          <el-icon><clock /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ resultStats.notUploaded }}</div>
          <div class="filter-label">未上传</div>
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
    
    <!-- 成绩管理表格 -->
    <div class="results-table-container">
      <!-- 固定表头 -->
      <div class="table-header-fixed">
        <table class="results-table header-table">
          <thead>
            <tr>
              <th>课程代码</th>
              <th>课程名称</th>
              <th>课程成绩</th>
              <th>通过情况</th>
              <th>操作</th>
            </tr>
          </thead>
        </table>
      </div>
      
      <!-- 可滚动表格内容 -->
      <div class="table-body-scrollable">
        <table class="results-table body-table">
          <tbody>
            <tr v-for="result in filteredResultsList" :key="result.courseId">
              <td>{{ result.courseCode }}</td>
              <td>{{ result.courseName }}</td>
              <td>{{ result.score !== null && result.score !== undefined ? result.score : '-' }}</td>
              <td>
                <span class="status-badge" :class="getStatusClass(result)">
                  {{ getStatusText(result) }}
                </span>
              </td>
              <td>
                <button class="action-btn appeal" @click="handleAppeal(result)">
                  申述
                </button>
              </td>
            </tr>
            <tr v-if="filteredResultsList.length === 0">
              <td colspan="5" class="no-data">暂无成绩数据</td>
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
  List,
  CircleClose,
  Clock,
  CircleCheck
} from '@element-plus/icons-vue'

export default {
  name: 'StudentExamResults',
  components: {
    List,
    CircleClose,
    Clock,
    CircleCheck
  },
  data() {
    return {
      resultsList: [], // 学生成绩列表
      filteredResultsList: [], // 筛选后的成绩列表
      currentFilter: 'all', // 当前筛选状态: 'all', 'passed', 'failed', 'not_uploaded'
      searchKeyword: '', // 搜索关键词
      studentId: null, // 当前登录学生的ID
      declareInformation: null, // 学生的报名信息
      resultStats: {
        total: 0,
        passed: 0,
        failed: 0,
        notUploaded: 0
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    initData() {
      // 从路由参数获取学生ID
      this.studentId = this.$route.query.userId ? parseInt(this.$route.query.userId) : null
      if (!this.studentId) {
        ElMessage.error('无法获取学生信息，请重新登录')
        return
      }
      
      this.loadData()
    },
    
    async loadData() {
      await this.loadDeclareInformation()
      await this.loadResultsList()
    },
    
    async loadDeclareInformation() {
      if (!this.studentId) return
      
      try {
        const res = await axios.get(`http://localhost:8080/api/declare/getDeclareInformationById/${this.studentId}`)
        if (res.data.success) {
          this.declareInformation = res.data.data
        } else {
          // 如果报名记录不存在，尝试创建
          await this.createDeclareRecord()
        }
      } catch (error) {
        console.error('加载报名信息失败:', error)
        // 尝试创建报名记录
        await this.createDeclareRecord()
      }
    },
    
    async createDeclareRecord() {
      try {
        const res = await axios.post(`http://localhost:8080/api/declare/createDeclareRecordForNewStudent?userId=${this.studentId}`)
        if (res.data.success) {
          // 重新加载报名信息
          await this.loadDeclareInformation()
        }
      } catch (error) {
        console.error('创建报名记录失败:', error)
      }
    },
    
    async loadResultsList() {
      try {
        let studentGrades = null
        let courses = []
        
        // 分别获取成绩和课程信息，避免一个失败影响另一个
        try {
          const scoreRes = await axios.get(`http://localhost:8080/api/grades/getStudentGradesById/${this.studentId}`)
          if (scoreRes.data.success && scoreRes.data.data) {
            studentGrades = scoreRes.data.data
          } else {
            console.warn('学生成绩记录不存在，可能是新学生或尚未录入成绩')
          }
        } catch (scoreError) {
          console.warn('获取学生成绩失败:', scoreError)
          // 如果是404错误，说明学生成绩记录不存在，这是正常情况
          if (scoreError.response?.status === 404) {
            console.info('学生成绩记录不存在，将显示未上传状态')
          } else {
            ElMessage.warning('获取成绩信息失败，将显示未上传状态')
          }
        }
        
        try {
          const courseRes = await axios.get('http://localhost:8080/api/course/getCourse')
          if (Array.isArray(courseRes.data)) {
            courses = courseRes.data
          } else {
            console.error('课程数据格式错误')
            ElMessage.error('无法加载课程信息')
            return
          }
        } catch (courseError) {
          console.error('获取课程信息失败:', courseError)
          ElMessage.error('无法加载课程信息')
          return
        }
        
        // 只显示学生已报名和已通过的学科
        if (this.declareInformation) {
          const declaredAndPassedCourses = courses.filter(course => {
            const status = this.declareInformation[course.courseName]
            return status === '已报名' || status === '已通过'
          })
          
          // 合并课程信息和成绩信息
          this.resultsList = declaredAndPassedCourses.map(course => {
            // 从StudentGrades对象中获取对应课程的成绩
            let score = null
            if (studentGrades && studentGrades[course.courseName] !== undefined) {
              score = studentGrades[course.courseName]
            }
            
            const result = {
              courseId: course.courseId,
              courseCode: course.courseCode,
              courseName: course.courseName,
              score: score,
              declareStatus: this.declareInformation[course.courseName] || '未报名'
            }
            
            // 注意：成绩≥60分且当前状态为"已报名"的课程会在批量更新中自动处理
            // 这里先保持原状态，等待批量更新处理
            
            return result
          })
        } else {
          this.resultsList = []
        }
        
        this.calculateResultStats()
        this.applyFilter()
        
        // 批量检查并更新符合条件的课程报名状态
        await this.batchUpdatePassedCourses()
      } catch (error) {
        console.error('加载成绩列表失败:', error)
        ElMessage.error('加载成绩列表失败')
      }
    },
    
    async updateDeclareStatusToPass(courseName) {
      try {
        // 调用后端API更新报名状态为已通过
        const response = await axios.put('http://localhost:8080/api/declare/updateCourseStatus', null, {
          params: {
            id: this.studentId,
            courseName: courseName,
            status: '已通过'
          }
        })
        
        if (response.data.success) {
          // 同步更新前端数据
          if (this.declareInformation) {
            this.declareInformation[courseName] = '已通过'
          }
          console.log(`课程 ${courseName} 报名状态已自动更新为"已通过"`)
          return true
        } else {
          console.error('更新报名状态失败:', response.data.message)
          return false
        }
      } catch (error) {
        console.error('更新报名状态失败:', error)
        // 即使后端更新失败，前端也保持状态一致性
        if (this.declareInformation) {
          this.declareInformation[courseName] = '已通过'
        }
        return false
      }
    },
    
    calculateResultStats() {
      const stats = {
        total: this.resultsList.length,
        passed: 0,
        failed: 0,
        notUploaded: 0
      }
      
      this.resultsList.forEach(result => {
        if (result.score === null || result.score === undefined) {
          stats.notUploaded++
        } else if (result.score >= 60) {
          stats.passed++
        } else {
          stats.failed++
        }
      })
      
      this.resultStats = stats
    },
    
    filterResults(filter) {
      this.currentFilter = filter
      this.applyFilter()
    },
    
    applyFilter() {
      let filtered = [...this.resultsList]
      
      // 按成绩状态筛选
      switch (this.currentFilter) {
        case 'all':
          break // 不需要进一步筛选
        case 'passed':
          filtered = filtered.filter(result => result.score !== null && result.score >= 60)
          break
        case 'failed':
          filtered = filtered.filter(result => result.score !== null && result.score < 60)
          break
        case 'not_uploaded':
          filtered = filtered.filter(result => result.score === null || result.score === undefined)
          break
        default:
          break
      }
      
      // 应用搜索过滤
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        filtered = filtered.filter(result => 
          result.courseCode.toLowerCase().includes(keyword) ||
          result.courseName.toLowerCase().includes(keyword)
        )
      }
      
      this.filteredResultsList = filtered
    },
    
    handleSearch() {
      this.applyFilter()
    },
    
    handleManualRefresh() {
      this.loadData()
      ElMessage.success('数据已刷新')
    },
    
    /**
     * 批量检查并更新所有符合条件的课程报名状态
     * 当成绩≥60分且当前状态为"已报名"时，自动更新为"已通过"
     */
    async batchUpdatePassedCourses() {
      if (!this.resultsList.length) return
      
      const coursesToUpdate = []
      
      this.resultsList.forEach(result => {
        if (result.score !== null && 
            result.score >= 60 && 
            result.declareStatus === '已报名') {
          coursesToUpdate.push(result.courseName)
        }
      })
      
      if (coursesToUpdate.length > 0) {
        console.log(`发现 ${coursesToUpdate.length} 门课程成绩达到通过标准，准备批量更新报名状态`)
        
        let successCount = 0
        // 批量更新报名状态
        for (const courseName of coursesToUpdate) {
          const success = await this.updateDeclareStatusToPass(courseName)
          if (success) {
            successCount++
          }
        }
        
        // 显示用户友好的提示信息
        if (successCount > 0) {
          ElMessage.success(`🎉 检测到 ${successCount} 门课程成绩达到60分，报名状态已自动更新为"已通过"`)
        }
        
        // 重新加载数据以确保状态同步
        await this.loadDeclareInformation()
        this.calculateResultStats()
        this.applyFilter()
      }
    },
    
    getStatusClass(result) {
      if (result.score === null || result.score === undefined) {
        return 'not-uploaded'
      } else if (result.score >= 60) {
        return 'passed'
      } else {
        return 'failed'
      }
    },
    
    getStatusText(result) {
      if (result.score === null || result.score === undefined) {
        return '未上传'
      } else if (result.score >= 60) {
        return '已通过'
      } else {
        return '未通过'
      }
    },
    
    handleAppeal(result) {
      // 申述功能暂无具体实现，显示提示信息
      ElMessage.info(`${result.courseName} 的申述功能正在开发中`)
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

.filter-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

.filter-card.black {
  background: #000000;
  color: #ffffff;
}

.filter-card.white {
  background: #ffffff;
  color: #000000;
}

.filter-icon {
  font-size: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.filter-content {
  flex: 1;
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

/* 搜索区域 */
.search-container {
  margin-bottom: 20px;
  flex-shrink: 0;
  position: relative;
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

.action-btn.appeal {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

/* 成绩表格容器 */
.results-table-container {
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

.header-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border: none;
  table-layout: fixed;
}

.results-table th,
.results-table td {
  padding: 6px 15px;
  text-align: center;
  vertical-align: middle;
  font-size: 14px;
  font-weight: bold;
  color: #000000;
  line-height: 1.1;
}

.results-table th {
  background-color: #000000;
  color: #ffffff;
  font-weight: bold;
  height: 50px;
  border-right: 1px solid #ffffff;
}

.results-table th:last-child {
  border-right: none;
}

/* 可滚动表格内容容器 */
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

.body-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border: none;
  table-layout: fixed;
}

.results-table td {
  border-bottom: 1px solid #000000;
  border-right: 1px solid #000000;
}

.results-table td:last-child {
  border-right: none;
}

.results-table tbody tr {
  height: 50px;
}

.results-table tbody tr:nth-child(even) {
  background-color: #f8f8f8;
}

.results-table tbody tr:hover {
  background-color: #e8e8e8;
}

.results-table td {
  color: #000000 !important;
}

.results-table td * {
  color: inherit !important;
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

/* 已通过：黑底白字 */
.status-badge.passed {
  background-color: #000000;
  color: #ffffff !important;
}

/* 未通过：红底黑字，黑框 */
.status-badge.failed {
  background-color: #ff4444;
  color: #000000 !important;
  border: 2px solid #000000;
}

/* 未上传：白底黑字，黑框 */
.status-badge.not-uploaded {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

.no-data {
  color: #666666 !important;
  font-style: italic;
}

/* 表格列宽设置 */
.results-table th:nth-child(1), .results-table td:nth-child(1) { width: 15%; min-width: 120px; }
.results-table th:nth-child(2), .results-table td:nth-child(2) { width: 35%; min-width: 280px; }
.results-table th:nth-child(3), .results-table td:nth-child(3) { width: 15%; min-width: 120px; }
.results-table th:nth-child(4), .results-table td:nth-child(4) { width: 20%; min-width: 160px; }
.results-table th:nth-child(5), .results-table td:nth-child(5) { width: 15%; min-width: 120px; }
</style>
