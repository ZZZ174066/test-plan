<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 系统图标和标题 -->
      <div class="system-header">
        <div class="system-icon">
          <div class="icon-circle">
            <img src="@/assets/system.jpg" alt="考试计划管理系统" class="system-logo" />
          </div>
        </div>
        <div class="system-title">考试计划管理系统</div>
      </div>
      
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px" hide-required-asterisk class="login-form">
        <el-form-item label="学号" prop="username" class="login-form-item">
          <el-input v-model="loginForm.username" autocomplete="off" class="login-input" />
        </el-form-item>
        <el-form-item label="密码" prop="password" class="login-form-item">
          <el-input 
            v-model="loginForm.password" 
            :type="loginPasswordVisible ? 'text' : 'password'"
            autocomplete="off"
            class="login-input"
          >
            <template #suffix>
              <el-icon 
                class="password-toggle-icon"
                @click="loginPasswordVisible = !loginPasswordVisible"
                style="cursor: pointer;"
              >
                <View v-if="loginPasswordVisible" />
                <Hide v-else />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <div class="button-container">
          <button type="button" class="login-btn primary" @click="handleLogin">登录</button>
          <button type="button" class="login-btn secondary" @click="registerDialog = true">注册</button>
        </div>
      </el-form>
    </div>

    <!-- 注册弹窗 -->
    <el-dialog v-model="registerDialog" title="用户注册" :width="registerForm.userType === 2 ? '800px' : '500px'" @close="resetRegisterForm">
      <div class="form-content-wrapper">
        <div class="register-form-container" :class="{ 'teacher-layout': registerForm.userType === 2 }">
          <!-- 基本信息表单 -->
          <div class="basic-form">
            <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="90px" hide-required-asterisk>
              <el-form-item :label="registerForm.userType === 2 ? '学工号' : '学号'" prop="username">
                <el-input v-model="registerForm.username" autocomplete="off" />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input
                  v-model="registerForm.password"
                  :type="registerPasswordVisible ? 'text' : 'password'"
                  autocomplete="off"
                >
                  <template #suffix>
                    <el-icon
                      class="password-toggle-icon"
                      @click="registerPasswordVisible = !registerPasswordVisible"
                      style="cursor: pointer;"
                    >
                      <View v-if="registerPasswordVisible" />
                      <Hide v-else />
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="registerForm.realName" autocomplete="off" />
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="registerForm.phone" autocomplete="off" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="registerForm.email" autocomplete="off" />
              </el-form-item>
              <el-form-item label="用户类型" prop="userType">
                <el-select v-model="registerForm.userType" placeholder="请选择用户类型" @change="handleUserTypeChange">
                  <el-option label="教师" :value="2" />
                  <el-option label="学生" :value="3" />
                </el-select>
              </el-form-item>
            </el-form>
          </div>
          
          <!-- 教师专属字段 -->
          <div v-if="registerForm.userType === 2" class="teacher-subjects">
            <div class="subjects-scroll-container">
              <div class="subject-item">
                <el-form-item label="学科1" prop="subject1">
                  <el-input v-model="registerForm.subject1" placeholder="请输入教授学科" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科2">
                  <el-input v-model="registerForm.subject2" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科3">
                  <el-input v-model="registerForm.subject3" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科4">
                  <el-input v-model="registerForm.subject4" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科5">
                  <el-input v-model="registerForm.subject5" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科6">
                  <el-input v-model="registerForm.subject6" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科7">
                  <el-input v-model="registerForm.subject7" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科8">
                  <el-input v-model="registerForm.subject8" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科9">
                  <el-input v-model="registerForm.subject9" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
              <div class="subject-item">
                <el-form-item label="学科10">
                  <el-input v-model="registerForm.subject10" placeholder="请输入教授学科（可选）" />
                </el-form-item>
              </div>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button type="button" class="action-btn cancel" @click="closeRegisterDialog">取消</button>
          <button type="button" class="action-btn confirm" :disabled="registerLoading" @click="handleRegister">
            <span v-if="registerLoading">注册中...</span>
            <span v-else>确定</span>
          </button>
        </div>
      </template>
    </el-dialog>


  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { View, Hide } from '@element-plus/icons-vue'

