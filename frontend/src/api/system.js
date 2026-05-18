import api from './index'

// 获取系统配置
export const getConfigs = () => api.get('/system/configs')

// 更新系统配置
export const updateConfigs = (data) => api.put('/system/configs', data)

// 获取操作日志
export const getLogs = (params) => api.get('/system/logs', { params })

// 清理日志
export const cleanLogs = (days) => api.delete('/system/logs', { params: { days } })
