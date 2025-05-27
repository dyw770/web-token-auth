<template>
  <FaPageMain>

    <!-- 搜索区域 -->
    <div class="p-4">
      <el-form :model="searchRq" label-width="80px" inline @submit.prevent="refresh">
        <el-form-item label="权限ID" prop="permissionId">
          <el-input v-model="searchRq.permissionId" placeholder="请输入权限ID" clearable/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="openAddDialog">新增权限</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 权限表格 -->
    <el-table :data="permissionData" border style="width: 100%" table-layout="auto">
      <el-table-column prop="permissionId" label="权限ID" align="center"/>
      <el-table-column prop="permissionDesc" label="权限说明" align="center"/>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary" @click="openEditDialog(row)" size="small">编辑</el-button>
            <el-button type="danger" @click="deletePermission(row.permissionId)" size="small">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :total="total"
                   v-model:current-page="searchRq.page"
                   v-model:page-size="searchRq.size"
                   :page-sizes="[10, 50, 100]"
                   @change="refresh"
                   class="mt-5"/>

    <!-- 新增 编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑权限' : '新增权限'">
      <el-form ref="formRef" :model="form" label-width="100px" @submit.prevent="submitForm">
        <el-form-item label="权限ID" prop="permissionId" required>
          <el-input v-model="form.permissionId" :disabled="isEdit"/>
        </el-form-item>
        <el-form-item label="权限说明" prop="permissionDesc" required>
          <el-input v-model="form.permissionDesc"/>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" native-type="submit">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </FaPageMain>
</template>

<script setup lang="ts">
import type {Permission} from "#/api";
import adminApi from "@/api/modules/admin.ts";
import {ElMessageBox} from "element-plus";

const permissionData = ref<Permission.PermissionRs[]>([])
const searchRq = ref<Permission.PermissionSearchRq>({
  page: 1,
  size: 10
})

const total = ref(0)

// 弹窗相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<Permission.PermissionSaveRq>({
  permissionId: '',
  permissionDesc: ''
})

// 打开新增弹窗
const openAddDialog = () => {
  isEdit.value = false
  form.value = {
    permissionId: '',
    permissionDesc: ''
  }
  dialogVisible.value = true
}

// 打开编辑弹窗
const openEditDialog = (row: Permission.PermissionRs) => {
  isEdit.value = true
  form.value = {
    permissionId: row.permissionId,
    permissionDesc: row.permissionDesc
  }
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  try {
    if (isEdit.value) {
      await adminApi.updatePermission(form.value)
    } else {
      await adminApi.addPermission(form.value)
    }
    dialogVisible.value = false
    await refresh()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

// 删除权限
const deletePermission = async (permissionId: string) => {
  try {
    await ElMessageBox.confirm(
      `确认删除权限<strong class="text-red-500">${permissionId}</strong>?`,
      '删除角色',
      {
        distinguishCancelAndClose: true,
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消',
      }
    )
    await adminApi.deletePermission(permissionId)
    await refresh()
  } catch (error) {

  }
}

const refresh = async () => {
  const {data} = await adminApi.permissionList(searchRq.value)
  permissionData.value = data.records
  total.value = data.total
}

const resetSearch = () => {
  searchRq.value.permissionId = undefined
  refresh()
}

onMounted(async () => {
  await refresh()
})
</script>

<style scoped>

</style>
