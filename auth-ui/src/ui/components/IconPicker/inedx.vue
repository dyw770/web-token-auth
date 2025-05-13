<template>
  <el-popover v-bind="props" :width="400" ref="popoverRef">
    <template #reference>
      <el-button v-if="!icon">请选择</el-button>
      <span  v-else>
        <FaIcon :name="icon" class="text-8"/>
        <span>{{ icon }}</span>
      </span>
    </template>
    <template #default>
      <div class="flex flex-wrap w-400px h-400px overflow-scroll scrollbar-hide gap-2">
        <FaIcon class="text-8" :name="item" v-for="item in icons " :key="item" @click="picker(item)"/>
      </div>
    </template>
  </el-popover>
</template>

<script setup lang="ts">
import {popoverEmits, type PopoverInstance, popoverProps} from 'element-plus'
import {listIcons} from '@iconify/vue';

defineOptions({
  name: 'IconPicker'
})

const props = defineProps(popoverProps)
const emit = defineEmits(popoverEmits)

const popoverRef = ref<PopoverInstance>()

const icon = defineModel({required: true, type: String})

const icons = listIcons('', 'ant-design')

const picker = (item: string) => {
  icon.value = item
  popoverRef.value?.hide()
}
</script>

<style scoped>
.scrollbar-hide {
  -ms-overflow-style: none; /* IE 和 Edge 隐藏滚动条 */
  scrollbar-width: none; /* Firefox 隐藏滚动条 */
}

/* 可选：Chrome、Edge 等浏览器 */
.scrollbar-hide::-webkit-scrollbar {
  display: none;
}
</style>