export default {
  components: {
    View,
    Hide,
    Document
  },
  data() {
    return {
      loginForm: { username: '', password: '' },
      loginRules: {
        username: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      registerDialog: false,
      registerForm: { 
        username: '', 
        password: '', 
        realName: '', 
        phone: '', 
        userType: null,
        email: '',
        avatar: '/src/assets/user/user.jpg',
        onlineStatus: 0,
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
      },
      registerLoading: false,
      registerRules: {
        username: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        userType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        subject1: [{ required: false, message: '请输入至少一门教授学科', trigger: 'blur' }]
      },
      loginPasswordVisible: false,
      registerPasswordVisible: false,
    }
  },
  methods: {
    handleLogin() {
      if (!this.$refs.loginFormRef) {
        ElMessage.error('表单未初始化，请刷新页面重试');
        return;
      }
      
      this.$refs.loginFormRef.validate(valid => {
          if (!valid) {
            ElMessage.error('请正确填写学号和密码');
            return;
          }
        
        axios.post('http://localhost:8080/api/user/login', {
          username: this.loginForm.username,
          password: this.loginForm.password
        }).then(res => {
          if (res.data.success) {
            const user = res.data.user;
            ElMessage.success('登录成功');
            
            // 根据用户类型进行跳转
            if (user.userType === 1 || user.user_type === 1) {
              // 管理员跳转到管理界面
              this.$router.push({ 
                name: 'Admin', 
                query: { 
                  username: user.username, 
                  userId: user.id 
                } 
              });
            } else if (user.userType === 2 || user.user_type === 2) {
              // 教师跳转到教师界面
              this.$router.push({ 
                name: 'Teacher', 
                query: { 
                  username: user.username, 
                  userId: user.id 
                } 
              });
            } else if (user.userType === 3 || user.user_type === 3) {
              // 学生跳转到学生界面
              this.$router.push({ 
                name: 'Student', 
                query: { 
                  username: user.username, 
                  userId: user.id 
                } 
              });
            } else {
              console.log('未知用户类型登录成功:', user);
              ElMessage.warning('未知用户类型，请联系管理员');
            }
          } else {
            // 检查是否是维护模式
            if (res.data.maintenance) {
              ElMessage.warning(res.data.message);
              this.showMaintenanceMessage();
            } else {
              ElMessage.error(res.data.message || '学号或密码错误');
            }
          }
        }).catch(error => {
          console.error('登录请求失败:', error);
          ElMessage.error('登录失败，请稍后重试');
        });
      })
    },
    handleRegister() {
      this.registerLoading = true;
      if (!this.$refs.registerFormRef) {
        this.registerLoading = false;
        return;
      }
      this.registerForm.avatar = '/src/assets/user/user.jpg';
      
      // 如果是教师用户，检查是否至少填写了一门学科
      if (this.registerForm.userType === 2) {
        const hasSubject = this.registerForm.subject1 || this.registerForm.subject2 || 
                          this.registerForm.subject3 || this.registerForm.subject4 || 
                          this.registerForm.subject5 || this.registerForm.subject6 ||
                          this.registerForm.subject7 || this.registerForm.subject8 ||
                          this.registerForm.subject9 || this.registerForm.subject10;
        if (!hasSubject) {
          ElMessage.error('教师用户至少需要填写一门教授学科');
          this.registerLoading = false;
          return;
        }
      }
      
      this.$refs.registerFormRef.validate(valid => {
        if (!valid) {
          this.registerLoading = false;
          return;
        }
        axios.get('http://localhost:8080/api/user/getUser').then(res => {
          const users = res.data
          if (users.some(u => u.username === this.registerForm.username)) {
            ElMessage.error('学号已存在')
            this.registerLoading = false;
            return
          }
          
          // 准备用户数据
          const userData = {
            username: this.registerForm.username,
            password: this.registerForm.password,
            realName: this.registerForm.realName,
            phone: this.registerForm.phone,
            email: this.registerForm.email,
            userType: this.registerForm.userType,
            avatar: this.registerForm.avatar,
            onlineStatus: this.registerForm.onlineStatus
          };
          
          // 如果是教师用户，准备教学信息数据
          let teachingData = null;
          if (this.registerForm.userType === 2) {
            teachingData = {
              subject1: this.registerForm.subject1 || null,
              subject2: this.registerForm.subject2 || null,
              subject3: this.registerForm.subject3 || null,
              subject4: this.registerForm.subject4 || null,
              subject5: this.registerForm.subject5 || null,
              subject6: this.registerForm.subject6 || null,
              subject7: this.registerForm.subject7 || null,
              subject8: this.registerForm.subject8 || null,
              subject9: this.registerForm.subject9 || null,
              subject10: this.registerForm.subject10 || null
            };
          }
          
          // 注册用户
          axios.post('http://localhost:8080/api/user/registerTeacher', {
            userData: userData,
            teachingData: teachingData
          }).then(() => {
            ElMessage.success('注册成功，请登录')
            this.registerDialog = false
            this.resetRegisterForm()
          }).finally(() => {
            this.registerLoading = false;
          })
        }).catch(() => {
          this.registerLoading = false;
        })
      })
    },
    closeRegisterDialog() {
      this.registerDialog = false;
      this.resetRegisterForm();
    },
    handleUserTypeChange(userType) {
      // 当用户类型变更时，清空学科字段
      if (userType !== 2) {
        this.registerForm.subject1 = '';
        this.registerForm.subject2 = '';
        this.registerForm.subject3 = '';
        this.registerForm.subject4 = '';
        this.registerForm.subject5 = '';
        this.registerForm.subject6 = '';
        this.registerForm.subject7 = '';
        this.registerForm.subject8 = '';
        this.registerForm.subject9 = '';
        this.registerForm.subject10 = '';
      }
    },
    resetRegisterForm() {
      if (this.$refs.registerFormRef) {
        this.$refs.registerFormRef.resetFields();
      }
      this.registerForm = { 
        username: '', 
        password: '', 
        realName: '', 
        phone: '', 
        userType: null, 
        email: '', 
        avatar: '/src/assets/user/user.jpg', 
        onlineStatus: 0,
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
      };
      this.registerLoading = false;
    },
    showMaintenanceMessage() {
      this.$alert(
        '系统正在进行维护升级，暂时无法登录。', 
        '系统维护中', 
        {
          confirmButtonText: '确定',
          type: 'warning',
          center: true
        }
      );
    }
  }
}
</script>

<style scoped>
/* 主容器 */
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #ffffff;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

/* 登录卡片 */
.login-card {
  width: 100%;
  max-width: 450px;
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 12px;
  padding: 40px 30px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.8);
}

