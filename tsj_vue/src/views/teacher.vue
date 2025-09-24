<template>
  <div class="teacher-container">
    <!-- 顶部标题栏 -->
    <div class="teacher-header">
      <h1 class="header-title">欢迎您，{{ teacherInfo.realName || teacherInfo.username }}！</h1>
    </div>
    <!-- 主体内容区域 -->
    <div class="teacher-main">
      <!-- 左侧菜单 -->
      <div class="teacher-sidebar">
        <div class="menu-item" :class="{ active: currentView === 'exam-plan' }" @click="handleMenuSelect('exam-plan')">
          <el-icon><calendar /></el-icon>
          <span>制订考试计划</span>
        </div>
        <div class="menu-item" :class="{ active: currentView === 'exam-scores' }" @click="handleMenuSelect('exam-scores')">
          <el-icon><upload /></el-icon>
          <span>上传考试成绩</span>
        </div>
        <div class="menu-item" :class="{ active: currentView === 'exam-notice' }" @click="handleMenuSelect('exam-notice')">
          <el-icon><bell /></el-icon>
          <span>发布考试通知</span>
        </div>
        <div class="menu-item" :class="{ active: currentView === 'consultation' }" @click="handleMenuSelect('consultation')">
          <el-icon><chat-line-round /></el-icon>
          <span>回答在线咨询</span>
        </div>
        <div class="menu-item" :class="{ active: currentView === 'personal-info' }" @click="handleMenuSelect('personal-info')">
          <el-icon><user /></el-icon>
          <span>个人信息</span>
        </div>
      </div>
       <!-- 内容区域 -->
       <div class="teacher-content">
         <!-- 制订考试计划 -->
         <TeacherExamPlan v-if="currentView === 'exam-plan'" @switch-to-scores="handleSwitchToScores" />
        
        <!-- 上传考试成绩 -->
        <TeacherExamScores 
          v-if="currentView === 'exam-scores'" 
          :key="`scores-${selectedSubject || 'default'}`"
          :selected-subject="selectedSubject" 
        />
        
        <!-- 发布考试通知 -->
        <TeacherExamNotice v-if="currentView === 'exam-notice'" />
        
        <!-- 回答在线咨询 -->
        <TeacherConsultation v-if="currentView === 'consultation'" :teacherInfo="teacherInfo" />
        
        <!-- 个人信息 -->
        <TeacherInformation 
          v-if="currentView === 'personal-info'" 
          :teacherInfo="teacherInfo"
          @profile-updated="handleProfileUpdated"
          @logout="handleLogout"
            />
          </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { 
  Calendar,
  Bell,
  Upload,
  User,
  ChatLineRound
} from '@element-plus/icons-vue'
import TeacherExamPlan from './teacher-exam-plan.vue'
import TeacherExamNotice from './teacher-exam-notice.vue'
import TeacherExamScores from './teacher-exam-scores.vue'
import TeacherInformation from './teacher-information.vue'
import TeacherConsultation from './teacher-consultation.vue'

