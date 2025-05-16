<template>
  <FaPageMain>
    <el-page-header @back="goBack">
      <template #content>
      <span class="text-large font-600 mr-3">
            授权页面 {{route.params.id}}
      </span>
      </template>
    </el-page-header>
  </FaPageMain>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from 'vue-router'

const route = useRoute()
const router = useRouter()

const refresh = async () => {

}

const goBack = () => {
  router.back()
}

watch(() => route.params.id, refresh, { immediate: true })


</script>

<style scoped>

</style>
