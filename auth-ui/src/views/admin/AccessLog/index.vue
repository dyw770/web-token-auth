<template>
  <FaPageMain>
    <el-table :data="logsList" border style="width: 100%" table-layout="auto">
      <el-table-column prop="id" label="id"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="apiPath" label="API路径" show-overflow-tooltip/>
      <el-table-column prop="apiMethod" label="方法"/>
      <el-table-column prop="apiAccessTime" label="访问时间" width="160"/>
      <el-table-column prop="apiAccessDuration" label="耗时(ms)"/>
      <el-table-column prop="apiAccessIp" label="IP地址" width="140"/>
      <el-table-column prop="apiAccessUa" label="UA" show-overflow-tooltip/>
      <el-table-column prop="apiAccessResultType" label="结果类型"/>
      <el-table-column prop="apiAccessResultCode" label="结果码"/>
      <el-table-column prop="apiAccessResponseCode" label="响应码"/>
    </el-table>
    <el-pagination :total="total"
                   v-model:current-page="searchRq.page"
                   v-model:page-size="searchRq.size"
                   :page-sizes="[10, 50, 100]"
                   layout="prev, pager, next, jumper, sizes, ->, total"
                   @change="refresh"
                   class="mt-5"/>
  </FaPageMain>
</template>

<script setup lang="ts">

import adminApi from '@/api/modules/admin.ts'
import type {PageRq, SysApiAccessLogRs} from '#/api'

const searchRq = ref<PageRq>({
  page: 1,
  size: 10
})

const total = ref(0)

const logsList = ref<SysApiAccessLogRs[]>([])

onMounted(async () => {
  await refresh()
})

const refresh = async () => {
  const {data} = await adminApi.apiAccessLogList(searchRq.value)
  logsList.value = data.records
  total.value = data.total
}

</script>

<style scoped>


</style>
