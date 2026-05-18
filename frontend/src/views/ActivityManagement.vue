<template>
  <div class="page-container fade-in">
    <div class="toolbar">
      <div class="toolbar-row">
        <div class="search-filters">
          <input type="text" class="layui-input search-input" v-model="searchKeyword" placeholder="搜索活动标题" @keyup.enter="loadData">
          <select class="custom-select filter-select" v-model="filterClassId" @change="loadData"><option value="">全部班级</option><option v-for="c in classes" :key="c.id" :value="c.id">{{ c.className }}</option></select>
          <select class="custom-select filter-select" v-model="filterCourseId" @change="loadData"><option value="">全部课程</option><option v-for="c in courses" :key="c.id" :value="c.id">{{ c.courseName }}</option></select>
          <select class="custom-select filter-select-sm" v-model="filterType" @change="loadData"><option value="">全部类型</option><option value="lecture">讲座</option><option value="experiment">实验</option><option value="discussion">讨论</option><option value="exam">考试</option><option value="other">其他</option></select>
          <button class="layui-btn" @click="loadData"><i class="layui-icon layui-icon-search"></i> 搜索</button>
        </div>
        <div class="action-buttons">
          <button class="layui-btn layui-btn-warm" @click="handleExport"><i class="layui-icon layui-icon-export"></i> 导出</button>
          <button class="layui-btn layui-btn-normal" @click="showAddModal"><i class="layui-icon layui-icon-add-1"></i> 新增活动</button>
        </div>
      </div>
    </div>
    
    <div class="card">
      <table class="layui-table">
        <thead><tr><th>活动标题</th><th>班级</th><th>课程</th><th>类型</th><th>活动日期</th><th>附件</th><th style="width: 150px;">操作</th></tr></thead>
        <tbody>
          <tr v-for="item in dataList" :key="item.id">
            <td>{{ item.title }}</td>
            <td>{{ item.className || '-' }}</td>
            <td>{{ item.courseName || '-' }}</td>
            <td><span class="type-tag" :class="item.activityType">{{ getTypeName(item.activityType) }}</span></td>
            <td>{{ formatDate(item.activityDate) }}</td>
            <td>
              <a v-if="item.attachment" href="javascript:;" @click="downloadFile(item)" class="file-link">
                <i class="layui-icon layui-icon-file"></i> {{ item.attachmentName || '附件' }}
              </a>
              <span v-else>-</span>
            </td>
            <td><div class="action-btns"><button class="layui-btn layui-btn-xs layui-btn-normal" @click="showEditModal(item)">编辑</button><button class="layui-btn layui-btn-xs layui-btn-danger" @click="handleDelete(item)">删除</button></div></td>
          </tr>
        </tbody>
      </table>
      <div class="empty-state" v-if="dataList.length === 0"><i class="layui-icon layui-icon-note"></i><p>暂无活动数据</p></div>
      <div class="pagination" v-if="total > 0"><span class="total-info">共 {{ total }} 条记录</span><div class="page-btns"><button class="layui-btn layui-btn-sm" :disabled="page === 1" @click="changePage(page - 1)">上一页</button><span class="page-info">{{ page }} / {{ totalPages }}</span><button class="layui-btn layui-btn-sm" :disabled="page >= totalPages" @click="changePage(page + 1)">下一页</button></div></div>
    </div>
    
    <div class="modal-overlay" v-if="showModal" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header"><h3>{{ isEdit ? '编辑活动' : '新增活动' }}</h3><button class="close-btn" @click="closeModal">&times;</button></div>
        <div class="modal-body">
          <form class="layui-form">
            <div class="form-row"><label>活动标题 <span class="required">*</span></label><input type="text" class="layui-input" v-model="formData.title" placeholder="请输入活动标题"></div>
            <div class="form-row"><label>班级 <span class="required">*</span></label>
              <select class="custom-select" v-model="formData.classId"><option value="">请选择</option><option v-for="c in classes" :key="c.id" :value="c.id">{{ c.className }}</option></select>
            </div>
            <div class="form-row"><label>课程 <span class="required">*</span></label>
              <select class="custom-select" v-model="formData.courseId"><option value="">请选择</option><option v-for="c in courses" :key="c.id" :value="c.id">{{ c.courseName }}</option></select>
            </div>
            <div class="form-row"><label>活动类型 <span class="required">*</span></label>
              <select class="custom-select" v-model="formData.activityType">
                <option value="lecture">讲座</option>
                <option value="experiment">实验</option>
                <option value="discussion">讨论</option>
                <option value="exam">考试</option>
                <option value="other">其他</option>
              </select>
            </div>
            <div class="form-row"><label>活动日期 <span class="required">*</span></label><input type="datetime-local" class="layui-input" v-model="formData.activityDateStr"></div>
            <div class="form-row"><label>活动内容</label><textarea class="layui-textarea" v-model="formData.content" placeholder="请输入活动内容" rows="3"></textarea></div>
            <div class="form-row">
              <label>附件</label>
              <input type="file" ref="fileInput" @change="handleFileChange" class="file-input">
              <div v-if="formData.attachmentName" class="file-preview">
                <i class="layui-icon layui-icon-file"></i> {{ formData.attachmentName }}
                <button type="button" class="remove-file-btn" @click="removeFile">&times;</button>
              </div>
              <div class="file-tip">支持常见文档、图片格式，最大50MB</div>
            </div>
          </form>
        </div>
        <div class="modal-footer"><button class="layui-btn layui-btn-primary" @click="closeModal">取消</button><button class="layui-btn" @click="handleSubmit" :disabled="submitting">{{ submitting ? '提交中...' : '确定' }}</button></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getActivityList, addActivity, updateActivity, deleteActivity } from '../api/activity'
