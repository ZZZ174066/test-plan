<template>
  <div class="service-center-container">
    <!-- 服务功能卡片区域 -->
    <div class="service-cards">
      <!-- 学习资料 -->
      <div class="service-card" :class="{ active: currentService === 'learning-materials' }" @click="handleServiceClick('learning-materials')">
        <div class="card-icon">
          <el-icon><document /></el-icon>
        </div>
        <div class="card-title">学习资料</div>
    </div>
    
      <!-- 信息通知 -->
      <div class="service-card" :class="{ active: currentService === 'notifications' }" @click="handleServiceClick('notifications')">
        <div class="card-icon">
          <el-icon><bell /></el-icon>
    </div>
        <div class="card-title">信息通知</div>
  </div>
      
      <!-- 在线答疑 -->
      <div class="service-card" :class="{ active: currentService === 'online-qa' }" @click="handleServiceClick('online-qa')">
        <div class="card-icon">
          <el-icon><chat-line-round /></el-icon>
        </div>
        <div class="card-title">在线答疑</div>
      </div>
      
      <!-- 一周计划 -->
      <div class="service-card" :class="{ active: currentService === 'weekly-plan' }" @click="handleServiceClick('weekly-plan')">
        <div class="card-icon">
          <el-icon><calendar /></el-icon>
        </div>
        <div class="card-title">一周计划</div>
      </div>
      
      <!-- 联系我们 -->
      <div class="service-card" :class="{ active: currentService === 'contact-us' }" @click="handleServiceClick('contact-us')">
        <div class="card-icon">
          <el-icon><phone /></el-icon>
        </div>
        <div class="card-title">联系我们</div>
      </div>
    </div>
    
    <!-- 内容展示区域 -->
    <div class="content-area">
      <!-- 学习资料内容 -->
      <div v-if="currentService === 'learning-materials'" class="service-content">
        <div class="learning-materials-container">
          <!-- 搜索区域 -->
          <div class="search-area">
            <div class="search-input-wrapper">
              <el-input
                v-model="searchKeyword"
                placeholder="请输入书名"
                class="search-input"
                @keyup.enter="searchTextbooks"
              />
            </div>
            <button class="action-btn search-btn" @click="searchTextbooks">搜索</button>
            <button class="action-btn refresh-btn" @click="refreshTextbooks">刷新</button>
          </div>

          <!-- 教材卡片区域 -->
          <div class="textbooks-grid">
            <div 
              v-for="textbook in textbooks" 
              :key="textbook.textbookId"
              class="textbook-card"
            >
              <!-- 教材图片 -->
              <div class="textbook-image">
                <img 
                  v-if="textbook.textbookImageUrl" 
                  :src="textbook.textbookImageUrl" 
                  :alt="textbook.textbookName"
                  @error="handleImageError"
                />
                <div v-else class="image-placeholder">
                  <el-icon><document /></el-icon>
                  <span>图片</span>
                </div>
              </div>

              <!-- 教材信息 -->
              <div class="textbook-info">
                <div class="textbook-title">
                  <span class="label">书名：</span>
                  <span class="value">{{ textbook.textbookName }}</span>
                </div>
                <div class="textbook-version">
                  <span class="label">版本：</span>
                  <span class="value">{{ textbook.textbookVersion }}</span>
                </div>

                <!-- 操作按钮 -->
                <div class="textbook-actions">
                  <button class="action-btn detail-btn" @click="showTextbookDetail(textbook)">详情</button>
                  <button 
                    class="action-btn download-btn" 
                    @click="downloadTextbook(textbook)"
                    :disabled="!textbook.downloadUrl"
                  >
                    下载
                  </button>
                </div>
              </div>
            </div>

            <!-- 无数据提示 -->
            <div v-if="textbooks.length === 0" class="no-textbooks">
              <el-icon><document /></el-icon>
              <p>暂无教材资料</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 信息通知内容 -->
      <div v-if="currentService === 'notifications'" class="service-content">
        <div class="notification-interface">
          <!-- 左侧通知记录 -->
          <div class="notice-records">
            <div class="records-header">
              <h3>通知记录</h3>
            </div>
            <div class="records-list">
              <div 
                v-for="notice in studentNotices" 
                :key="notice.notificationId"
                class="notice-item"
                :class="{ active: selectedNotice && selectedNotice.notificationId === notice.notificationId }"
                @click="selectNotice(notice)"
              >
                <div class="notice-title">{{ notice.notificationSubject }}</div>
                <div class="notice-course">{{ notice.notificationCourse }}</div>
                <div class="notice-date">{{ formatDate(notice.publishDate) }}</div>
              </div>
              <div v-if="studentNotices.length === 0" class="no-notices">
                暂无通知记录
              </div>
            </div>
            <div class="records-footer">
              <button class="action-btn refresh" @click="refreshNotices">刷新</button>
            </div>
          </div>

           <!-- 右侧通知内容 -->
           <div class="notice-content">
             <div class="content-header">
               <h3>通知正文</h3>
             </div>
             
             <!-- 通知正文显示 -->
             <div class="notice-detail" v-if="selectedNotice">
               <div class="form-row">
                 <div class="form-group content-group">
                   <div class="detail-content-text">{{ selectedNotice.notificationContent }}</div>
                 </div>
               </div>
             </div>
             
             <!-- 空状态 -->
             <div class="empty-content" v-else>
               <el-icon><bell /></el-icon>
               <p>请点击左侧通知记录查看正文</p>
             </div>
           </div>
        </div>
      </div>
      
      <!-- 在线答疑内容 -->
      <div v-if="currentService === 'online-qa'" class="service-content qa-container">
        <div class="teachers-grid">
          <div 
            v-for="teacher in teachers" 
            :key="teacher.teacherId"
            class="teacher-card"
          >
            <!-- 头像和基本信息区域 -->
            <div class="teacher-header">
              <!-- 教师头像 -->
              <div class="teacher-avatar">
                <div class="avatar-circle-large">
                  <img 
                    v-if="teacher.avatar" 
                    :src="teacher.avatar" 
                    :alt="teacher.teacherName"
                    class="avatar-image-large"
                    @error="handleAvatarError"
                  />
                  <span v-else class="avatar-placeholder-large">头像</span>
                </div>
              </div>
              
              <!-- 教师基本信息 -->
              <div class="teacher-info">
                <div class="info-row">
                  <span class="info-label">教师名：</span>
                  <span class="info-value">{{ teacher.teacherName }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">手机号：</span>
                  <span class="info-value">{{ teacher.phoneNumber || '未设置' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">邮箱：</span>
                  <span class="info-value">{{ teacher.email || '未设置' }}</span>
                </div>
              </div>
            </div>
            
            <!-- 教师学科列表 -->
            <div class="teacher-subjects">
              <div 
                v-for="subject in getTeacherSubjects(teacher)" 
                :key="subject"
                class="subject-item"
              >
                {{ subject }}
              </div>
            </div>
            
            <!-- 操作按钮 -->
            <div class="teacher-actions">
              <span 
                class="status-label"
                :class="{ offline: !teacher.isOnline }"
              >
                {{ teacher.isOnline ? '在线' : '离线' }}
              </span>
              <button 
                class="action-btn consult-btn"
                @click="startConsultation(teacher)"
              >
                咨询
              </button>
            </div>
          </div>
          
          <div v-if="teachers.length === 0" class="no-teachers">
            <el-icon><chat-line-round /></el-icon>
            <p>暂无教师信息</p>
          </div>
        </div>
        
        <!-- 刷新按钮 -->
        <button class="qa-refresh-btn" @click="refreshTeachers">
          刷新
        </button>
      </div>
      
        <!-- 一周计划内容 -->
        <div v-if="currentService === 'weekly-plan'" class="service-content weekly-plan-container">
          <!-- 一周计划标题 -->
          <div class="weekly-plan-title">
            <h2>一周计划表</h2>
          </div>
          
          <!-- 一周计划网格 -->
          <div class="weekly-grid">
            <!-- 第一行：表头 -->
            <div class="session-header"></div>
            <div v-for="(day, dayIndex) in weekDays" :key="`header-${dayIndex}`" class="day-header">{{ day.name }}</div>
            
            <!-- 第二行：第一场 -->
            <div class="session-label">第一场</div>
            <div 
              v-for="(day, dayIndex) in weekDays" 
              :key="`session1-${dayIndex}`"
              class="exam-slot"
              :class="{ 
                'has-exam': getExamForSlot(dayIndex, 0),
                'exam-finished': isExamFinished(getExamForSlot(dayIndex, 0)),
                'today-exam': isToday(dayIndex) && getExamForSlot(dayIndex, 0) && !isExamFinished(getExamForSlot(dayIndex, 0))
              }"
            >
              <template v-if="getExamForSlot(dayIndex, 0)">
                <div class="exam-name">{{ getExamForSlot(dayIndex, 0).examName }}</div>
                <div class="exam-location">{{ getExamForSlot(dayIndex, 0).examLocation }}</div>
              </template>
            </div>
            
            <!-- 第三行：第二场 -->
            <div class="session-label">第二场</div>
            <div 
              v-for="(day, dayIndex) in weekDays" 
              :key="`session2-${dayIndex}`"
              class="exam-slot"
              :class="{ 
                'has-exam': getExamForSlot(dayIndex, 1),
                'exam-finished': isExamFinished(getExamForSlot(dayIndex, 1)),
                'today-exam': isToday(dayIndex) && getExamForSlot(dayIndex, 1) && !isExamFinished(getExamForSlot(dayIndex, 1))
              }"
            >
              <template v-if="getExamForSlot(dayIndex, 1)">
                <div class="exam-name">{{ getExamForSlot(dayIndex, 1).examName }}</div>
                <div class="exam-location">{{ getExamForSlot(dayIndex, 1).examLocation }}</div>
              </template>
            </div>
            
            <!-- 第四行：第三场 -->
            <div class="session-label">第三场</div>
            <div 
              v-for="(day, dayIndex) in weekDays" 
              :key="`session3-${dayIndex}`"
              class="exam-slot"
              :class="{ 
                'has-exam': getExamForSlot(dayIndex, 2),
                'exam-finished': isExamFinished(getExamForSlot(dayIndex, 2)),
                'today-exam': isToday(dayIndex) && getExamForSlot(dayIndex, 2) && !isExamFinished(getExamForSlot(dayIndex, 2))
              }"
            >
              <template v-if="getExamForSlot(dayIndex, 2)">
                <div class="exam-name">{{ getExamForSlot(dayIndex, 2).examName }}</div>
                <div class="exam-location">{{ getExamForSlot(dayIndex, 2).examLocation }}</div>
              </template>
            </div>
          </div>
          
          <!-- 无考试提示 -->
          <div v-if="weeklyExams.length === 0" class="no-exams">
            <el-icon><calendar /></el-icon>
            <p>本周暂无考试安排</p>
          </div>
        </div>
      
      <!-- 联系我们内容 -->
      <div v-if="currentService === 'contact-us'" class="service-content contact-us-container">
        <!-- 联系人卡片网格 -->
        <div class="contact-cards-grid">
          <!-- 罗鹏程 -->
          <div class="contact-card">
            <div class="contact-avatar">
              <img src="@/assets/user/罗鹏程.jpg" alt="罗鹏程" @error="handleImageError" />
            </div>
            <div class="contact-info">
              <div class="info-item">
                <span class="info-label">学号:</span>
                <span class="info-value">2022112510</span>
              </div>
              <div class="info-item">
                <span class="info-label">姓名:</span>
                <span class="info-value">罗鹏程</span>
              </div>
              <div class="info-item">
                <span class="info-label">手机号:</span>
                <span class="info-value">1XXXXXXXXXXX</span>
              </div>
              <div class="info-item">
                <span class="info-label">邮箱:</span>
                <span class="info-value">XXXXX@XXX.com</span>
              </div>
            </div>
          </div>
          
          <!-- 田思杰 -->
          <div class="contact-card">
            <div class="contact-avatar">
              <img src="@/assets/user/田思杰.jpg" alt="田思杰" @error="handleImageError" />
            </div>
            <div class="contact-info">
              <div class="info-item">
                <span class="info-label">学号:</span>
                <span class="info-value">2022112511</span>
              </div>
              <div class="info-item">
                <span class="info-label">姓名:</span>
                <span class="info-value">田思杰</span>
              </div>
              <div class="info-item">
                <span class="info-label">手机号:</span>
                <span class="info-value">1XXXXXXXXXXX</span>
              </div>
              <div class="info-item">
                <span class="info-label">邮箱:</span>
                <span class="info-value">XXXXX@XXX.com</span>
              </div>
            </div>
          </div>
          
          <!-- 赵智宇 -->
          <div class="contact-card">
            <div class="contact-avatar">
              <img src="@/assets/user/赵智宇.jpg" alt="赵智宇" @error="handleImageError" />
            </div>
            <div class="contact-info">
              <div class="info-item">
                <span class="info-label">学号:</span>
                <span class="info-value">2022112516</span>
              </div>
              <div class="info-item">
                <span class="info-label">姓名:</span>
                <span class="info-value">赵智宇</span>
              </div>
              <div class="info-item">
                <span class="info-label">手机号:</span>
                <span class="info-value">1XXXXXXXXXXX</span>
              </div>
              <div class="info-item">
                <span class="info-label">邮箱:</span>
                <span class="info-value">XXXXX@XXX.com</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 咨询对话框 -->
    <el-dialog
      v-model="showConsultDialog"
      :title="currentTeacher ? currentTeacher.teacherName : '在线咨询'"
      width="650px"
      :show-close="false"
      :before-close="handleCloseConsult"
      top="calc(5vh + 50px)"
    >
      <div v-if="currentTeacher" class="chat-container">
        <!-- 聊天消息区域 -->
        <div class="chat-messages" ref="chatMessages" @scroll="onScroll">
          <div v-if="messages.length === 0" class="no-messages">
            <p>开始对话吧！</p>
          </div>
          <div 
            v-for="message in messages" 
            :key="message.id"
            class="chat-message-item"
            :class="message.sender === 'student' ? 'student-message' : 'teacher-message'"
          >
            <img v-if="message.sender === 'student' && studentInfo && studentInfo.avatar" :src="studentInfo.avatar" @error="handleAvatarError" class="avatar-small" />
            <div v-else-if="message.sender === 'student'" class="avatar-placeholder-small">{{ studentInfo && studentInfo.username ? studentInfo.username.charAt(0) : '学' }}</div>
            <img v-else-if="message.sender === 'teacher' && currentTeacher.avatar" :src="currentTeacher.avatar" @error="handleAvatarError" class="avatar-small" />
            <div v-else class="avatar-placeholder-small">{{ currentTeacher.teacherName ? currentTeacher.teacherName.charAt(0) : 'T' }}</div>
            
            <div class="message-content">
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ formatDateTime(message.time) }}</div>
            </div>
          </div>
        </div>
        
        <!-- 聊天输入区域 -->
        <div class="chat-input-area">
          <div class="input-form">
            <el-input
              v-model="newMessage"
              placeholder="请输入您想发送的消息..."
              class="answer-input"
              @keyup.enter="sendMessage"
            />
            <button class="action-btn submit-btn" @click="sendMessage" :disabled="!newMessage.trim()">发送</button>
          </div>
        </div>
      </div>
      
      <template #footer>
        <!-- 空的footer -->
      </template>
    </el-dialog>
    
    <!-- 教材详情弹窗 -->
    <el-dialog
      v-model="showDetailDialog"
      title="教材详情"
      width="500px"
      :show-close="false"
      :before-close="handleCloseDetail"
      top="25vh"
    >
        <div v-if="selectedTextbook" class="textbook-detail-content">
          <!-- 左侧图片区域 -->
          <div class="detail-left">
            <div class="detail-image-container" v-if="selectedTextbook.textbookImageUrl">
              <img :src="selectedTextbook.textbookImageUrl" :alt="selectedTextbook.textbookName" />
            </div>
            <div v-else class="image-placeholder-large">
              <el-icon><document /></el-icon>
              <span>暂无图片</span>
            </div>
          </div>
          
          <!-- 右侧信息区域 -->
          <div class="detail-right">
            <div class="detail-info">
              <div class="detail-row">
                <span class="detail-label">教材名称：</span>
                <span class="detail-value">{{ selectedTextbook.textbookName }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">教材作者：</span>
                <span class="detail-value">{{ selectedTextbook.textbookAuthor }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">教材版本：</span>
                <span class="detail-value">{{ selectedTextbook.textbookVersion }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">出版社：</span>
                <span class="detail-value">{{ selectedTextbook.textbookPublisher }}</span>
              </div>
    </div>
    
            <!-- 操作按键 -->
            <div class="detail-actions">
              <button class="action-btn" @click="handleCloseDetail">关闭</button>
              <button 
                class="action-btn download-btn" 
                @click="downloadTextbook(selectedTextbook)"
                v-if="selectedTextbook && selectedTextbook.downloadUrl"
              >
                下载
              </button>
            </div>
    </div>
        </div>
      
      <template #footer>
        <!-- 空的footer，按键已移至右侧内容区域 -->
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { 
  Document, 
  Bell, 
  ChatLineRound, 
  Calendar, 
  Phone,
  Refresh
} from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'StudentServiceCenter',
  components: {
    Document,
    Bell,
    ChatLineRound,
    Calendar,
    Phone,
    Refresh
  },
  data() {
    return {
      currentService: 'learning-materials', // 默认选中学习资料
      studentNotices: [], // 学生相关通知列表
      selectedNotice: null, // 当前选中的通知
      studentCourses: [], // 学生报名的课程
      studentId: null, // 学生ID
      studentInfo: null, // 学生信息
      // 学习资料相关数据
      textbooks: [], // 教材列表
      searchKeyword: '', // 搜索关键词
      selectedTextbook: null, // 选中的教材
      showDetailDialog: false, // 详情弹窗显示状态
      // 在线答疑相关数据
      teachers: [], // 教师列表
      showConsultDialog: false, // 咨询对话框显示状态
      currentTeacher: null, // 当前咨询的教师
      messages: [], // 对话消息列表
      newMessage: '', // 新消息输入
      messageId: 1, // 消息ID计数器
      refreshTimer: null, // 自动刷新定时器
      isUserScrolling: false, // 用户是否在查看历史消息
      lastMessageCount: 0, // 上次消息数量
      // 一周计划相关数据
      weeklyExams: [] // 一周考试计划
    }
  },
  computed: {
    // 一周的天数数据
    weekDays() {
      const today = new Date()
      const currentWeekDay = today.getDay() // 0 = 周日, 1 = 周一, ..., 6 = 周六
      const mondayOffset = currentWeekDay === 0 ? -6 : 1 - currentWeekDay // 计算到周一的偏移
      
      const days = []
      const dayNames = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      
      for (let i = 0; i < 7; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() + mondayOffset + i)
        days.push({
          name: dayNames[i],
          date: date,
          dateString: this.formatPlanDate(date)
        })
      }
      
      return days
    }
  },
  mounted() {
    this.initData()
  },
  beforeUnmount() {
    this.stopAutoRefresh()
  },
  methods: {
    handleServiceClick(service) {
      this.currentService = service
      // 如果切换到信息通知，加载数据
      if (service === 'notifications') {
        this.loadNotificationData()
      }
      // 如果切换到学习资料，加载教材数据
      if (service === 'learning-materials') {
        this.loadTextbooks()
      }
      // 如果切换到在线答疑，加载教师数据
      if (service === 'online-qa') {
        this.loadTeachers()
      }
      // 如果切换到一周计划，加载考试数据
      if (service === 'weekly-plan') {
        this.loadWeeklyExams()
      }
    },
    
    // 初始化数据
    async initData() {
      // 从路由参数获取学生ID（与其他学生界面保持一致）
      this.studentId = this.$route.query.userId ? parseInt(this.$route.query.userId) : null
      
      // 如果路由参数没有，尝试从localStorage获取
      if (!this.studentId) {
        try {
          const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}')
          this.studentId = currentUser.id
          this.studentInfo = currentUser // 保存完整的学生信息
        } catch (error) {
          console.error('解析用户信息失败:', error)
        }
      } else {
        // 如果有studentId，通过API获取完整的学生信息
        await this.loadStudentInfo(this.studentId)
      }
      
      // 调试信息
      console.log('学生服务中心 - 当前学生ID:', this.studentId)
      console.log('路由参数userId:', this.$route.query.userId)
      
      if (this.studentId) {
        await this.loadStudentCourses()
      } else {
        console.warn('未获取到学生ID，无法加载课程信息')
        ElMessage.warning('未获取到用户信息，请重新登录')
      }
      
      // 默认加载学习资料数据（因为默认选中学习资料）
      this.loadTextbooks()
    },
    
    // 加载学生信息
    async loadStudentInfo(studentId) {
      try {
        const response = await axios.get(`http://localhost:8080/api/user/getUserById/${studentId}`)
        if (response.data && response.data.success) {
          const userData = response.data.data || response.data.user
          this.studentInfo = {
            id: userData.id,
            username: userData.username,
            realName: userData.realName,
            phone: userData.phone,
            email: userData.email,
            password: userData.password,
            userType: userData.userType,
            avatar: userData.avatar,
            onlineStatus: userData.onlineStatus
          }
          console.log('学生信息加载成功:', this.studentInfo)
        } else {
          console.error('加载学生信息失败:', response.data.message)
        }
      } catch (error) {
        console.error('加载学生信息失败:', error)
        // 如果API失败，尝试从localStorage获取
        try {
          const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}')
          if (currentUser.id === studentId) {
            this.studentInfo = currentUser
          }
        } catch (storageError) {
          console.error('从localStorage获取用户信息失败:', storageError)
        }
      }
    },
    
    // 加载通知相关数据
    async loadNotificationData() {
      // 确保有学生ID
      if (!this.studentId) {
        this.studentId = this.$route.query.userId ? parseInt(this.$route.query.userId) : null
        
        if (!this.studentId) {
          try {
            const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}')
            this.studentId = currentUser.id
          } catch (error) {
            console.error('解析用户信息失败:', error)
          }
        }
      }
      
      if (!this.studentId) {
        ElMessage.warning('未获取到用户信息，请重新登录')
        return
      }
      
      if (this.studentCourses.length === 0) {
        await this.loadStudentCourses()
      }
      await this.loadStudentNotices()
    },
    
    // 加载学生报名的课程
    async loadStudentCourses() {
      try {
        const response = await axios.get(`http://localhost:8080/api/declare/getDeclareInformationById/${this.studentId}`)
        if (response.data && response.data.success && response.data.data) {
          const declareInfo = response.data.data
          // 提取所有已报名和已通过的课程名称
          this.studentCourses = []
          
          // 遍历所有课程属性，找出状态为"已报名"或"已通过"的课程
          const courseFields = [
            '思想道德与法治', '中国近现代史纲要', '马克思主义基本原理', '毛泽东思想和中国特色社会主义理论体系概论',
            '习近平新时代中国特色社会主义思想概论', '形势与政策Ⅰ', '形势与政策Ⅱ', '形势与政策Ⅲ', '形势与政策Ⅳ',
            '形势与政策V', '形势与政策VⅠ', '形势与政策VⅡ', '形势与政策VⅢ', '智能加交通', '区块链技术及应用',
            '现代铁路信息技术', '军事理论', '军事技能', '体育Ⅰ', '体育Ⅱ', '体育Ⅲ', '体育Ⅳ', '体育健康课程Ⅰ',
            '体育健康课程Ⅱ', '英语Ⅰ', '英语Ⅱ', '通用学术英语', '职场英语', '交际与文化视听说', '语言文化与翻译',
            '英语公共演讲', '计算机学科前沿导论', '软件学科前沿导论', '高等数学Ⅰ', '高等数学Ⅱ', '线性代数B',
            '概率论与数理统计', '离散数学', '大学物理BⅠ', '大学物理BⅡ', '大学物理实验Ⅰ', '大学物理实验Ⅱ',
            '高级语言程序设计', '面向对象程序设计', '数字电子技术B', '数字电子技术实验', '数据结构A', '数据结构实验',
            '计算机组成原理B', '计算机组成实验', '算法分析与设计B', '算法分析与设计实验', '数据库原理', '数据库原理实验',
            '操作系统', '操作系统实验', '软件工程', '计算机网络', '计算机网络工程实验', '软件系统分析与设计',
            '编译原理', '人机交互技术', '人机交互技术实验', '新技术讲座', '软件项目管理', '计算机图形学',
            '计算机图形学实验', '软件质量保证与测试', '软件综合课程设计', 'Java开发技术', 'Linux内核分析',
            '软件设计模式', '软件设计模式实验', '数据挖掘', '人工智能', '云计算与并行技术', '幸福心理学', '信息检索'
          ]
          
          courseFields.forEach(courseName => {
            const status = declareInfo[courseName]
            if (status === '已报名' || status === '已通过') {
              this.studentCourses.push(courseName)
            }
          })
        }
      } catch (error) {
        console.error('加载学生课程失败:', error)
        ElMessage.error('加载学生课程失败')
      }
    },
    
    // 加载学生相关的通知记录
    async loadStudentNotices() {
      try {
        const response = await axios.get('http://localhost:8080/api/notification/getAllNotifications')
        if (response.data && response.data.success) {
          // 过滤出学生报名学科的通知
          let filteredNotices = response.data.data.filter(notice => 
            this.studentCourses.includes(notice.notificationCourse)
          )
          
          // 按发布时间排序（最新的在前）
          filteredNotices.sort((a, b) => {
            const dateA = new Date(a.publishDate)
            const dateB = new Date(b.publishDate)
            return dateB - dateA // 降序排列，最新的在前
          })
          
          this.studentNotices = filteredNotices
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
      } else {
        // 选中通知
        this.selectedNotice = notice
      }
    },
    
    // 刷新通知数据
    async refreshNotices() {
      await this.loadNotificationData()
      ElMessage.success('刷新成功')
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    // === 学习资料相关方法 ===
    
    // 加载所有教材
    async loadTextbooks() {
      try {
        console.log('开始加载教材数据...')
        const response = await axios.get('http://localhost:8080/api/textbook/getAllTextbooks')
        console.log('教材API响应:', response.data)
        
        if (response.data && response.data.success) {
          this.textbooks = response.data.data || []
          console.log('加载教材成功，数量:', this.textbooks.length)
        } else {
          console.error('加载教材失败:', response.data ? response.data.message : '响应格式错误')
          ElMessage.error('加载教材失败')
        }
      } catch (error) {
        console.error('加载教材失败:', error)
        console.error('错误详情:', error.response ? error.response.data : error.message)
        ElMessage.error('加载教材失败: ' + (error.response ? error.response.status : error.message))
      }
    },

    // 搜索教材
    async searchTextbooks() {
      if (!this.searchKeyword.trim()) {
        // 如果搜索关键词为空，加载所有教材
        this.loadTextbooks()
        return
      }

      try {
        const response = await axios.get('http://localhost:8080/api/textbook/searchByName', {
          params: { textbookName: this.searchKeyword.trim() }
        })
        if (response.data && response.data.success) {
          this.textbooks = response.data.data || []
          if (this.textbooks.length === 0) {
            ElMessage.info('未找到相关教材')
          }
        } else {
          console.error('搜索教材失败:', response.data ? response.data.message : '响应格式错误')
          ElMessage.error('搜索教材失败')
        }
      } catch (error) {
        console.error('搜索教材失败:', error)
        ElMessage.error('搜索教材失败: ' + (error.response ? error.response.status : error.message))
      }
    },

    // 刷新教材列表
    async refreshTextbooks() {
      this.searchKeyword = ''
      await this.loadTextbooks()
      ElMessage.success('刷新成功')
    },

    // 显示教材详情
    showTextbookDetail(textbook) {
      this.selectedTextbook = textbook
      this.showDetailDialog = true
    },

    // 关闭详情弹窗
    handleCloseDetail() {
      this.showDetailDialog = false
      this.selectedTextbook = null
    },

    // 下载教材
    downloadTextbook(textbook) {
      if (!textbook.downloadUrl) {
        ElMessage.warning('该教材暂无下载链接')
        return
      }
      
      // 打开下载链接
      window.open(textbook.downloadUrl, '_blank')
      ElMessage.success('开始下载教材')
    },

    // 处理图片加载错误
    handleImageError(event) {
      // 图片加载失败时隐藏图片，显示占位符
      event.target.style.display = 'none'
      const placeholder = event.target.nextElementSibling
      if (placeholder && placeholder.classList.contains('image-placeholder')) {
        placeholder.style.display = 'flex'
      }
    },

    // === 在线答疑相关方法 ===
    
    // 加载教师列表
    async loadTeachers() {
      try {
        console.log('开始加载教师数据...')
        const response = await axios.get('http://localhost:8080/api/teach/list')
        console.log('教师API响应:', response.data)
        
        if (response.data && response.data.success) {
          // 处理教师数据，将在线状态转换为布尔值
          this.teachers = (response.data.data || []).map(teacher => ({
            ...teacher,
            isOnline: teacher.onlineStatus === 1 // 1表示在线，0表示离线
          }))
          console.log('加载教师成功，数量:', this.teachers.length)
          console.log('教师数据示例:', this.teachers[0]) // 查看数据结构
        } else {
          console.error('加载教师失败:', response.data ? response.data.message : '响应格式错误')
          ElMessage.error('加载教师失败')
        }
      } catch (error) {
        console.error('加载教师失败:', error)
        console.error('错误详情:', error.response ? error.response.data : error.message)
        ElMessage.error('加载教师失败: ' + (error.response ? error.response.status : error.message))
      }
    },

    // 获取教师教授的学科
    getTeacherSubjects(teacher) {
      const subjects = []
      // 从subject1到subject10字段中提取学科信息
      const subjectFields = [
        'subject1', 'subject2', 'subject3', 'subject4', 'subject5',
        'subject6', 'subject7', 'subject8', 'subject9', 'subject10'
      ]
      
      subjectFields.forEach(fieldName => {
        const subject = teacher[fieldName]
        if (subject && subject.trim() !== '' && subject !== '未教授') {
          subjects.push(subject.trim())
        }
      })
      
      return subjects.length > 0 ? subjects : ['暂无课程']
    },

    // 开始咨询
    async startConsultation(teacher) {
      // 确保学生信息已加载
      if (!this.studentInfo && this.studentId) {
        await this.loadStudentInfo(this.studentId)
      }
      
      this.currentTeacher = teacher
      this.newMessage = ''
      this.showConsultDialog = true
      
      // 加载与该教师的对话消息（初始加载）
      this.loadConversationMessages(teacher.teacherId, true)
      
      // 启动自动刷新
      this.startAutoRefresh()
    },

    // 发送消息
    async sendMessage() {
      if (!this.newMessage.trim()) return
      
      const messageContent = this.newMessage.trim()
      
      // 立即显示学生发送的消息
      const newStudentMessage = {
        id: Date.now(),
        sender: 'student',
        content: messageContent,
        time: new Date()
      }
      this.messages.push(newStudentMessage)
      this.lastMessageCount = this.messages.length // 更新消息数量
      this.newMessage = ''
      
      // 滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom()
      })
      
      try {
        const response = await axios.post('http://localhost:8080/api/conversation/send', {
          teacherId: this.currentTeacher.teacherId,
          studentId: this.studentId,
          content: messageContent,
          senderType: 1 // 1-学生发送
        })
        
        if (!response.data || !response.data.success) {
          // 发送失败时移除刚才添加的消息
          this.messages.pop()
          this.lastMessageCount = this.messages.length // 同步更新消息数量
          ElMessage.error('消息发送失败: ' + (response.data.message || '未知错误'))
        }
      } catch (error) {
        // 发送失败时移除刚才添加的消息
        this.messages.pop()
        this.lastMessageCount = this.messages.length // 同步更新消息数量
        console.error('发送消息失败:', error)
        ElMessage.error('发送消息失败: ' + (error.response?.data?.message || error.message))
      }
    },


    // 格式化时间
    formatTime(time) {
      return time.toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    },

    // 格式化日期时间（显示年月日）
    formatDateTime(time) {
      return time.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },

    // 加载对话消息
    async loadConversationMessages(teacherId, isInitialLoad = false) {
      try {
        const response = await axios.get(`http://localhost:8080/api/conversation/messages/${teacherId}/${this.studentId}`)
        
        if (response.data && response.data.success) {
          const messageList = response.data.data || []
          
          // 检查是否有新消息
          const newMessageCount = messageList.length
          const hasNewMessages = newMessageCount > this.lastMessageCount
          
          // 转换为前端消息格式
          this.messages = messageList.map(msg => ({
            id: msg.messageId,
            sender: msg.senderType === 1 ? 'student' : 'teacher',
            content: msg.content,
            time: new Date(msg.sendTime)
          }))
          
          // 更新消息数量
          this.lastMessageCount = newMessageCount

          // 只在以下情况滚动到底部：
          // 1. 初始加载
          // 2. 有新消息且用户不在查看历史消息
          // 3. 用户当前就在底部附近
          if (isInitialLoad || (hasNewMessages && (!this.isUserScrolling || this.isNearBottom()))) {
            this.$nextTick(() => {
              this.scrollToBottom()
            })
          }
        } else {
          console.error('加载对话消息失败:', response.data.message)
          // 清空消息列表
          this.messages = []
          this.lastMessageCount = 0
        }
      } catch (error) {
        console.error('加载对话消息失败:', error)
        // 清空消息列表
        this.messages = []
        this.lastMessageCount = 0
      }
    },


    // 刷新教师列表
    async refreshTeachers() {
      try {
        await this.loadTeachers()
        ElMessage.success('教师列表刷新成功')
      } catch (error) {
        ElMessage.error('刷新失败')
      }
    },

    // 添加显示消息（用于历史记录显示）
    addDisplayMessage(sender, content, time) {
      this.messages.push({
        id: this.messageId++,
        sender: sender,
        content: content,
        time: new Date(time)
      })
      
      this.$nextTick(() => {
        const messagesArea = document.querySelector('.messages-area')
        if (messagesArea) {
          messagesArea.scrollTop = messagesArea.scrollHeight
        }
      })
    },

    // 关闭咨询对话框
    handleCloseConsult() {
      this.stopAutoRefresh()
      this.showConsultDialog = false
      this.currentTeacher = null
      this.messages = []
      this.newMessage = ''
      this.isUserScrolling = false
      this.lastMessageCount = 0
    },

    // 开始自动刷新
    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        this.refreshMessages()
      }, 100) // 每0.1秒刷新一次
    },

    // 停止自动刷新
    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },

    // 刷新消息（静默刷新，不显示提示）
    async refreshMessages() {
      try {
        if (this.currentTeacher) {
          await this.loadConversationMessages(this.currentTeacher.teacherId)
        }
      } catch (error) {
        // 静默处理错误，避免频繁提示
        console.error('自动刷新失败:', error)
      }
    },

    // 滚动到聊天底部
    scrollToBottom() {
      if (this.$refs.chatMessages) {
        this.$refs.chatMessages.scrollTop = this.$refs.chatMessages.scrollHeight
        this.isUserScrolling = false // 自动滚动后重置状态
      }
    },

    // 检查用户是否在底部附近
    isNearBottom() {
      if (!this.$refs.chatMessages) return true
      
      const chatMessages = this.$refs.chatMessages
      const scrollTop = chatMessages.scrollTop
      const scrollHeight = chatMessages.scrollHeight
      const clientHeight = chatMessages.clientHeight
      
      // 如果距离底部小于50px，认为在底部附近
      return scrollHeight - scrollTop - clientHeight < 50
    },

    // 监听滚动事件
    onScroll() {
      if (!this.$refs.chatMessages) return
      
      // 如果用户主动滚动且不在底部，标记为查看历史消息
      this.isUserScrolling = !this.isNearBottom()
    },

    // 一周计划相关方法
    // 加载一周考试计划
    async loadWeeklyExams() {
      // 确保有学生ID
      if (!this.studentId) {
        ElMessage.warning('未获取到用户信息，请重新登录')
        return
      }

      // 确保学生报名课程数据已加载
      if (this.studentCourses.length === 0) {
        await this.loadStudentCourses()
      }

      try {
        // 获取当前周的开始和结束日期
        const today = new Date()
        const currentWeekDay = today.getDay() // 0 = 周日, 1 = 周一, ..., 6 = 周六
        const mondayOffset = currentWeekDay === 0 ? -6 : 1 - currentWeekDay // 计算到周一的偏移
        
        const weekStart = new Date(today)
        weekStart.setDate(today.getDate() + mondayOffset)
        weekStart.setHours(0, 0, 0, 0)
        
        const weekEnd = new Date(weekStart)
        weekEnd.setDate(weekStart.getDate() + 6)
        weekEnd.setHours(23, 59, 59, 999)
        
        // 格式化日期为 YYYY-MM-DD 格式
        const formatDateForAPI = (date) => {
          return date.toISOString().split('T')[0]
        }
        
        const startDate = formatDateForAPI(weekStart)
        const endDate = formatDateForAPI(weekEnd)
        
        // 调用后端API获取所有考试信息
        const response = await axios.get(`http://localhost:8080/api/test/list`)
        
        if (response.data && response.data.success) {
          const allExams = response.data.data || []
          
          // 过滤出学生报名课程的考试，且在当前周内的考试
          const filteredExams = allExams.filter(exam => {
            // 检查考试是否有日期安排
            if (!exam.examDate) return false
            
            // 检查考试课程是否是学生报名的课程
            const isStudentCourse = this.studentCourses.includes(exam.courseName)
            if (!isStudentCourse) return false
            
            // 检查考试日期是否在当前周内
            const examDate = new Date(exam.examDate)
            const isInCurrentWeek = examDate >= weekStart && examDate <= weekEnd
            
            return isInCurrentWeek
          })
          
          // 处理过滤后的考试数据，转换为前端需要的格式
          this.weeklyExams = filteredExams.map(exam => {
            const examDate = new Date(exam.examDate)
            const dayOfWeek = this.getDayOfWeekIndex(examDate)
            const sessionIndex = this.getSessionIndexFromSession(exam.examSession)
            const isFinished = this.checkIfExamFinished(exam.examDate, exam.examSession)
            
            return {
              testId: exam.testId,
              examName: exam.courseName,
              examLocation: exam.examLocation,
              examDate: exam.examDate,
              examSession: exam.examSession,
              examStatus: exam.examStatus,
              dayOfWeek: dayOfWeek,
              sessionIndex: sessionIndex,
              isFinished: isFinished
            }
          })
          console.log('加载一周考试计划成功:', this.weeklyExams.length)
        } else {
          console.warn('获取考试计划返回异常:', response.data)
          this.weeklyExams = []
        }
      } catch (error) {
        console.error('加载一周考试计划失败:', error)
        ElMessage.error('加载考试计划失败')
        this.weeklyExams = []
      }
    },

    // 获取指定位置的考试
    getExamForSlot(dayIndex, sessionIndex) {
      return this.weeklyExams.find(exam => 
        exam.dayOfWeek === dayIndex && exam.sessionIndex === sessionIndex
      )
    },

    // 检查考试是否已结束
    isExamFinished(exam) {
      if (!exam) return false
      return exam.isFinished
    },

    // 检查是否为今天
    isToday(dayIndex) {
      const today = new Date()
      const currentWeekDay = today.getDay() // 0 = 周日, 1 = 周一, ..., 6 = 周六
      const todayIndex = currentWeekDay === 0 ? 6 : currentWeekDay - 1 // 转换为 0=周一, 6=周日
      return dayIndex === todayIndex
    },

    // 格式化日期（用于一周计划）
    formatPlanDate(date) {
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      })
    },

    // 处理图片加载错误
    handleImageError(event) {
      event.target.style.display = 'none'
      const placeholder = document.createElement('div')
      placeholder.className = 'avatar-placeholder'
      placeholder.textContent = '头像'
      event.target.parentNode.appendChild(placeholder)
    },

    // 获取日期对应的星期索引 (0=周一, 1=周二, ..., 6=周日)
    getDayOfWeekIndex(date) {
      const day = date.getDay()
      // 转换JavaScript的星期索引 (0=周日, 1=周一, ..., 6=周六) 
      // 为我们的索引 (0=周一, 1=周二, ..., 6=周日)
      return day === 0 ? 6 : day - 1
    },

    // 根据考试时间确定场次索引
    getSessionIndex(examTime) {
      if (!examTime) return 0
      
      // 提取开始时间
      const timeStr = examTime.split('-')[0] || examTime
      const hour = parseInt(timeStr.split(':')[0])
      
      // 根据时间段划分场次
      if (hour >= 8 && hour < 12) {
        return 0 // 第一场 (8:00-12:00)
      } else if (hour >= 12 && hour < 17) {
        return 1 // 第二场 (12:00-17:00)  
      } else {
        return 2 // 第三场 (17:00及以后)
      }
    },

    // 根据考试场次字符串确定场次索引
    getSessionIndexFromSession(examSession) {
      if (!examSession) return 0
      
      // 根据场次字符串返回索引
      if (examSession === '第一场') {
        return 0
      } else if (examSession === '第二场') {
        return 1
      } else if (examSession === '第三场') {
        return 2
      } else {
        return 0 // 默认第一场
      }
    },

    // 检查考试是否已结束
    checkIfExamFinished(examDate, examSession) {
      if (!examDate) return false
      
      try {
        const now = new Date()
        const examDateTime = new Date(examDate)
        
        // 根据考试场次设置结束时间
        let endHour = 12 // 默认第一场结束时间
        if (examSession === '第一场') {
          endHour = 12 // 第一场结束时间 12:00
        } else if (examSession === '第二场') {
          endHour = 17 // 第二场结束时间 17:00
        } else if (examSession === '第三场') {
          endHour = 21 // 第三场结束时间 21:00
        }
        
        examDateTime.setHours(endHour, 0, 0, 0)
        
        return now > examDateTime
      } catch (error) {
        console.error('检查考试结束时间出错:', error)
        return false
      }
    }
  }
}
</script>

