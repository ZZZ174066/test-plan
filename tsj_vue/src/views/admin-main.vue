<template>
  <div class="dashboard">
    <!-- 筛选按钮区域 -->
    <div class="stats-container">
      <div class="stat-card" :class="{ black: currentFilter === 'all', white: currentFilter !== 'all' }" @click="filterUsers('all')">
        <div class="stat-icon">
          <el-icon><user /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ userStats.total }}</div>
          <div class="stat-label">全部用户</div>
        </div>
      </div>
      <div class="stat-card" :class="{ black: currentFilter === 'students', white: currentFilter !== 'students' }" @click="filterUsers('students')">
        <div class="stat-icon">
          <el-icon><reading /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ userStats.students }}</div>
          <div class="stat-label">学生用户</div>
        </div>
      </div>
      <div class="stat-card" :class="{ black: currentFilter === 'teachers', white: currentFilter !== 'teachers' }" @click="filterUsers('teachers')">
        <div class="stat-icon">
          <el-icon><avatar /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ userStats.teachers }}</div>
          <div class="stat-label">教师用户</div>
        </div>
      </div>
      <div class="stat-card" :class="{ black: currentFilter === 'admin', white: currentFilter !== 'admin' }" @click="filterUsers('admin')">
        <div class="stat-icon">
          <el-icon><setting /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ userStats.admin }}</div>
          <div class="stat-label">管理员</div>
        </div>
      </div>
    </div>
    <!-- 搜索区域 -->
    <div class="search-container">
      <!-- 筛选标签区域 -->
      <div v-if="onlineStatusFilter !== null" class="filter-tags-container">
        <div class="filter-tags">
          <span class="filter-tag">
            {{ onlineStatusFilter === 1 ? '在线' : '离线' }}
            <el-icon class="filter-tag-close" @click="clearOnlineStatusFilter">
              <Close />
            </el-icon>
          </span>
        </div>
      </div>
      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索用户学号、姓名或手机号" class="search-input" @keyup.enter="handleSearch"></el-input>
        <button class="action-btn search" @click="handleSearch">搜索</button>
        <button class="action-btn refresh" @click="refreshAllData">刷新</button>
        <button class="action-btn add" @click="registerDialog = true">新增</button>
      </div>
    </div>
    <!-- 用户管理表格 -->
    <div class="user-table-container">
      <!-- 固定表头 -->
      <div class="table-header-fixed">
        <table class="user-table header-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>头像</th>
              <th>用户学号</th>
              <th>用户姓名</th>
              <th>手机号</th>
              <th>邮箱</th>
              <th>用户类型</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
        </table>
      </div>
      <!-- 可滚动表格内容 -->
      <div class="table-body-scrollable">
        <table class="user-table body-table">
          <tbody>
            <tr v-for="user in filteredUserList" :key="user.id">
              <td>{{ user.id }}</td>
              <td>
                <div class="user-avatar">
                  <img :src="user.avatar || '/src/assets/user/user.jpg'" :alt="user.username" />
                </div>
              </td>
              <td>{{ user.username }}</td>
              <td>{{ user.realName || '未设置' }}</td>
              <td>{{ user.phone}}</td>
              <td>{{ user.email}}</td>
              <td>
                <span v-if="user.userType === 1" class="user-type admin">管理员</span>
                <span v-else-if="user.userType === 2" class="user-type teacher">教师</span>
                <span v-else-if="user.userType === 3" class="user-type student">学生</span>
                <span v-else class="user-type unknown">未知</span>
              </td>
              <td>
                <span :class="['status', user.onlineStatus === 1 ? 'online' : 'offline', 'clickable']" @click="filterByOnlineStatus(user.onlineStatus)">
                  {{ user.onlineStatus === 1 ? '在线' : '离线' }}
                </span>
              </td>
              <td>
                <button v-if="user.userType !== 1" class="action-btn edit" @click="editUser(user)">编辑</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!-- 编辑用户对话框 -->
    <el-dialog v-model="editDialog" title="编辑用户" width="400px" :show-close="false">
      <div class="form-content-wrapper">
        <el-form :model="editForm" label-width="90px">
          <el-form-item label="用 户 学 号">
            <el-input v-model="editForm.username"></el-input>
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
            <div class="user-type-radio">
              <label class="radio-option" :class="{ active: editForm.userType === 2 }" @click="editForm.userType = 2">
                <span class="radio-dot"></span>
                <span class="radio-label">教师</span>
              </label>
              <label class="radio-option" :class="{ active: editForm.userType === 3 }" @click="editForm.userType = 3">
                <span class="radio-dot"></span>
                <span class="radio-label">学生</span>
              </label>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button class="action-btn save" @click="saveUser">保存</button>
          <button class="action-btn delete" @click="confirmDeleteUser">删除</button>
          <button class="action-btn cancel" @click="editDialog = false">取消</button>
        </div>
      </template>
    </el-dialog>

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
              <el-form-item label="姓名" prop="realName">
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
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { 
  User, 
  Setting, 
  Connection, 
  Avatar, 
  Reading,
  Close,
  View,
  Hide
} from '@element-plus/icons-vue'
export default {
  name: 'AdminMain',
  components: {
    User,
    Setting,
    Connection,
    Avatar,
    Reading,
    Close,
    View,
    Hide
  },
  data() {
    return {
      userStats: {
        total: 0,
        online: 0,
        teachers: 0,
        students: 0,
        admin: 0
      },
      userList: [],
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
      currentFilter: 'all', // 当前筛选状态: 'all', 'students', 'teachers', 'admin'
      filteredUserList: [], // 筛选后的用户列表
      searchKeyword: '', // 搜索关键词
      onlineStatusFilter: null, // 在线状态筛选: null, 0, 1
      // 注册相关
      registerDialog: false,
      registerPasswordVisible: false,
      registerLoading: false,
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
      registerRules: {
        username: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        userType: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        subject1: [{ required: false, message: '请输入至少一门教授学科', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    initData() {
      this.loadUserStats()
      this.loadUserList()
      this.currentFilter = 'all'
    },
    async loadUserStats() {
      try {
        const res = await axios.get('http://localhost:8080/api/user/getUser')
        const users = Array.isArray(res.data) ? res.data : []
        this.userStats = {
          total: users.length,
          online: users.filter(u => u.onlineStatus === 1 || u.online_status === 1).length,
          teachers: users.filter(u => (u.userType || u.user_type) === 2).length,
          students: users.filter(u => (u.userType || u.user_type) === 3).length,
          admin: users.filter(u => (u.userType || u.user_type) === 1).length
        }
      } catch (error) {
        console.error('加载用户统计失败:', error)
        ElMessage.error('加载用户统计失败')
      }
    },
    async loadUserList() {
      try {
        const res = await axios.get('http://localhost:8080/api/user/getUser')
        this.userList = Array.isArray(res.data) ? res.data.map(u => ({
          ...u,
          userType: u.userType ?? u.user_type,
          realName: u.realName ?? u.real_name,
          onlineStatus: u.onlineStatus ?? u.online_status,
          avatar: u.avatar ?? u.avatar_url,
          email: u.email ?? ''
        })).sort((a, b) => b.id - a.id) : [] // 按ID从大到小排序
        this.applyFilter()
      } catch (error) {
        console.error('加载用户列表失败:', error)
        ElMessage.error('加载用户列表失败')
      }
    },
    refreshAllData() {
      this.loadUserStats()
      this.loadUserList()
      ElMessage.success('数据已刷新')
    },
    filterUsers(filterType) {
      this.currentFilter = filterType
      this.applyFilter()
    },
    applyFilter() {
      let filtered = [...this.userList]
      
      // 按用户类型筛选
      switch (this.currentFilter) {
        case 'all': break
        case 'students': filtered = filtered.filter(user => user.userType === 3); break
        case 'teachers': filtered = filtered.filter(user => user.userType === 2); break
        case 'admin': filtered = filtered.filter(user => user.userType === 1); break
        default: break
      }
      
      // 按在线状态筛选
      if (this.onlineStatusFilter !== null) {
        filtered = filtered.filter(user => user.onlineStatus === this.onlineStatusFilter)
      }
      
      // 按搜索关键字筛选
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.trim().toLowerCase()
        filtered = filtered.filter(user => {
          return (user.username && user.username.toLowerCase().includes(keyword)) ||
                 (user.realName && user.realName.toLowerCase().includes(keyword)) ||
                 (user.phone && user.phone.includes(keyword))
        })
      }
      this.filteredUserList = filtered
    },
    handleSearch() {
      this.applyFilter()
    },
    
    // 按在线状态筛选
    filterByOnlineStatus(status) {
      this.onlineStatusFilter = status
      this.applyFilter()
    },
    
    // 清除在线状态筛选
    clearOnlineStatusFilter() {
      this.onlineStatusFilter = null
      this.applyFilter()
    },
    editUser(user) {
      this.editForm = { ...user }
      this.editDialog = true
    },
    async saveUser() {
      try {
        await axios.post('http://localhost:8080/api/user/updateUser', this.editForm)
        ElMessage.success('用户信息更新成功')
        this.editDialog = false
        this.loadUserList()
        this.loadUserStats()
      } catch (error) {
        console.error('更新用户失败:', error)
        ElMessage.error('更新用户失败')
      }
    },
    confirmDeleteUser() {
      ElMessageBox.confirm(`确定要删除用户 "${this.editForm.username}" 吗？`, '删除确认', {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await axios.get(`http://localhost:8080/api/user/deleteUser/${this.editForm.id}`)
          ElMessage.success('用户删除成功')
          this.editDialog = false
          this.loadUserList()
          this.loadUserStats()
        } catch (error) {
          console.error('删除用户失败:', error)
          ElMessage.error('删除用户失败')
        }
      })
    },
    // 注册相关方法
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
      }
      this.registerPasswordVisible = false
      if (this.$refs.registerFormRef) {
        this.$refs.registerFormRef.clearValidate()
      }
    },
    closeRegisterDialog() {
      this.registerDialog = false
      this.resetRegisterForm()
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
            ElMessage.success('注册成功')
            this.registerDialog = false
            this.resetRegisterForm()
            // 刷新用户列表
            this.loadUserList()
            this.loadUserStats()
          }).finally(() => {
            this.registerLoading = false;
          })
        }).catch(() => {
          this.registerLoading = false;
        })
      })
    }
  }
}
</script>
<style scoped>
/* 系统用户布局 */
.dashboard {
  display: flex;
  flex-direction: column;
  height: 100%;
}
/* 统计卡片容器 */
.stats-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  flex-shrink: 0;
}
/* 统计卡片 */
.stat-card {
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
.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}
.stat-card.black {
  background-color: #000000;
  color: #ffffff;
}
.stat-card.white {
  background-color: #ffffff;
  color: #000000;
}
.stat-icon {
  font-size: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 26px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  font-weight: bold;
}
/* 搜索区域 */
.search-container {
  margin-bottom: 20px;
  flex-shrink: 0;
  position: relative;
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
/* 用户表格容器 */
.user-table-container {
  margin-top: 0px;
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  border-radius: 12px;
  overflow: hidden;
}
/* 固定表头容器 */
.table-header-fixed {
  flex-shrink: 0;
  border: 2px solid #000000;
  border-bottom: none;
  border-radius: 12px 12px 0 0;
  overflow: hidden; 
}
/* 可滚动表格内容容器 */
.table-body-scrollable {
  flex: 1;
  overflow-y: auto;
  border: 2px solid #000000;
  border-top: none;
  border-radius: 0 0 12px 12px;
  scrollbar-width: none;
  -ms-overflow-style: none;
}
.table-body-scrollable::-webkit-scrollbar {
  display: none;
}
/* 用户表格 */
.user-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  table-layout: fixed;
}
/* 表头表格样式 */
.header-table {
  border: none;
}
/* 表体表格样式 */
.body-table {
  border: none;
}
/* 列宽设置 */
.user-table th:nth-child(1), .user-table td:nth-child(1) { width: 6%; min-width: 50px; }
.user-table th:nth-child(2), .user-table td:nth-child(2) { width: 8%; min-width: 60px; padding: 0px 15px; }
.user-table th:nth-child(3), .user-table td:nth-child(3) { width: 13%; min-width: 100px; }
.user-table th:nth-child(4), .user-table td:nth-child(4) { width: 13%; min-width: 100px; }
.user-table th:nth-child(5), .user-table td:nth-child(5) { width: 15%; min-width: 120px; }
.user-table th:nth-child(6), .user-table td:nth-child(6) { width: 19%; min-width: 150px; }
.user-table th:nth-child(7), .user-table td:nth-child(7) { width: 12%; min-width: 100px; }
.user-table th:nth-child(8), .user-table td:nth-child(8) { width: 12%; min-width: 100px; }
.user-table th:nth-child(9), .user-table td:nth-child(9) { width: 12%; min-width: 100px; }
.user-table th,
.user-table td {
  padding: 6px 15px;
  text-align: center;
  vertical-align: middle;
  border: 1px solid #000000;
  font-size: 16px;
  font-weight: bold;
  color: #000000;
  line-height: 1.1;
}
.user-table th {
  background-color: #000000;
  color: #ffffff;
  font-weight: bold;
  height: 50px;
  border-right: 1px solid #ffffff;
}

