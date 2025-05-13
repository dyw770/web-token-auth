<template>
  <div>
    <div v-show="!menu" class="w-full h-full">
      <div class="text-center">
        <FaIcon name="ant-design:file-outlined" size="64px"/>
        <p class="text-gray-500">暂无数据</p>
      </div>
    </div>

    <el-form ref="formRef"
             v-show="menu"
             :model="editMenu"
             label-width="80px"
             :rules="rules">

      <!-- 菜单ID -->
      <el-form-item label="菜单ID" prop="id">
        <el-input v-model="editMenu.id" disabled/>
      </el-form-item>

      <!-- 菜单名 -->
      <el-form-item label="菜单名" prop="menuName">
        <el-input v-model="editMenu.menuName"/>
      </el-form-item>

      <!-- 菜单路由 -->
      <el-form-item label="菜单路由" prop="menuRouter">
        <el-input v-model="editMenu.menuRouter"/>
      </el-form-item>

      <!-- 菜单图标 -->
      <el-form-item label="菜单图标" prop="menuIcon">
        <el-input v-model="editMenu.menuIcon"/>
      </el-form-item>

      <!-- 是否显示在导航栏 -->
      <el-form-item label="是否显示" prop="navShow">
        <el-switch v-model="editMenu.navShow"/>
      </el-form-item>

      <!-- 菜单顺序 -->
      <el-form-item label="菜单顺序" prop="menuOrder">
        <el-input-number v-model="editMenu.menuOrder" :min="1" :max="999"/>
      </el-form-item>

      <el-form-item label="创建时间" prop="createTime">
        <el-input v-model="editMenu.createTime" disabled/>
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-input v-model="editMenu.updateTime" disabled/>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button @click="resetForm">重置</el-button>
        <el-button type="primary" @click="updateMenu">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin'
import type {Menu} from "#/api";
import {toast} from "vue-sonner";

const emit = defineEmits({
  editSuccess: () => {
    return true
  }
})

// 表单引用用于校验
const formRef = ref()

const {menu} = defineProps<{ menu: Menu.MenuListRs | undefined }>()

const defaultEditMenu: Menu.MenuListRs = {
  id: 0,
  menuName: '',
  menuIcon: '',
  menuRouter: '',
  menuOrder: 0,
  navShow: true,
  createTime: '',
  updateTime: '',
  children: []
}
const editMenu = ref<Menu.MenuListRs>({
  ...defaultEditMenu
})

// 表单验证规则
const rules = {
  id: [
    {required: true, message: '菜单ID不能为空', trigger: 'blur'},
    {type: 'number', min: 1, message: '菜单ID必须大于等于1', trigger: 'blur'}
  ],
  menuName: [
    {required: true, message: '菜单名不能为空', trigger: 'blur'},
    {min: 2, max: 12, message: '长度在2到12个字符之间', trigger: 'blur'}
  ],
  menuIcon: [
    {required: true, message: '菜单图标不能为空', trigger: 'blur'}
  ],
  menuOrder: [
    {required: true, message: '菜单顺序不能为空', trigger: 'blur'},
    {type: 'number', min: 1, max: 999, message: '请输入1到999之间的数字', trigger: 'blur'}
  ]
}


watch(
  () => menu,
  (newVal) => {
    if (newVal) {
      editMenu.value = JSON.parse(JSON.stringify(newVal))
    }
  },
  {immediate: true}
)

const updateMenu = async () => {
  await formRef.value.validate()
  await adminApi.menuUpdate({...editMenu.value})
  toast.success("修改菜单成功")
  emit('editSuccess')
}

const resetForm = () => {
  editMenu.value = menu ? {...menu} : {...defaultEditMenu}
  formRef.value.resetFields()
}

</script>

<style scoped>

</style>
