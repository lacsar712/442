import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      showMessage(res.message || '请求失败', 'error')
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('userId')
        localStorage.removeItem('username')
        localStorage.removeItem('role')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    let message = '网络错误，请稍后重试'
    
    if (error.response) {
      // 优先使用后端返回的错误信息
      const data = error.response.data
      if (data && data.message) {
        message = data.message
      } else if (data && data.msg) {
        message = data.msg
      } else {
        // 如果没有后端消息，使用默认消息
        switch (error.response.status) {
          case 400:
            message = '请求参数错误'
            break
          case 401:
            message = '未登录或登录已过期'
            localStorage.removeItem('token')
            setTimeout(() => { window.location.href = '/login' }, 1500)
            break
          case 403:
            message = '没有权限访问'
            break
          case 404:
            message = '请求的资源不存在'
            break
          case 500:
            message = '服务器内部错误'
            break
        }
      }
    }
    
    showMessage(message, 'error')
    return Promise.reject(error)
  }
)

// 消息提示函数
function showMessage(msg, type = 'info') {
  if (typeof layui !== 'undefined' && layui.layer) {
    const icon = type === 'success' ? 1 : type === 'error' ? 2 : type === 'warning' ? 3 : 0
    layui.layer.msg(msg, { icon, time: 2500 })
  } else {
    // fallback to alert
    if (type === 'error') {
      alert('错误: ' + msg)
    } else {
      console.log(`[${type}] ${msg}`)
    }
  }
}

export default api
export { showMessage }
