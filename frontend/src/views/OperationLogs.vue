<template>
  <div class="page-container fade-in">
    <div class="toolbar">
      <div class="toolbar-left">
        <input type="text" class="layui-input" v-model="searchKeyword" placeholder="搜索用户名/操作" @keyup.enter="loadData" style="width: 200px;">
        <select class="layui-input" v-model="filterModule" @change="loadData" style="width: 140px;"><option value="">全部模块</option><option value="用户管理">用户管理</option><option value="班级管理">班级管理</option><option value="课程管理">课程管理</option><option value="课堂活动">课堂活动</option><option value="系统配置">系统配置</option><option value="文件管理">文件管理</option></select>
        <button class="layui-btn" @click="loadData"><i class="layui-icon layui-icon-search"></i></button>
      </div>
      <div class="toolbar-right"><button class="layui-btn layui-btn-danger" @click="handleCleanLogs"><i class="layui-icon layui-icon-delete"></i> 清理30天前日志</button></div>
    </div>
    
    <div class="card">
      <table class="layui-table">
        <thead><tr><th>用户</th><th>模块</th><th>操作</th><th>方法</th><th>IP</th><th>耗时</th><th>状态</th><th>操作时间</th></tr></thead>
        <tbody>
          <tr v-for="item in dataList" :key="item.id">
            <td>{{ item.username || '-' }}</td>
            <td>{{ item.module }}</td>
            <td>{{ item.operation }}</td>
            <td><code>{{ item.method }}</code></td>
            <td>{{ item.ip }}</td>
            <td>{{ item.duration }}ms</td>
            <td><span class="status-tag" :class="item.status === 1 ? 'active' : 'inactive'">{{ item.status === 1 ? '成功' : '失败' }}</span></td>
            <td>{{ formatDateTime(item.createTime) }}</td>
          </tr>
        </tbody>
      </table>
      <div class="empty-state" v-if="dataList.length === 0"><i class="layui-icon layui-icon-log"></i><p>暂无日志数据</p></div>
      <div class="pagination" v-if="total > 0"><span class="total-info">共 {{ total }} 条记录</span><div class="page-btns"><button class="layui-btn layui-btn-sm" :disabled="page === 1" @click="changePage(page - 1)">上一页</button><span class="page-info">{{ page }} / {{ totalPages }}</span><button class="layui-btn layui-btn-sm" :disabled="page >= totalPages" @click="changePage(page + 1)">下一页</button></div></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getLogs, cleanLogs } from '../api/system'

const dataList = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(15)
const searchKeyword = ref('')
const filterModule = ref('')

const totalPages = computed(() => Math.ceil(total.value / limit.value))

const loadData = async () => {
  try { const res = await getLogs({ keyword: searchKeyword.value, module: filterModule.value || undefined, page: page.value, limit: limit.value }); dataList.value = res.data.list; total.value = res.data.total }
  catch (error) { console.error('加载失败:', error) }
}

const handleCleanLogs = () => {
  layui.layer.confirm('确定要清理30天前的日志吗？此操作不可恢复！', { btn: ['确定', '取消'], icon: 3 }, async () => {
    try { await cleanLogs(30); layui.layer.msg('清理成功', { icon: 1 }); loadData() } catch (error) { console.error(error) }
  })
}

const changePage = (newPage) => { page.value = newPage; loadData() }
const formatDateTime = (dateStr) => dateStr ? new Date(dateStr).toLocaleString('zh-CN') : '-'

onMounted(() => { loadData(); if (typeof layui !== 'undefined') layui.use(['layer'], function() {}) })
</script>

<style scoped>
.page-container { max-width: 1400px; margin: 0 auto; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.toolbar-left, .toolbar-right { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.card { background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.layui-table { margin: 0; }
.layui-table code { background: #f5f5f5; padding: 2px 6px; border-radius: 4px; font-size: 12px; }
.pagination { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-top: 1px solid #f0f0f0; }
.total-info { color: #666; font-size: 14px; }
.page-btns { display: flex; align-items: center; gap: 12px; }
.page-info { color: #333; font-size: 14px; }
.empty-state { text-align: center; padding: 60px 20px; color: #999; }
.empty-state i { font-size: 48px; margin-bottom: 12px; opacity: 0.4; }
</style>
