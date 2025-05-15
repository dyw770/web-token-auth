<template>
  <!-- 修改用户弹窗 -->
  <el-dialog v-model="show" title="编辑" width="500px">
    <el-form ref="userFormRef" :model="userEditRq" label-width="100px" :rules="rules">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="userEditRq.username" disabled/>
      </el-form-item>

      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="userEditRq.nickname"/>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="userEditRq.password" show-password/>
      </el-form-item>

      <el-form-item label="头像">
        <el-input v-model="userEditRq.avatar" placeholder="请输入头像URL"/>
      </el-form-item>

      <el-form-item label="是否启用" prop="enabled">
        <el-switch v-model="userEditRq.enabled"/>
      </el-form-item>

      <el-form-item>
        <el-button @click="hideEditUserDialog">取消</el-button>
        <el-button type="primary" @click="submitEditUser">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import type {User} from "#/api";
import adminApi from "@/api/modules/admin.ts";
import {toast} from "vue-sonner";

// 控制新增弹窗显示
const show = defineModel({required: true, type: Boolean})

const {user} = defineProps<{ user: User.UserRs | undefined }>()

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
    {max: 12, message: '最长为12个字符', trigger: 'blur'}
  ],
  password: [
    {min: 6, max: 16, message: '长度在6到16个字符之间', trigger: 'blur'}
  ],
  enabled: [
    {required: true, message: '请选择是否启用', trigger: 'change'}
  ]
}

// 表单引用
const userFormRef = ref()

const defaultUserEditRq = (): User.UserEditRq => {
  return {
    username: user?.username ? user?.username : '',
    nickname: user?.nickname ? user?.nickname : '',
    password: '',
    avatar: user?.avatar ? user?.avatar : undefined,
    enabled: user?.enabled ? user?.enabled : true,
  }
}


const hideEditUserDialog = () => {
  userEditRq.value = defaultUserEditRq()
  show.value = false
}

const userEditRq = ref<User.UserEditRq>(defaultUserEditRq())

watch(() => user, (newVal) => {
  if (newVal) {
    userEditRq.value = {...newVal}
  }
})

// 提交新增用户
const submitEditUser = async () => {
  try {
    await userFormRef.value.validate()
    await adminApi.userEdit({
      username: userEditRq.value.username,
      nickname: userEditRq.value.nickname,
      password: userEditRq.value.password ? userEditRq.value.password : undefined,
      enabled: userEditRq.value.enabled,
      avatar: userEditRq.value.avatar
    })
    toast.success('修改成功')
    show.value = false
    emit('success')
  } catch (error) {
    toast.error('修改用户失败，请检查输入')
  }
}

</script>

<style scoped>

</style>