.user-table th:last-child {
  border-right: none;
}
.user-table tbody tr {
  height: 50px;
}
.user-table tbody tr:nth-child(even) {
  background-color: #f8f8f8;
}
.user-table tbody tr:hover {
  background-color: #e8e8e8;
}
.user-table td {
  color: #000000 !important;
}
.user-table td * {
  color: inherit !important;
}
/* 用户头像样式 */
.user-avatar {
  display: flex;
  justify-content: center;
  align-items: center;
}
.user-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #000000;
}
/* 用户类型标签 */
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
  background-color: #000000;
  color: #ffffff !important;
}
.user-type.teacher {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}
.user-type.student {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}
.user-type.unknown {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}
/* 状态标签 */
.status {
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
.status.online {
  background-color: #000000;
  color: #ffffff !important;
}
.status.offline {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
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
/* 编辑用户弹窗样式 */
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
:deep(.el-select .el-input__wrapper) {
  border: 2px solid #000000;
  border-radius: 4px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
}
/* 用户类型圆点选择样式 */
.user-type-radio {
  display: flex;
  gap: 30px;
  align-items: center;
  margin-left: 40px;
}
.radio-option {
  display: flex;
  align-items: center;
  cursor: pointer;
  position: relative;
  user-select: none;
}
.radio-dot {
  width: 18px;
  height: 18px;
  border: 2px solid #000000;
  border-radius: 50%;
  background: #ffffff;
  margin-right: 10px;
  position: relative;
  transition: all 0.2s ease;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.4);
}
.radio-dot::after {
  content: '';
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #000000;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(0);
  transition: transform 0.2s ease;
}
.radio-option.active .radio-dot::after {
  transform: translate(-50%, -50%) scale(1);
}
.radio-label {
  color: #000000;
  font-weight: bold;
  font-size: 14px;
}
.radio-option:hover .radio-dot {
  border-color: #333333;
  background: #f9f9f9;
}
.radio-option:hover .radio-label {
  color: #333333;
}
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
  padding: 0px 20px 3px 20px;
  margin-top: -5px;
  background: #ffffff;
  border-radius: 0 0 9px 9px;
}
/* 删除按钮特殊样式 */
.action-btn.delete:hover {
  background: #ff0000 !important;
  color: #ffffff !important;
  border-color: #ff0000 !important;
}
.action-btn.delete:active {
  background: #cc0000 !important;
  color: #ffffff !important;
  border-color: #cc0000 !important;
}
/* 取消按钮特殊样式 */
.action-btn.cancel:hover {
  background: #000000 !important;
  color: #ffffff !important;
  border-color: #000000 !important;
}
.action-btn.cancel:active {
  background: #000000 !important;
  color: #ffffff !important;
  border-color: #000000 !important;
}

