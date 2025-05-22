<template>
  <!-- 新增用户弹窗 -->
  <el-dialog v-model="show" title="新增用户" width="500px">
    <el-form ref="userFormRef" :model="userAddRq" label-width="100px" :rules="rules">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="userAddRq.username"/>
      </el-form-item>

      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="userAddRq.nickname"/>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="userAddRq.password" show-password/>
      </el-form-item>

      <el-form-item label="头像">
        <el-input v-model="userAddRq.avatar" placeholder="请输入头像URL"/>
      </el-form-item>

      <el-form-item label="是否启用" prop="enabled">
        <el-switch v-model="userAddRq.enabled"/>
      </el-form-item>

      <el-form-item>
        <el-button @click="hideAddUserDialog">取消</el-button>
        <el-button type="primary" @click="submitAddUser">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
// 表单验证规则
import type {User} from "#/api";
import adminApi from "@/api/modules/admin.ts";
import {toast} from "vue-sonner";

// 控制新增弹窗显示
const show = defineModel({required: true, type: Boolean})

const emit = defineEmits({
  success: () => {
    return true
  }
})

const rules = {
  username: [
    {required: true, message: '用户名不能为空', trigger: 'blur'},
    {min: 3, max: 12, message: '长度在3到12个字符之间', trigger: 'blur'}
  ],
  nickname: [
    {required: true, message: '昵称不能为空', trigger: 'blur'},
    {max: 12, min: 2, message: '最长为12个字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'},
    {min: 6, max: 16, message: '长度在6到16个字符之间', trigger: 'blur'}
  ],
  enabled: [
    {required: true, message: '请选择是否启用', trigger: 'change'}
  ]
}

// 表单引用
const userFormRef = ref()

const defaultUserAddRq = (): User.UserCreateRq => {
  return {
    username: '',
    nickname: '',
    password: '',
    enabled: true,
  }
}


const hideAddUserDialog = () => {
  userAddRq.value = defaultUserAddRq()
  show.value = false
}

const userAddRq = ref<User.UserCreateRq>(defaultUserAddRq())

// 提交新增用户
const submitAddUser = async () => {
  try {
    await userFormRef.value.validate()
    await adminApi.userAdd(userAddRq.value)
    toast.success('用户创建成功')
    show.value = false
    emit('success')
  } catch (error) {
    toast.error('创建用户失败，请检查输入')
  }
}

</script>

<style scoped>

</style>
