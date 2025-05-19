<template>
  <div>
    <el-table
      v-loading="reload"
      :data="permissionRs"
      border

      style="width: 100%"
      table-layout="auto">
      <el-table-column type="index" label="#" align="center"/>
      <el-table-column prop="permissionId" label="权限ID" align="center">
        <template #default="{row}">
          <el-input v-model="row.permissionId" v-if="row.edit" size="small"/>
          <template v-else>{{ row.permissionId }}</template>
        </template>
      </el-table-column>
      <el-table-column prop="permissionDesc" label="权限说明" align="center" show-overflow-tooltip>
        <template #default="{row}">
          <el-input v-model="row.permissionDesc" v-if="row.edit" size="small"/>
          <template v-else>{{ row.permissionDesc }}</template>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="{row}">
          <el-button-group v-if="!row.edit">
            <el-button size="small" @click="row.edit = true">
              <FaIcon name="i-ep:edit"/>
            </el-button>
            <el-button type="danger" size="small">
              <FaIcon name="i-ep:delete"/>
            </el-button>
          </el-button-group>
          <el-button-group v-else>
            <el-button size="small">
              <FaIcon name="i-ep:check"/>
            </el-button>
            <el-button size="small">
              <FaIcon name="i-ep:close"/>
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type {Menu} from "#/api"
import adminApi from "@/api/modules/admin";

const {menuId} = defineProps<{ menuId: number | undefined }>()

interface EditSysMenuPermissionRs extends Menu.SysMenuPermissionRs {
  edit?: boolean;
}

const permissionRs = ref<EditSysMenuPermissionRs[]>()
const reload = ref(false)

const refresh = async () => {
  reload.value = true
  if (menuId) {
    const {data} = await adminApi.menuPermissionList(menuId)
    permissionRs.value = data
  }
  reload.value = false
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
