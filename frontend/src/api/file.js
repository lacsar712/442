import axios from 'axios'

// 文件上传
export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return axios.post('/api/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
}

// 下载文件 (带认证)
export const downloadFile = async (filePath, fileName) => {
  try {
    const response = await axios.get('/api/files/download', {
      params: { filePath, fileName },
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    // 创建下载链接
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fileName || 'attachment')
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('下载失败:', error)
    if (typeof layui !== 'undefined' && layui.layer) {
      layui.layer.msg('下载失败', { icon: 2 })
    } else {
      alert('下载失败')
    }
  }
}
