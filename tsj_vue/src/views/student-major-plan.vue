<template>
  <div class="dashboard">
    <!-- 筛选按钮区域 -->
    <div class="filters-container">
      <!-- 报名状态筛选 -->
      <div class="filter-card" :class="{ black: currentDeclareFilter === 'all', white: currentDeclareFilter !== 'all' }" @click="filterByDeclareStatus('all')">
        <div class="filter-icon">
          <el-icon><list /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ declareStats.total }}</div>
          <div class="filter-label">全部状态</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentDeclareFilter === 'undeclared', white: currentDeclareFilter !== 'undeclared' }" @click="filterByDeclareStatus('undeclared')">
        <div class="filter-icon">
          <el-icon><circle-close /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ declareStats.undeclared }}</div>
          <div class="filter-label">未报名</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentDeclareFilter === 'declared', white: currentDeclareFilter !== 'declared' }" @click="filterByDeclareStatus('declared')">
        <div class="filter-icon">
          <el-icon><clock /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ declareStats.declared }}</div>
          <div class="filter-label">已报名</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentDeclareFilter === 'approved', white: currentDeclareFilter !== 'approved' }" @click="filterByDeclareStatus('approved')">
        <div class="filter-icon">
          <el-icon><circle-check /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ declareStats.approved }}</div>
          <div class="filter-label">已通过</div>
        </div>
      </div>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-container">
      <!-- 筛选标签区域 -->
      <div v-if="courseTypeFilter || courseNatureFilter || semesterFilter || collegeFilter" class="filter-tags-container">
        <div class="filter-tags">
          <span v-if="courseTypeFilter" class="filter-tag">
            {{ courseTypeFilter }}
            <el-icon class="filter-tag-close" @click="clearCourseTypeFilter">
              <Close />
            </el-icon>
          </span>
          <span v-if="courseNatureFilter" class="filter-tag">
            {{ courseNatureFilter }}
            <el-icon class="filter-tag-close" @click="clearCourseNatureFilter">
              <Close />
            </el-icon>
          </span>
          <span v-if="semesterFilter" class="filter-tag">
            第{{ semesterFilter }}学期
            <el-icon class="filter-tag-close" @click="clearSemesterFilter">
              <Close />
            </el-icon>
          </span>
          <span v-if="collegeFilter" class="filter-tag">
            {{ collegeFilter }}
            <el-icon class="filter-tag-close" @click="clearCollegeFilter">
              <Close />
            </el-icon>
          </span>
        </div>
      </div>
      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索课程代码或课程名称" class="search-input" @keyup.enter="handleSearch"></el-input>
        <button class="action-btn search" @click="handleSearch">搜索</button>
        <button class="action-btn refresh" @click="handleManualRefresh">刷新</button>
      </div>
    </div>
    
    <!-- 课程管理表格 -->
    <div class="course-table-container">
      <!-- 固定表头 -->
      <div class="table-header-fixed">
        <table class="course-table header-table">
          <thead>
            <tr>
              <th>课程代码</th>
              <th>课程名称</th>
              <th>课程类型</th>
              <th>课程性质</th>
              <th>学分</th>
              <th>开设学期</th>
              <th>开设学院</th>
              <th>操作</th>
            </tr>
          </thead>
        </table>
      </div>
      <!-- 可滚动表格内容 -->
      <div class="table-body-scrollable">
        <table class="course-table body-table">
          <tbody>
            <tr v-for="course in filteredCourseList" :key="course.courseId">
              <td>{{ course.courseCode }}</td>
              <td>{{ course.courseName }}</td>
              <td>
                <span class="course-type-clickable" @click="filterByCourseType(course.courseType)">
                  {{ course.courseType }}
                </span>
              </td>
              <td>
                <span v-if="course.courseNature === '必修'" class="course-nature required clickable" @click="filterByCourseNature(course.courseNature)">必修</span>
                <span v-else-if="course.courseNature === '限修'" class="course-nature limited clickable" @click="filterByCourseNature(course.courseNature)">限修</span>
                <span v-else-if="course.courseNature === '选修'" class="course-nature elective clickable" @click="filterByCourseNature(course.courseNature)">选修</span>
                <span v-else class="course-nature unknown clickable" @click="filterByCourseNature(course.courseNature)">未知</span>
              </td>
              <td>{{ course.credit }}</td>
              <td>
                <span class="semester-clickable" @click="filterBySemester(course.semester)">
                  第{{ course.semester }}学期
                </span>
              </td>
              <td>
                <span class="college-clickable" @click="filterByCollege(course.college)">
                  {{ course.college }}
                </span>
              </td>
              <td>
                <span v-if="course.declareStatus === '已通过'" class="status-badge approved">已通过</span>
                <button 
                  v-else-if="course.declareStatus === '未报名'" 
                  class="action-btn apply" 
                  @click="applyCourse(course)"
                  :disabled="course.declaring">
                  {{ course.declaring ? '报名中...' : '报名' }}
                </button>
                <button 
                  v-else-if="course.declareStatus === '已报名'" 
                  class="action-btn cancel" 
                  @click="cancelCourse(course)"
                  :disabled="course.canceling">
                  {{ course.canceling ? '取消中...' : '取消' }}
                </button>
                <span v-else class="status-badge unknown">未知状态</span>
              </td>
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
  CircleCheck,
  Close
} from '@element-plus/icons-vue'

