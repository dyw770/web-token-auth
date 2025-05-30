<template>
  <el-dialog v-model="show" title="授权" width="500px" @opened="getRoleList">
    <el-tree
      ref="treeRoleRef"
      :data="treeData"
      default-expand-all
      node-key="roleCode"
      :expand-on-click-node="false"
      :props="defaultProps"
    >
      <template #default="{ node, data }">
        <div
          class="tree-node"
          @mouseenter="data.showButton = true"
          @mouseleave="data.showButton = false"
        >
              <span class="justify-center inline-flex items-center gap-2">
                <FaIcon :name="data.checked ? 'ant-design:check-circle-filled' : 'ant-design:close-circle-filled'"
                        size="4"/>
                {{ node.label }}
              </span>
          <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small" :type="data.checked ? 'danger' : 'success'" @click="auth(data)">
                    {{ data.checked ? '取消授权' : '授权' }}
                  </el-button>
                </el-button-group>
              </span>
        </div>
      </template>
      <template #empty>
        <div class="text-center">
          <FaIcon name="ant-design:file-outlined" size="64px"/>
          <p class="text-gray-500">请先再系统中创建角色</p>
        </div>
      </template>
    </el-tree>
    <template #footer>
      <div class="dialog-footer">
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

interface TreeRole extends Role.RoleListRs {
  checked?: boolean
  showButton?: boolean
}

// 控制授权弹窗显示
const show = defineModel({required: true, type: Boolean})

const {user} = defineProps<{ user: User.UserRs | undefined }>()


watch(() => user, () => {
  getRoleList()
})

const emit = defineEmits({
  success: () => {
    return true
  }
})

const treeRef = ref<TreeInstance>()

const treeData = ref<TreeRole[]>([])

const getRoleList = async () => {
  const {data} = await adminApi.roleList()
  treeData.value = data
  buildTreeRole()
}

const buildTreeRole = () => {
  if (!(user && user.roles)) {
    return
  }
  const roles = user.roles.map(item => item.roleCode)
  treeData.value.forEach(item => {
    checkRole(item, roles, undefined)
  })
}

const checkRole  = (role: TreeRole, roles: string[], parentRole: TreeRole | undefined) => {
  if (roles.includes(role.roleCode)) {
    role.checked = true
  }
  if(role.parentRoleCode && roles.includes(role.parentRoleCode)) (
    role.checked = true
  )
  if (parentRole && parentRole.checked) {
    role.checked = true
  }
  role.children.forEach(item => {
    checkRole(item, roles, role)
  })
}

const closeAuthDialog = () => {
  show.value = false
  treeRef.value!.setCheckedNodes([])
}

const auth = async (role: TreeRole) => {
  await adminApi.addRoleForUser(user!.username, [role.roleCode])
  toast.success('授权成功')
  await getRoleList()
  emit('success')
}

const submitAuth = async () => {
  closeAuthDialog()
  emit('success')
}

const defaultProps = {
  children: 'children',
  label: 'roleName',
}

</script>


<style scoped>
.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  transition: background-color 0.2s ease;
  width: 100%;
}

.tree-node-buttons {
  display: inline-block;
  opacity: 0.9;
  transition: opacity 0.2s ease;
}

.el-tree {
  --el-tree-node-content-height: 48px
}
</style>
