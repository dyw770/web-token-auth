<template>

</template>

<script setup lang="ts">
// 控制授权弹窗显示
const show = defineModel({required: true, type: Boolean})

const {username} = defineProps<{ username: string | undefined }>()


</script>


<style scoped>

</style>
