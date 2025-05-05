<template>
  <div>
    <FaPageMain>
      多级导航2-2-1
      <div>
        {{token}}
      </div>
    </FaPageMain>
  </div>
</template>

<script setup lang="ts">
import useUserStore from "@/store/modules/user.ts";

const userStore = useUserStore()

const token = ref(userStore.token)
</script>
