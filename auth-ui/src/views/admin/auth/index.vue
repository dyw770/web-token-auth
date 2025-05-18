<template>
  <FaPageMain>
    <div class="flex space-x-4 h-full">
      <!-- 菜单 -->
      <div class="w-1/4">
        <div>
          菜单
        </div>
        <el-divider direction="horizontal"/>
        <el-tree
          v-loading="!treeMenuData && !treeRoleData"
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
              <FaIcon name="ant-design:file-outlined" size="64px"/>
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
        <el-divider direction="horizontal"/>
        <el-tree
          ref="treeRoleRef"
          :data="currentMenu ? treeRoleData : []"
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
            >
              <span class="justify-center">
                <FaIcon :name="checked(data)? 'ant-design:check-circle-filled' : 'ant-design:close-circle-filled'" size="4"/>
                {{ node.label }}
              </span>
              <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small" :type="checked(data)? 'danger' : 'success'" @click="menuAuth(data)">
                    {{ checked(data) ? '取消授权' : '授权' }}
                  </el-button>
                </el-button-group>
              </span>
            </div>
          </template>
          <template #empty>
            <div class="text-center">
              <FaIcon name="ant-design:file-outlined" size="64px"/>
              <p class="text-gray-500">请先选择需要授权的菜单</p>
            </div>
          </template>
        </el-tree>
      </div>

      <el-divider direction="vertical" class="h-auto"/>

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
    await refresh()
    if (treeMenuData.value) {
      updateCurrentMenu(treeMenuData.value, currentMenu.value.id)
    }
    toast.info('取消授权成功')
  } else {
    await adminApi.menuAddRoleAuth([currentMenu.value.id], role.roleCode)
    await refresh()
    currentMenu.value.roles.push(role.roleCode)
    toast.info('授权成功')
  }
}

const updateCurrentMenu = (menus: Menu.MenuListRs[], menuId: number) => {
  const menuTmp = [...menus]
  while (menuTmp.length > 0) {
    const menu = menuTmp.shift()
    if (menu && menu.id === menuId) {
      currentMenu.value = menu as Menu.MenuRoleListRs
    } else if (menu && menu.children && menu.children.length > 0) {
      menuTmp.push(...menu.children)
    }
  }
}

const checked = (role: TreeRole) => {
  if (!(currentMenu.value && currentMenu.value.roles)) {
    return false
  }
  return roleChecked(role, currentMenu.value.roles)
}

const roleChecked = (role: TreeRole, roles: string[]): boolean => {
  if (roles.includes(role.roleCode)) {
    return true
  }
  if (!(role.children && role.children.length > 0)) {
    return false
  }

  return role.children.some(child => roleChecked(child, roles))
}

const roleProps = {
  children: 'children',
  label: 'roleName',
  disabled: () => {
    return true
  }
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
</style>
