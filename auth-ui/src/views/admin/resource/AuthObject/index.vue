<template>
  <el-form ref="formRef" :model="authModel" :rules="rules" label-width="100px" @submit.prevent="submitAuth">
    <el-form-item label="授权类型" prop="authType" required>
      <el-select v-model="resourceAuthType" placeholder="请选择授权类型">
        <el-option :label="item.label" :value="item.type" :key="item.type" v-for="(item) in authType"/>
      </el-select>
    </el-form-item>
    <el-form-item label="授权对象" prop="authObject">

      <el-input v-model="authObject.authIp" placeholder="请输入IP"
                v-if="resourceAuthType === AuthType.IP"/>

      <el-select v-model="authObject.authStatic" placeholder="请选择授权对象"
                 class="w-full"
                 v-else-if="resourceAuthType === AuthType.STATIC">
        <el-option :label="v" :value="k" :key="k" v-for="(v, k) in staticAuth"/>
      </el-select>

      <el-cascader v-model="authObject.authRole"
                   placeholder="请选择授权角色"
                   class="w-full"
                   :props="props"
                   :options="roleData"
                   v-if="resourceAuthType === AuthType.ROLE"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" native-type="submit">提交</el-button>
      <el-button @click="cancelForm">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">

import {AuthType, authType, staticAuth} from "#/enums.ts";
import type {Resource, Role} from "#/api";
import adminApi from '@/api/modules/admin.ts'
import type {CascaderProps} from "element-plus";

const formRef = useTemplateRef("formRef")

const rules = {
  authType: [
    {
      required: true,
      message: '授权类型不能为空',
      trigger: 'blur',
      validator: (rule: any, _value: any, callback: any) => {
        if (!resourceAuthType.value) {
          callback(new Error(rule.message))
        } else {
          callback()
        }
      }
    },
  ],
  authObject: [
    {
      required: true,
      message: '授权对象不能为空',
      trigger: 'blur',
      validator: (rule: any, _value: any, callback: any) => {
        let result = true
        if (resourceAuthType.value === AuthType.ROLE) {
          if (!authObject.value.authRole) {
            result = false
          }
        } else if (resourceAuthType.value === AuthType.IP) {
          if (!authObject.value.authIp) {
            result = false
          }
        } else if (resourceAuthType.value === AuthType.STATIC) {
          if (!authObject.value.authStatic) {
            result = false
          }
        }
        if (result) {
          callback()
        } else {
          callback(new Error(rule.message))
        }
      }
    },
    {
      required: true,
      min: 2,
      max: 24,
      message: '授权对象长度必须在2到24个字符之间',
      trigger: 'change',
      validator: (rule: any, _value: any, callback: any) => {
        let result = true
        if (resourceAuthType.value === AuthType.ROLE) {
          if (authObject.value.authRole.length < rule.min || authObject.value.authRole.length > rule.max) {
            result = false
          }
        } else if (resourceAuthType.value === AuthType.IP) {
          if (authObject.value.authIp.length < rule.min || authObject.value.authIp.length > rule.max) {
            result = false
          }
        } else if (resourceAuthType.value === AuthType.STATIC) {
          if (authObject.value.authStatic.length < rule.min || authObject.value.authStatic.length > rule.max) {
            result = false
          }
        }
        if (result) {
          callback()
        } else {
          callback(new Error(rule.message))
        }
      }
    }
  ],
}

const authObject = ref<{
  authRole: string
  authIp: string
  authStatic: string
}>({
  authRole: "",
  authIp: "",
  authStatic: ""
})

const emit = defineEmits({
  submit: () => {
    return true
  },
  cancel: () => {
    return true
  }
})

const authModel = defineModel<Resource.ResourceAuth>({required: true})
const resourceAuthType = ref<AuthType>(authModel.value.authType)
// 隐藏对话框并重置状态
const cancelForm = () => {
  emit('cancel')
}

// 提交新增或编辑
const submitAuth = async () => {
  await formRef.value?.validate()
  authModel.value.authType = resourceAuthType.value
  if (resourceAuthType.value === AuthType.ROLE) {
    authModel.value.authObject = authObject.value.authRole
  } else if (resourceAuthType.value === AuthType.IP) {
    authModel.value.authObject = authObject.value.authIp
  } else if (resourceAuthType.value === AuthType.STATIC) {
    authModel.value.authObject = authObject.value.authStatic
  }
  emit('submit')
}

const roleData = ref<Role.RoleListRs[]>([])

const loadRoleData = async () => {
  const {data} = await adminApi.roleList()
  roleData.value = data
}

onMounted(async () => {
  await loadRoleData()
})

const props: CascaderProps = {
  expandTrigger: "hover",
  value: "roleCode",
  label: "roleName",
  children: "children",
  checkStrictly: true,
  emitPath: false
}

watch(() => authModel.value,
  () => {
    authObject.value = {
      authRole: "",
      authIp: "",
      authStatic: ""
    }
    if (authModel.value.authType === AuthType.ROLE) {
      authObject.value.authRole = authModel.value.authObject
    } else if (authModel.value.authType === AuthType.IP) {
      authObject.value.authIp = authModel.value.authObject
    } else if (authModel.value.authType === AuthType.STATIC) {
      authObject.value.authStatic = authModel.value.authObject
    }
    resourceAuthType.value = authModel.value.authType
  },
  {immediate: true}
)

</script>

<style scoped>

</style>