<style scoped>
.service-center-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #ffffff;
}

/* 服务卡片区域 */
.service-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 0px 0;
}

/* 服务卡片样式 */
.service-card {
  flex: 1;
  height: 120px;
  border: 3px solid #000000;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #ffffff;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  position: relative;
  color: #000000;
}

.service-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.7);
}

/* 活跃状态的卡片（反色：黑底白字） */
.service-card.active {
  background: #000000;
  color: #ffffff;
}

/* 卡片图标 */
.card-icon {
  font-size: 48px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000000;
}

.service-card.active .card-icon {
  color: #ffffff;
}

/* 卡片标题 */
.card-title {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  color: #000000;
}

.service-card.active .card-title {
  color: #ffffff;
}

/* 内容展示区域 */
.content-area {
  flex: 1;
  border: 3px solid #000000;
  border-radius: 20px;
  padding: 30px;
  background: #ffffff;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  overflow: hidden;
}

/* 服务内容 */
.service-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 内容头部 - 仅用于其他服务模块，不影响通知模块 */
.service-content > .content-header {
  margin-bottom: 30px;
  text-align: left;
}

.service-content > .content-header h3 {
  color: #000000;
  font-size: 28px;
  margin-bottom: 10px;
  font-weight: bold;
}

.content-header p {
  color: #666666;
  font-size: 16px;
  margin: 0;
}

