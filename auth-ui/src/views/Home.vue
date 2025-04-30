<script setup lang="ts">
import useUserStore from "@/stores/modules/user.ts";
import {useRouter} from "vue-router";

const router = useRouter()
const userStore = useUserStore();

if (!userStore.isLogin) {
  router.push('/login')
}
</script>

<template>
  <div>主页</div>
  <el-button @click="userStore.logout()">
    登出
  </el-button>
</template>

<style scoped>

</style>