export default {
  name: 'StudentMajorPlan',
  components: {
    List,
    CircleClose,
    Clock,
    CircleCheck,
    Close
  },
  data() {
    return {
      declareStats: {
        total: 0,
        undeclared: 0,
        declared: 0,
        approved: 0
      },
      courseList: [],
      currentDeclareFilter: 'all', // 当前报名状态筛选: 'all', 'undeclared', 'declared', 'approved'
      filteredCourseList: [], // 筛选后的课程列表
      searchKeyword: '', // 搜索关键词
      studentId: null, // 当前登录学生的ID
      declareInformation: null, // 学生的报名信息
      courseTypeFilter: null, // 当前课程类型筛选
      courseNatureFilter: null, // 当前课程性质筛选
      semesterFilter: null, // 当前学期筛选
      collegeFilter: null // 当前学院筛选
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
      
      this.loadCourseList()
      this.loadDeclareInformation()
      this.currentDeclareFilter = 'all'
    },
    async loadCourseList() {
      try {
        const res = await axios.get('http://localhost:8080/api/course/getCourse')
        this.courseList = Array.isArray(res.data) ? res.data.sort((a, b) => b.courseId - a.courseId) : [] // 按ID从大到小排序
        
        // 为每个课程添加报名状态信息
        this.courseList = this.courseList.map(course => ({
          ...course,
          declareStatus: '未报名', // 默认状态
          declaring: false, // 是否正在报名中
          canceling: false // 是否正在取消中
        }))
        
        // 如果已有报名信息，更新课程状态
        if (this.declareInformation) {
          this.updateCourseStatuses()
        }
        
        this.applyFilter()
      } catch (error) {
        console.error('加载课程列表失败:', error)
        ElMessage.error('加载课程列表失败')
      }
    },
    refreshAllData() {
      this.loadCourseList()
      this.loadDeclareInformation()
    },
    
    // 手动刷新按钮的方法（保留成功提示）
    handleManualRefresh() {
      this.refreshAllData()
      ElMessage.success('数据已刷新')
    },
    
    async loadDeclareInformation() {
      if (!this.studentId) return
      
      try {
        const res = await axios.get(`http://localhost:8080/api/declare/getDeclareInformationById/${this.studentId}`)
        if (res.data.success) {
          this.declareInformation = res.data.data
          this.updateCourseStatuses()
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
          ElMessage.success('报名记录创建成功')
          // 重新加载报名信息
          await this.loadDeclareInformation()
        }
      } catch (error) {
        console.error('创建报名记录失败:', error)
        ElMessage.error('创建报名记录失败')
      }
    },
    
    updateCourseStatuses() {
      if (!this.declareInformation || !this.courseList.length) return
      
      this.courseList = this.courseList.map(course => {
        // 从报名信息中获取对应课程的状态
        const declareStatus = this.declareInformation[course.courseName] || '未报名'
        return {
          ...course,
          declareStatus: declareStatus
        }
      })
      
      // 计算报名状态统计
      this.calculateDeclareStats()
      
      // 重新应用筛选
      this.applyFilter()
    },
    
    calculateDeclareStats() {
      if (!this.courseList.length) return
      
      const stats = {
        total: this.courseList.length,
        undeclared: 0,
        declared: 0,
        approved: 0
      }
      
      this.courseList.forEach(course => {
        switch (course.declareStatus) {
          case '未报名':
            stats.undeclared++
            break
          case '已报名':
            stats.declared++
            break
          case '已通过':
            stats.approved++
            break
        }
      })
      
      this.declareStats = stats
    },
    filterByDeclareStatus(statusType) {
      this.currentDeclareFilter = statusType
      this.applyFilter()
    },
    applyFilter() {
      let filtered = [...this.courseList]
      
      // 按课程类型筛选
      if (this.courseTypeFilter) {
        filtered = filtered.filter(c => c.courseType === this.courseTypeFilter)
      }
      
      // 按课程性质筛选
      if (this.courseNatureFilter) {
        filtered = filtered.filter(c => c.courseNature === this.courseNatureFilter)
      }
      
      // 按学期筛选
      if (this.semesterFilter) {
        filtered = filtered.filter(c => c.semester === this.semesterFilter)
      }
      
      // 按学院筛选
      if (this.collegeFilter) {
        filtered = filtered.filter(c => c.college === this.collegeFilter)
      }
      
      // 按报名状态筛选
      switch (this.currentDeclareFilter) {
        case 'all': break // 不需要进一步筛选
        case 'undeclared': filtered = filtered.filter(c => c.declareStatus === '未报名'); break
        case 'declared': filtered = filtered.filter(c => c.declareStatus === '已报名'); break
        case 'approved': filtered = filtered.filter(c => c.declareStatus === '已通过'); break
        default: break
      }
      
      // 应用搜索过滤
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        filtered = filtered.filter(course => 
          course.courseCode.toLowerCase().includes(keyword) ||
          course.courseName.toLowerCase().includes(keyword)
        )
      }
      
      this.filteredCourseList = filtered
    },
    handleSearch() {
      this.applyFilter()
    },
    
    // 按课程类型筛选
    filterByCourseType(courseType) {
      this.courseTypeFilter = courseType
      this.applyFilter()
    },
    
    // 按课程性质筛选
    filterByCourseNature(courseNature) {
      this.courseNatureFilter = courseNature
      this.applyFilter()
    },
    
    // 按学期筛选
    filterBySemester(semester) {
      this.semesterFilter = semester
      this.applyFilter()
    },
    
    // 按学院筛选
    filterByCollege(college) {
      this.collegeFilter = college
      this.applyFilter()
    },
    
    // 清除课程类型筛选
    clearCourseTypeFilter() {
      this.courseTypeFilter = null
      this.applyFilter()
    },
    
    // 清除课程性质筛选
    clearCourseNatureFilter() {
      this.courseNatureFilter = null
      this.applyFilter()
    },
    
    // 清除学期筛选
    clearSemesterFilter() {
      this.semesterFilter = null
      this.applyFilter()
    },
    
    // 清除学院筛选
    clearCollegeFilter() {
      this.collegeFilter = null
      this.applyFilter()
    },
    
    async applyCourse(course) {
      // 设置报名中状态
      course.declaring = true
      
      try {
        const res = await axios.post('http://localhost:8080/api/declare/declareCourse', {
          id: this.studentId,
          courseName: course.courseName
        })
        
        if (res.data.success) {
          ElMessage.success(`报名课程成功：${course.courseName}`)
          
          // 刷新整个列表数据
          await this.refreshAllData()
        } else {
          ElMessage.error('报名失败：' + res.data.message)
        }
      } catch (error) {
        console.error('报名课程失败:', error)
        ElMessage.error('报名课程失败，请稍后重试')
      } finally {
        course.declaring = false
      }
    },
    
    async cancelCourse(course) {
      // 设置取消中状态
      course.canceling = true
      
      try {
        const res = await axios.post('http://localhost:8080/api/declare/undeclareCourse', {
          id: this.studentId,
          courseName: course.courseName
        })
        
        if (res.data.success) {
          ElMessage.success(`取消报名成功：${course.courseName}`)
          
          // 刷新整个列表数据
          await this.refreshAllData()
        } else {
          ElMessage.error('取消报名失败：' + res.data.message)
        }
      } catch (error) {
        console.error('取消报名失败:', error)
        ElMessage.error('取消报名失败，请稍后重试')
      } finally {
        course.canceling = false
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

/* 筛选标签容器 */
.filter-tags-container {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0px;
  font-size: 16px;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
  height: 40px;
}

.filter-tags {
  display: flex;
  gap: 8px;
}

.filter-tag {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 8px 16px;
  background: #000000;
  color: #ffffff;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  min-width: 80px;
  height: 40px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  transition: all 0.3s;
  justify-content: center;
}

.filter-tag-close {
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
}

.filter-tag-close:hover {
  color: #ff4444;
}

/* 课程类型可点击样式 */
.course-type-clickable {
  cursor: pointer;
  font-weight: bold;
}

/* 课程性质可点击样式 */
.course-nature.clickable {
  cursor: pointer;
}

/* 学期可点击样式 */
.semester-clickable {
  cursor: pointer;
  font-weight: bold;
}

/* 学院可点击样式 */
.college-clickable {
  cursor: pointer;
  font-weight: bold;
}

/* 搜索区域 */
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

.action-btn.apply {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

.action-btn.cancel {
  background: #ffffff;
  border-color: #ff4444;
  color: #ff4444;
}

.action-btn.cancel:hover:not(:disabled) {
  background: #ff4444 !important;
  color: #ffffff !important;
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

.status-badge.approved {
  background-color: #000000;
  color: #ffffff !important;
}

.status-badge.unknown {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

/* 课程表格容器 */
.course-table-container {
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

.course-table th,
.course-table td {
  padding: 6px 15px;
  text-align: center;
  vertical-align: middle;
  font-size: 14px;
  font-weight: bold;
  color: #000000;
  line-height: 1.1;
}

.course-table th {
  background-color: #000000;
  color: #ffffff;
  font-weight: bold;
  height: 50px;
  border-right: 1px solid #ffffff;
}

.course-table th:last-child {
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

.course-table td {
  border-bottom: 1px solid #000000;
  border-right: 1px solid #000000;
}

.course-table td:last-child {
  border-right: none;
}

.course-table tbody tr {
  height: 50px;
}

.course-table tbody tr:nth-child(even) {
  background-color: #f8f8f8;
}

.course-table tbody tr:hover {
  background-color: #e8e8e8;
}

.course-table td {
  color: #000000 !important;
}

.course-table td * {
  color: inherit !important;
}

/* 课程性质标签 */
.course-nature {
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

.course-nature.required {
  background-color: #000000;
  color: #ffffff !important;
}

.course-nature.limited {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

.course-nature.elective {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

.course-nature.unknown {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

/* 表格列宽设置 */
.course-table th:nth-child(1), .course-table td:nth-child(1) { width: 11%; min-width: 100px; }
.course-table th:nth-child(2), .course-table td:nth-child(2) { width: 28%; min-width: 220px; }
.course-table th:nth-child(3), .course-table td:nth-child(3) { width: 13%; min-width: 110px; }
.course-table th:nth-child(4), .course-table td:nth-child(4) { width: 9%; min-width: 75px; }
.course-table th:nth-child(5), .course-table td:nth-child(5) { width: 6%; min-width: 50px; }
.course-table th:nth-child(6), .course-table td:nth-child(6) { width: 9%; min-width: 75px; }
.course-table th:nth-child(7), .course-table td:nth-child(7) { width: 14%; min-width: 120px; }
.course-table th:nth-child(8), .course-table td:nth-child(8) { width: 10%; min-width: 80px; }

</style>