/* 占位内容 */
.placeholder-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #999999;
}

.placeholder-content .el-icon {
  font-size: 80px;
  margin-bottom: 20px;
  color: #cccccc;
}

.placeholder-content p {
  font-size: 18px;
  color: #999999;
  margin: 0;
}

/* 通知界面样式 - 完全复制教师发布考试通知界面样式 */
.notification-interface {
  height: 100%;
  display: flex;
  background: #ffffff;
}

/* 左侧通知记录 */
.notice-records {
  width: 280px;
  border: 3px solid #000000;
  border-radius: 10px;
  margin: -10px 5px 5px -10px;
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
  margin: -10px 0px 5px 5px;
  display: flex;
  flex-direction: column;
  background: #ffffff;
}

.notice-content .content-header {
  background: #000000 !important;
  color: #ffffff !important;
  padding: 15px;
  text-align: center;
  border-radius: 7px 7px 0 0;
  margin-bottom: 0;
}

.notice-content .content-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #ffffff !important;
}

.notice-detail {
  flex: 1;
  padding: 20px 20px 10px 20px;
  overflow-y: auto;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 5px;
}

.form-group {
  flex: 1;
}

.form-group.subject-select {
  flex: none;
  width: 100%;
}

.form-group.date-select {
  flex: 1;
}

