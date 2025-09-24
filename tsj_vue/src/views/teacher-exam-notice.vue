<template>
  <div class="teacher-exam-notice">
    <!-- 左侧通知记录 -->
    <div class="notice-records">
      <div class="records-header">
        <h3>通知记录</h3>
      </div>
      <div class="records-list">
        <div 
          v-for="notice in teacherNotices" 
          :key="notice.notificationId"
          class="notice-item"
          :class="{ active: selectedNotice && selectedNotice.notificationId === notice.notificationId }"
          @click="selectNotice(notice)"
        >
          <div class="notice-title">{{ notice.notificationSubject }}</div>
          <div class="notice-course">{{ notice.notificationCourse }}</div>
          <div class="notice-date">{{ formatDate(notice.publishDate) }}</div>
        </div>
        <div v-if="teacherNotices.length === 0" class="no-notices">
          暂无通知记录
        </div>
      </div>
       <div class="records-footer">
         <button class="action-btn refresh" @click="refreshNotices">刷新</button>
         <button class="action-btn create" @click="createNewNotice">新建</button>
       </div>
    </div>

    <!-- 右侧通知内容 -->
    <div class="notice-content">
      <div class="content-header">
        <h3>{{ isEditing ? '编辑通知' : '通知内容' }}</h3>
      </div>
      
      <!-- 通知表单 -->
      <div class="notice-form" v-if="showForm">
        <div class="form-row">
          <div class="form-group subject-select">
            <label>请选择学科</label>
            <el-select v-model="noticeForm.notificationCourse" placeholder="请选择学科" class="course-select">
              <el-option 
                v-for="subject in teacherSubjects" 
                :key="subject" 
                :label="subject" 
                :value="subject"
              />
            </el-select>
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>请输入主题</label>
            <el-input 
              v-model="noticeForm.notificationSubject" 
              placeholder="请输入主题"
              maxlength="20"
              show-word-limit
            />
          </div>
          <div class="form-group">
            <label>请选择发布时间</label>
            <el-date-picker
              v-model="noticeForm.publishDate"
              type="date"
              placeholder="请选择发布时间"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group content-group">
            <label>请输入正文（上限300字）</label>
             <el-input
               v-model="noticeForm.notificationContent"
               type="textarea"
               placeholder="请输入正文内容"
               :rows="10"
               maxlength="300"
               show-word-limit
               class="content-textarea"
             />
          </div>
        </div>
        
         <div class="form-actions">
           <button class="action-btn delete" @click="deleteNotice" v-if="isEditing">删除</button>
           <button class="action-btn publish" @click="publishNotice">{{ isEditing ? '更新' : '发布' }}</button>
         </div>
      </div>
      
       <!-- 空状态 -->
       <div v-else class="empty-content">
         <p>请选择左侧通知记录查看详情，或点击新建按钮创建新通知</p>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'TeacherExamNotice',
  data() {
    return {
      teacherNotices: [], // 教师的通知列表
      teacherSubjects: [], // 教师教授的学科列表
      selectedNotice: null, // 当前选中的通知
      isEditing: false, // 是否为编辑模式
      showForm: true, // 默认显示表单
      teacherId: null, // 教师ID
      noticeForm: {
        notificationId: null,
        notificationSubject: '',
        notificationCourse: '',
        notificationContent: '',
        publishDate: ''
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    // 初始化数据
    async initData() {
      this.teacherId = this.$route.query.userId
      if (this.teacherId) {
        await this.loadTeacherSubjects()
        await this.loadTeacherNotices()
        // 初始化新增通知表单
        this.resetForm()
      }
    },
    
    // 加载教师教授的学科
    async loadTeacherSubjects() {
      try {
        const response = await axios.get(`http://localhost:8080/api/teach/${this.teacherId}`)
        if (response.data && response.data.success) {
          const teachInfo = response.data.data
          this.teacherSubjects = []
          
          // 提取非空的学科
          for (let i = 1; i <= 10; i++) {
            const subject = teachInfo[`subject${i}`]
            if (subject && subject.trim()) {
              this.teacherSubjects.push(subject.trim())
            }
          }
        }
      } catch (error) {
        console.error('加载教师学科失败:', error)
        ElMessage.error('加载教师学科失败')
      }
    },
    
    // 加载教师的通知记录
    async loadTeacherNotices() {
      try {
        const response = await axios.get('http://localhost:8080/api/notification/getAllNotifications')
        if (response.data && response.data.success) {
          // 过滤出该教师教授学科的通知
          let filteredNotices = response.data.data.filter(notice => 
            this.teacherSubjects.includes(notice.notificationCourse)
          )
          
          // 按发布时间排序（最新的在前）
          filteredNotices.sort((a, b) => {
            const dateA = new Date(a.publishDate)
            const dateB = new Date(b.publishDate)
            return dateB - dateA // 降序排列，最新的在前
          })
          
          this.teacherNotices = filteredNotices
        }
      } catch (error) {
        console.error('加载通知记录失败:', error)
        ElMessage.error('加载通知记录失败')
      }
    },
    
    // 选择通知
    selectNotice(notice) {
      if (this.selectedNotice && this.selectedNotice.notificationId === notice.notificationId) {
        // 取消选中
        this.selectedNotice = null
        this.showForm = false
        this.isEditing = false
      } else {
        // 选中通知
        this.selectedNotice = notice
        this.isEditing = true
        this.showForm = true
        this.fillForm(notice)
      }
    },
    
    // 填充表单
    fillForm(notice) {
      this.noticeForm = {
        notificationId: notice.notificationId,
        notificationSubject: notice.notificationSubject,
        notificationCourse: notice.notificationCourse,
        notificationContent: notice.notificationContent,
        publishDate: notice.publishDate
      }
    },
    
    // 创建新通知
    createNewNotice() {
      this.selectedNotice = null
      this.isEditing = false
      this.showForm = true
      this.resetForm()
    },
    
    // 刷新通知列表
    async refreshNotices() {
      try {
        await this.loadTeacherNotices()
        ElMessage.success('通知列表已刷新')
      } catch (error) {
        ElMessage.error('刷新失败')
      }
    },
    
    // 重置表单
    resetForm() {
      this.noticeForm = {
        notificationId: null,
        notificationSubject: '',
        notificationCourse: this.teacherSubjects.length > 0 ? this.teacherSubjects[0] : '',
        notificationContent: '',
        publishDate: new Date().toISOString().split('T')[0]
      }
    },
    
    // 发布/更新通知
    async publishNotice() {
      if (!this.validateForm()) {
        return
      }
      
      try {
        let response
        if (this.isEditing) {
          // 更新通知
          response = await axios.put('http://localhost:8080/api/notification/updateNotification', this.noticeForm)
        } else {
          // 创建新通知
          response = await axios.post('http://localhost:8080/api/notification/createNotification', this.noticeForm)
        }
        
        if (response.data && response.data.success) {
          ElMessage.success(this.isEditing ? '通知更新成功' : '通知发布成功')
          await this.loadTeacherNotices()
          this.showForm = false
          this.selectedNotice = null
          this.isEditing = false
        } else {
          ElMessage.error(response.data.message || '操作失败')
        }
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    },
    
    // 删除通知
    async deleteNotice() {
      try {
        await ElMessageBox.confirm('确定要删除这条通知吗？', '确认删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await axios.delete(`http://localhost:8080/api/notification/deleteNotification/${this.noticeForm.notificationId}`)
        if (response.data && response.data.success) {
          ElMessage.success('通知删除成功')
          await this.loadTeacherNotices()
          this.showForm = false
          this.selectedNotice = null
          this.isEditing = false
        } else {
          ElMessage.error(response.data.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          ElMessage.error('删除失败')
        }
      }
    },
    
    // 表单验证
    validateForm() {
      if (!this.noticeForm.notificationSubject.trim()) {
        ElMessage.error('请输入通知主题')
        return false
      }
      if (!this.noticeForm.notificationCourse) {
        ElMessage.error('请选择学科')
        return false
      }
      if (!this.noticeForm.notificationContent.trim()) {
        ElMessage.error('请输入通知内容')
        return false
      }
      if (!this.noticeForm.publishDate) {
        ElMessage.error('请选择发布时间')
        return false
      }
      return true
    },
    
    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    }
  }
}
</script>

<style scoped>
.teacher-exam-notice {
  height: 100%;
  display: flex;
  background: #ffffff;
}

/* 左侧通知记录 */
.notice-records {
  width: 280px;
  border: 3px solid #000000;
  border-radius: 10px;
  margin: 10px;
  display: flex;
  flex-direction: column;
  background: #ffffff;
}

.records-header {
  background: #000000;
  color: #ffffff;
  padding: 15px;
  text-align: center;
  border-radius: 7px 7px 0 0;
}

.records-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.records-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  max-height: calc(100vh - 180px);
  min-height: 200px;
}

.notice-item {
  padding: 12px;
  border: 2px solid #000000;
  border-radius: 8px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #ffffff;
}

.notice-item:hover {
  background: #f5f5f5;
}

.notice-item.active {
  background: #000000;
  color: #ffffff;
}

.notice-title {
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 4px;
  color: #000000;
}

.notice-item.active .notice-title {
  color: #ffffff;
}

.notice-course {
  font-size: 12px;
  color: #000000;
  margin-bottom: 4px;
}

.notice-item.active .notice-course {
  color: #cccccc;
}

.notice-date {
  font-size: 11px;
  color: #000000;
}

.notice-item.active .notice-date {
  color: #aaaaaa;
}

.no-notices {
  text-align: center;
  color: #999999;
  padding: 20px;
}

.records-footer {
  padding: 15px;
  border-top: 1px solid #000000;
  display: flex;
  gap: 10px;
  justify-content: center;
}

/* 右侧通知内容 */
.notice-content {
  flex: 1;
  border: 3px solid #000000;
  border-radius: 10px;
  margin: 10px 10px 10px 0;
  display: flex;
  flex-direction: column;
  background: #ffffff;
}

.content-header {
  background: #000000;
  color: #ffffff;
  padding: 15px;
  text-align: center;
  border-radius: 7px 7px 0 0;
}

.content-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.notice-form {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  flex: 1;
}

.subject-select {
  flex: none;
  width: 100%;
}

.content-group {
  flex: none;
  width: 100%;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #000000;
}

.course-select {
  width: 100%;
}

.content-textarea {
  width: 100%;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

/* 统一按钮样式 */
.action-btn {
  padding: 8px 16px;
  border: 2px solid #000000;
  background: #ffffff;
  color: #000000;
  cursor: pointer;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.action-btn:hover {
  background: #000000 !important;
  color: #ffffff !important;
}

.action-btn:active {
  background: #000000 !important;
  color: #ffffff !important;
}

.action-btn.refresh {
  background: #ffffff;
  border-color: #000000;
  color: #000000;
  min-width: 80px;
}

.action-btn.create {
  background: #000000;
  border-color: #000000;
  color: #ffffff;
  min-width: 80px;
}

.action-btn.delete {
  background: #ffffff;
  border-color: #ff4444;
  color: #ff4444;
  min-width: 80px;
}

.action-btn.delete:hover {
  background: #ff4444 !important;
  color: #ffffff !important;
}

.action-btn.publish {
  background: #000000;
  border-color: #000000;
  color: #ffffff;
  min-width: 80px;
}

.empty-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000000;
  font-size: 16px;
}

/* Element Plus 样式覆盖 */
:deep(.el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 5px;
  box-shadow: none;
  background: #ffffff;
}

:deep(.el-input__inner) {
  border: none;
  box-shadow: none;
  color: #000000;
}

:deep(.el-textarea__inner) {
  border: 2px solid #000000;
  border-radius: 5px;
  font-family: inherit;
  color: #000000;
}

:deep(.el-select .el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 5px;
  box-shadow: none;
  background: #ffffff;
}

:deep(.el-select .el-input__inner) {
  border: none;
  box-shadow: none;
  color: #000000;
}

:deep(.el-date-editor.el-input) {
  width: 100%;
}

:deep(.el-date-editor .el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 5px;
  box-shadow: none;
  background: #ffffff;
}

:deep(.el-date-editor .el-input__inner) {
  border: none;
  box-shadow: none;
  color: #000000;
}

:deep(.el-select__placeholder) {
  color: #666666;
}

:deep(.el-input__count) {
  color: #000000;
}

:deep(.el-textarea__count) {
  color: #000000;
}

/* 聚焦状态样式 */
:deep(.el-input.is-focus .el-input__wrapper) {
  border-color: #000000;
  box-shadow: none;
}


:deep(.el-select.is-focus .el-input__wrapper) {
  border-color: #000000;
  box-shadow: none;
}

:deep(.el-date-editor.is-focus .el-input__wrapper) {
  border-color: #000000;
  box-shadow: none;
}
</style>
