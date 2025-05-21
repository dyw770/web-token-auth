<template>
  <FaPageMain>
    <div>
      修改按钮权限后需要刷新才能生效
    </div>
    <div v-auth="permissions.simple.read">
      <el-button>读权限</el-button>
    </div>
    <div v-auth="permissions.simple.write">
      <el-button>写权限</el-button>
    </div>
    <div v-auth="permissions.simple.delete">
      <el-button>删除权限</el-button>
    </div>
  </FaPageMain>
</template>

<script setup lang="ts">
import permissions from '@/permission/permissions.ts'
</script>

<style scoped>

</style>