.form-group.content-group {
  flex: none;
  width: 100%;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #000000;
}

.detail-content {
  padding: 12px 15px;
  border: 2px solid #000000;
  border-radius: 5px;
  background: #ffffff;
  color: #000000;
  font-size: 14px;
  min-height: 20px;
}

.detail-content-text {
  padding: 15px;
  border: 2px solid #000000;
  border-radius: 5px;
  background: #ffffff;
  color: #000000;
  font-size: 20px;
  min-height: 300px;
  line-height: 1.6;
  white-space: pre-wrap;
}

.empty-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000000;
  font-size: 16px;
}

.empty-content .el-icon {
  font-size: 60px;
  margin-bottom: 15px;
  color: #cccccc;
}

.empty-content p {
  font-size: 16px;
  color: #000000;
  margin: 0;
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
  min-width: 80px;
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
}



/* === 学习资料样式 === */

.learning-materials-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.search-area {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 0 20px 20px 20px;
  background: #ffffff;
  flex-shrink: 0;
  position: sticky;
  top: 0;
  z-index: 10;
  transform: translateY(-5px);
}

.search-input-wrapper {
  flex: 1;
}

.search-input :deep(.el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 5px;
  box-shadow: none;
  background: #ffffff;
  height: 40px;
  padding: 0 12px;
}

