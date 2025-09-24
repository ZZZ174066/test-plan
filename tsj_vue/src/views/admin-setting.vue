<template>
  <!-- 系统设置双栏布局 -->
  <div class="settings-layout">
    <!-- 左侧系统设置 -->
    <div class="settings-left">
      <div class="settings-card">
        <div class="settings-header">
          <h3>系统设置</h3>
        </div>
        <div class="settings-content">
          <div class="setting-item">
            <label>系统名称:</label>
            <span>{{ systemSettings.systemName }}</span>
          </div>
          <div class="setting-item">
            <label>系统版本:</label>
            <span>{{ systemSettings.version }}</span>
          </div>
          <div class="setting-item">
            <label>维护模式:</label>
            <el-switch v-model="systemSettings.maintenanceMode" @change="saveSettings"></el-switch>
          </div>
          <div class="logout-section">
            <button class="logout-btn" @click="logout">退出登录</button>
          </div>
        </div>
      </div>
    </div>
    <!-- 右侧系统日志 -->
    <div class="settings-right">
      <div class="logs-card">
        <div class="logs-header">
          <h3>系统日志</h3>
        </div>
        <div class="logs-content-wrapper">
          <div class="logs-content">
            <div v-if="systemLogs.length === 0" class="no-logs">
              暂无日志记录
            </div>
            <div v-else class="log-list">
              <div v-for="log in systemLogs" :key="log.id" class="log-item">
                <div class="log-time">{{ log.time }}</div>
                <div class="log-content">{{ log.description }}</div>
              </div>
            </div>
          </div>
          <div class="logs-footer">
            <button class="refresh-logs-btn" @click="loadSystemLogs">
              刷新日志
            </button>
            <button class="clear-logs-btn" @click="clearSystemLogs" :disabled="systemLogs.length === 0">
              清除日志
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
export default {
  name: 'AdminSetting',
  data() {
    return {
      systemSettings: {
        systemName: '考试计划管理系统',
        version: '1.0.0',
        maintenanceMode: false
      },
      systemLogs: []
    }
  },
  mounted() {
    this.loadMaintenanceMode()
    this.loadSystemLogs()
  },
  methods: {
    async loadMaintenanceMode() {
      try {
        const res = await axios.get('http://localhost:8080/api/user/getMaintenanceMode')
        this.systemSettings.maintenanceMode = res.data.maintenanceMode
      } catch (error) {
        console.error('加载维护模式状态失败:', error)
      }
    },
    async saveSettings() {
      try {
        const res = await axios.post('http://localhost:8080/api/user/setMaintenanceMode', {
          maintenanceMode: this.systemSettings.maintenanceMode
        })
        if (res.data.success) {
          ElMessage.success('设置已保存')
          this.loadSystemLogs()
        } else {
          ElMessage.error('设置保存失败')
        }
      } catch (error) {
        console.error('保存设置失败:', error)
        ElMessage.error('保存设置失败')
      }
    },
    async loadSystemLogs() {
      try {
        const res = await axios.get('http://localhost:8080/api/user/getUserActivities')
        this.systemLogs = res.data || []
      } catch (error) {
        console.error('加载系统日志失败:', error)
        this.systemLogs = []
      }
    },
    async clearSystemLogs() {
      try {
        const res = await axios.post('http://localhost:8080/api/user/clearSystemLogs')
        if (res.data.success) {
          this.systemLogs = []
          ElMessage.success('日志已清除')
        } else {
          ElMessage.error('清除日志失败')
        }
      } catch (error) {
        console.error('清除日志失败:', error)
        ElMessage.error('清除日志失败')
      }
    },
    async logout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // 清理本地存储
          localStorage.removeItem('currentUser')
          sessionStorage.removeItem('currentUser')
          // 发送退出登录事件给父组件
          this.$emit('logout')
          ElMessage.success('已退出登录')
        } catch (error) {
          console.error('退出登录失败:', error)
          this.$emit('logout')
        }
      })
    }
  }
}
</script>
<style scoped>
.settings-layout {
  display: flex;
  gap: 20px;
  height: 100%;
}
.settings-left {
  flex: 1;
}
.settings-right {
  flex: 1;
}
.settings-card {
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
.settings-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.8);
}
.settings-header {
  background: #000000;
  color: #ffffff;
  padding: 20px;
  border-radius: 10px 10px 0 0;
  text-align: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}
.settings-header h3 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
}
.settings-content {
  padding: 30px;
  flex: 1;
}
.setting-item {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  font-size: 16px;
}
.setting-item label {
  width: 120px;
  color: #000000;
  font-weight: bold;
}
.setting-item span {
  color: #000000;
  font-size: 16px;
}
.logs-card {
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
.logs-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.8);
}
.logs-header {
  background: #000000;
  color: #ffffff;
  padding: 20px;
  border-radius: 10px 10px 0 0;
  text-align: center;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}
.logs-header h3 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
}
.logs-content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}
.logs-content {
  padding: 20px 20px 0 20px;
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}
.no-logs {
  color: #666666;
  font-size: 16px;
  margin-top: 50px;
}
.log-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.log-item {
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 10px;
}
.log-time {
  font-size: 14px;
  color: #666666;
  margin-bottom: 5px;
}
.log-content {
  font-size: 16px;
  color: #000000;
  font-weight: bold;
}
.logs-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
  padding: 15px 20px;
  border-top: 1px solid #e0e0e0;
  background: #ffffff;
  border-radius: 0 0 10px 10px;
  flex-shrink: 0;
}
.refresh-logs-btn {
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
.refresh-logs-btn:hover {
  background: #000000 !important;
  color: #ffffff !important;
}
.refresh-logs-btn:active {
  background: #000000 !important;
  color: #ffffff !important;
}
.clear-logs-btn {
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
.clear-logs-btn:hover:not(:disabled) {
  background: #000000 !important;
  color: #ffffff !important;
}
.clear-logs-btn:active:not(:disabled) {
  background: #000000 !important;
  color: #ffffff !important;
}
.clear-logs-btn:disabled {
  background: #cccccc;
  border-color: #cccccc;
  color: #666666;
  cursor: not-allowed;
  opacity: 0.6;
}
:deep(.el-switch) {
  --el-switch-on-color: #000000 !important;
  --el-switch-off-color: #ffffff !important;
  --el-switch-border-color: #000000 !important;
}
:deep(.el-switch__core) {
  border: 2px solid #000000 !important;
  background-color: #ffffff !important;
}
:deep(.el-switch.is-checked .el-switch__core) {
  background-color: #000000 !important;
  border-color: #000000 !important;
}
:deep(.el-switch__action) {
  background-color: #000000 !important;
  border: 1px solid #000000 !important;
}
:deep(.el-switch.is-checked .el-switch__action) {
  background-color: #ffffff !important;
  border: 1px solid #000000 !important;
}
.logout-section {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
.logout-btn {
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
.logout-btn:hover {
  background: #000000 !important;
  color: #ffffff !important;
}
.logout-btn:active {
  background: #000000 !important;
  color: #ffffff !important;
}
</style>