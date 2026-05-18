<template>
  <div class="login-page">
    <div class="login-bg"></div>
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <i class="layui-icon layui-icon-read"></i>
          </div>
          <h1>用户注册</h1>
          <p>Create Your Account</p>
        </div>
        
        <form class="login-form" @submit.prevent="handleRegister">
          <div class="form-group">
            <label>用户名 <span class="required">*</span></label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-username"></i>
              <input 
                type="text" 
                v-model="form.username" 
                placeholder="请输入用户名（2-50个字符）"
                autocomplete="username"
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label>密码 <span class="required">*</span></label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-password"></i>
              <input 
                :type="showPassword ? 'text' : 'password'" 
                v-model="form.password" 
                placeholder="请输入密码（6位以上）"
                autocomplete="new-password"
                required
              />
              <i 
                class="layui-icon toggle-password"
                :class="showPassword ? 'layui-icon-eye' : 'layui-icon-eye-invisible'"
                @click="showPassword = !showPassword"
              ></i>
            </div>
          </div>
          
          <div class="form-group">
            <label>真实姓名</label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-user"></i>
              <input 
                type="text" 
                v-model="form.realName" 
                placeholder="请输入真实姓名"
              />
            </div>
          </div>
          
          <div class="form-group">
            <label>邮箱</label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-email"></i>
              <input 
                type="email" 
                v-model="form.email" 
                placeholder="请输入邮箱（选填）"
              />
            </div>
          </div>
          
          <div class="form-group">
            <label>手机号</label>
            <div class="input-wrapper">
              <i class="layui-icon layui-icon-cellphone"></i>
              <input 
                type="tel" 
                v-model="form.phone" 
                placeholder="请输入手机号（选填）"
              />
            </div>
          </div>
          
          <button type="submit" class="login-btn" :disabled="loading">
            <i v-if="loading" class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop"></i>
            {{ loading ? '注册中...' : '注 册' }}
          </button>
        </form>
        
        <div class="login-footer">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  realName: '',
  email: '',
  phone: ''
})

const loading = ref(false)
const showPassword = ref(false)

// 消息提示函数
const showMsg = (msg, icon) => {
  if (typeof layui !== 'undefined' && layui.layer) {
    layui.layer.msg(msg, { icon })
  } else {
    alert(msg)
  }
}

// 验证表单（邮箱和手机号允许为空，填写时需验证格式）
const validateForm = () => {
  if (!form.value.username || form.value.username.length < 2) {
    showMsg('用户名至少2个字符', 2)
    return false
  }
  if (!form.value.password || form.value.password.length < 6) {
    showMsg('密码至少6个字符', 2)
    return false
  }
  // 邮箱：允许为空，填写时需验证格式
  if (form.value.email && !/^[\w.%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/.test(form.value.email)) {
    showMsg('邮箱格式不正确', 2)
    return false
  }
  // 手机号：允许为空，填写时需验证格式
  if (form.value.phone && !/^1[3-9]\d{9}$/.test(form.value.phone)) {
    showMsg('手机号格式不正确', 2)
    return false
  }
  return true
}

const handleRegister = async () => {
  if (!validateForm()) return
  
  loading.value = true
  try {
    // 构建数据：空字符串提交空字符串
    const data = {
      username: form.value.username,
      password: form.value.password,
      realName: form.value.realName || '',
      email: form.value.email || '',
      phone: form.value.phone || ''
    }
    await register(data)
    showMsg('注册成功，请登录', 1)
    setTimeout(() => {
      router.push('/login')
    }, 1000)
  } catch (error) {
    // 错误消息已由API拦截器统一处理显示，此处无需额外处理
  } finally {
    loading.value = false
  }
}

onMounted(() => {
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
  padding: 20px 0;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #36cfc9 0%, #1890ff 100%);
  z-index: 0;
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
  padding: 40px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  width: 70px;
  height: 70px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #36cfc9 0%, #1890ff 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 30px rgba(24, 144, 255, 0.4);
}

.logo i {
  font-size: 32px;
  color: white;
}

.login-header h1 {
  font-size: 22px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.login-header p {
  font-size: 12px;
  color: #999;
}

.form-group {
  margin-bottom: 18px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

.required {
  color: #ff4d4f;
}

.input-wrapper {
  position: relative;
}

.input-wrapper i {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  font-size: 16px;
}

.input-wrapper input {
  width: 100%;
  height: 44px;
  padding: 0 44px 0 42px;
  border: 2px solid #e8e8e8;
  border-radius: 10px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: #fafafa;
}

.input-wrapper input:focus {
  border-color: #1890ff;
  background: white;
  box-shadow: 0 0 0 4px rgba(24, 144, 255, 0.1);
  outline: none;
}

.input-wrapper input:-webkit-autofill {
  -webkit-box-shadow: 0 0 0 1000px #fafafa inset;
}

.toggle-password {
  left: auto !important;
  right: 14px;
  cursor: pointer;
}

.login-btn {
  width: 100%;
  height: 48px;
  background: linear-gradient(135deg, #36cfc9 0%, #1890ff 100%);
  border: none;
  border-radius: 10px;
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(24, 144, 255, 0.4);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.login-footer p {
  color: #999;
  font-size: 14px;
}

.login-footer a {
  color: #1890ff;
  font-weight: 600;
}
</style>
