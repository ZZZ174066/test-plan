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
              <img :src="studentInfo.avatar || '/src/assets/user/user.jpg'" :alt="studentInfo.username" />
              <div class="avatar-overlay">
                <span>点击更换头像</span>
              </div>
            </div>
            <input type="file" ref="avatarInput" @change="handleAvatarChange" accept="image/*" style="display: none;">
          </div>
          <!-- 信息列表 -->
          <div class="profile-info">
            <div class="profile-item">
              <label>用 户 学 号：</label>
              <span>{{ studentInfo.username }}</span>
            </div>
            <div class="profile-item">
              <label>用 户 姓 名：</label>
              <span>{{ studentInfo.realName }}</span>
            </div>
            <div class="profile-item">
              <label>手　机　号：</label>
              <span>{{ studentInfo.phone }}</span>
            </div>
            <div class="profile-item">
              <label>邮　　　箱：</label>
              <span>{{ studentInfo.email }}</span>
            </div>
            <div class="profile-item">
              <label>用 户 类 型：</label>
              <span class="user-type" :class="getUserTypeClass(studentInfo.userType)">{{ getUserTypeText(studentInfo.userType) }}</span>
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
    
    <!-- 右侧临时准考证 -->
    <div class="profile-right">
      <div class="certificate-card">
        <div class="certificate-header">
          <h3>临时准考证</h3>
        </div>
        <div class="certificate-content">
          <div class="certificate-main">
            <!-- 头像区域 -->
            <div class="certificate-avatar">
              <div class="avatar-container">
                <div class="avatar-placeholder">
                  <span>头像</span>
                </div>
              </div>
            </div>
            
            <!-- 信息区域 -->
            <div class="certificate-info">
              <div class="info-row">
                <span class="info-label">姓名：</span>
                <span class="info-value">{{ studentInfo.realName || '未设置' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">性别：</span>
                <span class="info-value">{{ studentGender }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">学号：</span>
                <span class="info-value">{{ studentInfo.username || '未设置' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">学院：</span>
                <span class="info-value">计算与人工智能学院</span>
              </div>
              <div class="info-row">
                <span class="info-label">专业：</span>
                <span class="info-value">软件工程</span>
              </div>
              <div class="info-row">
                <span class="info-label">考生号：</span>
                <span class="info-value">XXXXXXXXXXXXXXXX</span>
              </div>
              <div class="info-row">
                <span class="info-label">身份证号：</span>
                <span class="info-value">XXXXXXXXXXXXXXXX</span>
              </div>
              <div class="info-row">
                <span class="info-label">考试科目：</span>
                <span class="info-value">XXXX</span>
              </div>
            </div>
          </div>
          <!-- 导出按钮 -->
          <div class="export-section">
            <button class="export-btn" @click="exportCertificate">导出准考证</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息对话框 -->
    <el-dialog v-model="editDialog" title="编辑个人信息" width="400px" :show-close="false">
      <div class="form-content-wrapper">
        <el-form :model="editForm" label-width="90px">
          <el-form-item label="用 户 学 号">
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
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'StudentInformation',
  props: {
    studentInfo: {
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
      }
    }
  },
  computed: {
    studentGender() {
      // 可以根据实际需要从studentInfo或其他数据源获取性别
      // 这里使用虚拟信息作为示例
      return '男'
    }
  },
  methods: {
    editProfile() {
      this.editForm = {
        id: this.studentInfo.id,
        username: this.studentInfo.username,
        password: this.studentInfo.password || '',
        realName: this.studentInfo.realName,
        phone: this.studentInfo.phone,
        email: this.studentInfo.email,
        userType: this.studentInfo.userType
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
          userType: this.studentInfo.userType,
          avatar: this.studentInfo.avatar,
          onlineStatus: this.studentInfo.onlineStatus
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
          id: this.studentInfo.id || currentUser.id,
          username: this.studentInfo.username || currentUser.username,
          password: this.studentInfo.password || currentUser.password || '',
          realName: this.studentInfo.realName || currentUser.realName,
          phone: this.studentInfo.phone || currentUser.phone,
          email: this.studentInfo.email || currentUser.email,
          userType: this.studentInfo.userType || currentUser.userType,
          avatar: avatarData,
          onlineStatus: this.studentInfo.onlineStatus || currentUser.onlineStatus || 1
        }
        
        // 验证关键字段不为空
        if (!updateData.realName) {
          ElMessage.error('用户姓名信息缺失，请刷新页面重试')
          return
        }
        
        const response = await axios.post('http://localhost:8080/api/user/updateUser', updateData)
        
        if (response.data.success) {
          // 更新本地组件数据
          this.studentInfo.avatar = avatarData
          
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
    exportCertificate() {
      try {
        // 创建准考证内容的HTML
        const certificateContent = `
          <div style="width: 400px; margin: 20px auto; border: 3px solid #000; border-radius: 12px; background: #fff; font-family: 'Microsoft YaHei', Arial, sans-serif;">
            <div style="background: #000; color: #fff; padding: 20px; text-align: center; border-radius: 10px 10px 0 0;">
              <h2 style="margin: 0; font-size: 24px; font-weight: bold;">临时准考证</h2>
            </div>
            <div style="padding: 20px;">
              <div style="text-align: center; margin-bottom: 20px;">
                <div style="width: 100px; height: 120px; border: 2px solid #000; border-radius: 8px; margin: 0 auto; background: #f5f5f5; display: flex; align-items: center; justify-content: center;">
                  <span style="color: #666; font-size: 14px;">头像</span>
                </div>
              </div>
              <div style="margin-left: 20px; font-size: 20px; line-height: 2;">
                <div><strong>姓名：</strong>${this.studentInfo.realName || '未设置'}</div>
                <div><strong>性别：</strong>${this.studentGender}</div>
                <div><strong>学号：</strong>${this.studentInfo.username || '未设置'}</div>
                <div><strong>学院：</strong>计算与人工智能学院</div>
                <div><strong>专业：</strong>软件工程</div>
                <div><strong>考生号：</strong>XXXXXXXXXXXXXXXX</div>
                <div><strong>身份证号：</strong>XXXXXXXXXXXXXXXX</div>
                <div><strong>考试科目：</strong>XXXX</div>
              </div>
            </div>
          </div>
        `
        
        // 创建新窗口并打印
        const printWindow = window.open('', '_blank')
        printWindow.document.write(`
          <html>
            <head>
              <title>临时准考证</title>
              <style>
                body { margin: 0; padding: 20px; }
                @media print {
                  body { margin: 0; }
                  @page { margin: 10mm; }
                }
              </style>
            </head>
            <body>
              ${certificateContent}
              <script>
                window.onload = function() {
                  window.print();
                  window.onafterprint = function() {
                    window.close();
                  }
                }
              <\/script>
            </body>
          </html>
        `)
        printWindow.document.close()
        
        ElMessage.success('准考证导出中...')
      } catch (error) {
        console.error('导出准考证失败:', error)
        ElMessage.error('导出准考证失败，请重试')
      }
    },
    async logout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          if (this.studentInfo.id) {
            await axios.post(`http://localhost:8080/api/user/logout?userId=${this.studentInfo.id}`)
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
        default: return '学生'
      }
    },
    getUserTypeClass(userType) {
      switch(userType) {
        case 1: return 'admin'
        case 2: return 'teacher'
        case 3: return 'student'
        default: return 'student'
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
  border-radius: 50%;
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
.certificate-card {
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
.certificate-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.8);
}
.certificate-header {
  background: #000000;
  color: #ffffff;
  padding: 20px;
  border-radius: 10px 10px 0 0;
  text-align: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}
.certificate-header h3 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
}
.certificate-content {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}
.certificate-main {
  display: flex;
  flex-direction: column;
  gap: 15px;
  height: 100%;
}
.certificate-avatar {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-left: 20px;
  margin-top: 20px;
}
.avatar-container {
  width: 100px;
  height: 120px;
  border: 2px solid #000000;
  border-radius: 8px;
  overflow: hidden;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-container img {
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
  background: #f5f5f5;
  color: #666666;
  font-size: 14px;
  font-weight: normal;
  border-radius: 6px;
}
.certificate-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-left: 20px;
  margin-top: 20px;
}
.info-row {
  display: flex;
  align-items: center;
  font-size: 20px;
  line-height: 1.4;
}
.info-label {
  color: #000000;
  font-weight: bold;
  min-width: 80px;
  text-align: left;
}
.info-value {
  color: #000000;
  font-weight: normal;
}
.export-section {
  display: flex;
  justify-content: center;
  margin-top: -10px;
  margin-left: 400px;
  padding-bottom: 10px;
}
.export-btn {
  background: #000000;
  color: #ffffff;
  border: none;
  padding: 12px 24px;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}
.export-btn:hover {
  background: #333333;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.4);
}
.export-btn:active {
  background: #000000;
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
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
</style>
