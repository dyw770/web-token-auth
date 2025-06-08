<template>
  <div>
    <div class="mb-2">
    <span>
      菜单ID: {{menuId}}
    </span>
      <span>
     角色ID: {{roleCode}}
    </span>
    </div>
    <el-table
      v-loading="reload"
      :data="roleMenuPermissions?.menuPermissions"
      border
      style="width: 100%"
      table-layout="auto">
      <template #empty>
        请先为该菜单添加权限池
      </template>
      <el-table-column type="index" label="#" align="center"/>
      <el-table-column prop="permissionId" label="权限ID" align="center"/>
      <el-table-column prop="permissionDesc" label="权限说明" align="center" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template #default="{row}">
          <el-button-group>
            <el-button type="danger" size="small"
                       @click="deleteRoleMenuPermission(row.permissionId)"
                       v-if="hasPermission(row.permissionId)">
              <FaIcon name="i-ep:delete"/>
            </el-button>
            <el-button size="small"
                       @click="addRoleMenuPermission(row.permissionId)"
                       v-else>
              <FaIcon name="i-ep:plus"/>
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">

import type {Menu} from "#/api";
import adminApi from "@/api/modules/admin.ts"
import {toast} from "vue-sonner";

const {menuId, roleCode} = defineProps<{ menuId: number, roleCode: string }>()

const roleMenuPermissions = ref<Menu.RoleMenuPermissionRs>()
const reload = ref(false)

const hasPermission = (permission: string): boolean => {
  return !!(roleMenuPermissions.value?.rolePermissions?.some(p => p.permissionId === permission))
}

const deleteRoleMenuPermission = async (permissionId: string) => {
  const auth = roleMenuPermissions.value?.rolePermissions?.find(
    p => p.permissionId === permissionId
  )
  if (auth) {
    await adminApi.deletePermissionRoleAuth(roleCode, auth.permissionId)
    toast.success("撤销授权成功")
    await refresh()
  }
}

const addRoleMenuPermission = async (permissionId: string) => {
  await adminApi.addRoleMenuPermissions({
    menuId,
    permissionId,
    roleCode
  })
  toast.success("授权成功")
  await refresh()
}

const refresh = async () => {
  reload.value = true
  const {data} = await adminApi.getRoleMenuPermissions(roleCode, menuId)
  roleMenuPermissions.value = data
  reload.value = false
}

onMounted(() => {
  refresh()
})

watch(() => [menuId, roleCode], () => {
  refresh()
})

</script>

<style scoped>

</style>
