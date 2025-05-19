<template>
  <FaPageMain>
    <!-- 搜索区域 -->
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="searchRq.username" placeholder="请输入用户名" clearable/>
        </el-form-item>

        <el-form-item label="访问IP" prop="apiAccessIp">
          <el-input v-model="searchRq.apiAccessIp" placeholder="请输入访问IP" clearable/>
        </el-form-item>

        <el-form-item label="访问URL" prop="apiPath">
          <el-input v-model="searchRq.apiPath" placeholder="请输入访问URL" clearable/>
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
      <el-table-column prop="apiPath" label="API路径" show-overflow-tooltip sortable="custom"/>
      <el-table-column prop="apiMethod" label="方法"/>
      <el-table-column prop="apiAccessTime" label="访问时间" sortable="custom" />
      <el-table-column prop="apiAccessDuration" label="耗时(ms)"/>
      <el-table-column prop="apiAccessIp" label="IP地址" sortable="custom" />
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
import type {ApiAccessLogSearchRq, SysApiAccessLogRs} from '#/api'
import dayjs from "@/utils/dayjs.ts";

const dateTimeFormat = "YYYY-MM-DDTHH:mm:ss"
const defaultEndTime: Date = new Date(2000, 1, 1, 23, 59, 59)

const searchRq = ref<ApiAccessLogSearchRq>({
  page: 1,
  size: 10
})

const total = ref(0)

const logsList = ref<SysApiAccessLogRs[]>([])

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

const resetSearch = () => {
  searchRq.value.username = undefined
  searchRq.value.apiAccessIp = undefined
  searchRq.value.startTime = undefined
  searchRq.value.endTime = undefined
  searchRq.value.apiPath = undefined
}

const sort = (data: {column: any, prop: string, order: any }) => {
  searchRq.value.orders  = [{
    column: data.prop,
    asc: data.order === 'ascending'
  }]
  refresh()
}

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
