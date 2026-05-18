import api from './index'

// 获取班级列表
export const getClassList = (params) => api.get('/classes', { params })

// 获取所有班级
export const getAllClasses = () => api.get('/classes/all')

// 获取班级详情
export const getClassById = (id) => api.get(`/classes/${id}`)

// 新增班级
export const addClass = (data) => api.post('/classes', data)

// 更新班级
export const updateClass = (id, data) => api.put(`/classes/${id}`, data)

// 删除班级
export const deleteClass = (id) => api.delete(`/classes/${id}`)

// 导出班级
export const exportClasses = () => {
  return api.get('/classes/export', { responseType: 'blob' })
}
