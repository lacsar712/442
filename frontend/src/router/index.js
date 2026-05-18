import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/UserManagement.vue'),
        meta: { title: '用户管理', roles: [0] }
      },
      {
        path: 'classes',
        name: 'Classes',
        component: () => import('../views/ClassManagement.vue'),
        meta: { title: '班级管理' }
      },
      {
        path: 'courses',
        name: 'Courses',
        component: () => import('../views/CourseManagement.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'activities',
        name: 'Activities',
        component: () => import('../views/ActivityManagement.vue'),
        meta: { title: '课堂活动' }
      },
      {
        path: 'config',
        name: 'Config',
        component: () => import('../views/SystemConfig.vue'),
        meta: { title: '系统配置', roles: [0] }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('../views/OperationLogs.vue'),
        meta: { title: '操作日志', roles: [0] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { title: '个人中心' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userRole = parseInt(localStorage.getItem('role') || '2')
  
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
    return
  }
  
  if (to.path === '/login' && token) {
    next('/dashboard')
    return
  }
  
  // 检查角色权限
  if (to.meta.roles && !to.meta.roles.includes(userRole)) {
    next('/dashboard')
    return
  }
  
  next()
})

export default router
