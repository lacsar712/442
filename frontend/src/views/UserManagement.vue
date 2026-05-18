<template>
  <div class="page-container fade-in">
    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <div class="search-box">
          <input type="text" class="layui-input" v-model="searchKeyword" 
                 placeholder="搜索用户名/姓名/邮箱" @keyup.enter="loadData">
          <button class="layui-btn" @click="loadData">
            <i class="layui-icon layui-icon-search"></i> 搜索
          </button>
        </div>
        <select class="layui-input" v-model="filterRole" @change="loadData" style="width: 120px;">
          <option value="">全部角色</option>
          <option value="0">管理员</option>
          <option value="1">教师</option>
          <option value="2">学生</option>
        </select>
      </div>
      <div class="toolbar-right">
        <button class="layui-btn layui-btn-normal" @click="showAddModal">
          <i class="layui-icon layui-icon-add-1"></i> 新增用户
        </button>
      </div>
    </div>
    
    <!-- 数据表格 -->
    <div class="card">
      <table class="layui-table">
        <thead>
          <tr>
            <th>用户名</th>
            <th>真实姓名</th>
            <th>邮箱</th>
            <th>手机号</th>
            <th>角色</th>
            <th>状态</th>
            <th>创建时间</th>
            <th style="width: 180px;">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in dataList" :key="item.id">
            <td>{{ item.username }}</td>
            <td>{{ item.realName || '-' }}</td>
            <td>{{ item.email || '-' }}</td>
            <td>{{ item.phone || '-' }}</td>
            <td>
              <span class="role-tag" :class="getRoleClass(item.role)">
                {{ getRoleName(item.role) }}
              </span>
            </td>
            <td>
              <span class="status-tag" :class="item.status === 1 ? 'active' : 'inactive'">
                {{ item.status === 1 ? '正常' : '禁用' }}
              </span>
            </td>
            <td>{{ formatDateTime(item.createTime) }}</td>
            <td>
              <div class="action-btns">
                <button class="layui-btn layui-btn-xs layui-btn-normal" @click="showEditModal(item)">
                  编辑
                </button>
                <span :title="isCurrentUser(item.id) ? '不能禁用自己' : ''">
                  <button class="layui-btn layui-btn-xs" 
                          :class="item.status === 1 ? 'layui-btn-warm' : 'layui-btn-normal'"
                          @click="toggleStatus(item)"
                          :disabled="isCurrentUser(item.id)">
                    {{ item.status === 1 ? '禁用' : '启用' }}
                  </button>
                </span>
                <span :title="isCurrentUser(item.id) ? '不能删除自己' : ''">
                  <button class="layui-btn layui-btn-xs layui-btn-danger" 
                          @click="handleDelete(item)"
                          :disabled="isCurrentUser(item.id)">
                    删除
                  </button>
                </span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div class="empty-state" v-if="dataList.length === 0">
        <i class="layui-icon layui-icon-user"></i>
        <p>暂无用户数据</p>
      </div>
      
      <!-- 分页 -->
      <div class="pagination" v-if="total > 0">
        <span class="total-info">共 {{ total }} 条记录</span>
        <div class="page-btns">
          <button class="layui-btn layui-btn-sm" :disabled="page === 1" @click="changePage(page - 1)">
            上一页
          </button>
          <span class="page-info">{{ page }} / {{ totalPages }}</span>
          <button class="layui-btn layui-btn-sm" :disabled="page >= totalPages" @click="changePage(page + 1)">
            下一页
          </button>
        </div>
      </div>
    </div>
    
    <!-- 新增/编辑弹窗 -->
    <div class="modal-overlay" v-if="showModal" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑用户' : '新增用户' }}</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form class="layui-form">
            <div class="form-row">
              <label>用户名 <span class="required">*</span></label>
              <input type="text" class="layui-input" v-model="formData.username" 
                     placeholder="请输入用户名" :disabled="isEdit">
            </div>
            <div class="form-row" v-if="!isEdit">
              <label>密码 <span class="required">*</span></label>
              <input type="password" class="layui-input" v-model="formData.password" 
                     placeholder="请输入密码（留空默认123456）">
            </div>
            <div class="form-row">
              <label>真实姓名</label>
              <input type="text" class="layui-input" v-model="formData.realName" 
                     placeholder="请输入真实姓名">
            </div>
            <div class="form-row">
              <label>邮箱</label>
              <input type="email" class="layui-input" v-model="formData.email" 
                     placeholder="请输入邮箱">
            </div>
            <div class="form-row">
              <label>手机号</label>
              <input type="tel" class="layui-input" v-model="formData.phone" 
                     placeholder="请输入手机号">
            </div>
            <div class="form-row">
              <label>角色 <span class="required">*</span></label>
              <div class="select-wrapper">
                <select class="custom-select" v-model="formData.role" :disabled="isEditingSelf">
                  <option value="0">管理员</option>
                  <option value="1">教师</option>
                  <option value="2">学生</option>
                </select>
                <span v-if="isEditingSelf" class="edit-tip">（不能修改自己的角色）</span>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button class="layui-btn layui-btn-primary" @click="closeModal">取消</button>
          <button class="layui-btn" @click="handleSubmit" :disabled="submitting">
            {{ submitting ? '提交中...' : '确定' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getUserList, addUser, updateUser, updateUserStatus, deleteUser } from '../api/user'

const dataList = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(10)
const searchKeyword = ref('')
const filterRole = ref('')

const showModal = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formData = ref({
  username: '',
  password: '',
  realName: '',
  email: '',
  phone: '',
  role: '2'
})

const currentUserId = computed(() => parseInt(localStorage.getItem('userId') || '0'))
const totalPages = computed(() => Math.ceil(total.value / limit.value))
// 是否正在编辑自己（管理员不能修改自己的角色）
const isEditingSelf = computed(() => isEdit.value && formData.value.id === currentUserId.value)

const loadData = async () => {
  try {
    const res = await getUserList({
      keyword: searchKeyword.value,
      role: filterRole.value || undefined,
      page: page.value,
      limit: limit.value
    })
    dataList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    console.error('加载用户列表失败:', error)
  }
}

const showAddModal = () => {
  isEdit.value = false
  formData.value = { username: '', password: '', realName: '', email: '', phone: '', role: '2' }
  showModal.value = true
}

const showEditModal = (item) => {
  isEdit.value = true
  formData.value = { ...item, role: String(item.role) }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}

const validateForm = () => {
  if (!formData.value.username || formData.value.username.length < 2) {
    layui.layer.msg('用户名至少2个字符', { icon: 2 })
    return false
  }
  if (!isEdit.value && formData.value.password && formData.value.password.length < 6) {
    layui.layer.msg('密码至少6个字符', { icon: 2 })
    return false
  }
  if (formData.value.email && !/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(formData.value.email)) {
    layui.layer.msg('邮箱格式不正确', { icon: 2 })
    return false
  }
  if (formData.value.phone && !/^1[3-9]\d{9}$/.test(formData.value.phone)) {
    layui.layer.msg('手机号格式不正确', { icon: 2 })
    return false
  }
  return true
}

const handleSubmit = async () => {
  if (!validateForm()) return
  
  submitting.value = true
  try {
    const data = { ...formData.value, role: parseInt(formData.value.role) }
    if (isEdit.value) {
      await updateUser(data.id, data)
      layui.layer.msg('更新成功', { icon: 1 })
    } else {
      await addUser(data)
      layui.layer.msg('新增成功', { icon: 1 })
    }
    closeModal()
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
  } finally {
    submitting.value = false
  }
}

const toggleStatus = async (item) => {
  if (isCurrentUser(item.id)) return
  
  const newStatus = item.status === 1 ? 0 : 1
  layui.layer.confirm(`确定要${newStatus === 1 ? '启用' : '禁用'}该用户吗？`, {
    btn: ['确定', '取消'],
    icon: 3
  }, async () => {
    try {
      await updateUserStatus(item.id, newStatus)
      layui.layer.msg('操作成功', { icon: 1 })
      loadData()
    } catch (error) {
      console.error('操作失败:', error)
    }
  })
}

const handleDelete = (item) => {
  if (isCurrentUser(item.id)) return
  
  layui.layer.confirm('确定要删除该用户吗？此操作不可恢复！', {
    btn: ['确定', '取消'],
    icon: 3
  }, async () => {
    try {
      await deleteUser(item.id)
      layui.layer.msg('删除成功', { icon: 1 })
      loadData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

const isCurrentUser = (id) => id === currentUserId.value

const changePage = (newPage) => {
  page.value = newPage
  loadData()
}

const getRoleName = (role) => {
  const roles = { 0: '管理员', 1: '教师', 2: '学生' }
  return roles[role] || '未知'
}

const getRoleClass = (role) => {
  const classes = { 0: 'admin', 1: 'teacher', 2: 'student' }
  return classes[role] || ''
}

const formatDateTime = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

onMounted(() => {
  loadData()
  if (typeof layui !== 'undefined') {
    layui.use(['layer'], function() {})
  }
})
</script>

<style scoped>
.page-container {
  max-width: 1400px;
  margin: 0 auto;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 16px;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.search-box {
  display: flex;
  gap: 8px;
}

.search-box .layui-input {
  width: 220px;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 0;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.layui-table {
  margin: 0;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
}

.total-info {
  color: #666;
  font-size: 14px;
}

.page-btns {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-info {
  color: #333;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.4;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 100%;
  max-width: 480px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h3 {
  font-size: 18px;
  font-weight: 600;
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: #f5f5f5;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
}

.form-row {
  margin-bottom: 20px;
}

.form-row label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
}

.required {
  color: #ff4d4f;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
}

/* 自定义下拉框样式 */
.select-wrapper {
  position: relative;
}

.custom-select {
  width: 100% !important;
  height: 38px !important;
  padding: 0 36px 0 12px !important;
  border: 1px solid #d9d9d9 !important;
  border-radius: 6px !important;
  background-color: #fff !important;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23666' d='M2.5 4.5L6 8l3.5-3.5z'/%3E%3C/svg%3E") !important;
  background-repeat: no-repeat !important;
  background-position: right 12px center !important;
  background-size: 12px !important;
  font-size: 14px !important;
  color: #333 !important;
  cursor: pointer !important;
  display: block !important;
  appearance: none !important;
  -webkit-appearance: none !important;
  -moz-appearance: none !important;
}

.custom-select:focus {
  border-color: #1890ff !important;
  outline: none !important;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2) !important;
}

.custom-select:disabled {
  background-color: #f5f5f5 !important;
  cursor: not-allowed !important;
  color: #999 !important;
}

.custom-select option {
  padding: 8px 12px !important;
}

.edit-tip {
  display: block;
  margin-top: 6px;
  font-size: 12px;
  color: #ff4d4f;
}

/* 操作按钮区域样式 */
.action-btns {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: flex-start;
}

.action-btns button {
  white-space: nowrap;
}

.action-btns span {
  display: inline-block;
}

.action-btns button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
