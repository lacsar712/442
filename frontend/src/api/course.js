import api from './index'

// 获取课程列表
export const getCourseList = (params) => api.get('/courses', { params })

// 获取所有课程
export const getAllCourses = () => api.get('/courses/all')

// 获取课程详情
export const getCourseById = (id) => api.get(`/courses/${id}`)

// 新增课程
export const addCourse = (data) => api.post('/courses', data)

// 更新课程
export const updateCourse = (id, data) => api.put(`/courses/${id}`, data)

// 删除课程
export const deleteCourse = (id) => api.delete(`/courses/${id}`)

// 导出课程
export const exportCourses = () => {
  return api.get('/courses/export', { responseType: 'blob' })
}
