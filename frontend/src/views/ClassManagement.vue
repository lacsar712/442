<template>
  <div class="page-container fade-in">
    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <div class="search-box">
          <input type="text" class="layui-input" v-model="searchKeyword" 
                 placeholder="搜索班级名称/年级/专业" @keyup.enter="loadData">
          <button class="layui-btn" @click="loadData">
            <i class="layui-icon layui-icon-search"></i> 搜索
          </button>
        </div>
      </div>
      <div class="toolbar-right">
        <button class="layui-btn layui-btn-warm" @click="handleExport">
          <i class="layui-icon layui-icon-export"></i> 导出
        </button>
        <button class="layui-btn layui-btn-normal" @click="showAddModal">
          <i class="layui-icon layui-icon-add-1"></i> 新增班级
        </button>
      </div>
    </div>
    
    <!-- 数据表格 -->
    <div class="card">
      <table class="layui-table">
        <thead>
          <tr>
            <th>班级名称</th>
            <th>年级</th>
            <th>专业</th>
            <th>学生人数</th>
            <th>班主任</th>
            <th>状态</th>
            <th>创建时间</th>
            <th style="width: 150px;">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in dataList" :key="item.id">
            <td>{{ item.className }}</td>
            <td>{{ item.grade }}</td>
            <td>{{ item.major || '-' }}</td>
            <td>{{ item.studentCount || 0 }}</td>
            <td>{{ item.teacherName || '-' }}</td>
            <td>
              <span class="status-tag" :class="item.status === 1 ? 'active' : 'inactive'">
                {{ item.status === 1 ? '正常' : '禁用' }}
              </span>
            </td>
            <td>{{ formatDateTime(item.createTime) }}</td>
            <td>
              <div class="action-btns">
                <button class="layui-btn layui-btn-xs layui-btn-normal" @click="showEditModal(item)">编辑</button>
                <button class="layui-btn layui-btn-xs layui-btn-danger" @click="handleDelete(item)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div class="empty-state" v-if="dataList.length === 0">
        <i class="layui-icon layui-icon-group"></i>
        <p>暂无班级数据</p>
      </div>
      
      <div class="pagination" v-if="total > 0">
        <span class="total-info">共 {{ total }} 条记录</span>
        <div class="page-btns">
          <button class="layui-btn layui-btn-sm" :disabled="page === 1" @click="changePage(page - 1)">上一页</button>
          <span class="page-info">{{ page }} / {{ totalPages }}</span>
          <button class="layui-btn layui-btn-sm" :disabled="page >= totalPages" @click="changePage(page + 1)">下一页</button>
        </div>
      </div>
    </div>
    
    <!-- 新增/编辑弹窗 -->
    <div class="modal-overlay" v-if="showModal" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑班级' : '新增班级' }}</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        <div class="modal-body">
          <form class="layui-form">
            <div class="form-row">
              <label>班级名称 <span class="required">*</span></label>
              <input type="text" class="layui-input" v-model="formData.className" placeholder="请输入班级名称">
            </div>
            <div class="form-row">
              <label>年级 <span class="required">*</span></label>
              <input type="text" class="layui-input" v-model="formData.grade" placeholder="如: 2024级">
            </div>
            <div class="form-row">
              <label>专业</label>
              <input type="text" class="layui-input" v-model="formData.major" placeholder="请输入专业">
            </div>
            <div class="form-row">
              <label>学生人数</label>
              <input type="number" class="layui-input" v-model.number="formData.studentCount" placeholder="请输入学生人数">
            </div>
            <div class="form-row">
              <label>班主任 <span class="required">*</span></label>
              <select class="custom-select" v-model="formData.teacherId">
                <option value="">请选择班主任</option>
                <option v-for="t in teachers" :key="t.id" :value="t.id">{{ t.realName || t.username }}</option>
              </select>
            </div>
            <div class="form-row">
              <label>描述</label>
              <textarea class="layui-textarea" v-model="formData.description" placeholder="请输入描述"></textarea>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button class="layui-btn layui-btn-primary" @click="closeModal">取消</button>
          <button class="layui-btn" @click="handleSubmit" :disabled="submitting">{{ submitting ? '提交中...' : '确定' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getClassList, addClass, updateClass, deleteClass } from '../api/classInfo'
import { getTeachers } from '../api/user'
import axios from 'axios'

const dataList = ref([])
const teachers = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(10)
const searchKeyword = ref('')
const showModal = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formData = ref({ className: '', grade: '', major: '', studentCount: 0, teacherId: '', description: '' })

const totalPages = computed(() => Math.ceil(total.value / limit.value))

const loadData = async () => {
  try {
    const res = await getClassList({ keyword: searchKeyword.value, page: page.value, limit: limit.value })
    dataList.value = res.data.list
    total.value = res.data.total
  } catch (error) { console.error('加载失败:', error) }
}

const loadTeachers = async () => {
  try {
    const res = await getTeachers()
    teachers.value = res.data
  } catch (error) { console.error('加载教师失败:', error) }
}

const showAddModal = () => {
  isEdit.value = false
  formData.value = { className: '', grade: '', major: '', studentCount: 0, teacherId: '', description: '' }
  showModal.value = true
}

const showEditModal = (item) => {
  isEdit.value = true
  formData.value = { ...item }
  showModal.value = true
}

const closeModal = () => { showModal.value = false }

const handleSubmit = async () => {
  if (!formData.value.className || !formData.value.grade || !formData.value.teacherId) {
    layui.layer.msg('请填写必填项', { icon: 2 })
    return
  }
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateClass(formData.value.id, formData.value)
      layui.layer.msg('更新成功', { icon: 1 })
    } else {
      await addClass(formData.value)
      layui.layer.msg('新增成功', { icon: 1 })
    }
    closeModal()
    loadData()
  } catch (error) { console.error('保存失败:', error) }
  finally { submitting.value = false }
}