import { getAllClasses } from '../api/classInfo'
import { getAllCourses } from '../api/course'
import { uploadFile, downloadFile as downloadFileApi } from '../api/file'
import axios from 'axios'

const dataList = ref([])
const classes = ref([])
const courses = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(10)
const searchKeyword = ref('')
const filterClassId = ref('')
const filterCourseId = ref('')
const filterType = ref('')
const showModal = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const fileInput = ref(null)
const selectedFile = ref(null)

const formData = ref({ title: '', classId: '', courseId: '', activityType: 'lecture', activityDateStr: '', content: '', attachment: '', attachmentName: '' })

const totalPages = computed(() => Math.ceil(total.value / limit.value))

const loadData = async () => {
  try {
    const res = await getActivityList({ keyword: searchKeyword.value, classId: filterClassId.value || undefined, courseId: filterCourseId.value || undefined, activityType: filterType.value || undefined, page: page.value, limit: limit.value })
    dataList.value = res.data.list
    total.value = res.data.total
  } catch (error) { console.error('加载失败:', error) }
}

const loadClasses = async () => { try { const res = await getAllClasses(); classes.value = res.data } catch (error) { console.error(error) } }
const loadCourses = async () => { try { const res = await getAllCourses(); courses.value = res.data } catch (error) { console.error(error) } }

const showAddModal = () => {
  isEdit.value = false
  formData.value = { title: '', classId: '', courseId: '', activityType: 'lecture', activityDateStr: '', content: '', attachment: '', attachmentName: '' }
  selectedFile.value = null
  if (fileInput.value) fileInput.value.value = ''
  showModal.value = true
}

const showEditModal = (item) => {
  isEdit.value = true
  const dateStr = item.activityDate ? new Date(item.activityDate).toISOString().slice(0, 16) : ''
  formData.value = { ...item, activityDateStr: dateStr }
  selectedFile.value = null
  if (fileInput.value) fileInput.value.value = ''
  showModal.value = true
}

const closeModal = () => { showModal.value = false }

const handleFileChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    // 检查文件大小 (50MB)
    if (file.size > 50 * 1024 * 1024) {
      if (typeof layui !== 'undefined') layui.layer.msg('文件大小不能超过50MB', { icon: 2 })
      else alert('文件大小不能超过50MB')
      e.target.value = ''
      return
    }
    selectedFile.value = file
  }
}

const removeFile = () => {
  formData.value.attachment = ''
  formData.value.attachmentName = ''
  selectedFile.value = null
  if (fileInput.value) fileInput.value.value = ''
}

const handleSubmit = async () => {
  if (!formData.value.title || !formData.value.classId || !formData.value.courseId || !formData.value.activityDateStr) { 
    if (typeof layui !== 'undefined') layui.layer.msg('请填写必填项', { icon: 2 })
    else alert('请填写必填项')
    return 
  }
  submitting.value = true
  try {
    // 如果有新选择的文件，先上传
    if (selectedFile.value) {
      const res = await uploadFile(selectedFile.value)
      formData.value.attachment = res.data.data.filePath
      formData.value.attachmentName = res.data.data.fileName
    }
    const data = { ...formData.value, activityDate: new Date(formData.value.activityDateStr).toISOString() }
    if (isEdit.value) { 
      await updateActivity(data.id, data)
      if (typeof layui !== 'undefined') layui.layer.msg('更新成功', { icon: 1 })
    } else { 
      await addActivity(data)
      if (typeof layui !== 'undefined') layui.layer.msg('新增成功', { icon: 1 })
    }
    closeModal(); loadData()
  } catch (error) { console.error('保存失败:', error) }
  finally { submitting.value = false }
}