.search-input :deep(.el-input__inner) {
  color: #000000;
  font-size: 16px;
  height: 36px;
  line-height: 36px;
}

.search-btn, .refresh-btn {
  min-width: 80px;
  height: 40px;
  font-size: 16px;
}

.textbooks-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 356px);
  gap: 20px;
  padding: 10px 20px 20px 20px;
  overflow-y: auto;
  flex: 1;
  margin-top: -10px;
  justify-content: start;
}

.textbook-card {
  border: 3px solid #000000;
  border-radius: 5px;
  background: #ffffff;
  padding: 20px;
  display: flex;
  gap: 15px;
  transition: all 0.3s;
  font-weight: bold;
  width:359px;
  height: 140px;
  box-sizing: border-box;
}

.textbook-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.textbook-image {
  width: 80px;
  height: 100px;
  flex-shrink: 0;
  position: relative;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.textbook-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  color: #999;
  font-size: 12px;
}

.image-placeholder .el-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.textbook-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
  position: relative;
}

.textbook-title, .textbook-version {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.textbook-title .label, .textbook-version .label {
  font-weight: bold;
  color: #000000;
  min-width: 50px;
}

.textbook-title .value, .textbook-version .value {
  color: #000000;
  flex: 1;
}

.textbook-actions {
  position: absolute;
  bottom: -5px;
  right: -5px;
  display: flex;
  gap: 10px;
}

.detail-btn, .download-btn {
  min-width: 60px;
  height: 32px;
  font-size: 14px;
  padding: 6px 12px;
}

.download-btn {
  background: #000000;
  color: #ffffff;
}

.download-btn:hover:not(:disabled) {
  background: #333333;
}

.download-btn:disabled {
  background: #cccccc;
  color: #666666;
  cursor: not-allowed;
  border-color: #cccccc;
}

.no-textbooks {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #999999;
}

.no-textbooks .el-icon {
  font-size: 60px;
  margin-bottom: 15px;
}

.no-textbooks p {
  font-size: 16px;
  margin: 0;
}

/* 教材详情弹窗样式 - 符合项目整体风格 */
:deep(.el-dialog) {
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 12px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.8);
  margin-left: auto;
  margin-right: 400px;
}

