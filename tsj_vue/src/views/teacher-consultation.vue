<template>
  <div class="consultation-container">
    <div class="consultation-content">
      <!-- 咨询列表 -->
      <div class="consultations-list">
        <div class="list-header">
          <h3>消息列表</h3>
        </div>

        <div class="consultations-scroll">
           <div 
             v-for="conversation in groupedConversations" 
             :key="conversation.studentId"
             class="message-item"
             :class="{ selected: selectedStudent && selectedStudent.studentId === conversation.studentId }"
             @click="selectStudent(conversation)"
           >
             <div class="avatar-section">
               <img 
                 v-if="conversation.studentAvatar" 
                 :src="conversation.studentAvatar" 
                 class="user-avatar"
                 @error="handleAvatarError"
               />
               <span v-else class="user-avatar-placeholder">
                 {{ conversation.studentName ? conversation.studentName.charAt(0) : 'S' }}
               </span>
             </div>
             <div class="message-info">
               <div class="user-name">{{ conversation.studentName || '未知学生' }}</div>
               <div class="last-message">{{ conversation.lastMessageContent || '暂无消息' }}</div>
               <div class="message-time">{{ formatDateTime(conversation.lastMessageTime) }}</div>
             </div>
           </div>

           <div v-if="groupedConversations.length === 0" class="no-consultations">
             <p>暂无学生对话</p>
           </div>
        </div>
      </div>

      <!-- 聊天内容 -->
      <div class="chat-detail">
        <div v-if="selectedStudent" class="chat-container">
          <!-- 聊天头部 -->
          <div class="chat-header">
            <h3>{{ selectedStudent.studentName || '未知学生' }}</h3>
          </div>

          <!-- 对话消息区域 -->
          <div class="chat-messages" ref="chatMessages">
             <div 
               v-for="message in currentStudentMessages" 
               :key="message.messageId"
               class="chat-message-item"
               :class="message.senderType === 1 ? 'student-message' : 'teacher-message'"
             >
               <!-- 学生消息 -->
               <template v-if="message.senderType === 1">
                 <div class="message-avatar">
                   <img 
                     v-if="selectedStudent.studentAvatar" 
                     :src="selectedStudent.studentAvatar" 
                     class="avatar-small"
                     @error="handleAvatarError"
                   />
                   <span v-else class="avatar-placeholder-small">
                     {{ selectedStudent.studentName ? selectedStudent.studentName.charAt(0) : 'S' }}
                   </span>
                 </div>
                 <div class="message-content">
                   <div class="message-text">
                     {{ message.content }}
                   </div>
                   <div class="message-time">{{ formatDateTime(message.sendTime) }}</div>
                 </div>
               </template>

              <!-- 教师消息 -->
              <template v-else>
                <div class="message-avatar">
                  <img 
                    v-if="teacherInfo.avatar" 
                    :src="teacherInfo.avatar" 
                    class="avatar-small"
                    @error="handleAvatarError"
                  />
                  <span v-else class="avatar-placeholder-small">
                    {{ teacherInfo.realName ? teacherInfo.realName.charAt(0) : 'T' }}
                  </span>
                </div>
                 <div class="message-content">
                   <div class="message-text">
                     {{ message.content }}
                   </div>
                   <div class="message-time">{{ formatDateTime(message.sendTime) }}</div>
                 </div>
              </template>
            </div>

            <div v-if="currentStudentMessages.length === 0" class="no-messages">
              <p>暂无对话记录</p>
            </div>
          </div>

           <!-- 消息输入区域 -->
           <div class="chat-input-area" v-if="selectedStudent">
             <div class="input-form">
               <el-input
                 v-model="messageText"
                 placeholder="输入您想发送的消息..."
                 class="answer-input"
                 @keyup.enter="sendMessage"
               />
               <button 
                 class="action-btn submit-btn" 
                 @click="sendMessage"
                 :disabled="!messageText.trim()"
               >
                 发送
               </button>
             </div>
           </div>
        </div>

        <div v-else class="no-selection">
          <div class="no-selection-content">
            <el-icon class="no-selection-icon"><chat-line-round /></el-icon>
            <p>请选择一个学生查看对话历史</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { ElMessage, ElInput, ElIcon } from 'element-plus'
