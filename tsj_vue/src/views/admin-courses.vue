<template>
  <div class="dashboard">
    <!-- 筛选按钮区域 -->
    <div class="filters-container">
      <!-- 课程性质筛选 -->
      <div class="filter-card" :class="{ black: currentFilter === 'all', white: currentFilter !== 'all' }" @click="filterCourses('all')">
        <div class="filter-icon">
          <el-icon><list /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ courseStats.total }}</div>
          <div class="filter-label">全部</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'required', white: currentFilter !== 'required' }" @click="filterCourses('required')">
        <div class="filter-icon">
          <el-icon><star /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ courseStats.required }}</div>
          <div class="filter-label">必修</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'elective', white: currentFilter !== 'elective' }" @click="filterCourses('elective')">
        <div class="filter-icon">
          <el-icon><circle-check /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ courseStats.elective }}</div>
          <div class="filter-label">选修</div>
        </div>
      </div>
      <div class="filter-card" :class="{ black: currentFilter === 'limited', white: currentFilter !== 'limited' }" @click="filterCourses('limited')">
        <div class="filter-icon">
          <el-icon><warning /></el-icon>
        </div>
        <div class="filter-content">
          <div class="filter-number">{{ courseStats.limited }}</div>
          <div class="filter-label">限修</div>
        </div>
      </div>
    </div>
    
    <!-- 搜索区域 -->
    <div class="search-container">
      <!-- 筛选标签区域 -->
      <div v-if="courseTypeFilter || courseNatureFilter || semesterFilter || collegeFilter" class="filter-tags-container">
        <div class="filter-tags">
          <span v-if="courseTypeFilter" class="filter-tag">
            {{ courseTypeFilter }}
            <el-icon class="filter-tag-close" @click="clearCourseTypeFilter">
              <Close />
            </el-icon>
          </span>
          <span v-if="courseNatureFilter" class="filter-tag">
            {{ courseNatureFilter }}
            <el-icon class="filter-tag-close" @click="clearCourseNatureFilter">
              <Close />
            </el-icon>
          </span>
          <span v-if="semesterFilter" class="filter-tag">
            第{{ semesterFilter }}学期
            <el-icon class="filter-tag-close" @click="clearSemesterFilter">
              <Close />
            </el-icon>
          </span>
          <span v-if="collegeFilter" class="filter-tag">
            {{ collegeFilter }}
            <el-icon class="filter-tag-close" @click="clearCollegeFilter">
              <Close />
            </el-icon>
          </span>
        </div>
      </div>
      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索课程代码或课程名称" class="search-input" @keyup.enter="handleSearch"></el-input>
        <button class="action-btn search" @click="handleSearch">搜索</button>
        <button class="action-btn refresh" @click="handleManualRefresh">刷新</button>
        <button class="action-btn add" @click="addCourseDialog = true">新增</button>
      </div>
    </div>
    
    <!-- 课程管理表格 -->
    <div class="course-table-container">
      <!-- 固定表头 -->
      <div class="table-header-fixed">
        <table class="course-table header-table">
          <thead>
            <tr>
              <th>课程代码</th>
              <th>课程名称</th>
              <th>课程类型</th>
              <th>课程性质</th>
              <th>学分</th>
              <th>开设学期</th>
              <th>开设学院</th>
              <th>操作</th>
            </tr>
          </thead>
        </table>
      </div>
      
      <!-- 可滚动表格内容 -->
      <div class="table-body-scrollable">
        <table class="course-table body-table">
          <tbody>
            <tr v-for="course in filteredCourseList" :key="course.courseId">
              <td>{{ course.courseCode }}</td>
              <td>{{ course.courseName }}</td>
              <td>
                <span class="course-type-clickable" @click="filterByCourseType(course.courseType)">
                  {{ course.courseType }}
                </span>
              </td>
              <td>
                <span v-if="course.courseNature === '必修'" class="course-nature required clickable" @click="filterByCourseNature(course.courseNature)">必修</span>
                <span v-else-if="course.courseNature === '限修'" class="course-nature limited clickable" @click="filterByCourseNature(course.courseNature)">限修</span>
                <span v-else-if="course.courseNature === '选修'" class="course-nature elective clickable" @click="filterByCourseNature(course.courseNature)">选修</span>
                <span v-else class="course-nature unknown clickable" @click="filterByCourseNature(course.courseNature)">未知</span>
              </td>
              <td>{{ course.credit }}</td>
              <td>
                <span class="semester-clickable" @click="filterBySemester(course.semester)">
                  第{{ course.semester }}学期
                </span>
              </td>
              <td>
                <span class="college-clickable" @click="filterByCollege(course.college)">
                  {{ course.college }}
                </span>
              </td>
              <td>
                <button class="action-btn edit" @click="editCourse(course)">编辑</button>
              </td>
            </tr>
            <tr v-if="filteredCourseList.length === 0">
              <td colspan="8" class="no-data">暂无课程数据</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 编辑课程弹窗 -->
    <el-dialog v-model="editCourseDialog" title="编辑课程信息" width="600px" @close="resetEditForm">
      <div class="form-content-wrapper">
        <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="90px" hide-required-asterisk>
          <el-form-item label="课程代码" prop="courseCode">
            <el-input v-model="editForm.courseCode" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程名称" prop="courseName">
            <el-input v-model="editForm.courseName" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程类型" prop="courseType">
            <el-input v-model="editForm.courseType" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程性质" prop="courseNature">
            <el-select v-model="editForm.courseNature" placeholder="请选择课程性质">
              <el-option label="必修" value="必修" />
              <el-option label="选修" value="选修" />
              <el-option label="限修" value="限修" />
            </el-select>
          </el-form-item>
          <el-form-item label="学分" prop="credit">
            <el-input-number v-model="editForm.credit" :min="0" :max="10" />
          </el-form-item>
          <el-form-item label="开设学期" prop="semester">
            <el-input-number v-model="editForm.semester" :min="1" :max="8" />
          </el-form-item>
          <el-form-item label="开设学院" prop="college">
            <el-input v-model="editForm.college" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button type="button" class="action-btn cancel" @click="editCourseDialog = false">取消</button>
          <button type="button" class="action-btn delete" @click="handleDeleteCourse">删除</button>
          <button type="button" class="action-btn confirm" :disabled="editLoading" @click="handleEditCourse">
            <span v-if="editLoading">保存中...</span>
            <span v-else>确定</span>
          </button>
        </div>
      </template>
    </el-dialog>

    <!-- 新增课程弹窗 -->
    <el-dialog v-model="addCourseDialog" title="新增课程" width="600px" @close="resetAddForm">
      <div class="form-content-wrapper">
        <el-form :model="addForm" :rules="addRules" ref="addFormRef" label-width="90px" hide-required-asterisk>
          <el-form-item label="课程代码" prop="courseCode">
            <el-input v-model="addForm.courseCode" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程名称" prop="courseName">
            <el-input v-model="addForm.courseName" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程类型" prop="courseType">
            <el-input v-model="addForm.courseType" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程性质" prop="courseNature">
            <el-select v-model="addForm.courseNature" placeholder="请选择课程性质">
              <el-option label="必修" value="必修" />
              <el-option label="选修" value="选修" />
              <el-option label="限修" value="限修" />
            </el-select>
          </el-form-item>
          <el-form-item label="学分" prop="credit">
            <el-input-number v-model="addForm.credit" :min="0" :max="10" />
          </el-form-item>
          <el-form-item label="开设学期" prop="semester">
            <el-input-number v-model="addForm.semester" :min="1" :max="8" />
          </el-form-item>
          <el-form-item label="开设学院" prop="college">
            <el-input v-model="addForm.college" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <button type="button" class="action-btn cancel" @click="addCourseDialog = false">取消</button>
          <button type="button" class="action-btn confirm" :disabled="addLoading" @click="handleAddCourse">
            <span v-if="addLoading">保存中...</span>
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
  List,
  Star,
  CircleCheck,
  Warning,
  Close
} from '@element-plus/icons-vue'

