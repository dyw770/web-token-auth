<template>
  <FaPageMain>
    <div class="container">
      <!-- 左侧角色树 -->
      <el-card class="w-2xl">
        <template #header>
          <span>角色列表</span>
        </template>
        <el-tree
          ref="treeRef"
          :data="treeData"
          default-expand-all
          node-key="roleCode"
          draggable
          :allow-drop="allowDrop"
          @node-drop="handleDrop"
          :expand-on-click-node="false"
          highlight-current
          :props="defaultProps"
        >
          <template #default="{ node, data }">
            <div
              @click="currentRole = data"
              @mouseenter="data.showButton = true"
              @mouseleave="data.showButton = false"
              class="tree-node"
            >
              <span>{{ node.label }}</span>
              <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small" @click="showAddRoleDialog(data.roleCode)">
                    <FaIcon name="ant-design:plus-outlined"/>
                  </el-button>
                  <el-button type="danger" size="small" @click="roleDelete(data)">
                    <FaIcon name="ant-design:delete-outlined"/>
                  </el-button>
                </el-button-group>
              </span>
            </div>
          </template>
          <template #empty>
            <div class="text-center" @click="showAddRoleDialog()">
              <FaIcon name="ant-design:plus-outlined" size="64px"/>
              <p class="text-gray-500">新增角色</p>
            </div>
          </template>
        </el-tree>
      </el-card>

      <!-- 右侧角色信息 -->
      <edit :current-role="currentRole" @edit-success="refresh"/>
    </div>
    <add v-model="showAddRole" :parent-role-code="parentRoleCode" @success="refresh"/>
  </FaPageMain>
</template>

<script setup lang="ts">
import type {Role} from "#/api";
import adminApi from '@/api/modules/admin.ts'
import {ref} from "vue";
import {ElMessageBox, type TreeInstance} from "element-plus";
import Add from "@/views/admin/role/add.vue";
import {toast} from "vue-sonner";
import Edit from "@/views/admin/role/edit.vue";
import type Node from 'element-plus/es/components/tree/src/model/node'
import type {AllowDropType, NodeDropType} from 'element-plus/es/components/tree/src/tree.type'
import type {DragEvents} from 'element-plus/es/components/tree/src/model/useDragNode'

interface TreeRole extends Role.RoleListRs {
  showButton?: boolean
}

const showAddRole = ref(false)
const parentRoleCode = ref<string>()

const treeRef = ref<TreeInstance>()

const treeData = ref<TreeRole[]>()

const currentRole = ref<TreeRole>()

const showAddRoleDialog = (parentRole?: string) => {
  parentRoleCode.value = parentRole
  showAddRole.value = true
}

const allowDrop = (_draggingNode: Node, _dropNode: Node, type: AllowDropType) => {
  return type === 'inner'
}

const handleDrop = async (
  draggingNode: Node,
  dropNode: Node,
  dropType: NodeDropType,
  _ev: DragEvents
) => {

  // 如果是拖拽到内部
  if (dropType === 'inner') {
    if (dropNode.data.parentRoleCode !== draggingNode.data.roleCode) {
      await adminApi.roleUpdateHierarchy(draggingNode.data.roleCode, dropNode.data.roleCode)
      await refresh()
    }
  }
  // 如果是拖拽到某个节点的兄弟节点上
  if (dropType === 'after' || dropType === 'before') {
    if(draggingNode.data.parentRoleCode !== dropNode.parent?.data.roleCode) {
      await adminApi.roleUpdateHierarchy(draggingNode.data.roleCode, dropNode.parent?.data.roleCode)
      await refresh()
    }
  }
}

onMounted(async () => {
  await refresh()
})

const defaultProps = {
  children: 'children',
  label: 'roleName',
}

const refresh = async () => {
  const {data} = await adminApi.roleList()
  treeData.value = data
}

const roleDelete = async (role: Role.RoleListRs) => {
  await ElMessageBox.confirm(
    `确认删除角色<strong class="text-red-500">${role.roleName}</strong>?`,
    '删除角色',
    {
      distinguishCancelAndClose: true,
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  )
  await adminApi.roleDelete(role.roleCode)
  toast.info('删除角色成功')
  await refresh()
}
</script>

<style scoped>
.container {
  display: flex;
  gap: 24px;
  padding: 16px;
}

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
