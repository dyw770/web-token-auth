<template>
  <FaPageMain>
    <div class="flex space-x-4 h-full">
      <!-- 菜单 -->
      <div class="w-1/4">
        <el-tree
          ref="treeRef"
          :data="treeMenuData"
          default-expand-all
          node-key="id"
          :expand-on-click-node="false"
          highlight-current
          :props="menuProps"
        >
          <template #default="{ node, data }">
            <div
              @click="currentMenu = data"
              class="tree-node">
              <span>
                <FaIcon :name="data.menuIcon"/>
                {{ node.label }}
              </span>
            </div>
          </template>

          <template #empty>
            <div class="text-center">
              <FaIcon name="ant-design:plus-outlined" size="64px"/>
              <p class="text-gray-500">暂无数据</p>
            </div>
          </template>
        </el-tree>
      </div>

      <el-divider direction="vertical"  class="h-auto" />

      <!-- 角色 -->
      <div class="w-1/4">
        <el-tree
          ref="treeRef"
          :data="treeRoleData"
          default-expand-all
          node-key="roleCode"
          :expand-on-click-node="false"
          :props="roleProps"
        >
          <template #default="{ node, data }">
            <div
              @click="currentRole = data"
              class="tree-node"
            >
              <span>{{ node.label }}</span>
            </div>
          </template>
          <template #empty>
            <div class="text-center">
              <FaIcon name="ant-design:plus-outlined" size="64px"/>
              <p class="text-gray-500">暂无数据</p>
            </div>
          </template>
        </el-tree>
      </div>
    </div>
  </FaPageMain>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin'
import {ref} from "vue";
import type {Menu, Role} from "#/api";


const currentMenu = ref<Menu.MenuListRs>()
const treeMenuData = ref<Menu.MenuListRs[]>()

const treeRoleData = ref<Role.RoleListRs[]>()
const currentRole = ref<Role.RoleListRs>()


const refresh = async () => {
  const menu = await adminApi.menuList()
  treeMenuData.value = menu.data
  const role = await adminApi.roleList()
  treeRoleData.value = role.data
}


onMounted(async () => {
  await refresh()
})

const roleProps = {
  children: 'children',
  label: 'roleName',
}

const menuProps = {
  children: 'children',
  label: 'menuName',
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

.el-tree {
  --el-tree-node-content-height: 48px
}
</style>