export default {
  name: 'AdminCourses',
  components: {
    List,
    Star,
    CircleCheck,
    Warning,
    Close
  },
  data() {
    return {
      courseList: [],
      filteredCourseList: [],
      searchKeyword: '',
      currentFilter: 'all',
      courseStats: {
        total: 0,
        required: 0,
        elective: 0,
        limited: 0
      },
      // 筛选条件
      courseTypeFilter: null,
      courseNatureFilter: null,
      semesterFilter: null,
      collegeFilter: null,
      // 编辑课程
      editCourseDialog: false,
      editLoading: false,
      editForm: {
        courseId: null,
        courseCode: '',
        courseName: '',
        courseType: '',
        courseNature: '',
        credit: 0,
        semester: 1,
        college: ''
      },
      editRules: {
        courseCode: [{ required: true, message: '请输入课程代码', trigger: 'blur' }],
        courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        courseType: [{ required: true, message: '请输入课程类型', trigger: 'blur' }],
        courseNature: [{ required: true, message: '请选择课程性质', trigger: 'change' }],
        credit: [{ required: true, message: '请输入学分', trigger: 'blur' }],
        semester: [{ required: true, message: '请输入开设学期', trigger: 'blur' }],
        college: [{ required: true, message: '请输入开设学院', trigger: 'blur' }]
      },
      // 新增课程
      addCourseDialog: false,
      addLoading: false,
      addForm: {
        courseCode: '',
        courseName: '',
        courseType: '',
        courseNature: '',
        credit: 0,
        semester: 1,
        college: ''
      },
      addRules: {
        courseCode: [{ required: true, message: '请输入课程代码', trigger: 'blur' }],
        courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
        courseType: [{ required: true, message: '请输入课程类型', trigger: 'blur' }],
        courseNature: [{ required: true, message: '请选择课程性质', trigger: 'change' }],
        credit: [{ required: true, message: '请输入学分', trigger: 'blur' }],
        semester: [{ required: true, message: '请输入开设学期', trigger: 'blur' }],
        college: [{ required: true, message: '请输入开设学院', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadCourseList()
  },
  methods: {
    async loadCourseList() {
      try {
        const response = await axios.get('http://localhost:8080/api/course/getCourse')
        if (response.data && Array.isArray(response.data)) {
          this.courseList = response.data.sort((a, b) => b.courseId - a.courseId) // 按ID从大到小排序
          this.updateCourseStats()
          this.applyFilter()
        }
      } catch (error) {
        console.error('加载课程列表失败:', error)
        ElMessage.error('加载课程列表失败')
      }
    },
    updateCourseStats() {
      this.courseStats.total = this.courseList.length
      this.courseStats.required = this.courseList.filter(course => course.courseNature === '必修').length
      this.courseStats.elective = this.courseList.filter(course => course.courseNature === '选修').length
      this.courseStats.limited = this.courseList.filter(course => course.courseNature === '限修').length
    },
    filterCourses(filter) {
      this.currentFilter = filter
      this.applyFilter()
    },
    applyFilter() {
      let filtered = [...this.courseList]
      
      // 按课程性质筛选
      switch (this.currentFilter) {
        case 'required':
          filtered = filtered.filter(course => course.courseNature === '必修')
          break
        case 'elective':
          filtered = filtered.filter(course => course.courseNature === '选修')
          break
        case 'limited':
          filtered = filtered.filter(course => course.courseNature === '限修')
          break
        default:
          break
      }
      
      // 按课程类型筛选
      if (this.courseTypeFilter) {
        filtered = filtered.filter(course => course.courseType === this.courseTypeFilter)
      }
      
      // 按课程性质筛选（点击筛选）
      if (this.courseNatureFilter) {
        filtered = filtered.filter(course => course.courseNature === this.courseNatureFilter)
      }
      
      // 按开设学期筛选
      if (this.semesterFilter) {
        filtered = filtered.filter(course => course.semester === this.semesterFilter)
      }
      
      // 按开设学院筛选
      if (this.collegeFilter) {
        filtered = filtered.filter(course => course.college === this.collegeFilter)
      }
      
      // 按搜索关键词筛选
      if (this.searchKeyword.trim()) {
        const keyword = this.searchKeyword.toLowerCase()
        filtered = filtered.filter(course => 
          course.courseCode.toLowerCase().includes(keyword) ||
          course.courseName.toLowerCase().includes(keyword)
        )
      }
      
      this.filteredCourseList = filtered
    },
    handleSearch() {
      this.applyFilter()
    },
    handleManualRefresh() {
      this.searchKeyword = ''
      this.currentFilter = 'all'
      this.courseTypeFilter = null
      this.courseNatureFilter = null
      this.semesterFilter = null
      this.collegeFilter = null
      this.loadCourseList()
      ElMessage.success('数据已刷新')
    },
    // 筛选方法
    filterByCourseType(courseType) {
      this.courseTypeFilter = courseType
      this.applyFilter()
    },
    clearCourseTypeFilter() {
      this.courseTypeFilter = null
      this.applyFilter()
    },
    filterByCourseNature(courseNature) {
      this.courseNatureFilter = courseNature
      this.applyFilter()
    },
    clearCourseNatureFilter() {
      this.courseNatureFilter = null
      this.applyFilter()
    },
    filterBySemester(semester) {
      this.semesterFilter = semester
      this.applyFilter()
    },
    clearSemesterFilter() {
      this.semesterFilter = null
      this.applyFilter()
    },
    filterByCollege(college) {
      this.collegeFilter = college
      this.applyFilter()
    },
    clearCollegeFilter() {
      this.collegeFilter = null
      this.applyFilter()
    },
    editCourse(course) {
      this.editForm = {
        courseId: course.courseId,
        courseCode: course.courseCode,
        courseName: course.courseName,
        courseType: course.courseType,
        courseNature: course.courseNature,
        credit: course.credit,
        semester: course.semester,
        college: course.college
      }
      this.editCourseDialog = true
    },
    async handleEditCourse() {
      try {
        const valid = await this.$refs.editFormRef.validate()
        if (!valid) return

        this.editLoading = true

        const response = await axios.put('http://localhost:8080/api/course/updateCourse', this.editForm)
        
        if (response.data && response.data.success) {
          ElMessage.success('课程信息更新成功')
          this.editCourseDialog = false
          this.loadCourseList()
        } else {
          ElMessage.error(response.data?.message || '更新失败')
        }
      } catch (error) {
        console.error('更新课程失败:', error)
        ElMessage.error('更新课程失败')
      } finally {
        this.editLoading = false
      }
    },
    async handleAddCourse() {
      try {
        const valid = await this.$refs.addFormRef.validate()
        if (!valid) return

        this.addLoading = true

        const response = await axios.post('http://localhost:8080/api/course/insertCourse', this.addForm)
        
        if (response.data && response.data.success) {
          ElMessage.success('课程添加成功')
          this.addCourseDialog = false
          this.resetAddForm()
          this.loadCourseList()
        } else {
          ElMessage.error(response.data?.message || '添加失败')
        }
      } catch (error) {
        console.error('添加课程失败:', error)
        ElMessage.error('添加课程失败')
      } finally {
        this.addLoading = false
      }
    },
    async handleDeleteCourse() {
      try {
        const result = await ElMessageBox.confirm(
          '确定要删除这门课程吗？删除后将无法恢复！',
          '删除确认',
          {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        if (result === 'confirm') {
          const response = await axios.delete(`http://localhost:8080/api/course/deleteCourse/${this.editForm.courseId}`)
          
          if (response.data && response.data.success) {
            ElMessage.success('课程删除成功')
            this.editCourseDialog = false
            this.loadCourseList()
          } else {
            ElMessage.error(response.data?.message || '删除失败')
          }
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除课程失败:', error)
          ElMessage.error('删除课程失败')
        }
      }
    },
    resetEditForm() {
      this.editForm = {
        courseId: null,
        courseCode: '',
        courseName: '',
        courseType: '',
        courseNature: '',
        credit: 0,
        semester: 1,
        college: ''
      }
      if (this.$refs.editFormRef) {
        this.$refs.editFormRef.clearValidate()
      }
    },
    resetAddForm() {
      this.addForm = {
        courseCode: '',
        courseName: '',
        courseType: '',
        courseNature: '',
        credit: 0,
        semester: 1,
        college: ''
      }
      if (this.$refs.addFormRef) {
        this.$refs.addFormRef.clearValidate()
      }
    }
  }
}
</script>

<style scoped>
/* 课程管理布局 */
.dashboard {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 筛选按钮容器 */
.filters-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  flex-shrink: 0;
}

/* 筛选卡片 */
.filter-card {
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

.filter-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

.filter-card.black {
  background-color: #000000;
  color: #ffffff;
}

.filter-card.white {
  background-color: #ffffff;
  color: #000000;
}

.filter-icon {
  font-size: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.filter-content {
  flex: 1;
}

.filter-number {
  font-size: 26px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 4px;
}

.filter-label {
  font-size: 14px;
  font-weight: bold;
}

/* 搜索区域 */
.search-container {
  margin-bottom: 20px;
  flex-shrink: 0;
  position: relative;
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

/* 课程表格容器 */
.course-table-container {
  margin-top: 0px;
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  border: 2px solid #000000;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

/* 固定表头 */
.table-header-fixed {
  flex-shrink: 0;
  border: 2px solid #000000;
  border-bottom: none;
  border-radius: 12px 12px 0 0;
  overflow: hidden;
  background: #000000;
}

.header-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border: none;
  table-layout: fixed;
}

.course-table th,
.course-table td {
  padding: 6px 15px;
  text-align: center;
  vertical-align: middle;
  font-size: 14px;
  font-weight: bold;
  color: #000000;
  line-height: 1.1;
}

.course-table th {
  background-color: #000000;
  color: #ffffff;
  font-weight: bold;
  height: 50px;
  border-right: 1px solid #ffffff;
}

.course-table th:last-child {
  border-right: none;
}

/* 可滚动表格内容容器 */
.table-body-scrollable {
  flex: 1;
  overflow-y: auto;
  border: 2px solid #000000;
  border-top: none;
  border-radius: 0 0 12px 12px;
  background: #ffffff;
}

.table-body-scrollable::-webkit-scrollbar {
  display: none;
}

.body-table {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border: none;
  table-layout: fixed;
}

.course-table td {
  border-bottom: 1px solid #000000;
  border-right: 1px solid #000000;
}

.course-table td:last-child {
  border-right: none;
}

.course-table tbody tr {
  height: 50px;
}

.course-table tbody tr:nth-child(even) {
  background-color: #f8f8f8;
}

.course-table tbody tr:hover {
  background-color: #e8e8e8;
}

.course-table td {
  color: #000000 !important;
}

.course-table td * {
  color: inherit !important;
}

/* 课程类型可点击样式 */
.course-type-clickable {
  cursor: pointer;
  font-weight: bold;
}

/* 课程性质可点击样式 */
.course-nature.clickable {
  cursor: pointer;
}

/* 学期可点击样式 */
.semester-clickable {
  cursor: pointer;
  font-weight: bold;
}

/* 学院可点击样式 */
.college-clickable {
  cursor: pointer;
  font-weight: bold;
}

/* 课程性质标签 */
.course-nature {
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
  transition: all 0.3s;
}

.course-nature.required {
  background-color: #000000;
  color: #ffffff !important;
}

.course-nature.limited {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

.course-nature.elective {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

.course-nature.unknown {
  background-color: #ffffff;
  color: #000000 !important;
  border: 2px solid #000000;
}

/* 表格列宽设置 */
.course-table th:nth-child(1), .course-table td:nth-child(1) { width: 11%; min-width: 100px; }
.course-table th:nth-child(2), .course-table td:nth-child(2) { width: 28%; min-width: 220px; }
.course-table th:nth-child(3), .course-table td:nth-child(3) { width: 13%; min-width: 110px; }
.course-table th:nth-child(4), .course-table td:nth-child(4) { width: 9%; min-width: 75px; }
.course-table th:nth-child(5), .course-table td:nth-child(5) { width: 6%; min-width: 50px; }
.course-table th:nth-child(6), .course-table td:nth-child(6) { width: 9%; min-width: 75px; }
.course-table th:nth-child(7), .course-table td:nth-child(7) { width: 14%; min-width: 120px; }
.course-table th:nth-child(8), .course-table td:nth-child(8) { width: 10%; min-width: 80px; }

/* 无数据提示 */
.no-data {
  text-align: center;
  color: #666666;
  font-style: italic;
  padding: 40px;
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

.action-btn:disabled {
  background: #cccccc !important;
  color: #666666 !important;
  cursor: not-allowed !important;
  border-color: #cccccc !important;
}

/* 弹窗样式 */
:deep(.el-dialog) {
  background: #ffffff;
  border: 3px solid #000000;
  border-radius: 12px;
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

.dialog-footer .action-btn.delete {
  background: #ffffff;
  color: #ff4444;
  border-color: #ff4444;
}

.dialog-footer .action-btn.delete:hover {
  background: #ff4444;
  color: #ffffff;
  border-color: #ff4444;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 68, 68, 0.4);
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
