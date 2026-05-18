import api from './index'

// 获取活动列表
export const getActivityList = (params) => api.get('/activities', { params })

// 获取最近活动
export const getRecentActivities = (limit = 5) => api.get('/activities/recent', { params: { limit } })

// 获取统计数据
export const getStatistics = () => api.get('/activities/statistics')

// 获取活动详情
export const getActivityById = (id) => api.get(`/activities/${id}`)

// 新增活动
export const addActivity = (data) => api.post('/activities', data)

// 更新活动
export const updateActivity = (id, data) => api.put(`/activities/${id}`, data)

// 删除活动
export const deleteActivity = (id) => api.delete(`/activities/${id}`)

// 导出活动
export const exportActivities = (params) => {
  return api.get('/activities/export', { params, responseType: 'blob' })
}
