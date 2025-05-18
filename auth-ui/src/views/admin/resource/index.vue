<template>
  <FaPageMain>

    <!-- 搜索区域 -->
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="请求路径">
          <el-input v-model="searchRq.apiPath" placeholder="请输入请求路径" clearable/>
        </el-form-item>

        <el-form-item label="请求方法">
          <el-select v-model="searchRq.apiMethod" placeholder="请选择请求方法" clearable class="w-160px">
            <el-option
              v-for="method in ApiMethod"
              :key="method"
              :label="method"
              :value="method"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="searchRq.description" placeholder="请输入描述" clearable/>
        </el-form-item>

        <el-form-item label="匹配类型">
          <el-select v-model="searchRq.matchType" placeholder="请选择匹配类型" clearable class="w-120px">
            <el-option
              v-for="type in MatchType"
              :key="type"
              :label="type"
              :value="type"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="showAddDialog = true">新增资源</el-button>
          <el-button type="warning" @click="refreshAuth">刷新资源授权</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据展示 -->
    <el-table :data="resourceList" border table-layout="auto" style="width: 100%">
      <el-table-column prop="id" label="资源ID" align="center"/>
      <el-table-column prop="apiPath" label="请求路径" align="center"/>
      <el-table-column prop="apiMethod" label="请求方法" align="center"/>
      <el-table-column prop="description" label="描述" align="center"/>
      <el-table-column prop="enable" label="是否启用" align="center">
        <template #default="{ row }">
          <el-switch v-model="row.enable" :before-change="confirmEnableResource(row)" @change="enableResource(row)"/>
        </template>
      </el-table-column>
      <el-table-column prop="matchType" label="匹配类型" align="center">
        <template #default="{ row }">
          {{ row.matchType === 'REGEX' ? '正则' : 'ANT' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column prop="updateTime" label="更新时间" align="center"/>
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button-group>
            <el-button size="small" @click="authResource(row)">授权</el-button>
            <el-button size="small" type="primary" @click="showEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteResource(row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :total="total"
                   :default-current-page="searchRq.page"
                   :page-size="searchRq.size"
                   :page-sizes="[10, 50, 100]"
                   @change="refresh"
                   class="mt-5"/>

    <add v-model="showAddDialog" @success="refresh"/>
    <edit :resource="editResource" v-model="showEditDialog" @success="refresh"/>
  </FaPageMain>
</template>

<script setup lang="ts">

import adminApi from '@/api/modules/admin.ts'
import type {Resource} from '#/api'
import {ApiMethod, MatchType} from "#/enums.ts"
import {ElMessageBox} from "element-plus";
import Add from "@/views/admin/resource/add.vue";
import {toast} from "vue-sonner";
import Edit from "@/views/admin/resource/edit.vue";
import {useRouter} from 'vue-router'

defineOptions({
  name: 'AdminResourceIndex',
})

const showAddDialog = ref(false)
const showEditDialog = ref(false)

const editResource = ref<Resource.ResourceListRs>()

const searchRq = ref<Resource.ResourceSearchRq>({
  page: 1,
  size: 10
})

const showEdit = (resource: Resource.ResourceListRs) => {
  editResource.value = resource
  showEditDialog.value = true
}

const total = ref(0)

const resourceList = ref<Resource.ResourceListRs[]>([])

const resetSearch = () => {
  searchRq.value.apiPath = undefined
  searchRq.value.apiMethod = undefined
  searchRq.value.description = undefined
  searchRq.value.matchType = undefined
  refresh()
}

const deleteResource = async (resource: Resource.ResourceListRs) => {
  await ElMessageBox.confirm(
    `确认删除系统API资源?`,
    `删除统API资源`,
    {
      distinguishCancelAndClose: true,
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  )
  await adminApi.resourceDelete(resource.id)
  toast.success("删除系统API资源成功")
  await refresh()
}

const enableResource = async (resource: Resource.ResourceListRs) => {
  await adminApi.resourceEnable(resource.enable, resource.id)
}

const router = useRouter()

const authResource = (resource: Resource.ResourceListRs) => {
  router.push({
    name: 'adminResourceAuth',
    params: {
      id: resource.id
    }
  })
}

const refreshAuth = async () => {
  try {
    await ElMessageBox.confirm(
      `确认刷新资源授权?`,
      `刷新资源授权`,
      {
        distinguishCancelAndClose: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消',
      }
    )

    await adminApi.resourceAuthRefresh()
    toast.success("系统资源授权刷新成功")
  } catch (err) {
    toast.error("系统资源授权刷新失败")
  }
}

const confirmEnableResource = (resource: Resource.ResourceListRs) => {
  const txt = resource.enable ? '禁用' : '启用'
  return () =>
    ElMessageBox.confirm(
      `确认${txt}系统API资源?`,
      `${txt}系统API资源`,
      {
        distinguishCancelAndClose: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消',
      }
    )
      .then(() => Promise.resolve(true))
      .catch(() => Promise.reject(false))
}

onMounted(async () => {
  await refresh()
})

const refresh = async () => {
  const {data} = await adminApi.resourceList(searchRq.value)
  resourceList.value = data.records
  total.value = data.total
}


</script>

<style scoped>


</style>
