<template>
  <div class="login-page">
    <div class="login-bg"></div>
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <i class="layui-icon layui-icon-read"></i>
          </div>
          <h1>课堂信息管理系统</h1>
          <p>Classroom Information Management System</p>
        </div>
        
        <form class="login-form" @submit.prevent="handleLogin">
          <div class="form-group">
            <label>用户名</label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-username"></i>
              <input 
                type="text" 
                v-model="form.username" 
                placeholder="请输入用户名"
                autocomplete="username"
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label>密码</label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-password"></i>
              <input 
                :type="showPassword ? 'text' : 'password'" 
                v-model="form.password" 
                placeholder="请输入密码"
                autocomplete="current-password"
                required
              />
              <i 
                class="layui-icon toggle-password"
                :class="showPassword ? 'layui-icon-eye' : 'layui-icon-eye-invisible'"
                @click="showPassword = !showPassword"
              ></i>
            </div>
          </div>
          
          <button type="submit" class="login-btn" :disabled="loading">
            <i v-if="loading" class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop"></i>
            {{ loading ? '登录中...' : '登 录' }}
          </button>
        </form>
        
        <div class="quick-fill">
          <button type="button" class="quick-fill-btn" @click="fillAdmin">
            <i class="layui-icon layui-icon-auz"></i>
            <span class="quick-fill-text">
              <strong>一键填写管理员账号</strong>
              <small>用于用户管理和系统配置</small>
            </span>
          </button>
        </div>
        
        <div class="login-footer">
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/user'

const router = useRouter()

const form = ref({
  username: '',
  password: ''
})

const loading = ref(false)
const showPassword = ref(false)

// 一键填写管理员账号
const fillAdmin = () => {
  form.value.username = 'admin'
  form.value.password = '123456'
}

// 消息提示函数
const showMsg = (msg, icon) => {
  if (typeof layui !== 'undefined' && layui.layer) {
    layui.layer.msg(msg, { icon })
  } else {
    alert(msg)
  }
}

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    showMsg('请输入用户名和密码', 2)
    return
  }
  
  loading.value = true
  try {
    const res = await login(form.value)
    // 保存用户信息
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userId', res.data.userId)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('realName', res.data.realName || '')
    localStorage.setItem('role', res.data.role)
    
    showMsg('登录成功', 1)
    setTimeout(() => {
      router.push('/dashboard')
    }, 500)
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 加载LayUI
  if (typeof layui !== 'undefined') {
    layui.use(['layer'], function() {})
  }
})
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  z-index: 0;
}

.login-bg::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 60%);
  animation: pulse 15s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(-20%, -20%); }
}

.login-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  padding: 20px;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 48px 40px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.logo i {
  font-size: 40px;
  color: white;
}

.login-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.login-header p {
  font-size: 12px;
  color: #999;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.input-wrapper {
  position: relative;
}

.input-wrapper i {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  font-size: 18px;
}

.input-wrapper input {
  width: 100%;
  height: 50px;
  padding: 0 50px 0 48px;
  border: 2px solid #e8e8e8;
  border-radius: 12px;
  font-size: 15px;
  transition: all 0.3s ease;
  background: #fafafa;
}

.input-wrapper input:focus {
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
  outline: none;
}

.input-wrapper input:-webkit-autofill {
  -webkit-box-shadow: 0 0 0 1000px #fafafa inset;
  transition: background-color 5000s ease-in-out 0s;
}

.toggle-password {
  left: auto !important;
  right: 16px;
  cursor: pointer;
  user-select: none;
}

.toggle-password:hover {
  color: #667eea;
}

.login-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.quick-fill {
  margin-top: 16px;
  text-align: center;
}

.quick-fill-btn {
  background: transparent;
  border: 1px dashed #667eea;
  color: #667eea;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s ease;
}

.quick-fill-btn i {
  font-size: 20px;
}

.quick-fill-text {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  line-height: 1.4;
}

.quick-fill-text strong {
  font-size: 14px;
  font-weight: 600;
}

.quick-fill-text small {
  font-size: 11px;
  opacity: 0.8;
}

.quick-fill-btn:hover {
  background: rgba(102, 126, 234, 0.1);
  border-style: solid;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.login-footer p {
  color: #999;
  font-size: 14px;
}

.login-footer a {
  color: #667eea;
  font-weight: 600;
  transition: color 0.3s;
}

.login-footer a:hover {
  color: #764ba2;
}
</style>
