<route lang="yaml">
meta:
  title: 导航2-2-2
</route>

<template>
  <div>
    <FaPageMain>
      <ol class="horizontal-list">
        <li v-for="item in data.collections" :key="item" @click="changeIcon(item)">{{item}}</li>
      </ol>

      <div>
        <FaIcon :name="item" v-for="item in icon " :key="item" @click="copy(item)"/>
      </div>
    </FaPageMain>
  </div>
</template>

<script setup lang="ts">

import {listIcons} from '@iconify/vue';

import data from '@/iconify/index.json'
import {toast} from "vue-sonner";

const icon = ref<string[]>();

const changeIcon = (item: string) => {
  icon.value = listIcons('', item);
};

const copy =  async (icon: string) => {
  await navigator.clipboard.writeText(icon)

  toast.info('复制成功')
};
</script>

<style scoped>
.horizontal-list {
  display: flex;            /* 使用 Flex 布局 */
  list-style: none;         /* 移除默认的序号样式 */
  padding: 0;
  margin: 0;
  gap: 10px;                /* 列表项之间留出间距 */
}

.horizontal-list li {
  cursor: pointer;
}
</style>
