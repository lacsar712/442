<template>
  <div class="dashboard fade-in">
    <!-- 统计卡片 -->
    <div class="stat-grid">
      <div class="stat-card blue">
        <i class="layui-icon layui-icon-user stat-icon"></i>
        <div class="stat-info">
          <div class="stat-number">{{ stats.totalUsers || 0 }}</div>
          <div class="stat-label">用户总数</div>
        </div>
      </div>
      
      <div class="stat-card green">
        <i class="layui-icon layui-icon-group stat-icon"></i>
        <div class="stat-info">
          <div class="stat-number">{{ stats.classCount || 0 }}</div>
          <div class="stat-label">班级数量</div>
        </div>
      </div>
      
      <div class="stat-card orange">
        <i class="layui-icon layui-icon-read stat-icon"></i>
        <div class="stat-info">
          <div class="stat-number">{{ stats.courseCount || 0 }}</div>
          <div class="stat-label">课程数量</div>
        </div>
      </div>
      
      <div class="stat-card purple">
        <i class="layui-icon layui-icon-note stat-icon"></i>
        <div class="stat-info">
          <div class="stat-number">{{ stats.activityCount || 0 }}</div>
          <div class="stat-label">课堂活动</div>
        </div>
      </div>
    </div>
    
    <!-- 内容区 -->
    <div class="dashboard-content">
      <!-- 最近活动 -->
      <div class="card recent-activities">
        <div class="card-header">
          <h3 class="card-title">
            <i class="layui-icon layui-icon-date"></i>
            最近课堂活动
          </h3>
          <router-link to="/activities" class="view-more">
            查看更多 <i class="layui-icon layui-icon-right"></i>
          </router-link>
        </div>
        
        <div class="activity-list" v-if="recentActivities.length > 0">
          <div class="activity-item" v-for="item in recentActivities" :key="item.id">
            <div class="activity-type" :class="item.activityType">
              {{ getTypeName(item.activityType) }}
            </div>
            <div class="activity-info">
              <div class="activity-title">{{ item.title }}</div>
              <div class="activity-meta">
                <span><i class="layui-icon layui-icon-group"></i> {{ item.className }}</span>
                <span><i class="layui-icon layui-icon-read"></i> {{ item.courseName }}</span>
                <span><i class="layui-icon layui-icon-time"></i> {{ formatDate(item.activityDate) }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="empty-state" v-else>
          <i class="layui-icon layui-icon-note"></i>
          <p>暂无课堂活动</p>
        </div>
      </div>
      
      <!-- 快捷操作 -->
      <div class="card quick-actions">
        <div class="card-header">
          <h3 class="card-title">
            <i class="layui-icon layui-icon-app"></i>
            快捷操作
          </h3>
        </div>
        
        <div class="action-grid">
          <router-link to="/classes" class="action-item">
            <i class="layui-icon layui-icon-group"></i>
            <span>班级管理</span>
          </router-link>
          
          <router-link to="/courses" class="action-item">
            <i class="layui-icon layui-icon-read"></i>
            <span>课程管理</span>
          </router-link>
          
          <router-link to="/activities" class="action-item">
            <i class="layui-icon layui-icon-note"></i>
            <span>课堂活动</span>
          </router-link>
          
          <router-link to="/profile" class="action-item">
            <i class="layui-icon layui-icon-user"></i>
            <span>个人中心</span>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStatistics, getRecentActivities } from '../api/activity'

const stats = ref({})
const recentActivities = ref([])

const loadData = async () => {
  try {
    const [statsRes, activitiesRes] = await Promise.all([
      getStatistics(),
      getRecentActivities(5)
    ])
    stats.value = statsRes.data
    recentActivities.value = activitiesRes.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const getTypeName = (type) => {
  const types = {
    lecture: '讲座',
    experiment: '实验',
    discussion: '讨论',
    exam: '考试',
    other: '其他'
  }
  return types[type] || '其他'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

/* 统计卡片网格 */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  border-radius: 16px;
  color: white;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.stat-card.blue {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
}

.stat-card.green {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.stat-card.orange {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.stat-card.purple {
  background: linear-gradient(135deg, #8b5cf6 0%, #6d28d9 100%);
}

.stat-icon {
  font-size: 42px;
  opacity: 0.9;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-top: 4px;
}

/* 内容区 */
.dashboard-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

/* 卡片样式 */
.card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-title i {
  color: #3b82f6;
}

.view-more {
  font-size: 13px;
  color: #3b82f6;
  display: flex;
  align-items: center;
  gap: 4px;
}

.view-more:hover {
  color: #1d4ed8;
}

/* 活动列表 */
.activity-list {
  padding: 8px 0;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  transition: background 0.2s ease;
}

.activity-item:hover {
  background: #f9fafb;
}

.activity-type {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

.activity-type.lecture {
  background: #dbeafe;
  color: #1d4ed8;
}

.activity-type.experiment {
  background: #d1fae5;
  color: #059669;
}

.activity-type.discussion {
  background: #fef3c7;
  color: #d97706;
}

.activity-type.exam {
  background: #fee2e2;
  color: #dc2626;
}

.activity-type.other {
  background: #e5e7eb;
  color: #4b5563;
}

.activity-info {
  flex: 1;
  min-width: 0;
}

.activity-title {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.activity-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #999;
}

.activity-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.activity-meta i {
  font-size: 14px;
}

/* 快捷操作 */
.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 20px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 24px 16px;
  background: #f9fafb;
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.3s ease;
}

.action-item:hover {
  background: #3b82f6;
  color: white;
  transform: translateY(-2px);
}

.action-item:hover i {
  color: white;
}

.action-item i {
  font-size: 28px;
  color: #3b82f6;
  transition: color 0.3s ease;
}

.action-item span {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.action-item:hover span {
  color: white;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 48px 20px;
  color: #999;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.4;
}

/* 响应式 */
@media (max-width: 1200px) {
  .stat-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .dashboard-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .stat-grid {
    grid-template-columns: 1fr;
  }
}
</style>
