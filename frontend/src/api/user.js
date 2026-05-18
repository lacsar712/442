import api from './index'

// 登录
export const login = (data) => api.post('/auth/login', data)

// 注册
export const register = (data) => api.post('/auth/register', data)

// 获取当前用户信息
export const getProfile = () => api.get('/users/profile')

// 更新个人信息
export const updateProfile = (data) => api.put('/users/profile', data)

// 修改密码
export const updatePassword = (data) => api.put('/users/password', data)

// 获取用户列表
export const getUserList = (params) => api.get('/users', { params })

// 获取教师列表
export const getTeachers = () => api.get('/users/teachers')

// 获取用户详情
export const getUserById = (id) => api.get(`/users/${id}`)

// 新增用户
export const addUser = (data) => api.post('/users', data)

// 更新用户
export const updateUser = (id, data) => api.put(`/users/${id}`, data)

// 更新用户状态
export const updateUserStatus = (id, status) => api.put(`/users/${id}/status`, null, { params: { status } })

// 删除用户
export const deleteUser = (id) => api.delete(`/users/${id}`)
