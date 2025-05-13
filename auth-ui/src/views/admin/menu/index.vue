<template>
  <FaPageMain>
    <div class="container">
      <!-- 左侧角色树 -->
      <el-card class="w-100 h-full">
        <template #header>
          <span>菜单列表</span>
        </template>
        <div>
          <el-button v-show="treeData?.length !== 0" class="w-full" @click="addMenu(undefined)">新增根菜单</el-button>
        </div>
        <el-tree
          ref="treeRef"
          :data="treeData"
          default-expand-all
          node-key="id"
          draggable
          :allow-drop="allowDrop"
          @node-drop="handleDrop"
          :expand-on-click-node="false"
          highlight-current
          :props="defaultProps"
        >
          <template #default="{ node, data }">
            <div
              @mouseenter="data.showButton = true"
              @mouseleave="data.showButton = false"
              @click="currentMenu = data"
              class="tree-node"
            >
              <span>
                <FaIcon :name="data.menuIcon"/>
                {{ node.label }}
              </span>
              <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small" @click="addMenu(data.id)">
                    <FaIcon name="ant-design:plus-outlined"/>
                  </el-button>
                     <el-button size="small">
                    <FaIcon name="ant-design:edit-outlined"/>
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteMenu(data)">
                    <FaIcon name="ant-design:delete-outlined"/>
                  </el-button>
                </el-button-group>
              </span>
            </div>
          </template>

          <template #empty>
            <div class="text-center">
              <FaIcon name="ant-design:plus-outlined" size="64px"/>
              <p class="text-gray-500">新增菜单</p>
            </div>
          </template>
        </el-tree>
      </el-card>

      <el-card>
        <template #header>
          <span>菜单详情</span>
        </template>
        <edit :menu="currentMenu" @edit-success="refresh" class="w-100" />
      </el-card>
    </div>

    <add v-model="showAddMenu" :parent-menu-id="parentMenuId" @success="refresh" />
  </FaPageMain>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin'
import type Node from "element-plus/es/components/tree/src/model/node";
import type {AllowDropType, NodeDropType} from "element-plus/es/components/tree/src/tree.type";
import {ref} from "vue";
import type {Menu} from "#/api";
import Add from "@/views/admin/menu/add.vue";
import {ElMessageBox} from "element-plus";
import Edit from "@/views/admin/menu/edit.vue";
import type {DragEvents} from "element-plus/es/components/tree/src/model/useDragNode";

interface MenuTree extends Menu.MenuListRs {
  showButton: boolean
}

const currentMenu = ref<MenuTree>()

const treeData = ref<MenuTree[]>()

const showAddMenu = ref(false)
const parentMenuId = ref<number>()

const refresh = async () => {
  const {data} = await adminApi.menuList()
  treeData.value = data
}

const addMenu = (menuId?: number) => {
  showAddMenu.value = true;
  parentMenuId.value = menuId
}

const deleteMenu = async (menu: MenuTree) => {
  ElMessageBox.confirm(
    `确认删除菜单<strong class="text-red-500">${menu.menuName}</strong>?`,
    '删除菜单',
    {
      distinguishCancelAndClose: true,
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    }
  ).then(async () => {
    await adminApi.menuDelete(menu.id)
    await refresh()
  })
}

const handleDrop = async (
  draggingNode: Node,
  dropNode: Node,
  dropType: NodeDropType,
  _ev: DragEvents
) => {

  // 如果是拖拽到内部
  if (dropType === 'inner') {
    if (dropNode.data.parentMenuId !== draggingNode.data.id) {
      await adminApi.menuUpdateHierarchy(draggingNode.data.id, dropNode.data.id)
      await refresh()
    }
  }
  // 如果是拖拽到某个节点的兄弟节点上
  if (dropType === 'after' || dropType === 'before') {
    if(draggingNode.data.parentMenuId !== dropNode.parent?.data.id) {
      await adminApi.menuUpdateHierarchy(draggingNode.data.id, dropNode.parent?.data.id)
      await refresh()
    }
  }
}

onMounted(async () => {
  await refresh()
})

const defaultProps = {
  children: 'children',
  label: 'menuName',
}

const allowDrop = (_draggingNode: Node, _dropNode: Node, type: AllowDropType) => {
  return type === 'inner'
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

.el-tree {
  --el-tree-node-content-height: 48px
}

.tree-node-buttons {
  display: inline-block;
  opacity: 0.9;
  transition: opacity 0.2s ease;
}
</style>