// 负责管理四个子模块：制订考试计划、发布考试通知、上传考试成绩、个人信息
export default {
  name: 'TeacherPanel',
  components: {
    Calendar,
    Bell,
    Upload,
    User,
    ChatLineRound,
    TeacherExamPlan,
    TeacherExamNotice,
    TeacherExamScores,
    TeacherInformation,
    TeacherConsultation
  },
  data() {
    return {
      currentView: 'exam-plan',
      selectedSubject: null, // 用于传递选中的学科
      teacherInfo: {
        id: null, 
        username: '', 
        realName: '', 
        phone: '', 
        email: '', 
        password: '',
        userType: null,
        avatar: '', 
        onlineStatus: 0
      }
    }
  },
  mounted() {
    this.initTeacherData()
  },
  methods: {
    // 初始化教师数据，从路由参数中获取用户ID并加载数据
    async initTeacherData() {
      const userId = this.$route.query.userId 
      if (userId) {
        await this.loadTeacherProfile(userId)
      }
    },
    // 通过用户ID从后端加载教师信息
    async loadTeacherProfile(userId) {
      try {
        const response = await axios.get(`http://localhost:8080/api/user/getUserById/${userId}`)
        if (response.data && response.data.success) {
          const userData = response.data.data || response.data.user
          this.teacherInfo = {
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
        }
      } catch (error) {
        console.error('加载教师信息失败:', error)
      }
    },
    /**
     * 处理菜单选择事件
     * @param {string} index - 菜单项索引
     */
    handleMenuSelect(index) {
      this.currentView = index
      // 如果不是切换到成绩管理界面，清除选中的学科
      if (index !== 'exam-scores') {
        this.selectedSubject = null
      }
    },
    /**
     * 处理从考试计划界面切换到成绩管理的事件
     * @param {string} subject - 学科名称
     */
    handleSwitchToScores(subject) {
      console.log('父组件接收到学科切换请求:', subject)
      
      // 先清除之前的选择，确保能触发watch
      this.selectedSubject = null
      
      // 使用nextTick确保DOM更新后再设置新值
      this.$nextTick(() => {
        this.selectedSubject = subject
        this.currentView = 'exam-scores'
        console.log('父组件已切换到成绩界面，传递学科:', subject)
      })
    },
    // 处理子组件的退出登录事件
    handleLogout() {
      this.$router.push({ name: 'Login' })
      ElMessage.success('已退出登录')
    },
    // 处理子组件的个人信息更新事件
    handleProfileUpdated(updatedInfo) {
      // 更新本地数据
      if (updatedInfo.realName !== undefined) {
        this.teacherInfo.realName = updatedInfo.realName
      }
      if (updatedInfo.phone !== undefined) {
        this.teacherInfo.phone = updatedInfo.phone
      }
      if (updatedInfo.email !== undefined) {
        this.teacherInfo.email = updatedInfo.email
      }
      if (updatedInfo.password !== undefined) {
        this.teacherInfo.password = updatedInfo.password
      }
      if (updatedInfo.avatar !== undefined) {
        this.teacherInfo.avatar = updatedInfo.avatar
      }
      if (updatedInfo.id !== undefined) {
        this.teacherInfo.id = updatedInfo.id
      }
      if (updatedInfo.username !== undefined) {
        this.teacherInfo.username = updatedInfo.username
      }
      if (updatedInfo.userType !== undefined) {
        this.teacherInfo.userType = updatedInfo.userType
      }
      if (updatedInfo.onlineStatus !== undefined) {
        this.teacherInfo.onlineStatus = updatedInfo.onlineStatus
      }
      // 更新本地存储的用户信息
      this.updateLocalUserInfo()
    },
    // 更新本地存储的用户信息，同步更新localStorage和sessionStorage中的用户数据
    updateLocalUserInfo() {
      try {
        const updatedUserInfo = {
          id: this.teacherInfo.id,
          username: this.teacherInfo.username,
          realName: this.teacherInfo.realName,
          phone: this.teacherInfo.phone,
          email: this.teacherInfo.email,
          userType: this.teacherInfo.userType,
          avatar: this.teacherInfo.avatar,
          onlineStatus: this.teacherInfo.onlineStatus
        }
        // 更新localStorage
        if (localStorage.getItem('currentUser')) {
          localStorage.setItem('currentUser', JSON.stringify(updatedUserInfo))
        }
        // 更新sessionStorage
        if (sessionStorage.getItem('currentUser')) {
          sessionStorage.setItem('currentUser', JSON.stringify(updatedUserInfo))
        }
      } catch (error) {
        console.error('更新本地用户信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
/* 整体容器 */
.teacher-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #ffffff;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}
/* 顶部标题栏 */
.teacher-header {
  background: #ffffff;
  border-bottom: 3px solid #000000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  height: 70px;
  box-sizing: border-box;
}
.header-title {
  margin: 0;
  color: #000000;
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  flex: 1;
}
/* 主体内容区域 */
.teacher-main {
  display: flex;
  flex: 1;
  height: calc(100vh - 70px);
}
/* 左侧菜单栏 */
.teacher-sidebar {
  width: 200px;
  background: #ffffff;
  border-right: 2px solid #000000;
  padding: 0;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  cursor: pointer;
  border-bottom: 1px solid #000000;
  color: #000000;
  font-size: 20px;
  font-weight: bold;
  transition: background-color 0.3s;
}
.menu-item:hover {
  background-color: #f0f0f0;
}
.menu-item.active {
  background-color: #000000;
  color: #ffffff;
}
.menu-item .el-icon {
  margin-right: 12px;
  font-size: 24px;
}
/* 内容区域 */
.teacher-content {
  flex: 1;
  background: #ffffff;
  padding: 30px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
</style>
