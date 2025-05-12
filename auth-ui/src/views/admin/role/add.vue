<template>
  <!-- 新增弹窗 -->
  <el-dialog v-model="show" title="新增用户" width="500px">
    <el-form ref="roleFormRef" :model="roleAddRq" label-width="100px" :rules="rules">
      <el-form-item label="角色ID" prop="roleCode" required>
        <el-input v-model="roleAddRq.roleCode"/>
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName" required>
        <el-input v-model="roleAddRq.roleName"/>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" v-model="roleAddRq.description"/>
      </el-form-item>
      <el-form-item>
        <el-button @click="hideAddRoleDialog">取消</el-button>
        <el-button type="primary" @click="submitAddRole">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
// 表单验证规则
import type {Role} from "#/api";
import adminApi from "@/api/modules/admin.ts";
import {toast} from "vue-sonner";

// 控制新增弹窗显示
const show = defineModel({required: true, type: Boolean})

const {parentRoleCode} = defineProps<{ parentRoleCode?: string }>()

const emit = defineEmits({
  success: () => {
    return true
  }
})

const rules = {
  roleCode: [
    {required: true, message: '角色编码不能为空', trigger: 'blur'},
    {min: 2, max: 12, message: '角色编码长度必须在2到12个字符之间', trigger: 'blur'}
  ],
  roleName: [
    {required: true, message: '角色名称不能为空', trigger: 'blur'},
    {min: 2, max: 12, message: '角色名称长度必须在2到12个字符之间', trigger: 'blur'}
  ],
  description: [
    {max: 128, message: '描述不能超过128个字符', trigger: 'blur'}
  ]
}

// 表单引用
const roleFormRef = ref()

const defaultRoleAddRq = (): Role.RoleSaveRq => {
  return {
    roleCode: '',
    roleName: '',
    description: ''
  }
}


const hideAddRoleDialog = () => {
  roleAddRq.value = defaultRoleAddRq()
  show.value = false
}

const roleAddRq = ref<Role.RoleSaveRq>(defaultRoleAddRq())

// 提交新增用户
const submitAddRole = async () => {
  try {
    await roleFormRef.value.validate()
    await adminApi.roleAdd(roleAddRq.value, parentRoleCode)
    toast.success('角色创建成功')
    hideAddRoleDialog()
    emit('success')
  } catch (error) {
    toast.error('创建角色失败，请检查输入')
  }
}

</script>

<style scoped>

</style>
