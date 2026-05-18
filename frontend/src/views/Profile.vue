<template>
  <div class="page-container fade-in">
    <div class="profile-layout">
      <div class="profile-card">
        <div class="profile-header">
          <div class="avatar-section">
            <div class="avatar">{{ userInitial }}</div>
            <div class="user-info">
              <h2>{{ profile.realName || profile.username }}</h2>
              <span class="role-tag" :class="getRoleClass(profile.role)">{{ getRoleName(profile.role) }}</span>
            </div>
          </div>
        </div>
        <div class="profile-form">
          <h3><i class="layui-icon layui-icon-edit"></i> 基本信息</h3>
          <form class="layui-form">
            <div class="form-row"><label>用户名</label><input type="text" class="layui-input" :value="profile.username" disabled></div>
            <div class="form-row"><label>真实姓名</label><input type="text" class="layui-input" v-model="profile.realName" placeholder="请输入真实姓名"></div>
            <div class="form-row"><label>邮箱</label><input type="email" class="layui-input" v-model="profile.email" placeholder="请输入邮箱（选填）"></div>
            <div class="form-row"><label>手机号</label><input type="tel" class="layui-input" v-model="profile.phone" placeholder="请输入手机号（选填）"></div>
          </form>
          <div class="form-actions"><button class="layui-btn" @click="handleUpdateProfile" :disabled="saving">{{ saving ? '保存中...' : '保存信息' }}</button></div>
        </div>
      </div>
      
      <div class="profile-card">
        <div class="profile-form">
          <h3><i class="layui-icon layui-icon-password"></i> 修改密码</h3>
          <form class="layui-form">
            <div class="form-row"><label>原密码 <span class="required">*</span></label><input type="password" class="layui-input" v-model="passwordForm.oldPassword" placeholder="请输入原密码"></div>
            <div class="form-row"><label>新密码 <span class="required">*</span></label><input type="password" class="layui-input" v-model="passwordForm.newPassword" placeholder="请输入新密码（6位以上）"></div>
            <div class="form-row"><label>确认密码 <span class="required">*</span></label><input type="password" class="layui-input" v-model="passwordForm.confirmPassword" placeholder="请再次输入新密码"></div>
          </form>
          <div class="form-actions"><button class="layui-btn layui-btn-warm" @click="handleUpdatePassword" :disabled="changingPwd">{{ changingPwd ? '修改中...' : '修改密码' }}</button></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getProfile, updateProfile, updatePassword } from '../api/user'

const profile = ref({ username: '', realName: '', email: '', phone: '', role: 2 })
const passwordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const saving = ref(false)
const changingPwd = ref(false)

const userInitial = computed(() => (profile.value.realName || profile.value.username || '?').charAt(0).toUpperCase())

const loadProfile = async () => {
  try { const res = await getProfile(); profile.value = res.data } catch (error) { console.error(error) }
}

// 验证邮箱和手机号（允许为空，填写时需符合格式）
const validateProfile = () => {
  if (profile.value.email && !/^[\w.%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/.test(profile.value.email)) {
    if (typeof layui !== 'undefined') layui.layer.msg('邮箱格式不正确', { icon: 2 })
    else alert('邮箱格式不正确')
    return false
  }
  if (profile.value.phone && !/^1[3-9]\d{9}$/.test(profile.value.phone)) {
    if (typeof layui !== 'undefined') layui.layer.msg('手机号格式不正确', { icon: 2 })
    else alert('手机号格式不正确')
    return false
  }
  return true
}

const handleUpdateProfile = async () => {
  if (!validateProfile()) return
  saving.value = true
  try {
    await updateProfile({ realName: profile.value.realName, email: profile.value.email || '', phone: profile.value.phone || '' })
    localStorage.setItem('realName', profile.value.realName || '')
    // 同步更新头部用户信息
    if (window.refreshHeaderUserInfo) {
      window.refreshHeaderUserInfo()
    }
    if (typeof layui !== 'undefined') layui.layer.msg('保存成功', { icon: 1 })
    else alert('保存成功')
  } catch (error) { console.error(error) }
  finally { saving.value = false }
}

const handleUpdatePassword = async () => {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword || !passwordForm.value.confirmPassword) { 
    if (typeof layui !== 'undefined') layui.layer.msg('请填写完整信息', { icon: 2 })
    else alert('请填写完整信息')
    return 
  }
  if (passwordForm.value.newPassword.length < 6) { 
    if (typeof layui !== 'undefined') layui.layer.msg('新密码至少6位', { icon: 2 })
    else alert('新密码至少6位')
    return 
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) { 
    if (typeof layui !== 'undefined') layui.layer.msg('两次密码不一致', { icon: 2 })
    else alert('两次密码不一致')
    return 
  }
  changingPwd.value = true
  try {
    await updatePassword(passwordForm.value)
    if (typeof layui !== 'undefined') layui.layer.msg('密码修改成功', { icon: 1 })
    else alert('密码修改成功')
    passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch (error) { console.error(error) }
  finally { changingPwd.value = false }
}

const getRoleName = (role) => ({ 0: '管理员', 1: '教师', 2: '学生' }[role] || '用户')
const getRoleClass = (role) => ({ 0: 'admin', 1: 'teacher', 2: 'student' }[role] || '')

onMounted(() => { loadProfile(); if (typeof layui !== 'undefined') layui.use(['layer'], function() {}) })
</script>

<style scoped>
.page-container { max-width: 800px; margin: 0 auto; }
.profile-layout { display: flex; flex-direction: column; gap: 24px; }
.profile-card { background: white; border-radius: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); overflow: hidden; }
.profile-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 32px; color: white; }
.avatar-section { display: flex; align-items: center; gap: 20px; }
.avatar { width: 80px; height: 80px; background: rgba(255,255,255,0.2); border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 32px; font-weight: 700; }
.user-info h2 { font-size: 24px; margin-bottom: 8px; }
.role-tag { padding: 4px 12px; border-radius: 16px; font-size: 12px; background: rgba(255,255,255,0.25); }
.profile-form { padding: 24px 32px 32px; }
.profile-form h3 { font-size: 16px; font-weight: 600; margin-bottom: 24px; display: flex; align-items: center; gap: 8px; color: #333; }
.profile-form h3 i { color: #667eea; }
.form-row { margin-bottom: 20px; }
.form-row label { display: block; font-size: 14px; font-weight: 500; margin-bottom: 8px; color: #333; }
.required { color: #ff4d4f; }
.form-actions { margin-top: 24px; }
</style>
