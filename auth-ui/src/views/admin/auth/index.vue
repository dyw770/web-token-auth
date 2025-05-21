<template>
  <FaPageMain>
    <div class="flex space-x-4 h-full">
      <!-- 菜单 -->
      <div class="w-1/3">
        <div>
          菜单
        </div>
        <el-divider direction="horizontal"/>
        <el-tree
          class="min-h-60vh max-h-[60vh] overflow-y-auto max-h-[calc(100vh-25vh-20px)] scrollbar-hide"
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
          class="min-h-60vh max-h-[60vh] overflow-y-auto max-h-[calc(100vh-25vh-20px)] scrollbar-hide"
          ref="treeRoleRef"
          :data="currentMenu ? treeRoleData : []"
          default-expand-all
          node-key="roleCode"
          :expand-on-click-node="false"
          :props="roleProps"
        >
          <template #default="{ node, data }">
            <div
              class="tree-node"
              @mouseenter="data.showButton = true"
              @mouseleave="data.showButton = false"
            >
              <span class="justify-center inline-flex items-center gap-2">
                <FaIcon :name="checked(data)? 'ant-design:check-circle-filled' : 'ant-design:close-circle-filled'"
                        size="4"/>
                {{ node.label }}
              </span>
              <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small" v-show="checked(data)" @click="editRoleMenuPermission(data.roleCode)">
                    编辑子权限
                  </el-button>
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

      <!-- 菜单权限 -->
      <div class="w-5/12">
        <div>
          菜单权限
        </div>
        <el-divider direction="horizontal"/>
        <role-menu-permission-edit
          class="min-h-60vh max-h-[60vh] overflow-y-auto max-h-[calc(100vh-25vh-20px)] scrollbar-hide"
          :menu-id="currentEditRole.menuId"
          :role-code="currentEditRole.roleCode"
          v-if="currentEditRole"/>
        <div class="text-center" v-else>
          <div class="text-center">
            <FaIcon name="ant-design:file-outlined" size="64px"/>
            <p class="text-gray-500">
              请先选择需要授权的角色和菜单
            </p>
          </div>
        </div>
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
import RoleMenuPermissionEdit from "@/views/admin/auth/RoleMenuPermissionEdit.vue";

interface TreeRole extends Role.RoleListRs {
  showButton?: boolean
}

const currentMenu = ref<Menu.MenuRoleListRs>()
const treeMenuData = ref<Menu.MenuRoleListRs[]>()

const treeRoleData = ref<TreeRole[]>()
const currentEditRole = ref<{
  menuId: number,
  roleCode: string
}>()

const editRoleMenuPermission = (roleCode: string) => {
  if (currentMenu.value) {
    currentEditRole.value = {
      menuId: currentMenu.value.id,
      roleCode: roleCode
    }
  }
}

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

.scrollbar-hide {
  -ms-overflow-style: none; /* IE 和 Edge 隐藏滚动条 */
  scrollbar-width: none; /* Firefox 隐藏滚动条 */
}

/* 可选：Chrome、Edge 等浏览器 */
.scrollbar-hide::-webkit-scrollbar {
  display: none;
}
</style>
