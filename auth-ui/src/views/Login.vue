<template>
  <div class="login-container">
    <el-form ref="ruleFormRef" :model="form" label-position="left" label-width="80px" class="login-form" :rules="rules">
      <h2>用户登录</h2>
      <el-form-item label="用户名" prop="username" required>
        <el-input v-model="form.username" type="text" placeholder="请输入用户名"/>
      </el-form-item>
      <el-form-item label="密码" prop="password" required>
        <el-input v-model="form.password" type="password" placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm(ruleFormRef)" block>登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import type {FormInstance, FormRules} from "element-plus";
import useUserStore from "@/stores/modules/user.ts";

const userStore = useUserStore();
const router = useRouter();

const form = ref({
  username: '',
  password: ''
});

const ruleFormRef = ref<FormInstance>();

const rules = reactive<FormRules<typeof form>>({
  username: [{required: true, trigger: 'blur', message: '请输入用户名'}],
  password: [{required: true, trigger: 'blur', message: '请输入密码'}],
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      userStore.login(form.value)
        .then(() => {
          router.push('/')
        })
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.login-form {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 420px;
}

.login-form h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}
</style>
