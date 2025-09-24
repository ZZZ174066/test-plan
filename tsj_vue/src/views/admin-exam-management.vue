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
          <div class="filter-label">已考试</div>
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
               <th style="width: 10%;">课程代码</th>
               <th style="width: 30%;">课程名称</th>
               <th style="width: 15%;">考试状态</th>
               <th style="width: 12%;">考试日期</th>
               <th style="width: 11%;">考试时段</th>
               <th style="width: 11%;">考试地点</th>
               <th style="width: 11%;">操作</th>
             </tr>
           </thead>
        </table>
      </div>
      
      <!-- 可滚动表格内容 -->
      <div class="table-body-scrollable">
        <table class="exam-table body-table">
           <tbody>
             <tr v-for="exam in filteredExamList" :key="exam.testId">
               <td style="width: 10%;">{{ exam.courseCode || '-' }}</td>
               <td style="width: 30%;">{{ exam.courseName }}</td>
               <td style="width: 15%;">
                 <span class="status-badge" :class="getStatusClass(exam.examStatus)">
                   {{ exam.examStatus }}
                 </span>
               </td>
               <td style="width: 12%;">{{ exam.examDate || '-' }}</td>
               <td style="width: 11%;">{{ exam.examSession || '-' }}</td>
               <td style="width: 11%;">{{ exam.examLocation || '-' }}</td>
               <td style="width: 11%;">
                <button 
                  v-if="exam.examStatus === '未安排'" 
                  class="action-btn arrange" 
                  @click="arrangeExam(exam)">
                  安排
                </button>
                <button 
                  v-else-if="exam.examStatus === '已安排'" 
                  class="action-btn modify" 
                  @click="modifyExam(exam)">
                  修改
                </button>
                <button 
                  v-else-if="exam.examStatus === '已考试'" 
                  class="action-btn cancel" 
                  @click="resetExam(exam)">
                  重置
                </button>
              </td>
            </tr>
            <tr v-if="filteredExamList.length === 0">
              <td colspan="7" class="no-data">暂无考试数据</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 安排/修改考试弹窗 -->
    <el-dialog v-model="examDialog" :title="dialogTitle" width="500px" :show-close="false">
      <div class="form-content-wrapper">
        <el-form :model="examForm" :rules="examRules" ref="examFormRef" label-width="100px">
          <el-form-item label="课程代码">
            <el-input v-model="examForm.courseCode" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="课程名称">
            <el-input v-model="examForm.courseName" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="考试日期" prop="examDate">
            <el-date-picker
              v-model="examForm.examDate"
              type="date"
              placeholder="选择考试日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="考试时段" prop="examSession">
            <el-select v-model="examForm.examSession" placeholder="请选择考试时段">
              <el-option label="第一场" value="第一场" />
              <el-option label="第二场" value="第二场" />
              <el-option label="第三场" value="第三场" />
            </el-select>
          </el-form-item>
          <el-form-item label="考试地点" prop="examLocation">
            <el-input v-model="examForm.examLocation" placeholder="请输入考试地点"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button class="action-btn save" @click="saveExam">确定</button>
          <button class="action-btn cancel" @click="examDialog = false">取消</button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { 
  Calendar,
  Document, 
  CircleClose, 
  Clock, 
  CircleCheck
} from '@element-plus/icons-vue'

