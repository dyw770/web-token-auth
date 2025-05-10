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
                  <el-button type="info" size="small">
                    <FaIcon name="ant-design:edit-outlined"/>
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

      <!-- 右侧表单 -->
      <el-card>
        <template #header>
          <span>角色详情</span>
        </template>
        <el-form label-width="80px">
          <el-form-item label="角色ID">
            <el-input v-model="currentRole.roleCode" disabled/>
          </el-form-item>
          <el-form-item label="角色名称">
            <el-input v-model="currentRole.roleName" disabled/>
          </el-form-item>
          <el-form-item label="描述">
            <el-input type="textarea" v-model="currentRole.description" disabled/>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-input v-model="currentRole.createTime" disabled/>
          </el-form-item>
          <el-form-item label="更新时间">
            <el-input v-model="currentRole.updateTime" disabled/>
          </el-form-item>
        </el-form>
      </el-card>
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

interface TreeRole extends Role.RoleListRs {
  showButton?: boolean
}

const showAddRole = ref(false)
const parentRoleCode = ref<string>()

const treeRef = ref<TreeInstance>()

const treeData = ref<TreeRole[]>()

const currentRole = ref<TreeRole>({
  roleCode: '',
  roleName: '',
  description: '',
  createTime: '',
  updateTime: '',
  children: [],
})

const showAddRoleDialog = (parentRole?: string) => {
  parentRoleCode.value = parentRole
  showAddRole.value = true
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
    `确认删除角色<strong style="color: red">${role.roleName}</strong>?`,
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
</style>
