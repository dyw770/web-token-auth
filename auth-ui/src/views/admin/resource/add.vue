<template>
  <el-dialog v-model="show" title="新增API资源" width="500px">
    <el-form ref="formRef" label-width="100px" :model="addResource" :rules="rules">
      <!-- API路径 -->
      <el-form-item label="API路径" prop="apiPath">
        <el-input v-model="addResource.apiPath" />
      </el-form-item>

      <!-- 匹配类型 -->
      <el-form-item label="匹配类型" prop="matchType">
        <el-select v-model="addResource.matchType" class="w-full">
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
        <el-select v-model="addResource.apiMethod" class="w-full">
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
        <el-input v-model="addResource.description" type="textarea" :rows="3" />
      </el-form-item>

      <!-- 按钮 -->
      <el-form-item>
        <el-button @click="hideDialog">取消</el-button>
        <el-button type="primary" @click="submitAddMenu">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import type {Resource} from '#/api'
import {ApiMethod, MatchType} from '#/enums.ts'
import adminApi from '@/api/modules/admin'
import {toast} from "vue-sonner";

const show = defineModel({required: true, type: Boolean})


const emit = defineEmits({
  success: () => {
    return true
  }
})

// 表单引用用于校验
const formRef = ref()

// 默认值
const defaultAddResource: Resource.ResourceSaveRq = {
  apiPath: '',
  apiMethod: ApiMethod.ALL,
  matchType: MatchType.ANT,
  description: ''
}

const addResource = ref<Resource.ResourceSaveRq>({...defaultAddResource})

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

const hideDialog = () => {
  show.value = false
  resetForm()
}

// 提交方法
const submitAddMenu = async () => {
  try {
    await formRef.value.validate()
    // 发起请求
    await adminApi.resourceSava(addResource.value)
    toast.success('新增资源成功')
    // 成功后关闭弹窗并刷新列表等操作
    hideDialog()
    emit('success')
  } catch (error) {
    toast.error('创建资源失败，请检查输入')
  }
}

// 重置表单
const resetForm = () => {
  addResource.value = {...defaultAddResource}
  formRef.value.resetFields()
}
</script>
