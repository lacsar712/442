<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header">
        <i class="layui-icon layui-icon-read"></i>
        <span v-if="!sidebarCollapsed">课堂信息管理</span>
      </div>
      
      <nav class="sidebar-menu">
        <router-link to="/dashboard" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-home"></i>
          <span>仪表盘</span>
        </router-link>
        
        <router-link v-if="isAdmin" to="/users" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-user"></i>
          <span>用户管理</span>
        </router-link>
        
        <router-link to="/classes" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-group"></i>
          <span>班级管理</span>
        </router-link>
        
        <router-link to="/courses" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-read"></i>
          <span>课程管理</span>
        </router-link>
        
        <router-link to="/activities" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-note"></i>
          <span>课堂活动</span>
        </router-link>
        
        <router-link v-if="isAdmin" to="/config" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-set"></i>
          <span>系统配置</span>
        </router-link>
        
        <router-link v-if="isAdmin" to="/logs" class="menu-item" active-class="active">
          <i class="layui-icon layui-icon-log"></i>
          <span>操作日志</span>
        </router-link>
      </nav>
    </aside>
    
    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 顶部导航 -->
      <header class="header">
        <div class="header-left">
          <button class="toggle-btn" @click="sidebarCollapsed = !sidebarCollapsed">
            <i class="layui-icon layui-icon-shrink-right" v-if="sidebarCollapsed"></i>
            <i class="layui-icon layui-icon-spread-left" v-else></i>
          </button>
          <h2 class="page-title">{{ currentTitle }}</h2>
        </div>
        
        <div class="header-right">
          <div class="user-info" @click="showUserMenu = !showUserMenu">
            <div class="user-avatar">
              {{ userInitial }}
            </div>
            <span class="user-name">{{ userName }}</span>
            <i class="layui-icon layui-icon-down"></i>
            
            <!-- 用户下拉菜单 -->
            <div class="user-menu" v-if="showUserMenu">
              <router-link to="/profile" class="menu-option">
                <i class="layui-icon layui-icon-user"></i>
                个人中心
              </router-link>
              <div class="menu-option" @click="handleLogout">
                <i class="layui-icon layui-icon-logout"></i>
                退出登录
              </div>
            </div>
          </div>
        </div>
      </header>
      
      <!-- 内容区 -->
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const sidebarCollapsed = ref(false)
const showUserMenu = ref(false)

const userName = ref(localStorage.getItem('realName') || localStorage.getItem('username') || '用户')

const userInitial = computed(() => {
  return userName.value.charAt(0).toUpperCase()
})

// 监听 localStorage 变化，实现头部用户信息同步
const updateUserName = () => {
  userName.value = localStorage.getItem('realName') || localStorage.getItem('username') || '用户'
}

// 为了让个人中心修改后能即时同步，暴露出方法
const refreshUserInfo = () => {
  updateUserName()
}
window.refreshHeaderUserInfo = refreshUserInfo

const isAdmin = computed(() => {
  return parseInt(localStorage.getItem('role') || '2') === 0
})

const currentTitle = computed(() => {
  return route.meta.title || '仪表盘'
})

const handleLogout = () => {
  layui.layer.confirm('确定要退出登录吗？', {
    btn: ['确定', '取消'],
    icon: 3
  }, () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('realName')
    localStorage.removeItem('role')
    layui.layer.closeAll()
    router.push('/login')
  })
}

// 点击其他地方关闭菜单
const handleClickOutside = (e) => {
  if (!e.target.closest('.user-info')) {
    showUserMenu.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  window.addEventListener('storage', updateUserName)
  if (typeof layui !== 'undefined') {
    layui.use(['layer'], function() {})
  }
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  window.removeEventListener('storage', updateUserName)
})

// 路由变化时关闭菜单
watch(route, () => {
  showUserMenu.value = false
})
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* 侧边栏 */
.sidebar {
  width: 240px;
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
  color: white;
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 100;
}

.sidebar.collapsed {
  width: 70px;
}

.sidebar.collapsed .sidebar-header span,
.sidebar.collapsed .menu-item span {
  display: none;
}

.sidebar-header {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header i {
  font-size: 28px;
  color: #60a5fa;
}

.sidebar-header span {
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
}

.sidebar-menu {
  flex: 1;
  padding: 16px 12px;
  overflow-y: auto;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  color: rgba(255, 255, 255, 0.7);
  border-radius: 10px;
  margin-bottom: 6px;
  transition: all 0.3s ease;
  text-decoration: none;
}

.menu-item:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.menu-item.active {
  color: white;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.menu-item i {
  font-size: 20px;
  width: 24px;
  text-align: center;
}

.menu-item span {
  font-size: 14px;
  white-space: nowrap;
}

/* 主容器 */
.main-container {
  flex: 1;
  margin-left: 240px;
  display: flex;
  flex-direction: column;
  transition: margin-left 0.3s ease;
}

.sidebar.collapsed + .main-container {
  margin-left: 70px;
}

/* 顶部导航 */
.header {
  height: 64px;
  background: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.toggle-btn {
  width: 36px;
  height: 36px;
  border: none;
  background: #f5f7fa;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.toggle-btn:hover {
  background: #e8eaed;
}

.toggle-btn i {
  font-size: 18px;
  color: #666;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.user-info:hover {
  background: #f5f7fa;
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 14px;
}

.user-name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.user-info > i {
  font-size: 12px;
  color: #999;
}

.user-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
  min-width: 160px;
  overflow: hidden;
  z-index: 100;
}

.menu-option {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  color: #333;
  text-decoration: none;
  transition: all 0.2s ease;
}

.menu-option:hover {
  background: #f5f7fa;
  color: #1890ff;
}

.menu-option i {
  font-size: 16px;
}

/* 内容区 */
.content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

/* 响应式 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
  }
  
  .sidebar.show {
    transform: translateX(0);
  }
  
  .main-container {
    margin-left: 0 !important;
  }
}
</style>
