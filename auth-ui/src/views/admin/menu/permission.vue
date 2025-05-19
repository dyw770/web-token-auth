<template>
  <div>
    <el-table
      v-loading="reload"
      :data="permissionRs"
      border
      style="width: 100%"
      table-layout="auto">
      <el-table-column type="index" label="#" align="center"/>
      <el-table-column prop="permissionId" label="权限ID" align="center"/>
      <el-table-column prop="permissionDesc" label="权限说明" align="center" show-overflow-tooltip/>
      <el-table-column width="120" align="center">
        <template #header>
          <el-button type="primary" @click="openAddDrawer" circle size="small">
            <FaIcon name="i-ep:plus"/>
          </el-button>
        </template>
        <template #default="{row}">
          <el-button-group>
            <el-button size="small" @click="openEditDrawer(row)">
              <FaIcon name="i-ep:edit"/>
            </el-button>
            <el-button type="danger" size="small" @click="deletePermission(row)">
              <FaIcon name="i-ep:delete"/>
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer v-model="showDrawer" :title="edit? '修改权限' : '新增权限'" direction="rtl">
      <el-form label-width="80px" :model="currentPermission" :rules="rules" style="padding: 20px">
        <el-form-item label="权限ID" prop="permissionId">
          <el-input v-model="currentPermission.permissionId"/>
        </el-form-item>
        <el-form-item label="权限说明" prop="permissionDesc">
          <el-input v-model="currentPermission.permissionDesc" type="textarea" :rows="3"/>
        </el-form-item>
        <el-form-item>
          <el-button @click="savePermission" type="primary">{{ edit ? '保存修改' : '提交新增' }}</el-button>
          <el-button @click="showDrawer = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import type {Menu} from "#/api"
import adminApi from "@/api/modules/admin";

const {menuId} = defineProps<{ menuId: number }>()

const showDrawer = ref(false)
const edit = ref(false)

const currentPermission = ref<Menu.MenuPermissionSaveRq>({
  menuId: menuId,
  permissionId: '',
  permissionDesc: '',
})

// 表单验证规则
const rules = {
  permissionId: [
    {required: true, message: '权限ID不能为空', trigger: 'blur'},
    {min: 2, max: 32, message: '长度在2到32个字符之间', trigger: 'blur'}
  ],
  permissionDesc: [
    {required: true, message: '权限描述不能为空', trigger: 'blur'},
    {min: 2, max: 128, message: '长度在2到128个字符之间', trigger: 'blur'}
  ],
}

const openAddDrawer = () => {
  edit.value = false
  currentPermission.value = {
    menuId: menuId,
    permissionId: '',
    permissionDesc: '',
  }
  showDrawer.value = true
}

const openEditDrawer = (row: Menu.MenuPermissionSaveRq) => {
  edit.value = true
  currentPermission.value = {...row}
  showDrawer.value = true
}

const permissionRs = ref<Menu.SysMenuPermissionRs[]>()
const reload = ref(false)

const refresh = async () => {
  reload.value = true
  const {data} = await adminApi.menuPermissionList(menuId)
  permissionRs.value = data
  reload.value = false
}

const savePermission = async () => {
  if (edit.value) {
    // 调用修改权限的 API
    await adminApi.updateMenuPermission(currentPermission.value)
  } else {
    // 调用新增权限的 API
    await adminApi.saveMenuPermission(currentPermission.value)
  }
  showDrawer.value = false
  await refresh() // 刷新表格数据
}

const deletePermission = async (row: Menu.MenuPermissionSaveRq) => {
  await adminApi.deleteMenuPermission(row.menuId, row.permissionId)
  await refresh()
}

watch(() => menuId, () => {
  refresh()
})

onMounted(() => {
  refresh()
})

</script>

<style scoped>

</style>
