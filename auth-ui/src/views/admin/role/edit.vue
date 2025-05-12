<template>
  <!-- 右侧表单 -->
  <el-card>
    <template #header>
      <span>角色详情</span>
    </template>
    <div v-show="!currentRole">
      <div class="text-center">
        <FaIcon name="ant-design:file-outlined" size="64px"/>
        <p class="text-gray-500">请先选择一个角色</p>
      </div>
    </div>
    <el-form label-width="80px" v-show="currentRole" ref="roleFormRef" :rules="rules" :model="editRole">
      <el-form-item label="角色ID" prop="roleCode" required>
        <el-input v-model="editRole.roleCode" disabled/>
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName" required>
        <el-input v-model="editRole.roleName" :disabled="!edit"/>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" v-model="editRole.description" :disabled="!edit"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-input v-model="editRole.createTime" disabled/>
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-input v-model="editRole.updateTime" disabled/>
      </el-form-item>

      <el-form-item>
        <el-button @click="reset">{{ edit ? '取消' : '编辑'}}</el-button>
        <el-button v-show="edit" type="primary" @click="submitUpdateRole">确定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import type {Role} from "#/api";
import adminApi from "@/api/modules/admin.ts";
import {toast} from "vue-sonner";

const {currentRole} = defineProps<{currentRole : Role.RoleListRs | undefined }>()

const edit = ref(false)

const emit = defineEmits({
  editSuccess: () => {
    return true
  }
})

const rules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' },
    { min: 2, max: 12, message: '角色名称长度必须在2到12个字符之间', trigger: 'blur' }
  ],
  description: [
    { max: 128, message: '描述不能超过128个字符', trigger: 'blur' }
  ]
}

// 表单引用
const roleFormRef = ref()

const editRole = ref<Role.RoleListRs>({
  roleCode: '',
  roleName: '',
  description: '',
  createTime: '',
  updateTime: '',
  children: [],
})

const reset = () => {
  editRole.value = JSON.parse(JSON.stringify(currentRole))
  edit.value = !edit.value
}

const submitUpdateRole = async () => {
  try {
    await roleFormRef.value.validate()
    await adminApi.roleUpdate({
      roleCode: editRole.value.roleCode,
      roleName: editRole.value.roleName,
      description: editRole.value.description
    })
    toast.success('角色更新成功')
    edit.value = false
    emit('editSuccess')
  } catch (error) {
    toast.error('角色更新失败，请检查输入')
  }
}

watch(
  () => currentRole,
  (newVal) => {
    if (newVal) {
      editRole.value = JSON.parse(JSON.stringify(newVal))
    }
    edit.value = false
  },
  { immediate: true }
)
</script>

<style scoped>

</style>
