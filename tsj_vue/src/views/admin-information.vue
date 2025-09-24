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
          <div class="profile-avatar">
            <div class="avatar-circle">
              <img :src="adminInfo.avatar || '/src/assets/user/user.jpg'" :alt="adminInfo.username" />
            </div>
          </div>
          <!-- 信息列表 -->
          <div class="profile-info">
            <div class="profile-item">
              <label>用 户 学 号：</label>
              <span>{{ adminInfo.username }}</span>
            </div>
            <div class="profile-item">
              <label>用 户 姓 名：</label>
              <span>{{ adminInfo.realName}}</span>
            </div>
            <div class="profile-item">
              <label>手　机　号：</label>
              <span>{{ adminInfo.phone}}</span>
            </div>
            <div class="profile-item">
              <label>邮　　　箱：</label>
              <span>{{ adminInfo.email}}</span>
            </div>
            <div class="profile-item">
              <label>用 户 类 型：</label>
              <span class="user-type" :class="getUserTypeClass(adminInfo.userType)">{{ getUserTypeText(adminInfo.userType) }}</span>
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
          <div class="certificate-placeholder">
            暂无
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
  name: 'AdminInformation',
  props: {
    adminInfo: {
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
  methods: {
    editProfile() {
      this.editForm = {
        id: this.adminInfo.id,
        username: this.adminInfo.username,
        password: this.adminInfo.password || '',
        realName: this.adminInfo.realName,
        phone: this.adminInfo.phone,
        email: this.adminInfo.email,
        userType: this.adminInfo.userType
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
          userType: this.adminInfo.userType,
          avatar: this.adminInfo.avatar,
          onlineStatus: this.adminInfo.onlineStatus
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
    async logout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          if (this.adminInfo.id) {
            await axios.post(`http://localhost:8080/api/user/logout?userId=${this.adminInfo.id}`)
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
        default: return '管理员'
      }
    },
    getUserTypeClass(userType) {
      switch(userType) {
        case 1: return 'admin'
        case 2: return 'teacher'
        case 3: return 'student'
        default: return 'admin'
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
}
.avatar-circle img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
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
  padding: 30px;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
.certificate-placeholder {
  color: #999999;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
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