/* 可点击状态样式 */
.status.clickable {
  cursor: pointer;
}

/* 注册弹窗样式 */
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
  border: 2px solid #000000;
  border-radius: 4px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.4);
  background: #ffffff;
}

.teacher-subjects :deep(.el-input__wrapper:hover) {
  border-color: #000000;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.teacher-subjects :deep(.el-input__wrapper.is-focus) {
  border-color: #000000;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.6);
}

.teacher-subjects :deep(.el-input__inner) {
  color: #000000;
  font-size: 14px;
  font-weight: bold;
}

/* 密码显示切换图标 */
.password-toggle-icon {
  color: #000000;
  font-size: 18px;
}

.password-toggle-icon:hover {
  color: #333333;
}

/* 弹窗底部按钮 */
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 20px 0 10px 0;
}

.dialog-footer .action-btn {
  min-width: 100px;
  height: 45px;
  padding: 12px 24px;
  border: 2px solid #000000;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.5);
}

.dialog-footer .action-btn.cancel {
  background: #ffffff;
  color: #000000;
}

.dialog-footer .action-btn.cancel:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

.dialog-footer .action-btn.confirm {
  background: #000000;
  color: #ffffff;
}

.dialog-footer .action-btn.confirm:hover {
  background: #333333;
  border-color: #333333;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

.dialog-footer .action-btn.confirm:disabled {
  background: #cccccc;
  border-color: #cccccc;
  color: #666666;
  cursor: not-allowed;
  transform: none;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}
</style>