const handleDelete = (item) => {
  layui.layer.confirm('确定要删除该班级吗？', { btn: ['确定', '取消'], icon: 3 }, async () => {
    try {
      await deleteClass(item.id)
      layui.layer.msg('删除成功', { icon: 1 })
      loadData()
    } catch (error) { console.error('删除失败:', error) }
  })
}

const handleExport = async () => {
  try {
    const response = await axios.get('/api/classes/export', {
      responseType: 'blob',
      headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    })
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', '班级列表.xlsx')
    document.body.appendChild(link)
    link.click()
    link.remove()
    layui.layer.msg('导出成功', { icon: 1 })
  } catch (error) { layui.layer.msg('导出失败', { icon: 2 }) }
}

const changePage = (newPage) => { page.value = newPage; loadData() }
const formatDateTime = (dateStr) => dateStr ? new Date(dateStr).toLocaleString('zh-CN') : '-'

onMounted(() => {
  loadData()
  loadTeachers()
  if (typeof layui !== 'undefined') layui.use(['layer'], function() {})
})
</script>

<style scoped>
.page-container { max-width: 1400px; margin: 0 auto; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 16px; }
.toolbar-left, .toolbar-right { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.search-box { display: flex; gap: 8px; }
.search-box .layui-input { width: 260px; }
.card { background: white; border-radius: 12px; padding: 0; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.layui-table { margin: 0; }
.pagination { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-top: 1px solid #f0f0f0; }
.total-info { color: #666; font-size: 14px; }
.page-btns { display: flex; align-items: center; gap: 12px; }
.page-info { color: #333; font-size: 14px; }
.empty-state { text-align: center; padding: 60px 20px; color: #999; }
.empty-state i { font-size: 48px; margin-bottom: 12px; opacity: 0.4; }
.modal-overlay { position: fixed; top:0;left:0;right:0;bottom:0; background:rgba(0,0,0,0.5); display:flex; align-items:center; justify-content:center; z-index:1000; }
.modal-content { background:white; border-radius:12px; width:100%; max-width:500px; max-height:90vh; overflow:hidden; display:flex; flex-direction:column; }
.modal-header { display:flex; justify-content:space-between; align-items:center; padding:20px 24px; border-bottom:1px solid #f0f0f0; }
.modal-header h3 { font-size:18px; font-weight:600; }
.close-btn { width:32px; height:32px; border:none; background:#f5f5f5; border-radius:50%; font-size:20px; cursor:pointer; display:flex; align-items:center; justify-content:center; }
.modal-body { padding:24px; overflow-y:auto; }
.form-row { margin-bottom:20px; }
.form-row label { display:block; font-size:14px; font-weight:500; margin-bottom:8px; color:#333; }
.required { color:#ff4d4f; }
.modal-footer { display:flex; justify-content:flex-end; gap:12px; padding:16px 24px; border-top:1px solid #f0f0f0; }
.custom-select { width:100%; height:38px; padding:0 12px; border:1px solid #e6e6e6; border-radius:4px; background-color:#fff; font-size:14px; color:#333; cursor:pointer; appearance:auto; -webkit-appearance:auto; -moz-appearance:auto; }
.custom-select:focus { border-color:#1890ff; outline:none; box-shadow:0 0 0 2px rgba(24,144,255,0.2); }
</style>
