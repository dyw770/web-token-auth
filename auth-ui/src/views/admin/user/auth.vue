<template>
  <el-dialog v-model="show" title="授权" width="500px" @opened="getRoleList">
    <el-tree
      ref="treeRef"
      style="max-width: 600px"
      :data="treeData"
      show-checkbox
      default-expand-all
      node-key="roleCode"
      highlight-current
      :props="defaultProps"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAuthDialog">取消</el-button>
        <el-button type="primary" @click="submitAuth">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import adminApi from "@/api/modules/admin.ts";
import {ref} from 'vue'
import type {TreeInstance} from 'element-plus'
import type {Role, User} from "#/api";
import {toast} from "vue-sonner";

// 控制授权弹窗显示
const show = defineModel({required: true, type: Boolean})

const {user} = defineProps<{ user: User.UserRs | undefined }>()

const emit = defineEmits({
  success: () => {
    return true
  }
})

const treeRef = ref<TreeInstance>()

const treeData = ref<Role.RoleListRs[]>()

const getRoleList = async () => {
  const {data} = await adminApi.roleList()
  treeData.value = data

  if (user) {
    const checkedKeys = user.roles.map(role => {
      return role.roleCode
    })
    treeRef.value!.setCheckedKeys(checkedKeys)
  }
}

const getCheckedKeys = () => {
  console.log(treeRef.value!.getCheckedKeys(false))
}

const closeAuthDialog = () => {
  getCheckedKeys()
  show.value = false
  treeRef.value!.setCheckedNodes([])
}

const submitAuth = async () => {
  const checkedNodes = treeRef.value!.getCheckedNodes(false)
  const roleCodes = checkedNodes.map(node => {
    return node.roleCode
  })
  await adminApi.addRoleForUser(user!.username, roleCodes)
  toast.success('授权成功')
  closeAuthDialog()
  emit('success')
}

const defaultProps = {
  children: 'children',
  label: 'roleName',
}

</script>


<style scoped>

</style>