export default {
  name: 'AdminExamManagement',
  components: {
    Calendar,
    Document,
    CircleClose,
    Clock,
    CircleCheck
  },
  data() {
    return {
      examList: [],
      filteredExamList: [],
      currentFilter: 'all',
      searchKeyword: '',
      examStats: {
        total: 0,
        unscheduled: 0,
        scheduled: 0,
        finished: 0
      },
      examDialog: false,
      dialogTitle: '',
      examForm: {
        testId: null,
        courseId: null,
        courseCode: '',
        courseName: '',
        examDate: '',
        examSession: '',
        examLocation: ''
      },
      examRules: {
        examDate: [
          { required: true, message: '请选择考试日期', trigger: 'change' }
        ],
        examSession: [
          { required: true, message: '请选择考试时段', trigger: 'change' }
        ],
        examLocation: [
          { required: true, message: '请输入考试地点', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadExamList()
  },
  methods: {
    async loadExamList() {
      try {
        const response = await axios.get('http://localhost:8080/api/test/list')
        if (response.data && response.data.success) {
          this.examList = (response.data.data || []).sort((a, b) => b.testId - a.testId) // 按ID从大到小排序
          // 检查并更新考试状态
          await this.updateExamStatuses()
          this.calculateStats()
          this.applyFilter()
        } else {
          console.error('获取考试列表失败:', response.data?.message)
          ElMessage.error(response.data?.message || '获取考试列表失败')
        }
      } catch (error) {
        console.error('获取考试列表失败:', error)
        ElMessage.error('获取考试列表失败')
      }
    },
    
    calculateStats() {
      const stats = {
        total: this.examList.length,
        unscheduled: 0,
        scheduled: 0,
        finished: 0
      }
      
      this.examList.forEach(exam => {
        switch (exam.examStatus) {
          case '未安排':
            stats.unscheduled++
            break
          case '已安排':
            stats.scheduled++
            break
          case '已考试':
            stats.finished++
            break
        }
      })
      
      this.examStats = stats
    },
    
    filterExams(filter) {
      this.currentFilter = filter
      this.applyFilter()
    },
    
    applyFilter() {
      let filtered = [...this.examList]
      
      // 按考试状态筛选
      if (this.currentFilter !== 'all') {
        if (this.currentFilter === 'unscheduled') {
          filtered = filtered.filter(exam => exam.examStatus === '未安排')
        } else if (this.currentFilter === 'scheduled') {
          filtered = filtered.filter(exam => exam.examStatus === '已安排')
        } else if (this.currentFilter === 'finished') {
          filtered = filtered.filter(exam => exam.examStatus === '已考试')
        }
      }
      
      // 按搜索关键字筛选
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        filtered = filtered.filter(exam => 
          exam.courseName.toLowerCase().includes(keyword) ||
          (exam.courseCode && exam.courseCode.toLowerCase().includes(keyword))
        )
      }
      
      this.filteredExamList = filtered
    },
    
    handleSearch() {
      this.applyFilter()
    },
    
    handleManualRefresh() {
      this.refreshAllData()
      ElMessage.success('数据已刷新')
    },
    
    async refreshAllData() {
      await this.loadExamList()
    },

    async updateExamStatuses() {
      const today = new Date()
      today.setHours(0, 0, 0, 0) // 设置为当天的开始时间
      
      for (let exam of this.examList) {
        if (exam.examStatus === '已安排' && exam.examDate) {
          const examDate = new Date(exam.examDate)
          examDate.setHours(0, 0, 0, 0)
          
          // 如果考试日期已过，更新状态为已考试
          if (examDate < today) {
            try {
              await axios.post('http://localhost:8080/api/test/updateStatus', {
                testId: exam.testId,
                examStatus: '已考试'
              })
              exam.examStatus = '已考试'
            } catch (error) {
              console.error('更新考试状态失败:', error)
            }
          }
        }
      }
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
    },
    
    arrangeExam(exam) {
      this.dialogTitle = '安排考试'
      this.examForm = {
        testId: exam.testId,
        courseId: exam.courseId || null,
        courseCode: exam.courseCode || '',
        courseName: exam.courseName,
        examDate: '',
        examSession: '',
        examLocation: ''
      }
      this.examDialog = true
    },
    
    modifyExam(exam) {
      this.dialogTitle = '修改考试'
      this.examForm = {
        testId: exam.testId,
        courseId: exam.courseId || null,
        courseCode: exam.courseCode || '',
        courseName: exam.courseName,
        examDate: exam.examDate || '',
        examSession: exam.examSession || '',
        examLocation: exam.examLocation || ''
      }
      this.examDialog = true
    },
    
    async resetExam(exam) {
      try {
        await ElMessageBox.confirm(
          `确定重置"${exam.courseName}"的考试安排吗？重置后该考试将变为未安排状态。`,
          '确认重置',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        const response = await axios.post(`http://localhost:8080/api/test/cancel/${exam.testId}`)
        
        if (response.data && response.data.success) {
          ElMessage.success('考试安排已重置')
          this.loadExamList()
        } else {
          ElMessage.error(response.data?.message || '重置考试安排失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('重置考试安排失败:', error)
          ElMessage.error('重置考试安排失败')
        }
      }
    },
    
    async saveExam() {
      if (!this.$refs.examFormRef) return
      
      this.$refs.examFormRef.validate(async (valid) => {
        if (!valid) return
        
        try {
          // 判断考试时间是否早于当前时间
          const today = new Date()
          today.setHours(0, 0, 0, 0) // 设置为当天的开始时间
          const examDate = new Date(this.examForm.examDate)
          examDate.setHours(0, 0, 0, 0)
          
          // 如果考试日期早于今天，状态设为"已考试"，否则设为"已安排"
          const examStatus = examDate < today ? '已考试' : '已安排'
          
          // 使用 /api/test/update 接口，需要完整的 TestInformation 对象
          const examData = {
            testId: this.examForm.testId,
            courseId: this.examForm.courseId || null,
            courseName: this.examForm.courseName,
            examStatus: examStatus,
            examDate: this.examForm.examDate,
            examSession: this.examForm.examSession,
            examLocation: this.examForm.examLocation
          }
          
          const res = await axios.post('http://localhost:8080/api/test/update', examData)
          
          if (res.data.success) {
            const statusText = examStatus === '已考试' ? '(自动设为已考试状态)' : ''
            ElMessage.success(`考试安排保存成功${statusText}`)
            this.examDialog = false
            await this.refreshAllData()
          } else {
            ElMessage.error(res.data.message || '保存失败')
          }
        } catch (error) {
          console.error('保存考试安排失败:', error)
          ElMessage.error('保存失败，请稍后重试')
        }
      })
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
  flex-wrap: nowrap;
  flex-shrink: 0;
  align-items: center;
  width: 100%;
}

/* 统一筛选卡片 */
.filter-card {
  flex: 1;
  min-width: 200px;
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

.action-btn.arrange {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

.action-btn.modify {
  background: #ffffff;
  border-color: #ff0000;
  color: #000000;
}

.action-btn.cancel {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
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

/* 弹窗样式 */
:deep(.el-dialog) {
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 12px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.8);
}

:deep(.el-dialog__header) {
  background: #000000;
  color: #ffffff;
  padding: 15px 30px;
  border-radius: 12px;
  margin: -5px -5px -5px -5px;
  text-align: center;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  position: relative;
  z-index: 10;
}

:deep(.el-dialog__title) {
  color: #ffffff;
  font-weight: bold;
  font-size: 22px;
  text-align: center;
  width: 100%;
}

:deep(.el-dialog__body) {
  padding: 17px 30px 0px 30px;
  background: #ffffff;
  margin-top: -5px;
  position: relative;
  z-index: 5;
}

.form-content-wrapper {
  border: 2px solid #000000;
  border-radius: 12px;
  padding: 20px;
  background: #ffffff;
  margin-bottom: 10px;
  margin-left: -30px;
  margin-right: -30px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

:deep(.el-form-item) {
  margin-bottom: 15px;
}

:deep(.el-form-item:last-child) {
  margin-bottom: 0px;
}

:deep(.el-form-item__label) {
  color: #000000;
  font-weight: bold;
}

:deep(.el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 4px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background-color: #f5f5f5;
  border-color: #cccccc;
}

:deep(.el-input.is-disabled .el-input__inner) {
  color: #999999;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-date-editor) {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
  padding: 0px 30px 5px 30px;
}

.action-btn.save {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

.action-btn.cancel {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
}

</style>