:deep(.el-dialog__header) {
  background: #000000;
  color: #ffffff;
  padding: 15px 30px;
  border-radius: 12px;
  margin: 0 29px 0 29px;
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

:deep(.el-dialog__headerbtn) {
  top: 18px;
  right: 20px;
  width: 30px;
  height: 30px;
}

:deep(.el-dialog__close) {
  color: #ffffff;
  font-size: 20px;
}

:deep(.el-dialog__close:hover) {
  color: #cccccc;
}

:deep(.el-dialog__body) {
  padding: 17px 30px 0px 30px;
  background: #ffffff;
  margin-top: -5px;
  position: relative;
  z-index: 5;
}

:deep(.el-dialog__footer) {
  background: #ffffff;
  padding: 10px 0px 0px 0px;
  border-top: none;
  border-radius: 0 0 12px 12px;
}

.textbook-detail-content {
  border: 2px solid #000000;
  border-radius: 12px;
  padding: 20px;
  background: #ffffff;
  margin-bottom: 0px;
  margin-left: -30px;
  margin-right: -30px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  display: flex;
  gap: 20px;
  min-height: 200px;
}

/* 左侧图片区域 */
.detail-left {
  flex: 0 0 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.detail-image-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.detail-image-container img {
  max-width: 150px;
  max-height: 180px;
  border: 1px solid #ddd;
  border-radius: 4px;
  object-fit: cover;
}

.image-placeholder-large {
  width: 150px;
  height: 180px;
  border: 2px dashed #ccc;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  background: #f9f9f9;
}

.image-placeholder-large .el-icon {
  font-size: 40px;
  margin-bottom: 10px;
}

.image-placeholder-large span {
  font-size: 14px;
}

/* 右侧信息区域 */
.detail-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.detail-info {
  flex: 1;
}

.detail-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
  font-size: 16px;
}

.detail-row:last-child {
  margin-bottom: 0;
}

.detail-label {
  font-weight: bold;
  color: #000000;
  min-width: 80px;
  flex-shrink: 0;
}

.detail-value {
  color: #000000;
  flex: 1;
  word-break: break-word;
}

/* 右侧操作按键区域 */
.detail-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 10px;
  padding-top: 0px;
  padding-right: 20px;
}