import { ChatLineRound } from '@element-plus/icons-vue'

export default {
  name: 'TeacherConsultation',
  components: {
    ElInput,
    ElIcon,
    ChatLineRound
  },
  props: {
    teacherInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      conversations: [], // 改为对话列表
      selectedStudent: null, // 选中的学生
      currentFilter: 'all',
      messageText: '', // 消息输入框
      messages: [], // 当前对话的消息列表
      refreshTimer: null // 自动刷新定时器
    }
  },
  computed: {
    // 对话列表（按最后消息时间排序）
    groupedConversations() {
      // 直接返回对话列表，按最后消息时间排序
      return this.conversations.sort((a, b) => new Date(b.lastMessageTime) - new Date(a.lastMessageTime))
    },

    filteredConversations() {
      if (this.currentFilter === 'pending') {
        return this.groupedConversations.filter(conv => conv.hasUnread)
      } else if (this.currentFilter === 'answered') {
        return this.groupedConversations.filter(conv => !conv.hasUnread)
      }
      return this.groupedConversations
    },

    // 当前选中学生的消息列表，按时间排序
    currentStudentMessages() {
      return this.messages.sort((a, b) => new Date(a.sendTime) - new Date(b.sendTime))
    }
  },
  mounted() {
    this.loadConversations()
    this.startAutoRefresh()
  },
  beforeUnmount() {
    this.stopAutoRefresh()
  },
  methods: {
    async loadConversations() {
      if (!this.teacherInfo.id) {
        ElMessage.error('教师信息获取失败')
        return
      }

      try {
        const response = await axios.get(`http://localhost:8080/api/conversation/teacher/${this.teacherInfo.id}`)
        
        if (response.data && response.data.success) {
          this.conversations = response.data.data || []
          console.log('加载对话数据成功:', this.conversations.length)
        } else {
          console.error('加载对话数据失败:', response.data.message)
          ElMessage.error('加载对话数据失败')
        }
      } catch (error) {
        console.error('加载对话数据失败:', error)
        ElMessage.error('加载对话数据失败: ' + (error.response?.data?.message || error.message))
      }
    },

    setFilter(filter) {
      this.currentFilter = filter
    },

    async selectStudent(conversation) {
      this.selectedStudent = conversation
      this.messageText = ''
      await this.loadMessages(conversation.studentId)
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    },

    async loadMessages(studentId) {
      if (!this.teacherInfo.id || !studentId) {
        return
      }

      try {
        const response = await axios.get(`http://localhost:8080/api/conversation/messages/${this.teacherInfo.id}/${studentId}`)
        
        if (response.data && response.data.success) {
          this.messages = response.data.data || []
          console.log('加载消息数据成功:', this.messages.length)
        } else {
          console.error('加载消息数据失败:', response.data.message)
        }
      } catch (error) {
        console.error('加载消息数据失败:', error)
      }
    },

    async sendMessage() {
      if (!this.messageText.trim()) {
        ElMessage.warning('请输入消息内容')
        return
      }

      if (!this.selectedStudent) {
        ElMessage.warning('请先选择对话学生')
        return
      }

      try {
        const response = await axios.post('http://localhost:8080/api/conversation/send', {
          teacherId: this.teacherInfo.id,
          studentId: this.selectedStudent.studentId,
          content: this.messageText.trim(),
          senderType: 2 // 2-教师发送
        })

        if (response.data && response.data.success) {
          ElMessage.success('消息发送成功')
          this.messageText = ''
          
          // 重新加载消息
          await this.loadMessages(this.selectedStudent.studentId)
          await this.loadConversations()
          
          // 更新当前选中的对话
          const updatedConversation = this.conversations.find(
            c => c.studentId === this.selectedStudent.studentId
          )
          if (updatedConversation) {
            this.selectedStudent = updatedConversation
          }
          
          this.$nextTick(() => {
            this.scrollToBottom()
          })
        } else {
          ElMessage.error('消息发送失败: ' + (response.data.message || '未知错误'))
        }
      } catch (error) {
        console.error('发送消息失败:', error)
        ElMessage.error('发送消息失败: ' + (error.response?.data?.message || error.message))
      }
    },

    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString('zh-CN')
    },

    scrollToBottom() {
      if (this.$refs.chatMessages) {
        this.$refs.chatMessages.scrollTop = this.$refs.chatMessages.scrollHeight
      }
    },

    handleAvatarError(event) {
      event.target.style.display = 'none'
      event.target.nextElementSibling.style.display = 'inline-block'
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
        // 刷新对话列表
        await this.loadConversations()
        
        // 如果有选中的学生，刷新其消息
        if (this.selectedStudent) {
          await this.loadMessages(this.selectedStudent.studentId)
        }
      } catch (error) {
        // 静默处理错误，避免频繁提示
        console.error('自动刷新失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.consultation-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #ffffff;
}


.consultation-content {
  display: flex;
  flex: 1;
  height: 100%;
  padding: 20px;
  gap: 0px;
}

/* 咨询列表样式 */
.consultations-list {
  width: 400px;
  border: 3px solid #000000;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  background: #ffffff;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  margin-right: 20px;
}

.list-header {
  padding: 15px;
  border-bottom: 2px solid #000000;
  background: #000000;
  border-radius: 9px 9px 0 0;
  text-align: center;
}

.list-header h3 {
  margin: 0;
  color: #ffffff;
  font-size: 18px;
  font-weight: bold;
}

.filter-buttons {
  display: flex;
  gap: 10px;
}

.filter-btn {
  padding: 6px 12px;
  border: 2px solid #000000;
  background: #ffffff;
  color: #000000;
  font-size: 14px;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  background: #f0f0f0;
}

.filter-btn.active {
  background: #000000;
  color: #ffffff;
}

.consultations-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
}

