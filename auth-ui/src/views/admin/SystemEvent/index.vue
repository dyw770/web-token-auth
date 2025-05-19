<template>
  <FaPageMain>
    <!-- 搜索区域 -->
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="searchRq.username" placeholder="请输入用户名" clearable/>
        </el-form-item>

        <el-form-item label="操作事件" prop="operationEvent">
          <el-input v-model="searchRq.operationEvent" placeholder="请输入操作事件" clearable/>
        </el-form-item>

        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="searchRq.startTime"
            type="datetime"
            placeholder="请选择开始时间"
            format="YYYY/MM/DD HH:mm:ss"
            :value-format="dateTimeFormat"
            :disabled-date="canStartTime"
            :arrow-control="true"
            clearable
          />
        </el-form-item>

        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="searchRq.endTime"
            type="datetime"
            placeholder="请选择结束时间"
            :default-time="defaultEndTime"
            format="YYYY/MM/DD HH:mm:ss"
            :value-format="dateTimeFormat"
            :disabled-date="canEndTime"
            :arrow-control="true"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="logsList" border style="width: 100%" table-layout="auto" @sort-change="sort">
      <el-table-column prop="id" label="id"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="operationTime" label="操作时间" sortable="custom"/>
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
import type {SysSystemOperationLogRs, SystemOperationSearchRq} from '#/api'
import dayjs from "@/utils/dayjs.ts";

const dateTimeFormat = "YYYY-MM-DDTHH:mm:ss"
const defaultEndTime: Date = new Date(2000, 1, 1, 23, 59, 59)

const searchRq = ref<SystemOperationSearchRq>({
  page: 1,
  size: 10
})

const total = ref(0)

const logsList = ref<SysSystemOperationLogRs[]>([])

const canStartTime = (date: Date) :boolean => {
  if (date.getTime() > Date.now()) {
    return true
  }
  if (searchRq.value.endTime) {
    // 如果已经选择了开始时间, 则小于开始时间的时间不可选
    const endTime = dayjs(searchRq.value.endTime, dateTimeFormat)
    const startTime = dayjs(date)
    if (endTime.isBefore(startTime)) {
      return true
    }
  }
  return false
}

const canEndTime = (date: Date) => {
  if (date.getTime() > Date.now()) {
    return true
  }
  if (searchRq.value.startTime) {
    // 如果已经选择了开始时间, 则小于开始时间的时间不可选
    const startTime = dayjs(searchRq.value.startTime, dateTimeFormat)
    const endTime = dayjs(date)
    if (endTime.isBefore(startTime)) {
      return true
    }
  }
  return false
}

const sort = (data: {column: any, prop: string, order: any }) => {
  searchRq.value.orders  = [{
    column: data.prop,
    asc: data.order === 'ascending'
  }]
  refresh()
}


const resetSearch = () => {
  searchRq.value.username = undefined
  searchRq.value.operationEvent = undefined
  searchRq.value.startTime = undefined
  searchRq.value.endTime = undefined
  refresh()
}

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
