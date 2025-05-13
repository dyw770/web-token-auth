<template>
  <div>
    <FaPageMain>
      多级导航1
      <icon-picker :show-arrow="false" placement="bottom-start" trigger="click" v-model="icon"/>
    </FaPageMain>
  </div>
</template>
<script setup lang="ts">
import IconPicker from "@/ui/components/IconPicker/inedx.vue";

const icon = ref('');
</script>