.detail-actions .action-btn {
  min-width: 80px;
  height: 40px;
  font-size: 16px;
  border: 2px solid #000000;
  border-radius: 4px;
  font-weight: bold;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  background: #ffffff;
  color: #000000;
}

.detail-actions .action-btn:hover {
  background: #000000;
  color: #ffffff;
}

.detail-actions .download-btn {
  background: #000000;
  color: #ffffff;
  border-color: #000000;
}

.detail-actions .download-btn:hover {
  background: #333333;
}

.detail-image {
  flex: 1;
}

.detail-image img {
  max-width: 200px;
  max-height: 250px;
  border: 1px solid #ddd;
  border-radius: 4px;
}


.dialog-footer {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
}

.dialog-footer .action-btn {
  min-width: 80px;
  height: 40px;
  font-size: 16px;
  border: 2px solid #000000;
  border-radius: 4px;
  font-weight: bold;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.dialog-footer .action-btn:hover {
  background: #000000 !important;
  color: #ffffff !important;
}

.dialog-footer .download-btn {
  background: #000000;
  color: #ffffff;
  border-color: #000000;
}

.dialog-footer .download-btn:hover {
  background: #333333 !important;
}


/* === 在线答疑教师卡片样式 === */
.qa-container {
  position: relative;
}

.teachers-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding: 20px;
  overflow-y: auto;
  flex: 1;
}

.qa-refresh-btn {
  position: absolute;
  bottom: 20px;
  right: 20px;
  background: #ffffff;
  color: #000000;
  border: 2px solid #000000;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 60px;
  height: 40px;
  transition: all 0.3s;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  z-index: 10;
}

.qa-refresh-btn:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.4);
}

.teacher-card {
  border: 3px solid #000000;
  border-radius: 10px;
  background: #ffffff;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  transition: all 0.3s;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  position: relative;
  height: 350px;
  box-sizing: border-box;
}

.teacher-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.4);
}

/* 教师头像和基本信息区域 */
.teacher-header {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 15px;
}

/* 教师头像 */
.teacher-avatar {
  flex-shrink: 0;
}