const handleDelete = (item) => {
  if (typeof layui !== 'undefined') {
    layui.layer.confirm('确定要删除该活动吗？', { btn: ['确定', '取消'], icon: 3 }, async () => {
      try { await deleteActivity(item.id); layui.layer.msg('删除成功', { icon: 1 }); loadData() } catch (error) { console.error(error) }
    })
  } else if (confirm('确定要删除该活动吗？')) {
    deleteActivity(item.id).then(() => loadData())
  }
}

const downloadFile = (item) => { 
  downloadFileApi(item.attachment, item.attachmentName) 
}

const handleExport = async () => {
  try {
    const response = await axios.get('/api/activities/export', { params: { classId: filterClassId.value, courseId: filterCourseId.value }, responseType: 'blob', headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }})
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a'); link.href = url; link.setAttribute('download', '课堂活动.xlsx'); document.body.appendChild(link); link.click(); link.remove()
    if (typeof layui !== 'undefined') layui.layer.msg('导出成功', { icon: 1 })
  } catch (error) { 
    if (typeof layui !== 'undefined') layui.layer.msg('导出失败', { icon: 2 })
  }
}

const changePage = (newPage) => { page.value = newPage; loadData() }
const getTypeName = (type) => ({ lecture: '讲座', experiment: '实验', discussion: '讨论', exam: '考试', other: '其他' }[type] || '其他')
const formatDate = (dateStr) => dateStr ? new Date(dateStr).toLocaleString('zh-CN', { month: 'numeric', day: 'numeric', hour: '2-digit', minute: '2-digit' }) : '-'

onMounted(() => { loadData(); loadClasses(); loadCourses(); if (typeof layui !== 'undefined') layui.use(['layer'], function() {}) })
</script>

<style scoped>
.page-container { max-width: 1400px; margin: 0 auto; }
.toolbar { background: white; border-radius: 12px; padding: 16px 20px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.toolbar-row { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px; }
.search-filters { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.search-input { width: 200px !important; }
.filter-select { width: 140px !important; }
.filter-select-sm { width: 120px !important; }
.action-buttons { display: flex; align-items: center; gap: 8px; }
.card { background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.layui-table { margin: 0; }
.type-tag { padding: 3px 10px; border-radius: 12px; font-size: 12px; font-weight: 500; }
.type-tag.lecture { background: #dbeafe; color: #1d4ed8; }
.type-tag.experiment { background: #d1fae5; color: #059669; }
.type-tag.discussion { background: #fef3c7; color: #d97706; }
.type-tag.exam { background: #fee2e2; color: #dc2626; }
.type-tag.other { background: #e5e7eb; color: #4b5563; }
.file-link { color: #1890ff; display: inline-flex; align-items: center; gap: 4px; text-decoration: none; }
.file-link:hover { color: #40a9ff; text-decoration: underline; }
.pagination { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-top: 1px solid #f0f0f0; }
.total-info { color: #666; font-size: 14px; }
.page-btns { display: flex; align-items: center; gap: 12px; }
.page-info { color: #333; font-size: 14px; }
.empty-state { text-align: center; padding: 60px 20px; color: #999; }
.empty-state i { font-size: 48px; margin-bottom: 12px; opacity: 0.4; }
.modal-overlay { position: fixed; top:0;left:0;right:0;bottom:0; background:rgba(0,0,0,0.5); display:flex; align-items:center; justify-content:center; z-index:1000; }
.modal-content { background:white; border-radius:12px; width:100%; max-width:560px; max-height:90vh; overflow:hidden; display:flex; flex-direction:column; }
.modal-header { display:flex; justify-content:space-between; align-items:center; padding:20px 24px; border-bottom:1px solid #f0f0f0; }
.modal-header h3 { font-size:18px; font-weight:600; }
.close-btn { width:32px; height:32px; border:none; background:#f5f5f5; border-radius:50%; font-size:20px; cursor:pointer; }
.modal-body { padding:24px; overflow-y:auto; }
.form-row { margin-bottom:18px; }
.form-row label { display:block; font-size:14px; font-weight:500; margin-bottom:8px; }
.required { color:#ff4d4f; }
.file-input { width: 100%; padding: 8px 0; }
.file-preview { margin-top: 8px; padding: 8px 12px; background: #f5f5f5; border-radius: 6px; font-size: 13px; display: flex; align-items: center; gap: 6px; }
.remove-file-btn { margin-left: auto; background: none; border: none; color: #999; font-size: 16px; cursor: pointer; padding: 0 4px; }
.remove-file-btn:hover { color: #ff4d4f; }
.file-tip { margin-top: 6px; font-size: 12px; color: #999; }
.modal-footer { display:flex; justify-content:flex-end; gap:12px; padding:16px 24px; border-top:1px solid #f0f0f0; }
</style>
