<script setup lang="ts">
import {onMounted, ref} from 'vue';
import Admin from "@/api/modules/admin.ts";
import FaIcon from '@/ui/components/FaIcon/index.vue';
import useUserStore from "@/store/modules/user.ts";
import {joinPaths} from "@/utils/url";

// 默认显示100行日志
const defaultLogLine = ref(100);
const logfile = ref('暂无日志');
const loading = ref(false);

// 刷新日志
const refresh = async () => {
  try {
    loading.value = true;
    const response = await Admin.logfile(defaultLogLine.value);
    logfile.value = response.data;
  } catch (error) {
    console.error('获取日志失败:', error);
  } finally {
    loading.value = false;
  }
};


// 下载日志
const downloadLog = () => {
  try {
    const userStore = useUserStore();
    // 获取API基础路径，考虑开发环境下的代理配置
    const baseURL = import.meta.env.VITE_APP_API_BASEURL;

    // 构造包含token参数的下载URL
    const url = joinPaths(baseURL, '/admin/logging/file?token=' + userStore.token);
    console.log(url)
    // 创建a标签并触发下载
    const link = document.createElement('a');
    link.href = url;
    link.target = '_blank';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error('下载日志失败:', error);
  }
};

// 组件挂载时刷新日志
onMounted(() => {
  refresh();
});
</script>

<template>
  <FaPageMain>
    <div class="log-container">
      <el-card shadow="hover" class="log-card">
        <template #header>
          <div class="card-header">
            <h2 class="card-title">系统日志</h2>
            <el-button type="success" @click="downloadLog" :disabled="!logfile || logfile === '暂无日志'">
              下载日志
            </el-button>
          </div>
        </template>

        <div class="log-controls">
          <el-form :inline="true" size="small">
            <el-form-item label="显示行数：">
              <el-input-number
                v-model="defaultLogLine"
                :min="10"
                :max="1000"
                :step="10"
                class="line-input"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="refresh" :loading="loading">
                <FaIcon name="i-ep:refresh" class="mr-1"/>
                刷新
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="log-content">
          <div
            v-text="logfile"
            class="log-display"
            :class="{'loading': loading}"
          ></div>
        </div>
      </el-card>
    </div>
  </FaPageMain>
</template>

<style scoped>
.log-container {
  padding: 16px;
}

.log-card {
  max-width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.log-controls {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.line-input {
  width: 120px;
}

.log-content {
  margin-top: 16px;
}

/* 日志显示样式 - 深色主题 */
.log-display {
  font-family: 'Courier New', Courier, monospace;
  font-size: 14px;
  line-height: 1.6;
  background-color: #1e1e1e;
  color: #d4d4d4;
  padding: 16px;
  border-radius: 4px;
  border: 1px solid #3c3c3c;
  min-height: 400px;
  max-height: 600px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-all;
}

/* 日志级别颜色 */
.log-display :deep(.log-level-debug) {
  color: #6a9955;
}

.log-display :deep(.log-level-info) {
  color: #569cd6;
}

.log-display :deep(.log-level-warn) {
  color: #ce9178;
}

.log-display :deep(.log-level-error) {
  color: #f44747;
}

.log-display :deep(.log-level-fatal) {
  color: #ff0000;
}

/* 加载状态 */
.log-display.loading {
  opacity: 0.7;
}

/* 滚动条样式 */
.log-display::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.log-display::-webkit-scrollbar-track {
  background: #2d2d2d;
  border-radius: 4px;
}

.log-display::-webkit-scrollbar-thumb {
  background: #555;
  border-radius: 4px;
}

.log-display::-webkit-scrollbar-thumb:hover {
  background: #777;
}
</style>
