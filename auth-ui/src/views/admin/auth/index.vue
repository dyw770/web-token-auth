<template>
  <FaPageMain>
    <div class="flex space-x-4 h-full">
      <!-- 菜单 -->
      <div class="w-1/4">
        <div>
          菜单
        </div>
        <el-tree
          ref="treeMenuRef"
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

      <el-divider direction="vertical" class="h-auto"/>

      <!-- 角色 -->
      <div class="w-1/4">
        <div>
          角色授权
        </div>
        <el-tree
          ref="treeRoleRef"
          v-show="currentMenu"
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
              @mouseenter="data.showButton = true"
              @mouseleave="data.showButton = false"
              :class="{ 'authorized': checked(data) }"
            >
              <span>
                <FaIcon
                  :name="checked(data) ? 'ant-design:check-circle-filled' : 'ant-design:plus-outlined'"
                  :class="{ 'text-green-500': checked(data), 'text-gray-400': !checked(data) }"
                  size="4"
                />
                {{ node.label }}
              </span>
              <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small" type="text" @click="menuAuth(data)">
                    {{ checked(data) ? '取消授权' : '授权' }}
                  </el-button>
                </el-button-group>
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
    </div>
  </FaPageMain>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin'
import {ref} from "vue";
import type {Menu, Role} from "#/api";
import type {TreeInstance} from "element-plus";
import {toast} from "vue-sonner";

interface TreeRole extends Role.RoleListRs {
  showButton?: boolean
}

const currentMenu = ref<Menu.MenuRoleListRs>()
const treeMenuData = ref<Menu.MenuRoleListRs[]>()

const treeRoleData = ref<TreeRole[]>()
const currentRole = ref<TreeRole>()

const treeRoleRef = ref<TreeInstance>()

const refresh = async () => {
  const menu = await adminApi.menuRoleList()
  treeMenuData.value = menu.data
  const role = await adminApi.roleList()
  treeRoleData.value = role.data
}

onMounted(async () => {
  await refresh()
})

const menuAuth = async (role: TreeRole) => {
  if (!currentMenu.value) {
    return
  }
  if (checked(role)) {
    await adminApi.menuDeleteRoleAuth([currentMenu.value.id], role.roleCode)
    currentMenu.value.roles = currentMenu.value.roles.filter(code => code !== role.roleCode)
    toast.info('取消授权成功')
  } else {
    await adminApi.menuAddRoleAuth([currentMenu.value.id], role.roleCode)
    currentMenu.value.roles.push(role.roleCode)
    toast.info('授权成功')
  }
  await refresh()
}

const checked = (role: TreeRole) => {
  return !!(currentMenu.value && currentMenu.value.roles && currentMenu.value.roles.includes(role.roleCode));
}

const roleProps = {
  children: 'children',
  label: 'roleName'
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

.tree-node-buttons {
  display: inline-block;
  opacity: 0.9;
  transition: opacity 0.2s ease;
}

.el-tree {
  --el-tree-node-content-height: 48px
}


::v-deep(.el-tree-node__expand-icon) {
  display: none !important;
}

.tree-node.authorized {
  background-color: #f0fff0; /* 浅绿色背景 */
  font-weight: bold;
  color: #1a73e8; /* 深蓝色文字 */
}

.tree-node.authorized .tree-node-buttons .el-button {
  color: #d93025; /* 取消授权按钮红色 */
}
</style>
