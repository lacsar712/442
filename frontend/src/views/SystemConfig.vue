<template>
  <div class="page-container fade-in">
    <div class="card">
      <div class="card-header"><h3 class="card-title"><i class="layui-icon layui-icon-set"></i> 系统配置</h3></div>
      <div class="config-form">
        <!-- 系统名称 -->
        <div class="config-item">
          <div class="config-info">
            <label>系统名称</label>
            <span class="config-key">system_name</span>
          </div>
          <input type="text" class="layui-input" v-model="configMap.system_name">
        </div>
        
        <!-- 系统版本 -->
        <div class="config-item">
          <div class="config-info">
            <label>系统版本</label>
            <span class="config-key">system_version</span>
          </div>
          <input type="text" class="layui-input" v-model="configMap.system_version">
        </div>
        
        <!-- 日志保留天数 -->
        <div class="config-item">
          <div class="config-info">
            <label>日志保留天数</label>
            <span class="config-key">log_retention_days</span>
          </div>
          <input type="number" class="layui-input" v-model="configMap.log_retention_days">
        </div>
        
        <!-- 是否允许用户注册 -->
        <div class="config-item">
          <div class="config-info">
            <label>是否允许用户注册</label>
            <span class="config-key">allow_register</span>
          </div>
          <select class="custom-select config-select" v-model="configMap.allow_register">
            <option value="true">是</option>
            <option value="false">否</option>
          </select>
        </div>
        
        <div class="form-actions">
          <button class="layui-btn" @click="handleSave" :disabled="saving">{{ saving ? '保存中...' : '保存配置' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getConfigs, updateConfigs } from '../api/system'

const configs = ref([])
const configMap = reactive({
  system_name: '',
  system_version: '',
  log_retention_days: '',
  allow_register: 'true'
})
const saving = ref(false)

const loadConfigs = async () => { 
  try { 
    const res = await getConfigs()
    configs.value = res.data
    // 将配置项转为map
    res.data.forEach(item => {
      if (configMap.hasOwnProperty(item.configKey)) {
        configMap[item.configKey] = item.configValue
      }
    })
  } catch (error) { console.error(error) } 
}

const handleSave = async () => {
  saving.value = true
  try { 
    // 将configMap转回数组格式
    const updatedConfigs = configs.value.map(item => {
      if (configMap.hasOwnProperty(item.configKey)) {
        return { ...item, configValue: configMap[item.configKey] }
      }
      return item
    })
    await updateConfigs(updatedConfigs)
    if (typeof layui !== 'undefined') layui.layer.msg('保存成功', { icon: 1 })
    else alert('保存成功')
  }
  catch (error) { console.error(error) }
  finally { saving.value = false }
}

onMounted(() => { loadConfigs(); if (typeof layui !== 'undefined') layui.use(['layer'], function() {}) })
</script>

<style scoped>
.page-container { max-width: 800px; margin: 0 auto; }
.card { background: white; border-radius: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); overflow: hidden; }
.card-header { padding: 20px 24px; border-bottom: 1px solid #f0f0f0; }
.card-title { font-size: 16px; font-weight: 600; display: flex; align-items: center; gap: 8px; }
.card-title i { color: #667eea; }
.config-form { padding: 24px; }
.config-item { display: flex; justify-content: space-between; align-items: center; padding: 16px 0; border-bottom: 1px solid #f5f5f5; }
.config-item:last-of-type { border-bottom: none; }
.config-info { flex: 1; }
.config-info label { font-size: 14px; font-weight: 500; color: #333; display: block; margin-bottom: 4px; }
.config-key { font-size: 12px; color: #999; font-family: monospace; }
.config-item .layui-input { width: 300px; }
.config-select { width: 300px !important; }
.input-with-tip { display: flex; align-items: center; gap: 8px; }
.input-with-tip .layui-input { width: 200px; }
.size-tip { font-size: 12px; color: #1890ff; white-space: nowrap; }
.form-actions { margin-top: 24px; padding-top: 24px; border-top: 1px solid #f0f0f0; }
</style>
