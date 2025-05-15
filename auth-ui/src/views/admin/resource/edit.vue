<template>
  <el-dialog v-model="show" title="编辑API资源" width="500px">
    <el-form ref="formRef" label-width="100px" :model="editResource" :rules="rules">
      <!-- API路径 -->
      <el-form-item label="API路径" prop="apiPath">
        <el-input v-model="editResource.apiPath" />
      </el-form-item>

      <!-- 匹配类型 -->
      <el-form-item label="匹配类型" prop="matchType">
        <el-select v-model="editResource.matchType" class="w-full">
          <el-option
            v-for="item in MatchType"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
      </el-form-item>

      <!-- API方法 -->
      <el-form-item label="API方法" prop="apiMethod">
        <el-select v-model="editResource.apiMethod" class="w-full">
          <el-option
            v-for="item in ApiMethod"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
      </el-form-item>

      <!-- 描述 -->
      <el-form-item label="描述" prop="description">
        <el-input v-model="editResource.description" type="textarea" :rows="3" />
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item>
        <el-button @click="hideDialog">取消</el-button>
        <el-button type="primary" @click="submitEditResource">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import type {Resource} from '#/api'
import {ApiMethod, MatchType} from '@/types/enums'
import adminApi from '@/api/modules/admin'
import {toast} from 'vue-sonner'

// 接收 show 控制显示
const show = defineModel({ required: true, type: Boolean })

const {resource} = defineProps<{ resource: Resource.ResourceListRs | undefined }>()

// 定义 emit
const emit = defineEmits<{
  (e: 'success'): void
}>()

// 表单引用
const formRef = ref()

const defaultResource = (): Resource.ResourceUpdateRq => {
  return {
    id: resource?.id?? 0,
    apiPath: resource?.apiPath?? '',
    matchType: resource?.matchType?? MatchType.ANT,
    apiMethod: resource?.apiMethod as ApiMethod?? ApiMethod.ALL,
    description: resource?.description?? ''
  }
}

// 编辑的数据模型
const editResource = ref<Resource.ResourceUpdateRq>(defaultResource())


// 表单验证规则
const rules = {
  apiPath: [
    { required: true, message: 'API路径不能为空', trigger: 'blur' },
    { min: 1, max: 128, message: '长度应在1~128个字符之间', trigger: 'blur' }
  ],
  matchType: [
    { required: true, message: '请选择匹配类型', trigger: 'change' }
  ],
  apiMethod: [
    { required: true, message: '请选择API方法', trigger: 'change' }
  ],
  description: [
    { required: true, message: '描述不能为空', trigger: 'blur' },
    { min: 1, max: 128, message: '长度应在1~128个字符之间', trigger: 'blur' }
  ]
}

// 关闭弹窗并重置表单
const hideDialog = () => {
  resetForm()
  show.value = false
}

// 提交修改
const submitEditResource = async () => {
  try {
    await formRef.value.validate()
    // 发起更新请求
    await adminApi.resourceUpdate(editResource.value)
    // 成功后关闭弹窗并通知刷新列表
    hideDialog()
    emit('success')
    toast.success('更新成功')
  } catch (error) {
    toast.error('更新失败，请检查输入')
  }
}

// 重置表单
const resetForm = () => {
  editResource.value = defaultResource()
  formRef.value.resetFields()
}
</script>

<style scoped>
.w-full {
  width: 100%;
}
</style>
