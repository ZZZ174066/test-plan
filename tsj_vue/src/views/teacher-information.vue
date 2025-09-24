<template>
  <div class="information-section">
    <!-- 左侧个人信息 -->
    <div class="profile-left">
      <div class="profile-card">
        <div class="profile-header">
          <h3>个人信息</h3>
        </div>
        <div class="profile-content">
          <!-- 头像 -->
          <div class="profile-avatar" @click="selectAvatar">
            <div class="avatar-circle">
              <img :src="teacherInfo.avatar || '/src/assets/user/user.jpg'" :alt="teacherInfo.username" />
              <div class="avatar-overlay">
                <span>点击更换头像</span>
              </div>
            </div>
            <input type="file" ref="avatarInput" @change="handleAvatarChange" accept="image/*" style="display: none;">
          </div>
          <!-- 信息列表 -->
          <div class="profile-info">
            <div class="profile-item">
              <label>用 户 工 号：</label>
              <span>{{ teacherInfo.username }}</span>
            </div>
            <div class="profile-item">
              <label>用 户 姓 名：</label>
              <span>{{ teacherInfo.realName }}</span>
            </div>
            <div class="profile-item">
              <label>手　机　号：</label>
              <span>{{ teacherInfo.phone }}</span>
            </div>
            <div class="profile-item">
              <label>邮　　　箱：</label>
              <span>{{ teacherInfo.email }}</span>
            </div>
            <div class="profile-item">
              <label>用 户 类 型：</label>
              <span class="user-type" :class="getUserTypeClass(teacherInfo.userType)">{{ getUserTypeText(teacherInfo.userType) }}</span>
            </div>
          </div>
          <!-- 按钮 -->
          <div class="profile-buttons">
            <button class="profile-btn edit-btn" @click="editProfile">编辑信息</button>
            <button class="profile-btn logout-btn" @click="logout">退出登录</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 右侧教学信息 -->
    <div class="profile-right">
      <div class="teaching-card">
        <div class="teaching-header">
          <h3>教学信息</h3>
        </div>
        <div class="teaching-content">
          <div v-if="teachingInfo" class="teaching-info">
            <div class="subject-item" v-if="teachingInfo.subject1">
              <label>教授学科1：</label>
              <span>{{ teachingInfo.subject1 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject2">
              <label>教授学科2：</label>
              <span>{{ teachingInfo.subject2 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject3">
              <label>教授学科3：</label>
              <span>{{ teachingInfo.subject3 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject4">
              <label>教授学科4：</label>
              <span>{{ teachingInfo.subject4 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject5">
              <label>教授学科5：</label>
              <span>{{ teachingInfo.subject5 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject6">
              <label>教授学科6：</label>
              <span>{{ teachingInfo.subject6 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject7">
              <label>教授学科7：</label>
              <span>{{ teachingInfo.subject7 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject8">
              <label>教授学科8：</label>
              <span>{{ teachingInfo.subject8 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject9">
              <label>教授学科9：</label>
              <span>{{ teachingInfo.subject9 }}</span>
            </div>
            <div class="subject-item" v-if="teachingInfo.subject10">
              <label>教授学科10：</label>
              <span>{{ teachingInfo.subject10 }}</span>
            </div>
            <div v-if="!hasAnySubject" class="teaching-placeholder">
              暂无教学任务
            </div>
            <div class="teaching-buttons">
              <button class="profile-btn edit-btn" @click="editTeaching">编辑教学信息</button>
            </div>
          </div>
          <div v-else class="teaching-placeholder">
            暂无教学信息
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息对话框 -->
    <el-dialog v-model="editDialog" title="编辑个人信息" width="400px" :show-close="false">
      <div class="form-content-wrapper">
        <el-form :model="editForm" label-width="90px">
          <el-form-item label="用 户 工 号">
            <el-input v-model="editForm.username" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="用 户 密 码">
            <el-input v-model="editForm.password"></el-input>
          </el-form-item>
          <el-form-item label="用 户 姓 名">
            <el-input v-model="editForm.realName"></el-input>
          </el-form-item>
          <el-form-item label="手　机　号">
            <el-input v-model="editForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="邮　　　箱">
            <el-input v-model="editForm.email"></el-input>
          </el-form-item>
          <el-form-item label="用 户 类 型">
            <el-input :value="getUserTypeText(editForm.userType)" :disabled="true"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button class="action-btn save" @click="saveProfile">保存</button>
          <button class="action-btn cancel" @click="editDialog = false">取消</button>
        </div>
      </template>
    </el-dialog>

    <!-- 编辑教学信息对话框 -->
    <el-dialog v-model="teachingDialog" title="编辑教学信息" width="500px" :show-close="false">
      <div class="teaching-form-wrapper">
        <el-form :model="teachingForm" ref="teachingFormRef" label-width="100px">
          <el-form-item label="教授学科1">
            <el-input v-model="teachingForm.subject1" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科2">
            <el-input v-model="teachingForm.subject2" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科3">
            <el-input v-model="teachingForm.subject3" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科4">
            <el-input v-model="teachingForm.subject4" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科5">
            <el-input v-model="teachingForm.subject5" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科6">
            <el-input v-model="teachingForm.subject6" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科7">
            <el-input v-model="teachingForm.subject7" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科8">
            <el-input v-model="teachingForm.subject8" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科9">
            <el-input v-model="teachingForm.subject9" placeholder="请输入教授学科"></el-input>
          </el-form-item>
          <el-form-item label="教授学科10">
            <el-input v-model="teachingForm.subject10" placeholder="请输入教授学科"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button class="action-btn save" @click="saveTeaching">保存</button>
          <button class="action-btn cancel" @click="teachingDialog = false">取消</button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'TeacherInformation',
  props: {
    teacherInfo: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      editDialog: false,
      editForm: {
        id: null,
        username: '',
        password: '',
        realName: '',
        phone: '',
        email: '',
        userType: null
      },
      teachingDialog: false,
      teachingInfo: null,
      teachingForm: {
        teacherId: null,
        subject1: '',
        subject2: '',
        subject3: '',
        subject4: '',
        subject5: '',
        subject6: '',
        subject7: '',
        subject8: '',
        subject9: '',
        subject10: ''
      }
    }
  },
  computed: {
    hasAnySubject() {
      return this.teachingInfo && (
        this.teachingInfo.subject1 || 
        this.teachingInfo.subject2 || 
        this.teachingInfo.subject3 || 
        this.teachingInfo.subject4 || 
        this.teachingInfo.subject5 ||
        this.teachingInfo.subject6 ||
        this.teachingInfo.subject7 ||
        this.teachingInfo.subject8 ||
        this.teachingInfo.subject9 ||
        this.teachingInfo.subject10
      )
    }
  },
  mounted() {
    this.loadTeachingInfo()
  },
  methods: {
    editProfile() {
      this.editForm = {
        id: this.teacherInfo.id,
        username: this.teacherInfo.username,
        password: this.teacherInfo.password || '',
        realName: this.teacherInfo.realName,
        phone: this.teacherInfo.phone,
        email: this.teacherInfo.email,
        userType: this.teacherInfo.userType
      }
      this.editDialog = true
    },
    async saveProfile() {
      try {
        if (!this.editForm.realName.trim()) {
          ElMessage.error('用户姓名不能为空')
          return
        }
        const updateData = {
          id: this.editForm.id,
          username: this.editForm.username,
          password: this.editForm.password,
          realName: this.editForm.realName,
          phone: this.editForm.phone,
          email: this.editForm.email,
          userType: this.teacherInfo.userType,
          avatar: this.teacherInfo.avatar,
          onlineStatus: this.teacherInfo.onlineStatus
        }
        const response = await axios.put('http://localhost:8080/api/user/updateUser', updateData)
        if (response.data.success) {
          this.$emit('profile-updated', {
            realName: this.editForm.realName,
            phone: this.editForm.phone,
            email: this.editForm.email,
            password: this.editForm.password
          })
          this.editDialog = false
          ElMessage.success('个人信息保存成功')
        } else {
          ElMessage.error(response.data.message || '保存失败')
        }
      } catch (error) {
        console.error('保存个人信息失败:', error)
        if (error.response) {
          const errorMsg = error.response.data?.message || '服务器错误'
          ElMessage.error(`保存失败: ${errorMsg}`)
        } else {
          ElMessage.error('保存个人信息失败')
        }
      }
    },
    selectAvatar() {
      this.$refs.avatarInput.click()
    },
    handleAvatarChange(event) {
      const file = event.target.files[0]
      if (file) {
        // 验证文件类型
        if (!file.type.startsWith('image/')) {
          ElMessage.error('请选择图片文件')
          return
        }
        
        // 验证文件大小（限制为10MB）
        if (file.size > 10 * 1024 * 1024) {
          ElMessage.error('图片大小不能超过10MB')
          return
        }
        
        // 压缩并上传图片
        this.compressAndUploadImage(file)
      }
    },
    compressAndUploadImage(file) {
      const canvas = document.createElement('canvas')
      const ctx = canvas.getContext('2d')
      const img = new Image()
      
      img.onload = () => {
        // 设置最大尺寸
        const maxWidth = 300
        const maxHeight = 300
        
        let { width, height } = img
        
        // 计算压缩后的尺寸
        if (width > height) {
          if (width > maxWidth) {
            height = (height * maxWidth) / width
            width = maxWidth
          }
        } else {
          if (height > maxHeight) {
            width = (width * maxHeight) / height
            height = maxHeight
          }
        }
        
        // 设置画布尺寸
        canvas.width = width
        canvas.height = height
        
        // 绘制压缩后的图片
        ctx.drawImage(img, 0, 0, width, height)
        
        // 转换为Base64（JPEG格式，压缩质量0.8）
        const compressedDataUrl = canvas.toDataURL('image/jpeg', 0.8)
        
        // 上传压缩后的图片
        this.uploadAvatar(compressedDataUrl)
      }
      
      img.onerror = () => {
        ElMessage.error('图片加载失败，请重试')
      }
      
      // 读取文件并加载到img对象
      const reader = new FileReader()
      reader.onload = (e) => {
        img.src = e.target.result
      }
      reader.readAsDataURL(file)
    },
    async uploadAvatar(avatarData) {
      try {
        // 先从最新的sessionStorage获取完整用户数据
        const currentUser = JSON.parse(sessionStorage.getItem('currentUser') || '{}')
        
        // 使用最新的完整用户数据进行更新，确保所有字段都有值
        const updateData = {
          id: this.teacherInfo.id || currentUser.id,
          username: this.teacherInfo.username || currentUser.username,
          password: this.teacherInfo.password || currentUser.password || '',
          realName: this.teacherInfo.realName || currentUser.realName,
          phone: this.teacherInfo.phone || currentUser.phone,
          email: this.teacherInfo.email || currentUser.email,
          userType: this.teacherInfo.userType || currentUser.userType,
          avatar: avatarData,
          onlineStatus: this.teacherInfo.onlineStatus || currentUser.onlineStatus || 1
        }
        
        // 验证关键字段不为空
        if (!updateData.realName) {
          ElMessage.error('用户姓名信息缺失，请刷新页面重试')
          return
        }
        
        const response = await axios.post('http://localhost:8080/api/user/updateUser', updateData)
        
        if (response.data.success) {
          // 更新本地组件数据
          this.teacherInfo.avatar = avatarData
          
          // 更新sessionStorage中的完整用户数据
          const updatedUser = {
            ...currentUser,
            ...updateData,
            avatar: avatarData
          }
          sessionStorage.setItem('currentUser', JSON.stringify(updatedUser))
          
          // 只触发avatar字段的更新
          this.$emit('profile-updated', { avatar: avatarData })
          
          ElMessage.success('头像更新成功')
        } else {
          ElMessage.error(response.data.message || '头像更新失败')
        }
      } catch (error) {
        console.error('头像上传失败:', error)
        ElMessage.error('头像上传失败，请重试')
      }
    },
    async logout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          if (this.teacherInfo.id) {
            await axios.post(`http://localhost:8080/api/user/logout?userId=${this.teacherInfo.id}`)
          }
          localStorage.removeItem('currentUser')
          sessionStorage.removeItem('currentUser')
          this.$emit('logout')
        } catch (error) {
          console.error('退出登录失败:', error)
          this.$emit('logout')
        }
      })
    },
    getUserTypeText(userType) {
      switch(userType) {
        case 1: return '管理员'
        case 2: return '教师'
        case 3: return '学生'
        default: return '教师'
      }
    },
    getUserTypeClass(userType) {
      switch(userType) {
        case 1: return 'admin'
        case 2: return 'teacher'
        case 3: return 'student'
        default: return 'teacher'
      }
    },
    
    async loadTeachingInfo() {
      if (!this.teacherInfo.id) return
      
      try {
        const res = await axios.get(`http://localhost:8080/api/teach/${this.teacherInfo.id}`)
        if (res.data.success) {
          this.teachingInfo = res.data.data
        } else {
          console.log('教学信息不存在，可能是新教师')
          this.teachingInfo = null
        }
      } catch (error) {
        console.error('加载教学信息失败:', error)
        this.teachingInfo = null
      }
    },
    
    editTeaching() {
      this.teachingForm = {
        teacherId: this.teacherInfo.id,
        subject1: this.teachingInfo?.subject1 || '',
        subject2: this.teachingInfo?.subject2 || '',
        subject3: this.teachingInfo?.subject3 || '',
        subject4: this.teachingInfo?.subject4 || '',
        subject5: this.teachingInfo?.subject5 || '',
        subject6: this.teachingInfo?.subject6 || '',
        subject7: this.teachingInfo?.subject7 || '',
        subject8: this.teachingInfo?.subject8 || '',
        subject9: this.teachingInfo?.subject9 || '',
        subject10: this.teachingInfo?.subject10 || ''
      }
      this.teachingDialog = true
    },
    
    async saveTeaching() {
      try {
        const res = await axios.put('http://localhost:8080/api/teach/update', this.teachingForm)
        if (res.data.success) {
          ElMessage.success('教学信息保存成功')
          this.teachingDialog = false
          await this.loadTeachingInfo()
        } else {
          ElMessage.error(res.data.message || '保存失败')
        }
      } catch (error) {
        console.error('保存教学信息失败:', error)
        if (error.response) {
          const errorMsg = error.response.data?.message || '服务器错误'
          ElMessage.error(`保存失败: ${errorMsg}`)
        } else {
          ElMessage.error('保存教学信息失败')
        }
      }
    }
  }
}
</script>

<style scoped>
.information-section {
  display: flex;
  gap: 20px;
  height: 100%;
}
.profile-left {
  flex: 1;
}
.profile-right {
  flex: 1;
}
.profile-card {
  border: 2px solid #000000;
  border-radius: 12px;
  background: #ffffff;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  transition: all 0.3s ease;
  cursor: default;
}
.profile-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.8);
}
.profile-header {
  background: #000000;
  color: #ffffff;
  padding: 20px;
  border-radius: 10px 10px 0 0;
  text-align: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}
.profile-header h3 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
}
.profile-content {
  padding: 30px;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.profile-avatar {
  margin-bottom: 30px;
  cursor: pointer;
  position: relative;
  display: flex;
  justify-content: center;
}
.avatar-circle {
  width: 120px;
  height: 120px;
  border: 3px solid #000000;
  border-radius: 50%;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
  transition: all 0.3s ease;
}
.avatar-circle:hover {
  transform: scale(1.05);
  border-color: #007bff;
}
.avatar-circle img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: all 0.3s ease;
}
.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 50%;
  font-size: 12px;
  text-align: center;
  line-height: 1.2;
}
.avatar-circle:hover .avatar-overlay {
  opacity: 1;
}
.avatar-circle:hover img {
  transform: scale(1.1);
}
.profile-info {
  width: 100%;
  margin-bottom: 30px;
}
.profile-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  font-size: 16px;
}
.profile-item label {
  width: 120px;
  color: #000000;
  font-weight: bold;
}
.profile-item span {
  color: #000000;
  font-size: 16px;
}
.user-type {
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
}
.user-type.admin {
  background: #000000;
  color: #ffffff;
}
.user-type.teacher {
  background: #ffffff;
  color: #000000;
  border: 2px solid #000000;
}
.user-type.student {
  background: #ffffff;
  color: #000000;
  border: 2px solid #000000;
}
.profile-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
}
.profile-btn {
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
.profile-btn:hover {
  background: #000000 !important;
  color: #ffffff !important;
}
.profile-btn:active {
  background: #000000 !important;
  color: #ffffff !important;
}
.teaching-card {
  border: 2px solid #000000;
  border-radius: 12px;
  background: #ffffff;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  transition: all 0.3s ease;
  cursor: default;
}
.teaching-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.8);
}
.teaching-header {
  background: #000000;
  color: #ffffff;
  padding: 20px;
  border-radius: 10px 10px 0 0;
  text-align: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}
.teaching-header h3 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
}
.teaching-content {
  padding: 30px;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
.teaching-placeholder {
  color: #999999;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.teaching-info {
  width: 100%;
}

.subject-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  font-size: 16px;
}

.subject-item label {
  width: 120px;
  color: #000000;
  font-weight: bold;
}

.subject-item span {
  color: #000000;
  font-size: 16px;
}

.teaching-buttons {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
:deep(.el-dialog) {
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 12px;
  transform: translate(100px, 50px) !important;
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
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
  padding: 0px 30px 5px 30px;
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

/* 教学信息表单容器样式 */
.teaching-form-wrapper {
  max-height: 280px;
  overflow-y: auto;
  padding: 15px 15px 15px 0px;
  border: 4px solid #000000;
  border-radius: 8px;
  background: #ffffff;
  margin: 0 -30px 10px -35px;
  width: calc(100% + 66px);
  box-sizing: border-box;
  scrollbar-width: none; /* Firefox */
}

/* 完全隐藏滚动条但保持滚动功能 */
.teaching-form-wrapper::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Edge */
}
</style>