.avatar-circle {
  width: 60px;
  height: 60px;
  border: 2px solid #000000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f8f8;
  font-size: 12px;
  font-weight: bold;
  color: #666666;
  overflow: hidden;
  position: relative;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.avatar-placeholder {
  font-size: 12px;
  font-weight: bold;
  color: #666666;
}

/* 大头像样式 */
.avatar-circle-large {
  width: 80px;
  height: 80px;
  border: 3px solid #000000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f8f8;
  font-size: 14px;
  font-weight: bold;
  color: #666666;
  overflow: hidden;
  position: relative;
}

.avatar-image-large {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.avatar-placeholder-large {
  font-size: 14px;
  font-weight: bold;
  color: #666666;
}

/* 咨询对话框中的小头像 */

/* 教师信息 */
.teacher-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
  justify-content: center;
}

.info-row {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.info-label {
  font-weight: bold;
  color: #000000;
  min-width: 60px;
  flex-shrink: 0;
}

.info-value {
  color: #000000;
  flex: 1;
}

/* 教师学科 */
.teacher-subjects {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 0px;
  margin-bottom: 10px;
}

.subject-item {
  padding: 4px 8px;
  background: #ffffff;
  border: 2px solid #000000;
  border-radius: 4px;
  font-size: 12px;
  color: #000000;
  font-weight: bold;
}

/* 操作按钮 */
.teacher-actions {
  position: absolute;
  bottom: 15px;
  right: 15px;
  display: flex;
  gap: 10px;
}

.teacher-actions .action-btn {
  padding: 8px 16px;
  border: 2px solid #000000;
  background: #000000;
  color: #ffffff;
  cursor: pointer;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.3s;
  min-width: 60px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

/* 状态标签样式 */
.status-label {
  padding: 8px 16px;
  border: 2px solid #000000;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  background: #000000;
  color: #ffffff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 60px;
  height: 40px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.status-label.offline {
  background: #ffffff;
  color: #000000;
  border-color: #000000;
}

/* 咨询按钮样式 */
.consult-btn {
  background: #000000;
  border-color: #000000;
  color: #ffffff;
}

.consult-btn:hover {
  background: #333333;
}

.no-teachers {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999999;
  padding: 60px 20px;
}

.no-teachers .el-icon {
  font-size: 80px;
  margin-bottom: 20px;
  color: #cccccc;
}

.no-teachers p {
  font-size: 16px;
  margin: 0;
}

/* === 咨询对话框样式 === */
/* 聊天容器样式 - 完全复制自教师界面 */
.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 500px; /* 固定高度 */
  border: 3px solid #000000;
  border-radius: 12px;
  background: #ffffff;
  overflow: hidden;
}

/* 聊天头部（已删除，保留样式以防需要） */

.teacher-avatar-header {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #000000;
}

.teacher-avatar-placeholder-header {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #000000;
  color: #ffffff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
}

.chat-teacher-name {
  font-size: 18px;
  font-weight: bold;
  color: #ffffff;
}

/* 对话消息区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  border-radius: 9px 9px 0 0;
  max-height: calc(500px - 80px); /* 减去输入区域的高度 */
  min-height: 0; /* 确保可以收缩 */
}

/* 隐藏滚动条 */
.chat-messages::-webkit-scrollbar {
  display: none;
}

.chat-messages {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.chat-message-item {
  display: flex;
  gap: 12px;
}

.student-message {
  flex-direction: row-reverse;
}

.teacher-message {
  flex-direction: row;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.student-message .message-content {
  align-items: flex-end;
}

.teacher-message .message-content {
  align-items: flex-start;
}

.message-text {
  padding: 12px 16px;
  border-radius: 8px;
  line-height: 1.5;
  font-size: 16px;
  font-weight: bold;
  border: 2px solid #000000;
  position: relative;
}

.student-message .message-text {
  background: #000000;
  color: #ffffff;
}

.teacher-message .message-text {
  background: #f8f8f8;
  color: #000000;
}

.chat-message-item .message-time {
  font-size: 16px;
  font-weight: bold;
  color: #000000;
  margin-top: 4px;
  padding: 0 4px;
}

.avatar-small {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #000000;
  flex-shrink: 0;
}

.avatar-placeholder-small {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: #f0f0f0;
  border: 2px solid #000000;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  color: #666;
  flex-shrink: 0;
}

/* 消息包装器样式 */
.message-wrapper {
  margin-bottom: 15px;
  display: flex;
}

.message-wrapper.student-message {
  flex-direction: row-reverse;
}

.message-wrapper.teacher-message {
  flex-direction: row;
}

.message-bubble {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  max-width: 70%;
}

.student-message .message-bubble {
  flex-direction: row-reverse;
}

.teacher-message .message-bubble {
  flex-direction: row;
}

.avatar-container {
  flex-shrink: 0;
}

/* 输入区域 */
.chat-input-area {
  border-top: 3px solid #000000;
  padding: 20px;
  background: #ffffff;
  border-radius: 0 0 9px 9px;
  height: 80px; /* 固定输入区域高度 */
  box-sizing: border-box;
  flex-shrink: 0; /* 防止收缩 */
}

.input-form {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: center;
}

.no-messages {
  padding: 40px 20px;
  text-align: center;
  color: #999999;
}

.answer-input {
  border: 2px solid #000000;
  border-radius: 8px;
  flex: 1;
}

:deep(.el-input__wrapper) {
  border: none;
  font-size: 16px;
  font-weight: bold;
  padding: 8px 16px;
  height: 36px;
  box-sizing: border-box;
}

:deep(.el-input__inner) {
  font-size: 16px;
  font-weight: bold;
  height: 100%;
}

.action-btn {
  padding: 8px 16px;
  border: 2px solid #000000;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
  height: 38px;
  box-sizing: border-box;
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.submit-btn {
  background: #000000;
  color: #ffffff;
}

.submit-btn:hover:not(:disabled) {
  background: #333333;
}

.send-btn {
  background: #000000;
  color: #ffffff;
}

.send-btn:hover:not(:disabled) {
  background: #333333;
}

.send-btn:disabled {
  background: #cccccc;
  color: #666666;
  cursor: not-allowed;
  border-color: #cccccc;
}

/* === 一周计划样式 === */
.weekly-plan-container {
  padding: 0px;
  margin-top: 0px;
  margin-bottom: 0px;
  margin-left: -15px;
  margin-right: -15px;
}

.weekly-plan-title {
  margin-top: -18px;
  margin-bottom: 10px;
}

.weekly-plan-title h2 {
  background: #000000;
  color: #ffffff;
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  padding: 8px 0;
  text-align: center;
  border-radius: 12px;
  border: 3px solid #000000;
}

.weekly-grid {
  display: grid;
  grid-template-columns: 100px repeat(7, 1fr);
  gap: 10px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 12px;
  border: 3px solid #000000;
}

.session-header {
  height: 50px;
  background: #000000;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  border-radius: 8px;
  border: 2px solid #000000;
}

.session-label {
  height: 80px;
  background: #000000;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
  border-radius: 8px;
  border: 2px solid #000000;
}

.day-header {
  height: 50px;
  background: #000000;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  border-radius: 8px;
  border: 2px solid #000000;
}

.exam-slot {
  height: 80px;
  background: #ffffff;
  border: 2px solid #000000;
  border-radius: 8px;
  padding: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  transition: all 0.3s;
}

.exam-slot.has-exam {
  background: #f0f0f0;
  border: 2px solid #000000;
}

.exam-slot.exam-finished {
  background: #000000;
  color: #ffffff;
  border: 2px solid #000000;
}

.exam-slot.today-exam {
  background: #ffffff;
  color: #000000;
  border: 2px solid #ff0000;
}

.exam-name {
  font-weight: bold;
  font-size: 14px;
  text-align: center;
  margin-bottom: 2px;
  line-height: 1.2;
  color: #000000;
}

.exam-location {
  font-size: 12px;
  text-align: center;
  color: #000000;
  line-height: 1.2;
}

.exam-slot.exam-finished .exam-name {
  color: #ffffff;
}

.exam-slot.exam-finished .exam-location {
  color: #cccccc;
}

.exam-slot.today-exam .exam-name {
  color: #000000;
}

.exam-slot.today-exam .exam-location {
  color: #000000;
}

.no-exams {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #999999;
}

.no-exams .el-icon {
  font-size: 60px;
  margin-bottom: 15px;
  color: #cccccc;
}
.no-exams p {
  font-size: 16px;
  margin: 0;
}

/* === 联系我们样式 === */
.contact-us-container {
  padding: 20px 20px 0px 20px;
  margin-top: -20px;
}

.contact-cards-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.contact-card {
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 15px;
  padding: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  transition: transform 0.3s ease;
  min-height: 280px;
}

.contact-card:hover {
  transform: translateY(-5px);
}

.contact-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 3px solid #000000;
  overflow: hidden;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

.contact-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f0f0;
  color: #666666;
  font-size: 14px;
  font-weight: bold;
}

.contact-info {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  font-size: 16px;
}

.info-label {
  font-weight: bold;
  color: #333333;
  min-width: 60px;
}

.info-value {
  color: #000000;
  text-align: right;
  font-weight: normal;
}
</style>