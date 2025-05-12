<template>
  <FaPageMain>
    <div class="container">
      <!-- 左侧角色树 -->
      <el-card class="w-100 h-full">
        <template #header>
          <span>菜单列表</span>
        </template>
        <div>
          <el-button v-show="treeData?.length !== 0" class="w-full">新增根菜单</el-button>
        </div>
        <el-tree
          ref="treeRef"
          :data="treeData"
          default-expand-all
          node-key="id"
          draggable
          :allow-drop="allowDrop"
          :expand-on-click-node="false"
          highlight-current
          :props="defaultProps"
        >
          <template #default="{ node, data }">
            <div
              @mouseenter="data.showButton = true"
              @mouseleave="data.showButton = false"
              class="tree-node"
            >
              <span>
                <FaIcon :name="data.menuIcon"/>
                {{ node.label }}
              </span>
              <span v-show="data.showButton" class="tree-node-buttons">
                <el-button-group>
                  <el-button size="small">
                    <FaIcon name="ant-design:plus-outlined"/>
                  </el-button>
                     <el-button size="small">
                    <FaIcon name="ant-design:edit-outlined"/>
                  </el-button>
                  <el-button type="danger" size="small">
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
        <div>

        </div>
      </el-card>
    </div>
  </FaPageMain>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin'
import type Node from "element-plus/es/components/tree/src/model/node";
import type {AllowDropType} from "element-plus/es/components/tree/src/tree.type";
import {ref} from "vue";
import type {Menu} from "#/api";

interface MenuTree extends Menu.MenuListRs {
  showButton: boolean
}

const treeData = ref<MenuTree[]>()

const refresh = async () => {
  const {data} = await adminApi.menuList()
  treeData.value = data
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
