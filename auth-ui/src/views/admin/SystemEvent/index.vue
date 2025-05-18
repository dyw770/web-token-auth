<template>
  <FaPageMain>
    <el-table :data="logsList" border style="width: 100%" table-layout="auto">
      <el-table-column prop="id" label="id"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="operationTime" label="操作时间"/>
      <el-table-column prop="operationIp" label="IP地址"/>
      <el-table-column prop="operationUa" label="用户客户端"/>
      <el-table-column prop="operationResultType" label="操作结果"/>
      <el-table-column prop="operationResultCode" label="操作结果码"/>
      <el-table-column prop="operationEvent" label="操作事件" show-overflow-tooltip/>
      <el-table-column prop="operationContent" label="操作内容" show-overflow-tooltip/>
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
import type {PageRq, SysSystemOperationLogRs} from '#/api'

const searchRq = ref<PageRq>({
  page: 1,
  size: 10
})

const total = ref(0)

const logsList = ref<SysSystemOperationLogRs[]>([])

onMounted(async () => {
  await refresh()
})

const refresh = async () => {
  const {data} = await adminApi.systemEventLogList(searchRq.value)
  logsList.value = data.records
  total.value = data.total
}

</script>

<style scoped>


</style>
