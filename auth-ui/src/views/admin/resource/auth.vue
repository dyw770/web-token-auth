<template>
  <FaPageMain>
    <el-page-header @back="goBack">
      <template #content>
      <span class="text-large font-600 mr-3">
            资源授权: {{currentResource?.apiPath}}
      </span>
      </template>
      <template #extra>
        <el-button type="primary" @click="showAddDialog">
          <FaIcon  name="ant-design:plus-outlined"/>
          新增授权
        </el-button>
      </template>
    </el-page-header>

    <div class="mt-10">
      <el-table :data="resourceAuthList" border table-layout="auto" style="width: 100%">
        <el-table-column prop="authType" label="授权类型">
          <template #default="{ row }">
            {{ formatAuthType(row) }}
          </template>
        </el-table-column>
        <el-table-column prop="authObject" label="授权对象" >
          <template #default="{ row }">
            {{ formatAuthObject(row)  }}
          </template>
        </el-table-column>
        <el-table-column prop="authTime" label="授权时间" />
        <el-table-column label="操作" align="center" width="320px">
          <template #default="{ row }">
            <el-button-group>
              <el-button type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="deleteResourceAuth(row)">
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑授权对话框 -->
    <el-dialog v-model="addDialogVisible" :title="isEdit ? '编辑授权' : '新增授权'" width="500px">
      <AuthObject v-model="addForm" @submit="submitAuth" @cancel="hideAddDialog"/>
    </el-dialog>
  </FaPageMain>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin.ts'
import {useRoute, useRouter} from 'vue-router'
import type {Resource} from "#/api";
import {AuthType, authType, staticAuth} from "#/enums.ts";
import {toast} from "vue-sonner";
import AuthObject from "./AuthObject/index.vue"

const route = useRoute()
const router = useRouter()

const currentResource = ref<Resource.ResourceListRs>()

const getResourceId = () => {
  return Number.parseInt(route.params.id as string)
}

const resourceAuthList = ref<Resource.ResourceAuthRs[]>()

// 是否是编辑状态
const isEdit = ref(false)

// 当前编辑的授权项
const currentEditAuth = ref<Resource.ResourceAuthRs | null>(null)

// 显示编辑对话框
const showEditDialog = (row: Resource.ResourceAuthRs) => {
  currentEditAuth.value = row
  addForm.value = {
    apiResourceId: row.apiResourceId,
    authType: row.authType,
    authObject: row.authObject
  }
  isEdit.value = true
  addDialogVisible.value = true
}

// 新增授权相关
const addDialogVisible = ref(false)
const addForm = ref<Resource.ResourceAuthAddRq>({
  apiResourceId: getResourceId(),
  authType: AuthType.ROLE,
  authObject: '',
})

// 显示新增对话框
const showAddDialog = () => {
  addDialogVisible.value = true
}

// 隐藏对话框并重置状态
const hideAddDialog = () => {
  addForm.value = {
    apiResourceId: getResourceId(),
    authType: AuthType.ROLE,
    authObject: ''
  }
  currentEditAuth.value = null
  isEdit.value = false
  addDialogVisible.value = false
}

const formatAuthObject = (auth: Resource.ResourceAuthRs) => {
  if (auth.authType === AuthType.STATIC) {
    return staticAuth[auth.authObject]
  }
  return auth.authObject
}

const formatAuthType = (auth: Resource.ResourceAuthRs) => {
  const type = authType.find((item) => item.type === auth.authType)
  if (type) {
    return type.label
  }
  return auth.authType
}

const deleteResourceAuth = async (auth: Resource.ResourceAuthRs) => {
  try {
    await adminApi.resourceDeleteAuth(auth.authId)
    toast.success('删除授权成功')
    await refresh()
  } catch (error) {
    toast.error('删除授权失败')
  }
}

// 提交新增或编辑
const submitAuth = async () => {
  try {
    if (isEdit.value && currentEditAuth.value) {
      // 编辑模式
      const updateData: Resource.ResourceAuthUpdateRq = {
        ...addForm.value,
        authId: currentEditAuth.value.authId
      }
      await adminApi.resourceUpdateAuth(updateData)
      toast.success('授权更新成功')
    } else {
      // 新增模式
      await adminApi.resourceAddAuth(addForm.value)
      toast.success('授权添加成功')
    }

    await refresh()
    hideAddDialog()
  } catch (error) {
    toast.error(isEdit.value ? '编辑授权失败' : '添加授权失败')
  }
}

const refresh = async () => {
  const id = route.params.id
  if (!id) return

  try {
    const res = await adminApi.resourceGet(getResourceId())
    currentResource.value = res.data

    const {data} = await adminApi.resourceAuthList(getResourceId())
    resourceAuthList.value = data
  } catch (error) {
    toast.error("加载资源授权失败")
  }
}

const goBack = () => {
  router.back()
}

watch(() => route.params.id, refresh, { immediate: true })

</script>

<style scoped>

</style>
