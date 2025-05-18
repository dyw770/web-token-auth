<template>
  <FaPageMain>
    <div>
      <div>
        {{ publicApiData }}
      </div>
      <span>/test/static</span>
      <el-button @click="getPublicApiData">请求测试公共接口</el-button>
    </div>

    <div>
      <div>
        {{ privateApiData }}
      </div>
      <span>/test/info</span>
      <el-button @click="getPrivateApiData">请求测试私有接口</el-button>
    </div>
  </FaPageMain>
</template>

<script setup lang="ts">
import adminApi from '@/api/modules/admin'

const publicApiData = ref<string>()

const getPublicApiData = () => {

  adminApi.testPublicApi()
    .then((data) => {
      publicApiData.value = JSON.stringify(data)
    })
    .catch((error) => {
      publicApiData.value = JSON.stringify(error)
    })
}

const privateApiData = ref<string>()

const getPrivateApiData = () => {
  adminApi.testPrivateApi()
    .then((data) => {
      privateApiData.value = JSON.stringify(data)
    })
    .catch((error) => {
      privateApiData.value = JSON.stringify(error)
    })
}

</script>

<style scoped>

</style>