/* 系统头部 */
.system-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.system-icon {
  margin-bottom: 15px;
}

.icon-circle {
  width: 180px;
  height: 180px;
  border: 3px solid #000000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffffff;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
  transition: all 0.3s ease;
}

.icon-circle:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

.icon-circle :deep(.el-icon) {
  font-size: 60px;
  color: #000000;
}

.system-logo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.system-title {
  font-size: 24px;
  font-weight: bold;
  color: #000000;
  text-align: center;
  letter-spacing: 1px;
}


/* 登录表单样式 */
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-form-item {
  width: 100%;
  max-width: 280px;
  margin-left: -80px;
}

.login-form-item :deep(.el-form-item__content) {
  width: 200px;
}

.login-input {
  width: 100%;
}

/* 表单样式 */
:deep(.el-form-item__label) {
  color: #000000;
  font-weight: bold;
  font-size: 16px;
}

:deep(.el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 6px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
  background: #ffffff;
}

:deep(.el-input__wrapper:hover) {
  border-color: #000000;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #000000;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.6);
}

:deep(.el-input__inner) {
  color: #000000;
  font-size: 16px;
  font-weight: bold;
}

/* 选择框样式 */
:deep(.el-select .el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 6px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
}

:deep(.el-select .el-input__wrapper:hover) {
  border-color: #000000;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

:deep(.el-select .el-input__wrapper.is-focus) {
  border-color: #000000;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.6);
}

:deep(.el-select-dropdown) {
  border: 2px solid #000000;
  border-radius: 6px;
  background: #ffffff;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

:deep(.el-select-dropdown__item) {
  color: #000000;
  font-weight: bold;
}

:deep(.el-select-dropdown__item:hover) {
  background-color: #f0f0f0;
}

:deep(.el-select-dropdown__item.selected) {
  background-color: #000000;
  color: #ffffff;
}

/* 按钮容器 */
.button-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: center;
  margin-top: 0px;
}

/* 登录按钮样式 */
.login-btn {
  width: 200px;
  height: 40px;
  padding: 0 20px;
  border: 2px solid #000000;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.login-btn.primary {
  background: #000000;
  color: #ffffff;
}

.login-btn.primary:hover {
  background: #333333;
  border-color: #333333;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

.login-btn.secondary {
  background: #ffffff;
  color: #000000;
}

.login-btn.secondary:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

/* 注册表单容器 */
.register-form-container {
  display: flex;
  gap: 0;
  align-items: flex-start;
}

.register-form-container.teacher-layout {
  gap: 30px;
}

/* 基本信息表单 */
.basic-form {
  flex: 1;
  min-width: 350px;
}

/* 教师专属字段 */
.teacher-subjects {
  flex: 1;
  background-color: #f8f8f8;
  border: 2px solid #000000;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
  min-width: 300px;
  display: flex;
  flex-direction: column;
  height: fit-content;
  max-height: 450px;
}



.subjects-scroll-container {
  flex: 1;
  max-height: 255px;
  overflow-y: auto;
  padding-right: 5px;
}

.subjects-scroll-container::-webkit-scrollbar {
  width: 0px;
  background: transparent;
}

.subjects-scroll-container {
  scrollbar-width: none; 
  -ms-overflow-style: none;
}

.subject-item {
  margin-bottom: 12px;
}

.subject-item:last-child {
  margin-bottom: 0;
}

.teacher-subjects :deep(.el-form-item) {
  margin-bottom: 0;
}

.teacher-subjects :deep(.el-form-item__label) {
  color: #000000;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 5px;
}

.teacher-subjects :deep(.el-input__wrapper) {
  border: 2px solid #666666;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.3);
}

.teacher-subjects :deep(.el-input__wrapper:hover) {
  border-color: #000000;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
}

.teacher-subjects :deep(.el-input__wrapper.is-focus) {
  border-color: #000000;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
}

/* 弹窗样式 */
:deep(.el-dialog) {
  border: 3px solid #000000;
  border-radius: 12px;
  background: #ffffff;
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

:deep(.el-dialog__footer) {
  padding: 0;
  background: #ffffff;
}

/* 表单内容圆角框 */
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

/* 弹窗按钮样式 */
.dialog-footer {
  display: flex;
  gap: 15px;
  padding: 20px 30px;
  background: #ffffff;
  border-radius: 0 0 9px 9px;
}

.action-btn {
  flex: 1;
  padding: 10px 20px;
  border: 2px solid #000000;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
}

.action-btn.cancel {
  background: #ffffff;
  color: #000000;
}

.action-btn.cancel:hover {
  background: #f0f0f0;
  transform: translateY(-1px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.action-btn.confirm {
  background: #000000;
  color: #ffffff;
}

.action-btn.confirm:hover {
  background: #333333;
  transform: translateY(-1px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.6);
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.action-btn:disabled:hover {
  background: #000000;
  transform: none;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
}

/* 密码显示切换图标 */
.password-toggle-icon {
  color: #000000;
  font-size: 18px;
}

</style>