.message-item {
  display: flex;
  padding: 15px;
  border: 3px solid #000000;
  border-radius: 8px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s;
  background: #ffffff;
  color: #000000;
  align-items: flex-start;
  gap: 12px;
}

.message-item.selected {
  background: #000000;
  color: #ffffff;
}

.message-item.selected .message-time {
  color: #ffffff;
}

.message-item.selected .last-message {
  color: #ffffff;
}

.avatar-section {
  flex-shrink: 0;
}

.user-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ddd;
}

.user-avatar-placeholder {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #f0f0f0;
  border: 2px solid #ddd;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  font-weight: bold;
  color: #666;
}

.message-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-weight: bold;
  font-size: 16px;
  color: inherit;
  margin-bottom: 4px;
}

.last-message {
  font-size: 16px;
  font-weight: bold;
  color: #000000;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.message-time {
  font-size: 14px;
  font-weight: bold;
  color: #000000;
}

.no-consultations {
  padding: 40px 20px;
  text-align: center;
  color: #999999;
}

/* 聊天详情样式 */
.chat-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 3px solid #000000;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 聊天头部 */
.chat-header {
  padding: 15px;
  border-bottom: 2px solid #000000;
  background: #000000;
  border-radius: 9px 9px 0 0;
  text-align: center;
}

.chat-header h3 {
  margin: 0;
  color: #ffffff;
  font-size: 18px;
  font-weight: bold;
}


/* 对话消息区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
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
  flex-direction: row;
}

.teacher-message {
  flex-direction: row-reverse;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.student-message .message-content {
  align-items: flex-start;
}

.teacher-message .message-content {
  align-items: flex-end;
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
  background: #f8f8f8;
  color: #000000;
}

.teacher-message .message-text {
  background: #000000;
  color: #ffffff;
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

/* 输入区域 */
.chat-input-area {
  border-top: 3px solid #000000;
  padding: 20px;
  background: #ffffff;
  border-radius: 0 0 9px 9px;
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


.no-selection {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.no-selection-content {
  text-align: center;
  color: #999999;
}

.no-selection-icon {
  font-size: 48px;
  margin-bottom: 20px;
  color: #cccccc;
}

.no-selection-content p {
  font-size: 18px;
  margin: 0;
}
</style>
