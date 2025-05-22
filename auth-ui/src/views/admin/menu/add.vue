<template>
  <el-dialog v-model="show" title="新增菜单" width="500px">
    <el-form :model="addMenu" label-width="80px" :rules="rules" ref="formRef">

      <!-- 菜单名 -->
      <el-form-item label="菜单标题" prop="menuName">
        <el-input v-model="addMenu.menuName"/>
      </el-form-item>

      <!-- 菜单路由 -->
      <el-form-item label="菜单路由" prop="menuRouter">
        <el-input v-model="addMenu.menuRouter"/>
      </el-form-item>

      <!-- 菜单图标 -->
      <el-form-item label="菜单图标" prop="menuIcon">
        <el-input v-model="addMenu.menuIcon"/>
      </el-form-item>

      <!-- 菜单顺序 -->
      <el-form-item label="菜单顺序" prop="menuOrder">
        <el-input-number v-model="addMenu.menuOrder" :min="-10" :max="999"/>
      </el-form-item>

      <!-- 是否显示在导航栏 -->
      <el-form-item label="是否显示" prop="navShow">
        <el-switch v-model="addMenu.navShow"/>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button @click="hideDialog">取消</el-button>
        <el-button type="primary" @click="submitAddMenu">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import type {Menu} from '#/api'
import adminApi from '@/api/modules/admin'
import {toast} from "vue-sonner";
import type {FormItemRule} from 'element-plus'

const show = defineModel({required: true, type: Boolean})
const {parentMenuId} = defineProps<{ parentMenuId?: number }>()

const emit = defineEmits({
  success: () => {
    return true
  }
})

// 表单引用用于校验
const formRef = useTemplateRef('formRef')

// 默认值
const defaultAddMenu: Menu.MenuSaveRq = {
  menuName: '',
  menuIcon: '',
  menuRouter: '',
  menuOrder: 1,
  navShow: true
}

const addMenu = ref<Menu.MenuSaveRq>({...defaultAddMenu})

// 表单验证规则
const rules: Record<string, FormItemRule[]> = {
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
  ],
  navShow: [
    {required: true, message: '请选择是否显示在导航栏', trigger: 'change'}
  ]
}

const hideDialog = () => {
  show.value = false
  resetForm()
}

// 提交方法
const submitAddMenu = async () => {
  try {
    await formRef.value?.validate()
    // 发起请求
    await adminApi.menuAdd(addMenu.value, parentMenuId)
    toast.success('新增菜单成功')
    // 成功后关闭弹窗并刷新列表等操作
    hideDialog()
    emit('success')
  } catch (error) {
    toast.error('创建菜单失败，请检查输入')
  }
}

// 重置表单
const resetForm = () => {
  addMenu.value = {...defaultAddMenu}
  formRef.value?.resetFields()
}
</script>
