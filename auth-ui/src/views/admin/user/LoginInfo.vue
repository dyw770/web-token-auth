<template>
  <el-drawer v-model="showDrawer" title="用户登录信息" direction="rtl" size="60%">
    <el-table :data="loginInfos" style="width: 100%" table-layout="auto" border>
      <el-table-column prop="token.token" label="Token" show-overflow-tooltip />
      <el-table-column prop="token.createTime" label="创建时间" width="160">
        <template #default="{ row }">
          {{ row.token.createTime }}
        </template>
      </el-table-column>
      <el-table-column prop="token.loginUserAgent" label="登录设备" show-overflow-tooltip />
      <el-table-column prop="expireTime" label="过期时间" width="160">
        <template #default="{ row }">
          {{ row.expireTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="320px">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="danger" size="small" @click="userOffline(row.token.token)">
              强制下线
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
  </el-drawer>
</template>

<script setup lang="ts">

import adminApi from "@/api/modules/admin.ts";
import type {User} from "#/api";

const showDrawer = defineModel({required: true, type: Boolean})

const {username} = defineProps<{ username: string | undefined }>()

const loginInfos = ref<User.TokenWrapperRs[]>([])

const userOffline = async (token: string) => {
  await adminApi.userOffline(token)
  await refresh()
}

const refresh = async () => {
  if (username) {
    const {data} = await adminApi.userLoginInfo(username)
    loginInfos.value = data
  }
}

onMounted(() => {
  refresh()
})

watch(() => username, (newVal) => {
  if (newVal) {
    refresh()
  }
})
</script>

<style scoped>

</style>
